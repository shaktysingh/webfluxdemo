package com.webservice.webfluxdemo.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

@Configuration
@EnableReactiveCouchbaseRepositories("repositories")
public class ReactiveCouchbaseConfiguration extends AbstractReactiveCouchbaseConfiguration {
 
    private CouchbaseProperties couchbaseProperties;
 
    public ReactiveCouchbaseConfiguration(CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
    }
 
    @Override
    protected List<String> getBootstrapHosts() {
        return couchbaseProperties.getBootstrapHosts();
    }
 
    @Override
    protected String getBucketName() {
        return couchbaseProperties.getBucketName();
    }
    
    
    @Override
    protected String getUsername() {
        return couchbaseProperties.getUserName();
    }

 
    @Override
    protected String getBucketPassword() {
        return couchbaseProperties.getBucketPassword();
    }
 
    @Override
    public CouchbaseEnvironment couchbaseEnvironment() {
        return DefaultCouchbaseEnvironment
          .builder()
          .connectTimeout(10000)
          .bootstrapHttpDirectPort(couchbaseProperties.getPort())
          .build();
    }
}