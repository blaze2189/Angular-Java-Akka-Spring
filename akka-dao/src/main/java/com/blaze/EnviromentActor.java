package com.blaze;



import org.springframework.boot.autoconfigure.SpringBootApplication;

import akka.actor.ActorSystem;

@SpringBootApplication
public class EnviromentActor {

	private static ActorSystem actorSystem;

	public static ActorSystem getActorSystem() {
		if (actorSystem == null) {
			actorSystem = ActorSystem.create("enviromentActor");
		}
		return actorSystem;
	}
	
}
