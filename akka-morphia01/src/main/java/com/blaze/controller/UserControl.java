package com.blaze.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blaze.model.User;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserControl {

//	private Connection connection;
//	private UserDAO userDAO;
	
	{
//	this.connection = new Connection();
	
	}
	
	@GET
	@Path("/{firstName}/{lastName}")
	public User getAll(@PathParam("firstName")String firstName,@PathParam("lastName")String lastName) {
//		return this.userDAO.getByFirstNameLastName(firstName, lastName);
		throw new UnsupportedOperationException("Not implemented");
	}
	
}
