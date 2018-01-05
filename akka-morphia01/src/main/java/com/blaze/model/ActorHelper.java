package com.blaze.model;

import com.blaze.actor.dao.UserDAOActor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorHelper {

		private UserDAOActor.action action;
		private User user;
	
}
