package eit.nl.utwente.sdm.encryptsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;

import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.actors.Consultant;
import eit.nl.utwente.sdm.encryptsql.guis.GUIClient;
import eit.nl.utwente.sdm.encryptsql.guis.GUIConsultants;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;
import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

public class Demo {

	public static void main(String[] args) {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("id_cons");
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
		Connection inMemoryDB = createInMemoryDBConn();
		eit.nl.utwente.sdm.encryptsql.actors.Server s = new eit.nl.utwente.sdm.encryptsql.actors.Server(r, DBUtils.getDBConnection());
				
		List<Client> clients = DBUtils.getClients();
		List<byte[]> keys = new ArrayList<byte[]>();
		List<Consultant> consultants = DBUtils.getConsultants();
		for (Client client : clients) {
			client.setServer(s);
			client.setRelation(r);
			client.setDB(inMemoryDB);
			
			Random rn = new Random();
			byte key[] = new byte[16];
			for (int i = 0; i < 16; i++) {
				key[i] = (byte) (rn.nextDouble() * 255);
			}
			client.setKey(key);
			keys.add(key);
		}
		for (Consultant consultant : consultants) {
			consultant.setServer(s);
			consultant.setRelation(r);
			consultant.setDb(inMemoryDB);
			for (int i = 0; i < clients.size(); i++) {
				Client c = clients.get(i);
				if (c.getIdConsultant() == consultant.getId()) {
					consultant.setKey(c.getId(), keys.get(i));
				}
			}
		}
		GUIClient guiClient = new GUIClient(clients);
		GUIConsultants guiCon = new GUIConsultants(consultants);
		
	}

	public static Connection createInMemoryDBConn() {
		try {
			final Server hsqlServer = startServer();
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

				@Override
				public void run() {
					hsqlServer.stop();
				}
			}));
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
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
		
			return conn;
		} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return null;
		}
	}

	public static Server startServer() throws IOException, AclFormatException {
		HsqlProperties props = new HsqlProperties();
		props.setProperty("server.database.0", "mem:test1");
		props.setProperty("server.database.1", "mem:test2");
		Server hsqlServer = new Server();
		props.setProperty("server.dbname.0", "xdb");
		hsqlServer.setRestartOnShutdown(false);
		hsqlServer.setNoSystemExit(true);
		hsqlServer.setProperties(props);
		hsqlServer.start();
		return hsqlServer;
	}

}
