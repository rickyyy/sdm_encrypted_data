package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.Identifier;
import eit.nl.utwente.sdm.encryptsql.Partition;
import eit.nl.utwente.sdm.encryptsql.Relation;

public class TestIdentifierCreation {

	@Test
	public void test() {
		HashMap<String, List<Partition>> bucket;
		HashMap <String, List<Identifier>> identifHshTbl;

		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("Id");
		attributes.add("name");
		ArrayList<Integer> domain = new ArrayList<Integer>();
		domain.add(100);
		domain.add(1000);
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		domainParts.add(3);
		domainParts.add(6);
		
		Relation r = new Relation(attributes, domain);
		bucket = r.partitionFunction(attributes, domain, domainParts);
		List<Partition> part1 = bucket.get("Id");
		List<Partition> part2 = bucket.get("name");
		identifHshTbl = r.identificatioFunction(bucket, attributes);
		List <Identifier> ide1 = identifHshTbl.get("Id");
		List <Identifier> ide2 = identifHshTbl.get("name");
		System.out.println("Identifiers for partition of attribute Id are: " + ide1.get(0).getValue() + " " + ide1.get(1).getValue() + " " + ide1.get(2).getValue() + "\n");
		System.out.println("Identifiers for partition of attribute name are: " + ide2.get(0).getValue() + " " + ide2.get(1).getValue() + " " + ide2.get(2).getValue() + " " + ide2.get(3).getValue() + " " + ide2.get(4).getValue() + " " + ide2.get(5).getValue() +"\n");
		Assert.assertEquals(ide1.size(), part1.size());
		Assert.assertEquals(ide2.size(), part2.size());
	}

}
