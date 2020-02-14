package com.webservice.webfluxdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.webservice.webfluxdemo.model.Brewery;
import com.webservice.webfluxdemo.repositories.BreweryRepository;
import com.webservice.webfluxdemo.exception.MyCustomException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BreweryService {

	private static final String API_BASE_URL = "https://api.openbrewerydb.org";
	private static final String USER_AGENT = "Spring 5 WebClient";
	private static final Logger logger = LoggerFactory.getLogger(BreweryService.class);
	
	@Autowired
	private BreweryRepository breweryRepository;

	private final WebClient webClient;

	public BreweryService() {
		this.webClient = WebClient.builder()
				.baseUrl(API_BASE_URL)
				.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
				.filter(logRequest())
				.filter(logResposneStatus())
				.build();
	}

	public Flux<Brewery> listBrewries() {
		Flux<Brewery> result = webClient
				.get()
				.uri("/breweries")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(),
	                        clientResponse -> Mono.error(new MyCustomException("Unproccesable Entity")))
				.bodyToFlux(Brewery.class);

		return result;
	}

	public Mono<Brewery> getBrewByID(String id) {
		Mono<Brewery> result = webClient
				.get()
				.uri("/breweries/"+id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(),
	                        clientResponse -> Mono.error(new MyCustomException("Unproccesable Entity")))
				.bodyToMono(Brewery.class)
				.flatMap(brew-> this.breweryRepository.save(brew));
		
		return result;
	}
	
	
	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}
	
	private ExchangeFilterFunction logResposneStatus() {
	    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
	        logger.info("Response Status {}", clientResponse.statusCode());
	        return Mono.just(clientResponse);
	    });
	}

}
