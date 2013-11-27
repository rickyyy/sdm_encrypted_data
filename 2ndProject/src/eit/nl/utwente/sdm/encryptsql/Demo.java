package eit.nl.utwente.sdm.encryptsql;

import java.util.ArrayList;

import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

public class Demo {

	public static void main(String[] args) {
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
		
	}
	
}
