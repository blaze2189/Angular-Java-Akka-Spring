package com.blaze.controller;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blaze.EnviromentActor;
import com.blaze.actor.dao.UserDAOActor;
import com.blaze.model.ActorHelper;
import com.blaze.model.Response;
import com.blaze.model.User;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.routing.SmallestMailboxPool;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "login")
public class LoginController {

	private Logger log = Logger.getLogger(getClass());

	private ActorSystem actorSystem = EnviromentActor.getActorSystem();
	private ActorRef userActor = actorSystem
			.actorOf(new SmallestMailboxPool(5).props(Props.create(UserDAOActor.class)), "loginUserActor");

	@RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
	public DeferredResult<Response> loginUser(@PathVariable("email") String email,
			@PathVariable("password") String password) {
		DeferredResult<Response> result = new DeferredResult<>();
		ActorHelper actorHelper = new ActorHelper();
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		actorHelper.setUser(user);
		actorHelper.setAction(UserDAOActor.action.LOGIN);
		Timeout timeout = new Timeout(Duration.create(25, TimeUnit.SECONDS));
		Future<Object> futureObject = Patterns.ask(userActor, actorHelper, timeout);
		Future<Response> futureUser = futureUser(futureObject);
		OnComplete<Response> onComplete = new OnComplete<Response>() {
			@Override
			public void onComplete(Throwable failure, Response success) throws Throwable {
				if (failure != null) {
					log.info("not correct user and password");
					result.setErrorResult(failure);
				} else {
					result.setResult(success);
				}
				// if(success!=null) {
				// result.setResult(success);
				// }else {
				// log.info("not correct user and password");
				// result.setErrorResult(failure);
				// }
			}
		};
		futureUser.onComplete(onComplete, actorSystem.dispatcher());
		return result;
	}

	protected Future<Response> futureUser(Future<Object> futureObject) {
		return futureObject.map(new Mapper<Object, Response>() {
			@Override
			public User apply(Object object) {
				User returnUser = null;
				if (object instanceof User) {
					returnUser = (User) object;
				}
				return returnUser;
			}
		}, actorSystem.dispatcher());
	}
}
