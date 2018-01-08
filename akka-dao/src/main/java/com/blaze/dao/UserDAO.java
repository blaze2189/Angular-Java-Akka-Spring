package com.blaze.dao;

import java.util.List;
import java.util.Map;

import com.blaze.model.User;

public interface UserDAO {

	User getByFirstNameLastName(String firstName,String lastName);
	List<User> getByFirstName(String firstName);
	User getByEmailAndPassword(String email,String password) ;
	User getByEmail(String email);
	List<User> getByFirstName() ;
	
	
}
