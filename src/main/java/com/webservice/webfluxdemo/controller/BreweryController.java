package com.webservice.webfluxdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.webservice.webfluxdemo.service.BreweryClient;

import reactor.core.publisher.Mono;

@RestController
public class BreweryController {
	
	
	 @Autowired
	 private BreweryClient breweryClient;
	   
	 @GetMapping("/breweries")
	 Mono<JsonNode> listBrewries(){
		 
		return breweryClient.listBrewries();
		 
	}

}
