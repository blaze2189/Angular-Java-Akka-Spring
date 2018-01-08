package com.blaze;

import org.springframework.boot.builder.SpringApplicationBuilder;

import akka.actor.ActorSystem;

public class AkkaApp {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(AkkaApp::shutdown));

		new SpringApplicationBuilder(EnviromentActor.class).properties("server.port=8000").run();

	}

	private static void shutdown() {
		final ActorSystem actorSystem = EnviromentActor.getActorSystem();
		if (actorSystem != null) {
			actorSystem.terminate();
		}
	}

}
