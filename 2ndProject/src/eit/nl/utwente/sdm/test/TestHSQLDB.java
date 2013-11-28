package eit.nl.utwente.sdm.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.FinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;
import eit.nl.utwente.sdm.encryptsql.helpers.EncryptionHelper;
import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

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
		String createTable = "CREATE TABLE financial_data2 ( "
				+ "id INTEGER NOT NULL,"
				+ "statement VARCHAR(10) NOT NULL,"
				+ "investment INTEGER NOT NULL,"
				+ "interest_rate INTEGER NOT NULL," + "PRIMARY KEY (id)"
				+ ")";
		PreparedStatement createTbStatement = conn
				.prepareStatement(createTable);
		createTbStatement.execute();
		
		String insertSQL = "insert into "
				+ "financial_data2"
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
		String selectSQL = "select * from financial_data2 where id < ? ";
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
	
	@Test
	public void testClientSearchInEncData() throws SQLException, IOException, AclFormatException {
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
				+ "id_cons INTEGER NOT NULL,"
				+ "id_client INTEGER NOT NULL,"
				+ "statement VARCHAR(10) NOT NULL,"
				+ "investment INTEGER NOT NULL,"
				+ "interest_rate INTEGER NOT NULL," + "PRIMARY KEY (id)"
				+ ")";
		PreparedStatement createTbStatement = conn
				.prepareStatement(createTable);
		createTbStatement.execute();
		
		Relation r = createRelation();
		MockServer s = new MockServer(r, conn);
		Random rn = new Random();
		byte key[] = new byte[16];
		for (int i = 0; i < 16; i++) {
			key[i] = (byte)(rn.nextDouble() * 255);
		}
		s.setKey(key);
		Client c = new Client(s, r, conn);
		c.setKey(key);
		List<FinancialData> searchEncData = c.searchEncData("select * from financial_data where id<10");
		Assert.assertSame(searchEncData.size(), 1);
		Assert.assertSame(searchEncData.get(0).id, 1);
		
	}
	
	class MockServer extends eit.nl.utwente.sdm.encryptsql.actors.Server {

		private byte[] key;
		private Relation r;

		public MockServer(Relation r, Connection conn) {
			super(r, conn);
			this.r = r;
		}
		
		public void setKey(byte[] key) {
			this.key = key;
		}
		
		@Override
		public List<EncryptedFinancialData> executeQueryEncData(String sql) {
			List<EncryptedFinancialData> result = new ArrayList<EncryptedFinancialData>();
			String encrypt = EncryptionHelper.encrypt("10-10-2-10000-GAMBLING", key);
			List<Comparable> values = new ArrayList<Comparable>();
			values.add(new Long(10));
			values.add(new Long(10));
			values.add(new Long(1000));
			values.add(new Long(2));
			values.add("GAMBLING");
			ArrayList<String> mappingFunction = r.mappingFunction(values);
			EncryptedFinancialData efd = new EncryptedFinancialData(1, encrypt, mappingFunction.get(0), mappingFunction.get(1), mappingFunction.get(2), mappingFunction.get(3), mappingFunction.get(4));
			result.add(efd);
			encrypt = EncryptionHelper.encrypt("10-10-2-10000-GAMBLING", key);
			values = new ArrayList<Comparable>();
			values.add(new Long(10));
			values.add(new Long(10));
			values.add(new Long(1000));
			values.add(new Long(2));
			values.add("GAMBLING");
			mappingFunction = r.mappingFunction(values);
			efd = new EncryptedFinancialData(10, encrypt, mappingFunction.get(0), mappingFunction.get(1), mappingFunction.get(2), mappingFunction.get(3), mappingFunction.get(4));
			result.add(efd);
			return result;
		}
		
		
	}
	
	private Relation createRelation() {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("id_consultant");
		attributes.add("id_client");
		attributes.add("investment");
		attributes.add("interest_rate");
		attributes.add("statement");
		ArrayList<Long> domain = new ArrayList<Long>();
		String maxID = GlobalProperties.getInstance().getProperty("MAX_ID");
		domain.add(Long.parseLong(maxID));
		domain.add(Long.parseLong(maxID));
		String maxInv = GlobalProperties.getInstance().getProperty(
				"MAX_INVESTMENT");
		String maxInt = GlobalProperties.getInstance().getProperty(
				"MAX_INTEREST");
		domain.add(Long.parseLong(maxInv));
		domain.add(Long.parseLong(maxInt));
		long maxS = Relation.getUpperLimitString();
		domain.add(maxS);
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		String partitionID = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_ID");
		domainParts.add(Integer.parseInt(partitionID));
		domainParts.add(Integer.parseInt(partitionID));
		String partitionInv = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_INVESTMENT");
		domainParts.add(Integer.parseInt(partitionInv));
		String partitionInt = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_INTEREST_RATE");
		domainParts.add(Integer.parseInt(partitionInt));
		String partitionSt = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_STATEMENT");
		domainParts.add(Integer.parseInt(partitionSt));

		Relation r = new Relation(attributes, domain, domainParts);
		return r;
	}

}
