package eit.nl.utwente.sdm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.*;

import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.Tuple;

public class TestPartitionCreation {
	@Test
	public void TestPartitionCreation(){
		HashMap<String, List<Tuple>> bucket;
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
		bucket = r.createPartition(attributes, domain, domainParts);
		List<Tuple> res = bucket.get("Id");
		List<Tuple> res2 = bucket.get("name");
		System.out.println(res2 + "\n");
		Assert.assertEquals(6, res2.size());
		Assert.assertEquals(3, res.size());
		
		Tuple check2 = res2.get(res2.size()-1);
		Integer upbound = check2.getUpperBound();
		System.out.println("UPBOUND: "+upbound);
		Integer domainExpected = 1000;
		
		Assert.assertEquals(domainExpected, upbound);
		
	}
}
