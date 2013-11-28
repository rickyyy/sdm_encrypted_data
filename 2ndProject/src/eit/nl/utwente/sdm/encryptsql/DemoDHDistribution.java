package eit.nl.utwente.sdm.encryptsql;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import eit.nl.utwente.sdm.poset.CertifiedAuthority;
import eit.nl.utwente.sdm.poset.Edge;
import eit.nl.utwente.sdm.poset.NodeX;
import eit.nl.utwente.sdm.poset.Poset;

public class DemoDHDistribution {

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
		List<Consultant> consultants = DBUtils.getConsultants();
		Map<Client, NodeX> clientNodes = new HashMap<Client, NodeX>();
		Map<Client, NodeX> virtualNodes = new HashMap<Client, NodeX>();
		for (Client client : clients) {
			client.setServer(s);
			client.setRelation(r);
			client.setDB(inMemoryDB);
			NodeX clientNode = new NodeX(client);
			NodeX virtualNode = new NodeX(0);
			clientNode.setParentVirtual(virtualNode);
			Edge edge = new Edge(virtualNode, clientNode);
			clientNode.addEdge(edge);
			virtualNode.addEdge(edge);
			clientNodes.put(client, clientNode);
			virtualNodes.put(client, virtualNode);
		}
		CertifiedAuthority ca = new CertifiedAuthority();
		for (Consultant consultant : consultants) {
			ArrayList<NodeX> posetNodes = new ArrayList<NodeX>();
			NodeX consultantNode = new NodeX(consultant);
			posetNodes.add(consultantNode);
			consultant.setServer(s);
			consultant.setRelation(r);
			consultant.setDb(inMemoryDB);
			ArrayList<NodeX> children = new ArrayList<NodeX>();
			List<Client> childrenClients = new ArrayList<Client>();
			for (Client client : clients) {
				if (consultant.getId() == client.getIdConsultant()) {
					NodeX clientNode = clientNodes.get(client);
					clientNode.setParentConsul(consultantNode);
					Edge edge = new Edge(consultantNode, clientNode);
					clientNode.addEdge(edge);
					consultantNode.addEdge(edge);
					posetNodes.add(clientNode);
					posetNodes.add(virtualNodes.get(client));
					children.add(clientNode);
					childrenClients.add(client);
				}
			}
			consultantNode.setConsultChildren(children);
			Poset p = new Poset(posetNodes, consultantNode.getIdentifier());
			BigInteger g = ca.keyAssignment(p);
			consultant.setNodeX(childrenClients, consultantNode);
			for (Client cl : childrenClients) {
				cl.setNodeX(clientNodes.get(cl));
				cl.setVirtualNodeX(virtualNodes.get(cl));
			}
		}
		for (Client cl : clients) {
			System.out.println(cl.getName() + " keys");
			System.out.println(cl.getNodeX().getPrivateKey() + " " + cl.getNodeX().getPublicKey());
			System.out.println(cl.getVirtualX().getPrivateKey() + " " + cl.getVirtualX().getPublicKey());
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
