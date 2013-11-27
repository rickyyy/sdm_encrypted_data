package eit.nl.utwente.sdm.poset;

import java.util.ArrayList;

public class Poset {
	int id;	//To differentiate between different poset
	ArrayList <NodeX> nodeList;	//correspond to the set H in the paper p.172
	ArrayList <Edge> relation;
	
	//we assign the id of the Poset as id Consultant. Because client can have one consultant.
	//So the consultant represents the poset.
	public Poset(ArrayList<NodeX> nodes, ArrayList<Edge> edges, int idConsultant){
		this.nodeList = nodes;
		this.relation = edges;
		this.id = idConsultant;
	}
	
	public ArrayList<NodeX> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<NodeX> h) {
		nodeList = h;
	}

	public ArrayList<Edge> getRelation() {
		return relation;
	}

	public void setRelation(ArrayList<Edge> relation) {
		this.relation = relation;
	}

	//returns the first node that covers x
	public NodeX leftParent(Poset P, NodeX x){
		NodeX leftParent = null;
		return leftParent;
	}
	
	//returns the last node that covers x
	public NodeX rightParent(Poset P, NodeX x){
		NodeX rightParent = null;
		return rightParent;
	}
	
	//adds a relation x < y to P.
	public void InsertEdge(Poset P, NodeX x, NodeX y){
		
	}
	
	//creates a new node in P and returns it
	public void createNode(Poset P){
		
	}
}
