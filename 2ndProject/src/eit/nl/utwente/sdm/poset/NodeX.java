package eit.nl.utwente.sdm.poset;
import java.math.BigInteger;
import java.util.ArrayList;

import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.actors.Consultant;

//Every client is an independent User Class (following the definition in the paper)
public class NodeX {
	private int flag; //if 0 is a Virtual Node, if 1 is Consultant, if 2 is Client
	private int identifier;
	private BigInteger publicKey;
	private BigInteger privateKey;
	private NodeX parentConsul;
	private NodeX parentVirtual;
	private NodeX virtualChildren;
	private ArrayList<NodeX> consultChildren;
	private ArrayList<Edge> relations = new ArrayList<Edge>();

	//General Constructor
	public NodeX(int clientOrConsId, int f){
		this.identifier = clientOrConsId;
		this.flag = f;
	}
	
	public NodeX(Consultant c){
		this.identifier = c.getId();
		this.flag = 1;
	}
	
	public NodeX(Client cl){
		this.identifier = cl.getId();
		this.flag = 2;
	}

	//Virtual node doesn't have any identifier.
	public NodeX(int f){
		this.flag = f;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public NodeX getParentConsul() {
		return parentConsul;
	}

	public void setParentConsul(NodeX parentConsul) {
		this.parentConsul = parentConsul;
	}

	public NodeX getParentVirtual() {
		return parentVirtual;
	}

	public void setParentVirtual(NodeX parentVirtual) {
		this.parentVirtual = parentVirtual;
	}

	public NodeX getVirtualChildren() {
		return virtualChildren;
	}

	public void setVirtualChildren(NodeX virtualChildren) {
		this.virtualChildren = virtualChildren;
	}

	public ArrayList<NodeX> getConsultChildren() {
		return consultChildren;
	}

	public void setConsultChildren(ArrayList<NodeX> consultChildren) {
		this.consultChildren = consultChildren;
	}

	public ArrayList<Edge> getRelations() {
		return relations;
	}

	public void setRelations(ArrayList<Edge> relations) {
		this.relations = relations;
	}

	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public BigInteger getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(BigInteger publicKey) {
		this.publicKey = publicKey;
	}
	public BigInteger getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}
	
	public byte[] getPrivateKeyByteArray() {
		byte [] res = new byte[16];
		for (int i = 0; i < 16; i++) {
			res[i] = 0;
		}
		byte[] byteArray = privateKey.toByteArray();
		for (int i = 0; i < res.length && i< byteArray.length; i++) {
			res[i] = byteArray[i];
		}
		return res;
	}

	public byte[] getPublicKeyByteArray() {
		byte [] res = new byte[16];
		for (int i = 0; i < 16; i++) {
			res[i] = 0;
		}
		byte[] byteArray = publicKey.toByteArray();
		for (int i = 0; i < res.length && i< byteArray.length; i++) {
			res[i] = byteArray[i];
		}
		return res;
	}
	@Override
	public String toString() {
		return "NodeX [flag=" + flag + ", identifier=" + identifier + ", publicKey=" + publicKey
				+ ", privateKey=" + privateKey + "]";
	}

	public void addEdge(Edge edge) {
		relations.add(edge);
	}

	public static byte[] computeKey(BigInteger privateKey2, BigInteger vPk) {
		BigInteger secretChild = vPk.pow(privateKey2.intValue());
		secretChild = secretChild.mod(CertifiedAuthority.prime);
		byte byteArray[] = secretChild.toByteArray();
		byte [] res = new byte[16];
		for (int i = 0; i < 16; i++) {
			res[i] = 0;
		}
		for (int i = 0; i < res.length && i< byteArray.length; i++) {
			res[i] = byteArray[i];
		}
		return res;
	}
	
}
