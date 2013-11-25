package eit.nl.utwente.sdm.encryptsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



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
	
	public HashMap<String, List<Partition>> partitionFunction (List<String> attributes, List<Integer> maxDomain, List<Integer> domainParts){
		
		//create bucket ( Attribute, [list of partitions] )
		HashMap<String, List<Partition>> bucket = new HashMap<String, List<Partition>>();
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
			ArrayList<Partition> partitions = new ArrayList<Partition>();
			
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
					Partition p = new Partition(counterBot, counterUp);
					System.out.println("( " + counterBot + "," + counterUp + " )\n");
					partitions.add(p);
				} else {
					counterUp += valueRange;
					Partition p = new Partition(counterBot, counterUp);
					System.out.println("( " + counterBot + "," + counterUp + " )\n");
					counterBot = counterUp+1;
					partitions.add(p);
				}
			}
			bucket.put(att, partitions);
			
		}
		
		return bucket;
		
	}
	
	public HashMap <String, List<Identifier>> identificatioFunction(HashMap<String, List<Partition>> bucket, List<String> attributes){
		
		HashMap <String, List<Identifier>> identifHshTbl = new HashMap<String, List<Identifier>>();
		
		for (String att : attributes){			
			List<Identifier> ident = new ArrayList<Identifier>();
			List<Partition> partitionList = new ArrayList<Partition>();
			partitionList = bucket.get(att);
			
			Random rnd = new Random();
			int size = partitionList.size();
			System.out.println("size : " + size);
			
			Integer position;
			Integer value;
			List<Integer> identValue = new ArrayList<Integer>();
			
			//create identifiers randomly without repetition
			for (int i = 0; i<size; i++){
				if (identValue.isEmpty()){
					value = rnd.nextInt(size*2);	//we take as a range the double of the size of the partition of one attribute
					identValue.add(value);
				}
				else {
					do{
						value = rnd.nextInt(size*2);
					} while (identValue.contains(value));
					identValue.add(value);
				}
			}
			
			System.out.println("size of identValue : " + identValue.size());
			// create identifiers for each partition		
			for (Partition partition : partitionList){
				position = partitionList.indexOf(partition);
				Integer v = identValue.get(position);
				Identifier ide = new Identifier(v);
				ident.add(position, ide);
			}
			identifHshTbl.put(att, ident);
		}
		
		return identifHshTbl;
		
	}
	
	
	public ArrayList <String> mappingFunction(HashMap<String, List<Partition>> part, HashMap <String, List<Identifier>> ident, List<String> attributes){
		ArrayList <String> mappedAttributes = new ArrayList<String>();
		List<Partition> partTemp;
		List<Identifier> identTemp;
		Integer temp;
		int position;
		for (String att : attributes){
			temp = Integer.parseInt(att);	//transform string of attributes in Integer
			partTemp = part.get(att);		//retrieve the set of partition for this attribute
			identTemp = ident.get(att);		//retrieve the set of identifier for this attribute
			for (Partition p : partTemp){
				position = partTemp.indexOf(p);
				if(temp <= p.getUpperBound()){
					Integer val = identTemp.get(position).getValue();
					mappedAttributes.add(String.valueOf(val));
				}
			}
		}
		return mappedAttributes;	// return a list of mapped values of attributes using identifier and partition values
	}
	
	
}
