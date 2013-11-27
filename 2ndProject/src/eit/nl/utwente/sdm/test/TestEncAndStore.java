package eit.nl.utwente.sdm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.Identifier;
import eit.nl.utwente.sdm.encryptsql.Partition;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

public class TestEncAndStore {

	@Test
	public void simpleTest() {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("investment");
		attributes.add("interest_rate");
		attributes.add("statement");
		ArrayList<Long> domain = new ArrayList<Long>();
		String maxInv = GlobalProperties.getInstance().getProperty("MAX_INVESTMENT");
		String maxInt = GlobalProperties.getInstance().getProperty("MAX_INTEREST");
		domain.add(Long.parseLong(maxInv));
		domain.add(Long.parseLong(maxInt));
		long maxS = Relation.getUpperLimitString();
		domain.add(maxS);
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		String partitionInv = GlobalProperties.getInstance().getProperty("NO_PARTITIONS_INVESTMENT");
		domainParts.add(Integer.parseInt(partitionInv));
		String partitionInt = GlobalProperties.getInstance().getProperty("NO_PARTITIONS_INTEREST_RATE");
		domainParts.add(Integer.parseInt(partitionInt));
		String partitionSt = GlobalProperties.getInstance().getProperty("NO_PARTITIONS_STATEMENT");
		domainParts.add(Integer.parseInt(partitionSt));
		
		Relation r = new Relation(attributes, domain, domainParts);
		
		List<Comparable> values = new ArrayList<Comparable>();
		values.add(new Long(3000));
		values.add(new Long(2));
		values.add("Gambling");
		ArrayList<String> mappingFunction = r.mappingFunction(values);
		int i = 0;
		System.out.println("The results:");
		for (String mappedValue : mappingFunction) {
			System.out.println(values.get(i++) + " " +  mappedValue);
		}
		Assert.assertSame(mappingFunction.size(), 3);
	}
	
}
