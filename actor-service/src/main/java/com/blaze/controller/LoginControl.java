package com.blaze.controller;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blaze.Enviroment;
import com.blaze.model.ErrorResponse;
import com.blaze.model.Login;
import com.blaze.model.Response;
import com.blaze.model.SessionResponse;
import com.blaze.service.LoginService;

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
public class LoginControl {

	private Logger log = Logger.getLogger(getClass());

	private ActorSystem system = new Enviroment().getActorSystem();
	private ActorRef actorService = system.actorOf(new SmallestMailboxPool(5).props(Props.create(LoginService.class)),
			"loginService");

	public LoginControl() {
		System.out.println("constructor " + system + " " + actorService);
	}

	@RequestMapping(value = "/{username}/{password}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public DeferredResult<Response> loginUser(@PathVariable("username") String userName,
			@PathVariable("password") String password) {
		System.out.println("system " + system);

		DeferredResult<Response> result = new DeferredResult<>();
		Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
		Login login = new Login();
		login.setUserName(userName);
		login.setPassword(password);
		Future<Object> futureObject = Patterns.ask(actorService, login, timeout);
		Future<Response> futureResponse = futureLogin(futureObject);
		OnComplete<Response> onComplete = new OnComplete<Response>() {
			@Override
			public void onComplete(Throwable failure, Response success) throws Throwable {
				if (success != null) {
					result.setResult(success);
				} else if (failure != null) {
					result.setErrorResult(failure);
				}
			}
		};
		futureResponse.onComplete(onComplete, system.dispatcher());
		return result;
	}

	private Future<Response> futureLogin(Future<Object> futureObject) {
		return futureObject.map(new Mapper<Object, Response>() {
			@Override
			public Response apply(Object object) {
				Response response = null;
				if (object instanceof Response) {
					response = (Response) object;
				} else {
					System.out.println("heres is your bug idiot..... ~.~");
				}
				return response;
			}
		}, system.dispatcher());
	}

}
