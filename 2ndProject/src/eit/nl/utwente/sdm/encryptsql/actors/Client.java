package eit.nl.utwente.sdm.encryptsql.actors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.FinancialData;
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
	private Connection db;
	
	public Client(Server s, Relation r, Connection inMemDB) {
		this.server = s;
		this.relation = r;
		this.db = inMemDB;
	}
	
	public Client(int id, String nm, String cnt){
		this.id = id;
		this.name = name;
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
	
	public List<FinancialData> searchEncData(String sql) {
		/* Execute query on server side */
		List<EncryptedFinancialData> resultFromServer = server.executeQueryEncData(sql);
		/* Insert the values received from server in the virtual DB*/
		String insertSQL = "insert into "
				+ "financial_data"
				+ "(id, statement, investment, interest_rate, id_client, id_cons) VALUES"
				+ "(?,?,?,?,?,?)";
		PreparedStatement insertStatement;
		try {
			insertStatement = db
					.prepareStatement(insertSQL);
			for (EncryptedFinancialData encFD : resultFromServer) {
				String decryptedTouple = EncryptionHelper.decrypt(encFD.getEtuple(), key);
				List<String> etupleElements = getElementsOfEtuple(decryptedTouple);
				insertStatement.setInt(1, encFD.getId());
				insertStatement.setString(2, etupleElements.get(4));
				insertStatement.setString(3, etupleElements.get(3));
				insertStatement.setString(4, etupleElements.get(2));
				insertStatement.setString(5, etupleElements.get(1));
				insertStatement.setString(6, etupleElements.get(0));
				insertStatement.execute();
			}
			/* Execute initial query on the virtual DB and return results*/
			List<FinancialData> result = new ArrayList<FinancialData>();
			PreparedStatement userSt = db
					.prepareStatement(sql);
			ResultSet resultSet = userSt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				int idCons = resultSet.getInt(2);
				int idClient = resultSet.getInt(3);
				String statement = resultSet.getString(4);
				int investment = resultSet.getInt(5);
				int interestRate = resultSet.getInt(6);
				FinancialData fd = new FinancialData(id, idCons, idClient, interestRate, investment, statement);
				result.add(fd);
			}	
			
			/* Clean virtual DB*/
			String deleteSql = "DELETE FROM financial_data";
			PreparedStatement deleteSt = db
						.prepareStatement(deleteSql);
			deleteSt.execute();	
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> getElementsOfEtuple(String decryptedTouple) {
		String els[] = decryptedTouple.split("-");
		List<String> result = new ArrayList<String>();
		for (String el : els)
			result.add(el);
		return result;
	}
}
