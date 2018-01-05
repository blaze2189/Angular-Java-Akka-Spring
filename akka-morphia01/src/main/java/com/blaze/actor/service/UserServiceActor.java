package com.blaze.actor.service;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.blaze.EnviromentActor;
import com.blaze.actor.dao.UserDAOActor;
import com.blaze.model.ActorHelper;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.routing.SmallestMailboxPool;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

public class UserServiceActor extends UntypedActor {

	private Logger log = Logger.getLogger(getClass());
	private ActorSystem actorSystem = EnviromentActor.getActorSystem();
	private ActorRef actorService = actorSystem
			.actorOf(new SmallestMailboxPool(5).props(Props.create(UserDAOActor.class)));

	public static Props props() {
		return Props.create(UserServiceActor.class, () -> new UserServiceActor());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		log.info("Message instance " + message.getClass());
		if (message instanceof ActorHelper) {
			UserDAOActor.action action = ((ActorHelper) message).getAction();
			switch (action) {
			case SAVE:
				Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
				Patterns.ask(actorService, message, timeout);
				break;
			default:
				System.out.println(action + " not implemented or recognized yet");
			}
		}
	}

}
