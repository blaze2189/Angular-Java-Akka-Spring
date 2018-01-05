package com.blaze;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import akka.actor.ActorSystem;

@SpringBootApplication
public class Enviroment {

	private static ActorSystem actorSystem;

	public static ActorSystem getActorSystem() {
		System.out.println("--------------");
		if(actorSystem==null) {
			actorSystem = ActorSystem.create("enviroment");
		}
		System.out.println("actorSystem in enviromente "+actorSystem);
		return actorSystem;
	}

}
