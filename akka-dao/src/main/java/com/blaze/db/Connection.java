package com.blaze.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Connection {

	private Morphia morphia;
	private Datastore datastore;
	private String dataBaseName;

	static private final String PORT = "32768";
	static private final String HOST = "localhost";

	private MongoClient mongoClient;

	{
		mongoClient = new MongoClient(HOST + ":" + PORT);
		morphia = new Morphia();
	}

	private static Connection connection = new Connection();
	
	public static Connection getInstance() {
		return connection;
	}
	
	private Connection() {
		this("akka-morphia");
	}
	
	public Connection(String dataBaseName) {
		this.dataBaseName = dataBaseName;
		this.datastore = morphia.createDatastore(mongoClient, this.dataBaseName);
	}
}
