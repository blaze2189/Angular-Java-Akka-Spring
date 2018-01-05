package com.blaze;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import akka.actor.ActorSystem;

/**
 * Hello world!
 *
 */
 @SpringBootApplication
public class App {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(App::shutdown));
		new SpringApplicationBuilder(App.class).properties("server.port=8080").run();
	}

	@PostConstruct
	public void init(){

	}

	private static void shutdown() {
		final ActorSystem actorSystem = Enviroment.getActorSystem();
		if (actorSystem != null) {
			actorSystem.terminate();
		}
	}

}
