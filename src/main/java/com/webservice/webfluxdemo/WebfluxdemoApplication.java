package com.webservice.webfluxdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfluxdemoApplication {

	static Logger log = LoggerFactory.getLogger(WebfluxdemoApplication.class);
	public static void main(String[] args) {
		log.info("Starting Spring Boot Application");
		SpringApplication.run(WebfluxdemoApplication.class, args);
	}

}
