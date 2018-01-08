package com.blaze.actor.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;

import com.blaze.dao.UserDAO;
import com.blaze.dao.impl.UserDAOImpl;
import com.blaze.model.UserActor;
import com.blaze.model.ErrorResponse;
import com.blaze.model.Response;
import com.blaze.model.User;
import com.mongodb.MongoServerException;

import akka.actor.InvalidMessageException;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class UserDAOActor extends UntypedActor {

	private Logger log = Logger.getLogger(UserDAOActor.class);

	public static enum action {
		SAVE, GET, LOGIN, GET_ALL,GET_BY_EMAIL
	}

	protected UserDAO userDAO = UserDAOImpl.getInstance();
	// private Connection connection;

	public static Props props() {
		return Props.create(UserDAOActor.class, () -> new UserDAOActor());
	}

	public UserDAOActor() {
		log.info("-----------------------------------new instance-------------------------------------");
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		System.out.println("message received: " + message);
		Response response=null;
		if (message instanceof UserActor) {
			UserActor actorHelper = (UserActor) message;
			// userDAO = new UserDAOImpl();
			// userDAO = UserDAOImpl.getInstance();
			System.out.println("user dao direction: " + userDAO.toString());
			UserDAOActor.action action = actorHelper.getAction();
			User requestUser = null;
			User user = actorHelper.getUser();
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String email = user.getEmail();
			String password = user.getPassword();
			switch (action) {
			case SAVE:
				Key<User> keyUser = null;
				user = actorHelper.getUser();
				try {
					keyUser = ((UserDAOImpl) userDAO).save(user);
					log.info("saved");
					sender().tell(keyUser, sender());
				} catch (MongoServerException | InvalidMessageException ex) {
					log.info("error: " + ex.getMessage());
					sender().tell(ex, sender());
//					throw ex;
				}
				break;
			case GET:
				requestUser = userDAO.getByFirstNameLastName(firstName, lastName);
				requestUser = requestUser != null ? requestUser : new User();
				sender().tell(requestUser, sender());
				break;
			case GET_ALL:
				List<User> listUser = userDAO.getByFirstName();
				listUser.forEach(u -> {
					System.out.println(u.getEmail());
				});
				sender().tell(listUser, sender());
				break;
			case LOGIN:
				log.info("to implement");
				requestUser = userDAO.getByEmailAndPassword(email, password);
				if(requestUser!=null) {
					response = requestUser;
					sender().tell(response, sender());
				}else {
					ErrorResponse error = new ErrorResponse();
					error.setCodeMessage(102);
					error.setMessage("Not valid user or password");
					sender().tell(error, sender());
				}
				break;
			case GET_BY_EMAIL:
				log.info("to obtain email");
				requestUser = userDAO.getByEmail(email);
				if(requestUser!=null) {
					response = requestUser;
					sender().tell(response, sender());
				}else {
					ErrorResponse error = new ErrorResponse();
					error.setCodeMessage(102);
					error.setMessage("Not email found");
					sender().tell(error, sender());
				}
				break;
			default:
				log.info("nothing");
				break;
			}
		}

	}

}