package com.blaze.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity("user")
public class User extends Response{

	@Id
	private ObjectId objectId;
	@Property("first_name")
	@JsonProperty("first_name")
	private String firstName;
	@Property("last_name")
	@JsonProperty("last_name")
	private String lastName;
	@Property("email")
	@JsonProperty("email")
	private String email;
	@Property("password")
	@JsonProperty("password")
	private String password;
	@Property("actors")
	@JsonProperty("actors")
	private Actor actors;
				   
}
