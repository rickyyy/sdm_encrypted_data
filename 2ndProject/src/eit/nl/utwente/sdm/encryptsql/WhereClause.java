package eit.nl.utwente.sdm.encryptsql;

public class WhereClause {

	public final boolean secondIsAttr;
	public final String[] elements;
	public final char operator;
	public final boolean secondIsString;

	public WhereClause(char operator, String[] elements, boolean secondIsAttr, boolean secondIsString) {
		this.operator = operator;
		this.elements = elements;
		this.secondIsAttr = secondIsAttr;
		this.secondIsString = secondIsString;
	}

}
