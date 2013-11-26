package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class TestAttributeToInteger {

	@Test
	public void test() {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("1231");
		attributes.add("zzzzzzzzzz");
//		ArrayList<String> attToInt = new ArrayList<String>();
//		for (String a : attributes){
//			String temp = a;
//			long res = 0;
//			long resRet = 0;
//			System.out.println("String : " + a + "\n" + "Value Decimal = " + res + "\n");
//		}
//		System.out.println("Mapped List: " + attToInt);
		System.out.println(stringToInt("0"));
		long x = stringToInt("zzzzzzzzzz");
		System.out.println(x);
		System.out.println(intToString(x));
		Assert.assertEquals(intToString(x), "zzzzzzzzzz");
	}
	
	private long stringToInt(String s) {
		long res = 0;
		for(int i = 0; i<s.length(); i++){
			res = res*76 + (int)s.charAt(i) - 47;
		}
		return res;
	}
	
	private String intToString(long n) {
		String res = "";
		do {
			long charp =  n % 76;
			char charpc = (char) (charp + 47);
			res = charpc + res;
			n = n / 76;
		} while (n != 0);
		return res;
	}

}
