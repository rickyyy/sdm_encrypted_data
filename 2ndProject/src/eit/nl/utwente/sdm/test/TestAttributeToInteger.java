package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestAttributeToInteger {

	@Test
	public void test() {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("1231");
		attributes.add("Riccardo");
		ArrayList<String> attToInt = new ArrayList<String>();
		for (String a : attributes){
			char i = 'a';
			int b = (int)i;
			System.out.println(b+"\n");
		}
		System.out.println("Mapped List: " + attToInt);
		
	}

}
