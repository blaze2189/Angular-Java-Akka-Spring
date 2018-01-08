package com.blaze.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blaze.EnviromentActor;
import com.blaze.actor.dao.UserDAOActor;
import com.blaze.model.Response;
import com.blaze.model.User;
import com.blaze.model.UserActor;

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
public class UserController {

	private Logger log = Logger.getLogger(UserController.class);

	private ActorSystem actorSystem = EnviromentActor.getActorSystem();
	// protected ActorRef actorRef = actorSystem.actorOf(new
	// RoundRobinPool(6).props(Props.create(UserActor.class)), "userActor");
//	protected ActorRef actorRef = actorSystem
//			.actorOf(new SmallestMailboxPool(5).
//					props(Props.create(UserServiceActor.class)), 
//					"userServiceActor");
	protected ActorRef userActorDAO = actorSystem
			.actorOf(new SmallestMailboxPool(5).
					props(Props.create(UserDAOActor.class)), 
					"userServiceActor");
	
//	@RequestMapping(value = "/{firstName}/{lastName}", method = RequestMethod.GET)
//	public DeferredResult<User> getAll(@PathVariable("firstName") String firstName,
//			@PathVariable("lastName") String lastName) {
//		User user = new User();
//		user.setFirstName(firstName);
//		user.setLastName(lastName);
//		DeferredResult<User> result = new DeferredResult<>();
//		UserActor actorHelper = new UserActor();
//		actorHelper.setAction(UserDAOActor.action.GET);
//		actorHelper.setUser(user);
//			Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
//			Future<Object> futureObject = Patterns.ask(actorRef, actorHelper, timeout);
//			Future<User> futureUser = futureUser(futureObject);
//			OnComplete<User> onComplete = new OnComplete<User>() {
//				public void onComplete(Throwable failure, User success) throws Throwable {
//					if (success != null) {
//						User userResult = success != null ? (User) success : new User();
//						result.setResult(userResult);
//					} else {
//						result.setErrorResult(failure);
//					}
//				}
//			};
//			futureUser.onComplete(onComplete, actorSystem.dispatcher());
//		return result;
//	}

	
	@RequestMapping(value = "/{email}/", method = RequestMethod.GET)
	public DeferredResult<Response> getByEmail(@PathVariable("email") String email) {
		User user = new User();
		user.setEmail(email);
		DeferredResult<Response> result = new DeferredResult<>();
		UserActor actorHelper = new UserActor();
		actorHelper.setAction(UserDAOActor.action.GET_BY_EMAIL);
		actorHelper.setUser(user);
		try {
			Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
			Future<Object> futureObject = Patterns.ask(userActorDAO, actorHelper, timeout);
			Future<Response> futureUser = futureUser(futureObject);
			OnComplete<Response> onComplete = new OnComplete<Response>() {
				public void onComplete(Throwable failure, Response success) throws Throwable {
					if (success != null) {
						Response userResult =  (Response) success ;
						result.setResult(userResult);
					} else {
						result.setErrorResult(failure);
					}
				}
			};
			futureUser.onComplete(onComplete, actorSystem.dispatcher());
		} catch (Exception e) {
			log.info("general exception");
			log.info(e);
			log.info(e.getClass());
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public DeferredResult<List<User>> getAll() {
		User user = new User();
		DeferredResult<List<User>> result = new DeferredResult<>();
		UserActor actorHelper = new UserActor();
		actorHelper.setAction(UserDAOActor.action.GET_ALL);
		actorHelper.setUser(user);
		try {
			Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
			Future<Object> futureObject = Patterns.ask(userActorDAO, actorHelper, timeout);
			Future<List<User>> futureUser = futureListUser(futureObject);
			OnComplete<List<User>> onComplete = new OnComplete<List<User>>() {
				public void onComplete(Throwable failure, List<User> success) throws Throwable {
					if (success != null) {
						List<User> userResult = success != null ? (List) success : new ArrayList<>();
						result.setResult(userResult);
					} else {
						result.setErrorResult(failure);
					}
				}
			};
			futureUser.onComplete(onComplete, actorSystem.dispatcher());
		} catch (Exception e) {
			log.info("general exception");
			log.info(e);
			log.info(e.getClass());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	public DeferredResult<Key<User>> save(@RequestBody User user) {
		DeferredResult<Key<User>> result = new DeferredResult<>();
		UserActor actorHelper = new UserActor();
		actorHelper.setAction(UserDAOActor.action.SAVE);
		actorHelper.setUser(user);
		Timeout timeout = new Timeout(Duration.create(10, TimeUnit.SECONDS));
		Future<Object> futureObject = Patterns.ask(userActorDAO, actorHelper, timeout);
		Future<Key<User>> futureKeyUser = futureKeyUser(futureObject);

		OnComplete<Key<User>> onComplete = new OnComplete<Key<User>>() {
			public void onComplete(Throwable failure, Key<User> success) throws Throwable {
				if (success != null) {
					log.info("success");
					Key<User> keyUserResult = success != null ? (Key<User>) success : null;
					result.setResult(success);
				} else {
					log.info("fail");
					result.setErrorResult(failure);
				}
			}
		};
		futureKeyUser.onComplete(onComplete, actorSystem.dispatcher());
		return result;
	}

	protected Future<Key<User>> futureKeyUser(Future<Object> futureObject) {
		return futureObject.map(new Mapper<Object, Key<User>>() {
			@Override
			public Key<User> apply(Object o) {
				Key<User> keyUser = null;
				if (o instanceof Key) {
					keyUser = (Key<User>) o;
				} else {
					log.info(o.getClass());
				}
				return keyUser;
			}
		}, actorSystem.dispatcher());
	}

	protected Future<List<User>> futureListUser(Future<Object> futureObject) {
		return futureObject.map(new Mapper<Object, List<User>>() {
			@Override
			public List<User> apply(Object o) {
				List<User> listUser = null;
				if (o instanceof List) {
					listUser = (List) o;
				} else {
					log.info(o.getClass());
				}
				return listUser;
			}
		}, actorSystem.dispatcher());
	}

	protected Future<Response> futureUser(Future<Object> futureObject) {
		return futureObject.map(new Mapper<Object, Response>() {
			@Override
			public Response apply(Object object) {
				Response returnUser = null;
				if (object instanceof Response) {
					returnUser = (Response) object;
				}
				return returnUser;
			}
		}, actorSystem.dispatcher());
	}

}
