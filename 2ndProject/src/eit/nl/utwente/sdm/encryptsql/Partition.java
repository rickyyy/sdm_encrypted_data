package eit.nl.utwente.sdm.encryptsql;

public class Partition{
	private Integer lowerBound;
	private Integer upperBound;
	
	public Partition (Integer low, Integer up){
		this.lowerBound = low;
		this.upperBound = up;
	}

	public Integer getLowerBound() {
		return lowerBound;
	}

	public Integer getUpperBound() {
		return upperBound;
	}

	@Override
	public String toString() {
		return "Partition [lowerBound=" + lowerBound + ", upperBound="
				+ upperBound + "]";
	}
	
}
