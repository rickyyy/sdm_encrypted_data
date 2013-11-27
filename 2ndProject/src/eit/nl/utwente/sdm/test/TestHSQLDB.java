package eit.nl.utwente.sdm.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.Assert;
import org.junit.Test;

public class TestHSQLDB {
	

	private Server hsqlServer;
	
	@Test
	public void simpleInsertSelctTest() throws SQLException, IOException, AclFormatException {
		startServer();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				hsqlServer.stop();
			}
		}));

		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
		}

		Connection conn = DriverManager.getConnection(
				"jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
		String createTable = "CREATE TABLE financial_data ( "
				+ "id INTEGER NOT NULL,"
				+ "statement VARCHAR(10) NOT NULL,"
				+ "investment INTEGER NOT NULL,"
				+ "interest_rate INTEGER NOT NULL," + "PRIMARY KEY (id)"
				+ ")";
		PreparedStatement createTbStatement = conn
				.prepareStatement(createTable);
		createTbStatement.execute();
		
		String insertSQL = "insert into "
				+ "financial_data"
				+ "(id, statement, investment, interest_rate) VALUES"
				+ "(?,?,?,?)";
		PreparedStatement insertStatement = conn
				.prepareStatement(insertSQL);
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			insertStatement.setInt(1, i);
			insertStatement.setString(2, "TEST");
			insertStatement.setInt(3, r.nextInt());
			insertStatement.setInt(4, r.nextInt());
			insertStatement.execute();
		}
		String selectSQL = "select * from financial_data where id < ? ";
		PreparedStatement selectStatement = conn
				.prepareStatement(selectSQL);
		selectStatement.setInt(1, 50);
		ResultSet resultSet = selectStatement.executeQuery();
		int count = 0;
		while (resultSet.next()) {
			count++;
			int id = resultSet.getInt(1);
			String statement = resultSet.getString(2);
			int investment = resultSet.getInt(3);
			int interest_rate = resultSet.getInt(4);
			System.out.println(id + " " + statement + " " + investment + " " + interest_rate);
		}
		Assert.assertSame(count, 50);
	}


	private void startServer() throws IOException, AclFormatException {
		
			HsqlProperties props = new HsqlProperties();
			props.setProperty("server.database.0", "mem:test1");
			props.setProperty("server.database.1", "mem:test2");
			hsqlServer = new Server();
			props.setProperty("server.dbname.0", "xdb");
			hsqlServer.setRestartOnShutdown(false);
			hsqlServer.setNoSystemExit(true);
			hsqlServer.setProperties(props);
			hsqlServer.start();

		
	}

}
