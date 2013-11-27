package eit.nl.utwente.sdm.encryptsql.actors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;
import eit.nl.utwente.sdm.encryptsql.helpers.EncryptionHelper;


public class Client {
	
	private int id;
	private String name;
	private String contact;
	private byte key[];
	private Server server;
	private Relation relation;
	
	public Client(Server s, Relation r) {
		this.server = s;
		this.relation = r;
	}
	
	public Client(int idClient, String nm, String cnt){
		this.id = idClient;
		this.name = nm;
		this.contact = cnt;
	}
	
	public Client(String nm, String cnt){
		this.name = nm;
		this.contact = cnt;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public String getName (){
		return name;
	}
	
	public void setContact(String newContact){
		contact = newContact;
	}
	
	public String getContact(){
		return contact;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void persist() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement insertData = null;
		String insertString = "insert into "
				+ "client"
				+ "(name, contact) VALUES"
				+ "(?,?)";

		try {
			dbConnection = DBUtils.getDBConnection();
			insertData = dbConnection.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
			insertData.setString(1, name);
			insertData.setString(2, contact);

			// execute insert SQL statement
			insertData.execute();
			ResultSet generatedKeys = insertData.getGeneratedKeys();
			if (generatedKeys.next()) {
				setId(generatedKeys.getInt(1));
			}
			System.out.println("New client was persisted");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} finally {

			if (insertData != null) {
				insertData.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}
	
	public void delete() {
		if (getId() == DBUtils.ID_NOT_SET) { 
			//entity not yet persisted
			return;
		}
		Connection dbConnection = null;
		PreparedStatement sqlStatement = null;
		String sqlString = "delete from client where id = ?";

		try {
			dbConnection = DBUtils.getDBConnection();
			sqlStatement = dbConnection.prepareStatement(sqlString);
			sqlStatement.setInt(1, getId());
			sqlStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte key[]) {
		this.key = key;
	}
	
	public String etuplePreparation (long idCons, long idClient, long interest, long investment, String statement){
		String s = "";
		s.concat(String.valueOf(idCons));
		s.concat("-");
		s.concat(String.valueOf(idClient));
		s.concat("-");
		s.concat(String.valueOf(interest));
		s.concat("-");
		s.concat(String.valueOf(investment));
		s.concat("-");
		s.concat(statement);
		
		return s;
	}
	
	public ArrayList<Comparable> preparationMapping (long idCons, long idClient, long interest, long investment, String statement){
		ArrayList<Comparable> list = new ArrayList<Comparable>();
		list.add(idCons);
		list.add(idClient);
		list.add(interest);
		list.add(investment);
		list.add(statement);
		return list;
	}
	
	public void preparationEFD (String idConsEFD, String idClientEFD, String interestEFD, String investmentEFD, String statementEFD, ArrayList<String> mappedValues){
		int i;
		for (i = 0;i<mappedValues.size();i++){
			if(i == 0){
				idConsEFD = mappedValues.get(i);
			}
			else if (i == 1){
				idClientEFD = mappedValues.get(i);
			}
			else if (i == 2){
				interestEFD = mappedValues.get(i);
			}
			else if (i == 3){
				investmentEFD = mappedValues.get(i);
			}
			else {
				statementEFD = mappedValues.get(i);
			}
		}
	}
	
	public void store(long idCons, long idClient, long interest, long investment, String statement) {
		String s;
		String etuple;
		ArrayList<Comparable> listValues;
		ArrayList<String> mappedValues;
		
		String idConsEFD = "";
		String idClientEFD= "";
		String interestEFD = "";
		String investmentEFD = "";
		String statementEFD = "";
		
		//Encryption of etuple
		EncryptionHelper help = new EncryptionHelper();
		s = etuplePreparation(idCons, idClient, interest, investment, statement);
		etuple = help.encrypt(s, this.key);
		
		//Mapping Function
		listValues = preparationMapping(idCons, idClient, interest, investment, statement);
		mappedValues = this.relation.mappingFunction(listValues);
		preparationEFD(idConsEFD, idClientEFD, interestEFD, investmentEFD, statementEFD, mappedValues);

		//create EncryptedFinancialData
		EncryptedFinancialData ed = new EncryptedFinancialData(etuple, idConsEFD, idClientEFD, interestEFD, investmentEFD, statementEFD);
		server.store(ed);
	}
}
