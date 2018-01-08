package com.blaze.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {

	@JsonProperty(value="user_name")
	private String userName;
	@JsonProperty(value="password")
	private String password;
	
}
