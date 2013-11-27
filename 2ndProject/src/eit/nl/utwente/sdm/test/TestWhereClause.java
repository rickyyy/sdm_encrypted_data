package eit.nl.utwente.sdm.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.WhereClause;
import eit.nl.utwente.sdm.encryptsql.actors.Server;

public class TestWhereClause {

	@Test
	public void testWhere() {
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("investment");
		attributes.add("interest_rate");
		attributes.add("statement");
		WhereClause wc = Server.decodeWhereClause("select * from financial_data where id < 10", attributes);
		Assert.assertSame(wc.operator, '<');
		Assert.assertSame(wc.secondIsAttr, false);
		Assert.assertSame(wc.secondIsString, false);
		Assert.assertTrue(wc.elements[0].equals("id"));
		Assert.assertTrue(wc.elements[1].equals("10"));
		wc = Server.decodeWhereClause("select * from financial_data where id = statement", attributes);
		Assert.assertSame(wc.operator, '=');
		Assert.assertSame(wc.secondIsAttr, true);
		Assert.assertSame(wc.secondIsString, false);
		Assert.assertTrue(wc.elements[0].equals("id"));
		Assert.assertTrue(wc.elements[1].equals("statement"));
		wc = Server.decodeWhereClause("select * from financial_data where statement > 'bla'", attributes);
		Assert.assertSame(wc.operator, '>');
		Assert.assertSame(wc.secondIsAttr, false);
		Assert.assertSame(wc.secondIsString, true);
		Assert.assertTrue(wc.elements[0].equals("statement"));
		Assert.assertTrue(wc.elements[1].equals("bla"));
	}
	
}
