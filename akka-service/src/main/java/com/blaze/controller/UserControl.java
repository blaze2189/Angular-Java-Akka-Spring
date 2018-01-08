package com.blaze.controller;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blaze.Enviroment;
import com.blaze.model.Response;
import com.blaze.model.User;
import com.blaze.model.UserServiceMessage;
import com.blaze.service.UserService;

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
@RequestMapping(value = "user")
public class UserControl {

	private Logger log = Logger.getLogger(getClass());

	private ActorSystem actorSystem = Enviroment.getActorSystem();
	private ActorRef userActor = actorSystem
			.actorOf(new SmallestMailboxPool(5).props(Props.create(UserService.class)), "loginUserActor");

	
	private Future<Response> futureResponse(Future<Object> futureObject){
		return futureObject.map(new Mapper<Object, Response>(){
			@Override
			public Response apply(Object object) {
				Response response=null;
				if(object instanceof Response) {
					response = (Response)object;
				}
				return response;
			}
		},actorSystem.dispatcher());
	}
	
	@RequestMapping(value="/{email}",method=RequestMethod.GET)
	public DeferredResult<Response> getByEmail(@PathVariable("email")String email) {
		DeferredResult<Response>result=new DeferredResult<>();
		Timeout timeout = new Timeout(Duration.create(25,TimeUnit.SECONDS));
		UserServiceMessage message = new UserServiceMessage();
		User user = new User();
		user.setEmail(email);
		message.setAction(UserService.Actions.GET_BY_EMAIL);
		message.setUser(user);
		Future<Object> futureObject = Patterns.ask(userActor, message, timeout);
		Future<Response>futurResponse = futureResponse(futureObject);
		OnComplete<Response> onComplete = new OnComplete<Response>() {
			@Override
			public void onComplete(Throwable failure,Response success) {
				if(failure!=null) {
					result.setErrorResult(failure);
				}else {
					result.setResult(success);
				}
			}
		};
		return result;
	}
	
	
	
}
