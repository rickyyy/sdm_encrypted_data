package eit.nl.utwente.sdm.encryptsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import eit.nl.utwente.sdm.encryptsql.helpers.GlobalProperties;

/* In our Database we have financial_data which is already in "encrypted" form.
 * */
public class Relation {

	private HashMap<String, List<Partition>> bucket;
	private HashMap<String, List<Identifier>> identificatioFunction;
	private List<String> attributes;

	public Relation(List<String> attributes, List<Long> domain,
			ArrayList<Integer> domainParts) {
		this.attributes = attributes;
		bucket = partitionFunction(attributes, domain, domainParts);
		identificatioFunction = identificatioFunction(bucket, attributes);
	}

	public static long attributesToInt(String s) {
		long res = 0;
		for (int i = 0; i < s.length(); i++) {
			res = res * 76 + (int) s.charAt(i) - 47;
		}
		return res;
	}

	public static String intToAttributes(long n) {
		String res = "";
		do {
			long charp = n % 76;
			char charpc = (char) (charp + 47);
			res = charpc + res;
			n = n / 76;
		} while (n != 0);
		return res;
	}

	public static long getUpperLimitString() {
		String res = "";
		String maxL = GlobalProperties.getInstance().getProperty(
				"MAX_LENGTH_STRING");
		for (int i = 0; i < Integer.parseInt(maxL); i++) {
			res += 'z';
		}
		return attributesToInt(res);
	}

	// public Relation store (Relation r, String etuple){
	// Relation rEncrypted = new Relation(r.Attributes, r.maxDomain);
	// for (String att : Attributes){
	// att.concat("_S"); // "_S" to underline the fact that is the "encrypted"
	// version
	// }
	// rEncrypted.addEtuple(rEncrypted, etuple);
	// return rEncrypted;
	// }

	public HashMap<String, List<Partition>> partitionFunction(
			List<String> attributes, List<Long> maxDomain,
			List<Integer> domainParts) {

		// create bucket ( Attribute, [list of partitions] )
		HashMap<String, List<Partition>> bucket = new HashMap<String, List<Partition>>();
		long max;
		Integer position;
		Integer partNum;

		for (String att : attributes) {

			position = attributes.indexOf(att);
			max = maxDomain.get(position); // take the elements in the same
											// position to the attribute
											// (related to it)
			partNum = domainParts.get(position); // take the elements in the
													// same position to the
													// attribute (related to it)
			ArrayList<Partition> partitions = new ArrayList<Partition>();

			long valueRange = max / partNum.longValue(); // to create equivalent
															// partitions
			long counterBot = 0;
			long counterUp = 0;

			/*
			 * for each attribute: split the domain in partitions (value) add
			 * tuples ex: (0, 199] add tuples to the bucket
			 */
			for (int i = 1; i <= partNum; i++) {
				if (i == (partNum)) {
					counterUp = max;
					Partition p = new Partition(counterBot, counterUp);
					// System.out.println("( " + counterBot + "," + counterUp +
					// " )\n");
					partitions.add(p);
				} else {
					counterUp += valueRange;
					Partition p = new Partition(counterBot, counterUp);
					// System.out.println("( " + counterBot + "," + counterUp +
					// " )\n");
					counterBot = counterUp + 1;
					partitions.add(p);
				}
			}
			bucket.put(att, partitions);

		}

		return bucket;

	}

	public HashMap<String, List<Identifier>> identificatioFunction(
			HashMap<String, List<Partition>> bucket, List<String> attributes) {

		HashMap<String, List<Identifier>> identifHshTbl = new HashMap<String, List<Identifier>>();

		for (String att : attributes) {
			List<Identifier> ident = new ArrayList<Identifier>();
			List<Partition> partitionList = new ArrayList<Partition>();
			partitionList = bucket.get(att);

			Random rnd = new Random();
			int size = partitionList.size();

			Integer position;
			Integer value;
			List<Integer> identValue = new ArrayList<Integer>();

			// create identifiers randomly without repetition
			for (int i = 0; i < size; i++) {
				if (identValue.isEmpty()) {
					value = rnd.nextInt(size * 2); // we take as a range the
													// double of the size of the
													// partition of one
													// attribute
					identValue.add(value);
				} else {
					do {
						value = rnd.nextInt(size * 2);
					} while (identValue.contains(value));
					identValue.add(value);
				}
			}

			// create identifiers for each partition
			for (Partition partition : partitionList) {
				position = partitionList.indexOf(partition);
				Integer v = identValue.get(position);
				Identifier ide = new Identifier(v);
				ident.add(position, ide);
				System.out.println(partition + " " + ide.getValue());
			}
			identifHshTbl.put(att, ident);
		}

		return identifHshTbl;

	}

	public ArrayList<String> mappingFunction(List<Comparable> values) {
		ArrayList<String> mappedAttributes = new ArrayList<String>();
		List<Partition> partTemp;
		List<Identifier> identTemp;
		Comparable temp;
		int position;
		System.out.println("Value size = " + values.size());
		for (int i = 0; i < values.size(); i++) {
			Comparable value = values.get(i);
			if (value instanceof String) {
				temp = attributesToInt((String) value); // transform string of
														// attributes in Integer
			} else {
				temp = value;
			}
			String attribute = attributes.get(i);
			partTemp = bucket.get(attribute); // retrieve the set of partition
												// for this attribute
			identTemp = identificatioFunction.get(attribute); // retrieve the
																// set of
																// identifier
																// for this
																// attribute
			// System.out.println("inside for att");

			for (Partition p : partTemp) { // TODO THERE IS AN ERROR HERE!!!
				position = partTemp.indexOf(p);
				// System.out.println("inside for part");

				if ((temp.compareTo(p.getUpperBound()) <= 0)
						&& (temp.compareTo(p.getLowerBound()) >= 0)) {
					Integer val = identTemp.get(position).getValue();
					mappedAttributes.add(String.valueOf(val));
				}
			}
		}
		return mappedAttributes; // return a list of mapped values of attributes
									// using identifier and partition values
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public int mapSingleAttribute(String attribute, Long value) {
		List<Partition> partTemp = bucket.get(attribute);
		List<Identifier> identTemp = identificatioFunction.get(attribute);

		for (Partition p : partTemp) {
			int position = partTemp.indexOf(p);

			if ((value.compareTo(p.getUpperBound()) <= 0)
					&& (value.compareTo(p.getLowerBound()) >= 0)) {
				System.out
						.println("Debug mapping function check upper lower bound");
				Integer val = identTemp.get(position).getValue();
				return val;
			}
		}
		return -1;
	}

	public int getIndexOf(String string) {
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).equals(string))
				return i;
		}
		return -1;
	}

}
