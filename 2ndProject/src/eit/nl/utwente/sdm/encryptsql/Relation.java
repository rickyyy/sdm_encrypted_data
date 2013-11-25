package eit.nl.utwente.sdm.encryptsql;

import java.util.List;

public class Relation {
	private List<String> Attributes;
	
	public Relation (List<String> att){
		this.Attributes = att;
	}
	
	public List<String> getAttributes(Relation r){
		return r.Attributes;
	}
	
	public void addAttribute (Relation r, String s){
		r.Attributes.add(s);
	}
	
	public void addEtuple (Relation r, String s){
		r.Attributes.add(0, s);
	}
	
	public Relation store (Relation r, String etuple){
		Relation rEncrypted = new Relation(r.Attributes);
		for (String att : Attributes){
			att.concat("_S");		// "_S" to underline the fact that is the "encrypted" version
		}
		rEncrypted.addEtuple(rEncrypted, etuple);
		return rEncrypted;
	}
}
