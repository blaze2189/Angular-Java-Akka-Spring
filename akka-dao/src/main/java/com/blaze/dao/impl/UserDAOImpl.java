package com.blaze.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.blaze.dao.UserDAO;
import com.blaze.db.Connection;
import com.blaze.model.User;

public class UserDAOImpl extends BasicDAO<User, ObjectId> implements UserDAO {

	// private static UserDAO userDAO = new UserDAOImpl();
	Logger log = Logger.getLogger(UserDAO.class);

	private UserDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	public UserDAOImpl() {
		this(User.class, Connection.getInstance().getDatastore());
	}

	static private UserDAO userDAO = new UserDAOImpl(User.class, Connection.getInstance().getDatastore());

	public static UserDAO getInstance() {
		return userDAO;
	}

	@Override
	public User getByFirstNameLastName(String firstName, String lastName) {
		User user;
		Query<User> query = createQuery().field("first_name").equal(firstName).field("last_name").equal(lastName);
		user = query.get();
		return user;
	}

	@Override
	public List<User> getByFirstName(String firstName) {
		Query<User> query = createQuery().field("first_name").equal(firstName);
		return query.asList();
	}

	@Override
	public List<User> getByFirstName() {
		Query<User> query = createQuery();
		return query.asList();
	}

	@Override
	public User getByEmailAndPassword(String email, String password)   {
		Query<User> query = createQuery().field("email").equal(email).field("password").equal(password);
		User userResult = null;
		userResult = query.get();
		return userResult;
	}
	
	@Override
	public User getByEmail(String email) {
		User userResult=null;
		try {
		Query<User> query = createQuery().field("email").equal(email);
		userResult = query.get();
		}catch(Exception e) {
			System.out.println("error class "+e);
			e.printStackTrace();
		}
		return userResult;
	}


	
}
