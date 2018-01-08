package com.blaze;

import java.util.Date;
import java.util.List;

import com.blaze.dao.UserDAO;
import com.blaze.dao.impl.UserDAOImpl;
import com.blaze.db.Connection;
import com.blaze.model.User;

/**
 * Hello world!
 *
 */
public class App {
	private Connection morphiaService;
	private UserDAO userDAO;

	public App() {
		this.morphiaService =  Connection.getInstance();
		this.userDAO = UserDAOImpl.getInstance(); //new UserDAOImpl(User.class, morphiaService.getDatastore());
	}

	public void saveEntityExample() {

		System.out.println("Save entity example");

		User user1 = new User();
		User user2 = new User();
		User user3 = new User();

		user1.setFirstName("Joe");
		user1.setLastName("Jhonson");
		
		user2.setFirstName("Roe");
		user2.setLastName("Rhonson");
		
		user3.setFirstName("Doe");
		user3.setLastName("Dhonson");
		
		System.out.println("Before persist  : ");
		System.out.println("User1 objectId " + user1.getObjectId());
		System.out.println("User2 objectId " + user2.getObjectId());
		System.out.println("User3 objectId " + user3.getObjectId());

		((UserDAOImpl)userDAO).save(user1);
		((UserDAOImpl)userDAO).save(user2);
		((UserDAOImpl)userDAO).save(user3);

		
		System.out.println("Before persist  : ");
		System.out.println("User1 objectId " + user1.getObjectId());
		System.out.println("User2 objectId " + user2.getObjectId());
		System.out.println("User3 objectId " + user3.getObjectId());
	}

	public void retrieveEntityExample() {

		System.out.println("Retrieve entity example ");

		System.out.println("Retrieve by firstName lastName ");
		User fetchedUser = userDAO.getByFirstNameLastName("Doe", "Dhonson");
		System.out.println("firstName " + fetchedUser.getFirstName());
		System.out.println("lastName " + fetchedUser.getLastName());
//		System.out.println("birthDate " + fetchedUser.getBirthDate().toGMTString());
//		System.out.println("hasPremiumAccess " + fetchedUser.isHasPremiumAccess());

		System.out.println();

		System.out.println("Retrive list of users by firstName ");

		List<User> alexs = userDAO.getByFirstName("Joe");
		for (User user : alexs) {
			System.out.println("firstName " + user.getFirstName());
			System.out.println("lastName " + user.getLastName());
//			System.out.println("birthDate " + user.getBirthDate().toGMTString());
//			System.out.println("hasPremiumAccess " + user.isHasPremiumAccess());
			System.out.println("-------");
		}

	}

	public static void main(String[] args) {

		App app = new App();
		app.saveEntityExample();
		app.retrieveEntityExample();
	}
}
