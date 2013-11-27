package eit.nl.utwente.sdm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.*;

import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.Partition;

public class TestPartitionCreation {
	@Test
	public void test(){
		HashMap<String, List<Partition>> bucket;
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("Id");
		attributes.add("name");
		ArrayList<Long> domain = new ArrayList<Long>();
		domain.add((long)100);
		domain.add((long)1000);
		
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		domainParts.add(3);
		domainParts.add(6);
		
		Relation r = new Relation(attributes, domain, domainParts);
		bucket = r.partitionFunction(attributes, domain, domainParts);
		List<Partition> res = bucket.get("Id");
		List<Partition> res2 = bucket.get("name");
		System.out.println(res2 + "\n");
		Assert.assertEquals(6, res2.size());
		Assert.assertEquals(3, res.size());
		
		Partition check2 = res2.get(res2.size()-1);
		Long upbound = check2.getUpperBound(); //check if the partition reach the upperbound
		Integer convUpBound = upbound.intValue();
		System.out.println("UPBOUND: "+convUpBound);
		Integer domainExpected = 1000;
		
		Assert.assertEquals(domainExpected, convUpBound);
		
	}
}
