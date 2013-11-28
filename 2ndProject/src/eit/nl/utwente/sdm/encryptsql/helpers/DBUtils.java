package eit.nl.utwente.sdm.encryptsql.helpers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.actors.Consultant;

public class DBUtils {

	public static final int ID_NOT_SET = -1;

	public static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			GlobalProperties propProvider = GlobalProperties.getInstance();
			String DB_CONNECTION = propProvider.getProperty("DB_CONNECTION");
			String DB_USER = propProvider.getProperty("DB_USER");
			String DB_PASSWORD = propProvider.getProperty("DB_PASSWORD");
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return dbConnection;
	}

	public static List<Consultant> getConsultants() {
		List<Consultant> result = new ArrayList<Consultant>();
		Connection dbConnection = getDBConnection();
		PreparedStatement insertData = null;
		String insertString = "select * from consultant";
		try {
			insertData = dbConnection.prepareStatement(insertString);
			ResultSet resultSet = insertData.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String company = resultSet.getString(3);
				Consultant c = new Consultant(id, name, company);
				result.add(c);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Client> getClients() {
		List<Client> result = new ArrayList<Client>();
		Connection dbConnection = getDBConnection();
		PreparedStatement insertData = null;
		String insertString = "select * from client";
		try {
			insertData = dbConnection.prepareStatement(insertString);
			ResultSet resultSet = insertData.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				int idConsultant = resultSet.getInt(2);
				String name = resultSet.getString(3);
				String contact = resultSet.getString(4);
				Client c = new Client(id, idConsultant, name, contact);
				result.add(c);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Client> getClientsForConsultant(Consultant c) {
		List<Client> res = new ArrayList<Client>();
		List<Client> clients = DBUtils.getClients();
		for (Client client : clients) {
			if (client.getIdConsultant() == c.getId()) {
				res.add(client);
			}
		}
		return res;
	}
}