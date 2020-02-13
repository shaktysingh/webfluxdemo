package com.webservice.webfluxdemo.controller;

import java.time.Duration;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.webfluxdemo.domain.Order;
import com.webservice.webfluxdemo.repositories.OrderRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class OrderController {

	
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order/{id}")
    Mono<ResponseEntity<Order>> list(@PathVariable String id){
    	
    	Mono<ResponseEntity<Order>> notFound = Mono.just(ResponseEntity.notFound().build());
       
    	Mono<Order> orderMono = orderRepository.findById(id);
        Mono<ResponseEntity<Order>> response = orderMono
        										.map(order -> ResponseEntity.ok(order))
        										.switchIfEmpty(notFound);
        
        return response;
                
    }
    
    @GetMapping("/byDescription/{description}")
    Flux<Order> listByDescription(@PathVariable String description){
    	return orderRepository.findAllByDescription(description);
    	
    }
    @GetMapping("order/desc/{description}")
	Flux<Order> listByDescrip(@PathVariable String description) {
		return orderRepository.findAll()
				.filter(order -> order.getDescription().equalsIgnoreCase(description))
				.log("1")
				.delayElements(Duration.ofSeconds(1));
	}
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    Mono<Order> create(@RequestBody Order orderStream){
    	orderStream.setId(UUID.randomUUID());
        return orderRepository.save(orderStream);
    }
}
