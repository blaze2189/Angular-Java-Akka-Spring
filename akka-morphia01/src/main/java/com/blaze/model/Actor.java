package com.blaze.model;

import org.mongodb.morphia.annotations.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Actor {

	@JsonProperty("name")
	@Property("name")
	private String name;
	@JsonProperty("img_src")
	@Property("img_src")
	private String imageSource;
	
}
