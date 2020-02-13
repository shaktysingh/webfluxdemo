/**
 * 
 */
package com.webservice.webfluxdemo.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tuswahal0
 *
 */
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	@Id
	public UUID id;
	
	public String description;
	
	public JsonNode metadata;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JsonNode getMetadata() {
		return metadata;
	}

	public void setMetadata(JsonNode metadata) {
		this.metadata = metadata;
	}

}
