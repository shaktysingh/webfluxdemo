package com.webservice.webfluxdemo.configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:couchbase.properties")
public class CouchbaseProperties {
 
    private List<String> bootstrapHosts;
    private String bucketName;
    private String bucketPassword;
    private String userName;
    private int port;
 
    public CouchbaseProperties(
      @Value("${spring.couchbase.bootstrap-hosts}") List<String> bootstrapHosts, 
      @Value("${spring.couchbase.bucket.name}") String bucketName, 
      @Value("${spring.couchbase.bucket.password}") String bucketPassword, 
      @Value("${spring.couchbase.userName}") String userName, 
      @Value("${spring.couchbase.port}") int port) {
        this.bootstrapHosts = Collections.unmodifiableList(bootstrapHosts);
        this.bucketName = bucketName;
        this.bucketPassword = bucketPassword;
        this.port = port;
        this.userName=userName;
    }

	public List<String> getBootstrapHosts() {
		return bootstrapHosts;
	}

	public String getBucketName() {
		return bucketName;
	}

	public String getBucketPassword() {
		return bucketPassword;
	}

	public int getPort() {
		return port;
	}

	public String getUserName() {
		return userName;
	}
    // getters
}