package com.blaze.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends Response  {

	@JsonProperty("message")
	private String message;
	
}
