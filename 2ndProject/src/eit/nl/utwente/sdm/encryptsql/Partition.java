package eit.nl.utwente.sdm.encryptsql;

public class Partition{
	private long lowerBound;
	private long upperBound;
	
	public Partition (long low, long up){
		this.lowerBound = low;
		this.upperBound = up;
	}

	public long getLowerBound() {
		return lowerBound;
	}

	public long getUpperBound() {
		return upperBound;
	}

	@Override
	public String toString() {
		return "Partition [lowerBound=" + lowerBound + ", upperBound="
				+ upperBound + "]";
	}
	
}
