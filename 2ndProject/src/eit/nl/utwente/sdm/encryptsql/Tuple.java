package eit.nl.utwente.sdm.encryptsql;

public class Tuple{
	private Integer lowerBound;
	private Integer upperBound;
	
	public Tuple (Integer low, Integer up){
		this.lowerBound = low;
		this.upperBound = up;
	}

	public Integer getLowerBound() {
		return lowerBound;
	}

	public Integer getUpperBound() {
		return upperBound;
	}
	
}
