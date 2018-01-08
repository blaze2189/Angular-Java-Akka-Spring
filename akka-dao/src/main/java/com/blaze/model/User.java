package com.blaze.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
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
	@JsonProperty("first_Name")
	private String firstName;
	@Property("last_name")
	@JsonProperty("lastName")
	private String lastName;
	@Property("email")
	@JsonProperty("email")
	private String email;
	@Property("password")
	@JsonProperty("password")
	private String password;
//	@Property("actors")
	@JsonProperty("actors")
	@Embedded("actors")
//	private List<Map<String,String>> actors;
	private List<Actor> actors;
				   
}
