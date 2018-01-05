package com.blaze.dao;

import java.util.List;

import com.blaze.model.User;

public interface UserDAO {

	User getByFirstNameLastName(String firstName,String lastName);
	List<User> getByFirstName(String firstName);
	User getByEmailAndPassword(String email,String password) throws Exception;
	List<User> getByFirstName() ;
	
}
