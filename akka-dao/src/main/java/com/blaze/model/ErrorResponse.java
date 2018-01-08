package com.blaze.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends Response{

	private String message;
	private Integer codeMessage;
	
}
