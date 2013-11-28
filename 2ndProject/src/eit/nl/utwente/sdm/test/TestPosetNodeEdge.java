package eit.nl.utwente.sdm.test;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import eit.nl.utwente.sdm.poset.CertifiedAuthority;
import eit.nl.utwente.sdm.poset.Edge;
import eit.nl.utwente.sdm.poset.NodeX;
import eit.nl.utwente.sdm.poset.Poset;

public class TestPosetNodeEdge {

	//test done with general constructor of NodeX (for client and consultant)
	@Test
	public void test() {
		ArrayList<NodeX> allNode = new ArrayList<NodeX>();
		NodeX consultant = new NodeX(123, 1);
		NodeX client1 = new NodeX(52, 2);
		NodeX client2 = new NodeX(64, 2);
		NodeX client3 = new NodeX(63, 2);
		
		client1.setParentConsul(consultant);
		client2.setParentConsul(consultant);
		client3.setParentConsul(consultant);
		
		allNode.add(consultant);
		allNode.add(client1);
		allNode.add(client2);
		allNode.add(client3);
		
		ArrayList<NodeX> clients = new ArrayList<NodeX>();
		clients.add(client1);
		clients.add(client2);
		clients.add(client3);
		
		consultant.setConsultChildren(clients);
		
		Edge conC1 = new Edge(consultant, client1);
		Edge conC2 = new Edge(consultant, client2);
		Edge conC3 = new Edge(consultant, client3);
		
		ArrayList<Edge> relCons = new ArrayList<Edge>();
		relCons.add(conC1);
		relCons.add(conC2);
		relCons.add(conC3);
		
		ArrayList<Edge> relC1 = new ArrayList<Edge>();
		relC1.add(conC1);
		ArrayList<Edge> relC2 = new ArrayList<Edge>();
		relC2.add(conC2);
		ArrayList<Edge> relC3 = new ArrayList<Edge>();
		relC3.add(conC3);

		consultant.setRelations(relCons);
		client1.setRelations(relC1);
		client2.setRelations(relC2);
		client3.setRelations(relC3);
		
		for (NodeX c : clients){
			NodeX virtualParent = new NodeX(0);
			allNode.add(virtualParent);
			Edge virtualEdge = new Edge(virtualParent, c);
			c.setParentVirtual(virtualParent);
			c.getRelations().add(virtualEdge);
		}
		
		Poset p = new Poset(allNode, consultant.getIdentifier());
		CertifiedAuthority ca = new CertifiedAuthority();
		BigInteger g = ca.keyAssignment(p);
		
		BigInteger secKey = consultant.getPrivateKey();
		BigInteger pubKey = client1.getParentVirtual().getPublicKey();
		BigInteger secretChild = pubKey.pow(secKey.intValue());
		secretChild = secretChild.mod(CertifiedAuthority.prime);
		
		System.out.println(client1.getParentConsul().toString());
		System.out.println(client1.getParentVirtual().toString());

		Assert.assertEquals(secretChild, client1.getPrivateKey());
		
	}

}
