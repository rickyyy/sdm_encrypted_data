package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.Identifier;
import eit.nl.utwente.sdm.encryptsql.Partition;
import eit.nl.utwente.sdm.encryptsql.Relation;

public class TestMappingFunction {

	@Test
	public void test() {
		HashMap<String, List<Partition>> bucket;
		HashMap <String, List<Identifier>> identifHshTbl;
		ArrayList<String> mappedAttributes;
		
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
		mappedAttributes = r.mappingFunction(bucket, identifHshTbl, attributes);
	}

}
