package com.blaze.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blaze.actor.dao.UserDAOActor;
import com.blaze.model.ActorHelper;
import com.blaze.model.User;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

public class TestSpringUserController {

	private ActorSystem actorSystem = ActorSystem.create("test-user-actor-system");
	protected ActorRef actorRef = actorSystem.actorOf(UserDAOActor.props(), "userActor");

	@Test
	public void simpleTest() {
		User user = new User();
		ActorHelper actorHelper = new ActorHelper();
		user.setFirstName("testName " );
		user.setLastName("testLast " );
		actorHelper.setUser(user);
		actorHelper.setAction(UserDAOActor.action.SAVE);
		System.out.println("sve "+user.getFirstName()+":"+user.getLastName());
		Patterns.ask(actorRef, actorHelper, new Timeout(Duration.create(5, TimeUnit.SECONDS)));
		
	}
	
//	@Test
	public void multiplePost() {

		// User user1 = new User();
		// User user2 = new User();
		// User user3 = new User();
		// User user4 = new User();
		// User user5 = new User();
		// User user6 = new User();
		//
		//
		// user1.setFirstName("usr1Name"+Math.random());
		// user1.setFirstName("usr1Last"+Math.random());
		//
		// user2.setFirstName("usr2Name"+Math.random());
		// user2.setFirstName("usr2Last"+Math.random());
		//
		// user3.setFirstName("usr3Name"+Math.random());
		// user3.setFirstName("usr3Last"+Math.random());
		//
		// user4.setFirstName("usr4Name"+Math.random());
		// user4.setFirstName("usr4Last"+Math.random());
		//
		// user5.setFirstName("usr5Name"+Math.random());
		// user5.setFirstName("usr5Last"+Math.random());
		//
		// user6.setFirstName("usr6Name"+Math.random());
		// user6.setFirstName("usr6Last"+Math.random());

		Runnable runnable = () -> {
			// SpringUserController sUC = new SpringUserController();
			User user = new User();
			ActorHelper actorHelper = new ActorHelper();
			actorHelper.setUser(user);
			actorHelper.setAction(UserDAOActor.action.SAVE);
			user.setFirstName("testName " + Math.random());
			user.setFirstName("testLast " + Math.random());
			System.out.println("sve");
			Patterns.ask(actorRef, actorHelper, new Timeout(Duration.create(5, TimeUnit.SECONDS)));
			// sUC.save(user);
		};
		try {
			ExecutorService executor = Executors.newWorkStealingPool(5);
			executor.submit(new PostMethod());
			executor.execute(runnable);
			executor.submit(new PostMethod());
			executor.submit(runnable);
			executor.execute(new PostMethod());
			executor.shutdown();
			try {
				executor.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class PostMethod implements Runnable {

		@Override
		public void run() {
			User user = new User();
			ActorHelper actorHelper = new ActorHelper();
			actorHelper.setUser(user);
			actorHelper.setAction(UserDAOActor.action.SAVE);
			user.setFirstName("testName " + Math.random());
			user.setFirstName("testLast " + Math.random());
			Patterns.ask(actorRef, actorHelper, new Timeout(Duration.create(5, TimeUnit.SECONDS)));
			System.out.println("syso");
			// sUC.save(user);
		}

	}

}
