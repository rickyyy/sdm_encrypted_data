package eit.nl.utwente.sdm.encryptsql.actors;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;

public class Server {

	private Relation relation;

	public Server(Relation r) {
		this.relation = r;
	}
	
	public void store(EncryptedFinancialData ed) {
		//TODO store in db 
	}

}
