package com.webservice.webfluxdemo.repositories;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

import com.webservice.webfluxdemo.model.Brewery;

public interface BreweryRepository extends ReactiveCouchbaseRepository<Brewery, String> {

}
