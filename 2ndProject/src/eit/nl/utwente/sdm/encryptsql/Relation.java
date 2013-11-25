package eit.nl.utwente.sdm.encryptsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.PartialResultException;


/* In our Database we have financial_data which is already in "encrypted" form.
 * */
public class Relation {
	private List<String> Attributes;
	private List<Integer> maxDomain; //maximum value for that attribute
	
	public Relation (List<String> att, List<Integer> max){
		this.Attributes = att;
		this.maxDomain = max;
	}
	
	public List<String> getAttributes(Relation r){
		return r.Attributes;
	}
	
	public List<Integer> getMaxDomain() {
		return maxDomain;
	}

	public void setMaxDomain(List<Integer> maxDomain) {
		this.maxDomain = maxDomain;
	}

	public void setAttributes(List<String> attributes) {
		Attributes = attributes;
	}

	public void addAttribute (Relation r, String s){
		r.Attributes.add(s);
	}
	
	public void addEtuple (Relation r, String s){
		r.Attributes.add(0, s);
	}
	
	public Relation store (Relation r, String etuple){
		Relation rEncrypted = new Relation(r.Attributes, r.maxDomain);
		for (String att : Attributes){
			att.concat("_S");		// "_S" to underline the fact that is the "encrypted" version
		}
		rEncrypted.addEtuple(rEncrypted, etuple);
		return rEncrypted;
	}
	
	public HashMap<String, List<Tuple>> createPartition (List<String> attributes, List<Integer> maxDomain, List<Integer> domainParts){
		
		//create bucket ( Attribute, [list of partitions] )
		HashMap<String, List<Tuple>> bucket = new HashMap<String, List<Tuple>>();
		Integer max;
		Integer position;
		Integer partNum;
		for (String att : attributes){
			
			System.out.println("Attributes analized now : " + att + "\n");
			position = attributes.indexOf(att);
			max = maxDomain.get(position);	//take the elements in the same position to the attribute (related to it)
			System.out.println("His max value is : " + max + "\n");
			partNum = domainParts.get(position);	//take the elements in the same position to the attribute (related to it)
			System.out.println("It should be divided in X parts : " + partNum + "\n");
			ArrayList<Tuple> partitions = new ArrayList<Tuple>();
			
			int valueRange = max / partNum; // to create equivalent partitions
			int counterBot = 0;
			int counterUp = 0;
			
			/* for each attribute:
			 split the domain in partitions (value)
			 add tuples ex: (0, 199]
			 add tuples to the bucket*/
			for (int i=1; i<=partNum; i++){
				if (i == (partNum)){
					counterUp = max;
					Tuple p = new Tuple(counterBot, counterUp);
					System.out.println("( " + counterBot + "," + counterUp + " )\n");
					partitions.add(p);
				} else {
					counterUp += valueRange;
					Tuple p = new Tuple(counterBot, counterUp);
					System.out.println("( " + counterBot + "," + counterUp + " )\n");
					counterBot = counterUp+1;
					partitions.add(p);
				}
			}
			bucket.put(att, partitions);
			
		}
		
		return bucket;
		
	}
}
