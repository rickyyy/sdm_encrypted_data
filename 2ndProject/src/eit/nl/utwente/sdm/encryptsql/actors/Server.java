package eit.nl.utwente.sdm.encryptsql.actors;

import java.sql.Connection;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;

public class Server {

	private Relation relation;
	private Connection connection;

	public Server(Relation r, Connection conn) {
		this.relation = r;
		this.connection = conn;
	}
	
	public void store(EncryptedFinancialData ed) {
		//TODO store in db 
	}

}
