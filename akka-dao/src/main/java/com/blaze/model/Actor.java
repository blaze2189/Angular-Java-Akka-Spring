package com.blaze.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Actor extends Response{

	@JsonProperty("name")
	@Property("name")
	private String name;
	@JsonProperty("img_src")
	@Property("img_src")
	private String imageSource;
	
}
