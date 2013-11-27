package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.actors.Server;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;
import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

public class TestServerStorage {

	@Test
	public void test() {

		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("id_consultant");
		attributes.add("id_client");
		attributes.add("interest_rate");
		attributes.add("investment");
		attributes.add("statement");
		ArrayList<Long> domain = new ArrayList<Long>();
		String maxID = GlobalProperties.getInstance().getProperty("MAX_ID");
		domain.add(Long.parseLong(maxID));
		domain.add(Long.parseLong(maxID));
		String maxInt = GlobalProperties.getInstance().getProperty(
				"MAX_INTEREST");
		String maxInv = GlobalProperties.getInstance().getProperty(
				"MAX_INVESTMENT");
		domain.add(Long.parseLong(maxInt));
		domain.add(Long.parseLong(maxInv));
		long maxS = Relation.getUpperLimitString();
		domain.add(maxS);
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		String partitionID = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_ID");
		domainParts.add(Integer.parseInt(partitionID));
		domainParts.add(Integer.parseInt(partitionID));
		String partitionInt = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_INTEREST_RATE");
		domainParts.add(Integer.parseInt(partitionInt));
		String partitionInv = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_INVESTMENT");
		domainParts.add(Integer.parseInt(partitionInv));
		String partitionSt = GlobalProperties.getInstance().getProperty(
				"NO_PARTITIONS_STATEMENT");
		domainParts.add(Integer.parseInt(partitionSt));

		Connection conn = DBUtils.getDBConnection();
		Relation r = new Relation(attributes, domain, domainParts);
		
		Server s = new Server(r, conn);
		Client c = new Client(s, r, conn);

		Random rn = new Random();
		byte key[] = new byte[16];
		for (int i = 0; i < 16; i++) {
			key[i] = (byte) (rn.nextDouble() * 255);
		}
		c.setKey(key);
		c.store(1, 10, 12, 30000, "INVEST_ST");
	}
}