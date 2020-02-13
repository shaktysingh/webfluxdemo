/**
 * 
 */
package com.webservice.webfluxdemo.repositories;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.webservice.webfluxdemo.domain.Order;

import reactor.core.publisher.Flux;

/**
 * @author tuswahal0
 *
 */
@Repository
@N1qlPrimaryIndexed
public interface OrderRepository extends ReactiveCouchbaseRepository<Order, String> {
	
	@Query("#{#n1ql.selectEntity} WHERE description like $1 AND #{#n1ql.filter} ")
    Flux<Order> findAllByDescription(final String description);
	
	@Override
	@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ")
    Flux<Order> findAll();


}
