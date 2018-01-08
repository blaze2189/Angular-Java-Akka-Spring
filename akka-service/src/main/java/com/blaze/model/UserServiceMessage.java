package com.blaze.model;

import com.blaze.service.UserService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserServiceMessage {

	private UserService.Actions action;
	private User user;
	
}
