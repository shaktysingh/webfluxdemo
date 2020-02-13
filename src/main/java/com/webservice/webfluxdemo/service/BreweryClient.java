package com.webservice.webfluxdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@Service
public class BreweryClient {

	private static final String API_BASE_URL = "https://api.openbrewerydb.org";
	private static final String USER_AGENT = "Spring 5 WebClient";
	private static final Logger logger = LoggerFactory.getLogger(BreweryClient.class);

	private final WebClient webClient;

	public BreweryClient() {
		this.webClient = WebClient.builder()
				.baseUrl(API_BASE_URL)
				.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
				.filter(logRequest())
				.filter(logResposneStatus())
				.build();
	}

	public Mono<JsonNode> listBrewries() {
		Mono<JsonNode> result = webClient
				.get()
				.uri("/breweries")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(JsonNode.class) ;

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
