package eit.nl.utwente.sdm.poset;

public class Edge {
	private NodeX x;
	private NodeX y;
	
	public Edge(NodeX x1, NodeX y1){
		this.x = x1;
		this.y = y1;
	}
	
	@Override
	public String toString() {
		return "Edge =[ " + x.toString() + " ] --> [ " + y.toString() + " ]" ;
	}
}
