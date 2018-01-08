package com.blaze.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.blaze.model.Response;
import com.blaze.model.User;
import com.blaze.model.UserServiceMessage;

import akka.actor.UntypedActor;

public class UserService extends UntypedActor {

	private final String DAO_URL="http://localhost:8000/user/";
	
	public enum Actions {
		GET_BY_EMAIL
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof UserServiceMessage) {
			UserServiceMessage userServiceMessage= (UserServiceMessage) message;
			Actions action = userServiceMessage.getAction();
			User user = userServiceMessage.getUser();
			switch (action) {
			case GET_BY_EMAIL:
				String email = user.getEmail();
				getByEmail(email);
				break;
			}
		}
	}
	
	private Response getByEmail(String email) {
		Response result = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<User> request = new HttpEntity<>(header);
		ResponseEntity<User> responseEntity = restTemplate.exchange(DAO_URL+email
				, HttpMethod.GET,request,User.class);
		result = responseEntity.getBody();
		return result;
	}

}
