package eit.nl.utwente.sdm.encryptsql;

public class Identifier {
	
	private Integer value;
	
	public Identifier (Integer integ){
		this.value = integ;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
