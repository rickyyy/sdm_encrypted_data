package eit.nl.utwente.sdm.poset;

import java.util.ArrayList;

public class Poset {
	int id;	//To differentiate between different poset
	ArrayList <NodeX> nodeList;	//correspond to the set H in the paper p.172
	
	//we assign the id of the Poset as id Consultant. Because client can have one consultant.
	//So the consultant represents the poset.
	public Poset(ArrayList<NodeX> nodes, int idConsultant){
		this.nodeList = nodes;
		this.id = idConsultant;
	}
	
	public ArrayList<NodeX> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<NodeX> h) {
		nodeList = h;
	}

	//returns the first node that covers x
	public NodeX virtualParent(Poset P, NodeX x){
		NodeX virtualParent = x.getParentVirtual();
		return virtualParent;
	}
	
	//returns the last node that covers x
	public NodeX consultantParent(Poset P, NodeX x){
		NodeX consultantParent = x.getParentConsul();
		return consultantParent;
	}
	
	//adds a relation x < y to P.
	public void InsertEdge(Poset P, NodeX x, NodeX y){
		
	}
	
	//creates a new node in P and returns it
	public void createNode(Poset P){
		
	}
}
