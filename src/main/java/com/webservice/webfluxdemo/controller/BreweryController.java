package com.webservice.webfluxdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.webservice.webfluxdemo.service.BreweryClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "Brewery Management API's")
public class BreweryController {

	Logger log = LoggerFactory.getLogger(BreweryController.class);

	@Autowired
	private BreweryClient breweryClient;

	@GetMapping("/breweries")
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
	Mono<JsonNode> listBrewries() {
		log.info("Getting list of breweries");
		return breweryClient.listBrewries();

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

}
