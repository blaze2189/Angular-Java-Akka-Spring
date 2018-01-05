package com.blaze.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

	@JsonProperty("code")
	private Integer code;
	
}
