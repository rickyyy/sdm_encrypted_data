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

	public boolean intersects(Partition p2) {
		if (p2.lowerBound >= lowerBound && p2.lowerBound <= upperBound) {
			return true;
		}
		if (p2.upperBound >= lowerBound && p2.upperBound <= upperBound) {
			return true;
		}
		if (p2.lowerBound <= lowerBound && p2.upperBound >= upperBound) {
			return true;
		}
		return false;
	}

	public boolean containsElementsLessThan(Partition p2) {
		if (lowerBound <= p2.upperBound)
			return true;
		return false;
	}

	public boolean containsElementsGraterThan(Partition p2) {
		if (upperBound >= p2.lowerBound)
			return true;
		return false;
	}
	
}
