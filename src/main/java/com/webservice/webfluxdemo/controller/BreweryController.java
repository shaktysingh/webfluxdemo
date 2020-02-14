package com.webservice.webfluxdemo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.webservice.webfluxdemo.model.Brewery;
import com.webservice.webfluxdemo.model.ErrorResponse;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.webservice.webfluxdemo.service.BreweryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "Brewery Management API's")
public class BreweryController {

	Logger log = LoggerFactory.getLogger(BreweryController.class);

	@Autowired
	private BreweryService breweryClient;

	@GetMapping(path = "/breweries", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod = "fallback_method")
	/*
	 * @HystrixCommand(fallbackMethod = "fallback_method", threadPoolKey =
	 * "myThreadPool", groupKey = "Group1", commandKey = "Command1",
	 * commandProperties = {
	 * 
	 * @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	 * value = "1000") }, threadPoolProperties = {
	 * 
	 * @HystrixProperty(name = "coreSize", value = "10") })
	 */
	@ApiOperation(value = "View a list of Brewries", response = JsonNode.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	 Mono<ResponseEntity<List<Brewery>>> listBrewries(){
		 
		log.debug("Get All Brewries");
		Flux<Brewery> breweryFlux =  breweryClient.listBrewries().take(5);
		
//		breweryFlux.flatMap(brew ->breweryRepository.save(brew));
//		breweryRepository.saveAll(breweryFlux);
		
       Mono<ResponseEntity<List<Brewery>>> response = breweryFlux.collectList()
				.map(brewery -> ResponseEntity.ok(brewery));
		log.debug("Retrieved all Brewries");
		return response;
		 
	}

	
	 @GetMapping("/breweries/{id}")
	 Mono<ResponseEntity<Brewery>> getBreweryById(@PathVariable String id){
		 
		log.debug("Get Brewery By ID");
		Mono<Brewery> breweryMono =  breweryClient.getBrewByID(id);
		
       Mono<ResponseEntity<Brewery>> response = breweryMono.map(brewery -> ResponseEntity.ok(brewery));
		return response;
		 
	}

	 
	/**
	 * Fallback method of Hystrix after circuit gets open.
	 * 
	 * @return String
	 */
	@SuppressWarnings("unused")
	private String fallback_method() {
		return "Fallback method to invoke in case of circuit-break happening";
	}

	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<ErrorResponse>> handle(Exception ex) {
		log.error("Got Error while recieving Brewries");
		
		ErrorResponse error = new ErrorResponse();
		error.setType("422");
		error.setField("Unproccesable Entity");
		error.setMessage(ex.getMessage());
		
		Mono<ResponseEntity<ErrorResponse>> errorResponse = Mono.just(ResponseEntity.unprocessableEntity().body(error));
		
		return errorResponse;
	}
}
