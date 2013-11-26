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
		attributes.add("123");
		int attPos = attributes.indexOf("123");
		attributes.add("550");
		int attPos1 = attributes.indexOf("550");
		ArrayList<Integer> domain = new ArrayList<Integer>();
		domain.add(1000);
		domain.add(4000);
		ArrayList<Integer> domainParts = new ArrayList<Integer>();
		domainParts.add(3);
		domainParts.add(20);
		
		Relation r = new Relation(attributes, domain);
		bucket = r.partitionFunction(attributes, domain, domainParts);
		List<Partition> part1 = bucket.get("123");
		List<Partition> part2 = bucket.get("550");
		identifHshTbl = r.identificatioFunction(bucket, attributes);
		mappedAttributes = r.mappingFunction(bucket, identifHshTbl, attributes);
		System.out.println("Mapped attribute size : " + mappedAttributes.size());

		System.out.println("Attribute Id:");
		System.out.println("Partition of Id:");
		List<Identifier> list = identifHshTbl.get("123");
		List<Identifier> list1 = identifHshTbl.get("550");

		for (Partition p : part1){
			int position = part1.indexOf(p);
			Identifier i = list.get(position);
			System.out.println("- " + p.toString() + " Identifier: " + i.toString());
		}

		System.out.println("Mapped Attribute : " + mappedAttributes.get(attPos));
		
		for (Partition p : part2){
			int position = part2.indexOf(p);
			Identifier i = list1.get(position);
			System.out.println("- " + p.toString() + " Identifier: " + i.toString());
		}
		System.out.println("Mapped Attribute : " + mappedAttributes.get(attPos1));

	}

}
