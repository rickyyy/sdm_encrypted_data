package eit.nl.utwente.sdm.encryptsql.actors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;

public class Server {

	private Relation relation;
	private Connection connection;

	public Server(Relation r, Connection conn) {
		this.relation = r;
		this.connection = conn;
	}
	
	public void store(EncryptedFinancialData ed) {
		Connection dbConnection = null;
		PreparedStatement insertData = null;
		String insertString = "insert into "
				+ "financial_data"
				+ "(etuple, id_cons_s, id_client_s, statement_s, investment_s, interest_rate_s) VALUES"
				+ "(?,?,?,?,?,?)";

		try {
			dbConnection = connection;
			insertData = dbConnection.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
			insertData.setString(1, ed.getEtuple());
			insertData.setInt(2, ed.getIdCons());
			insertData.setInt(3, ed.getIdClient());
			insertData.setInt(4, ed.getStatement());
			insertData.setInt(5, ed.getInvestment());
			insertData.setInt(6, ed.getInterest());

			// execute insert SQL statement
			insertData.execute();
			ResultSet generatedKeys = insertData.getGeneratedKeys();
			if (generatedKeys.next()) {
				ed.setId(generatedKeys.getInt(1));
			}
			System.out.println("New client was persisted");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} 
	}

}
