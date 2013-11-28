package eit.nl.utwente.sdm.encryptsql.actors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import eit.nl.utwente.sdm.encryptsql.EncryptedFinancialData;
import eit.nl.utwente.sdm.encryptsql.Identifier;
import eit.nl.utwente.sdm.encryptsql.Partition;
import eit.nl.utwente.sdm.encryptsql.Relation;
import eit.nl.utwente.sdm.encryptsql.WhereClause;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;

public class Server {

	private Relation relation;
	private Connection connection;

	public Server(Relation r, Connection conn) {
		this.relation = r;
		this.connection = conn;
	}
	
	public void store(EncryptedFinancialData ed) {
		Connection dbConnection = null;
		PreparedStatement insertData = null;
		String insertString = "insert into "
				+ "financial_data"
				+ "(etuple, id_cons_s, id_client_s, statement_s, investment_s, interest_rate_s) VALUES"
				+ "(?,?,?,?,?,?)";

		try {
			dbConnection = connection;
			insertData = dbConnection.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
			insertData.setString(1, ed.getEtuple());
			insertData.setInt(2, ed.getIdCons());
			insertData.setInt(3, ed.getIdClient());
			insertData.setInt(4, ed.getStatement());
			insertData.setInt(5, ed.getInvestment());
			insertData.setInt(6, ed.getInterest());

			// execute insert SQL statement
			insertData.execute();
			ResultSet generatedKeys = insertData.getGeneratedKeys();
			if (generatedKeys.next()) {
				ed.setId(generatedKeys.getInt(1));
			}
			System.out.println("New encrypted financial data was persisted");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} 
	}

	/**
	 * We accept only Attr1 <=> Val1, Attr1 <=> Attr2. AND / OR operators are not accepted 
	 * 
	 */
	public List<EncryptedFinancialData> executeQueryEncData(String preparedSql) {
		List<EncryptedFinancialData> result = new ArrayList<EncryptedFinancialData>();
		String mappedQuery = Server.mapQuery(preparedSql, relation);
		System.out.println("SERVER SIDE QUERY: " + mappedQuery);
		PreparedStatement prepSt;
		try {
			prepSt = connection.prepareStatement(mappedQuery, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = prepSt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String etuple = resultSet.getString(2);
				String idCons = resultSet.getInt(3) + "";
				String idClient = resultSet.getInt(4) + "";
				String stat = resultSet.getInt(5) + "";
				String inv = resultSet.getInt(6) + "";
				String interest = resultSet.getInt(7) + "";
				EncryptedFinancialData efc = new EncryptedFinancialData(id, etuple, idCons, idClient, stat, inv, interest);
				result.add(efc);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String mapQuery(String oldQuery, Relation relation) {
		if (!oldQuery.contains("where") && !oldQuery.contains("WHERE") )
			return oldQuery;
		WhereClause wc = Server.decodeWhereClause(oldQuery, relation.getAttributes());
		String resultQuery = "select * from financial_data ";
		long value = 0;
		if (!wc.secondIsAttr) {
			if (wc.secondIsString) {
				value = Relation.attributesToInt(wc.elements[1]);
			} else {
				value = Long.parseLong(wc.elements[1]);
			}
		}
			
		if (wc.operator == '=') {
			if (wc.secondIsAttr) {
				List<Partition> partitionsAttr1 = relation.getPartitions(wc.elements[0]);
				List<Identifier> identifiers1 = relation.getIdentifiers(wc.elements[0]);
				List<Partition> partitionsAttr2 = relation.getPartitions(wc.elements[1]);
				List<Identifier> identifiers2 = relation.getIdentifiers(wc.elements[1]);
				boolean first = true;
				for (int i = 0; i < partitionsAttr1.size(); i++) {
					Partition p1 = partitionsAttr1.get(i);
					for (int j = 0; j < partitionsAttr2.size(); j++) {
						Partition p2 = partitionsAttr2.get(j);
						if (p1.intersects(p2)) {
							if (first) {
								resultQuery += "where (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
								first = false;
							} else {
								resultQuery += " OR (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
							}
						}
					}
				}
			} else {
				int mappedAttr = relation.mapSingleAttribute(wc.elements[0], value);
				resultQuery += "where " + wc.elements[0] + "_s=" + mappedAttr;
			}
		} else if (wc.operator == '>') {
			if (wc.secondIsAttr) {
				List<Partition> partitionsAttr1 = relation.getPartitions(wc.elements[0]);
				List<Identifier> identifiers1 = relation.getIdentifiers(wc.elements[0]);
				List<Partition> partitionsAttr2 = relation.getPartitions(wc.elements[1]);
				List<Identifier> identifiers2 = relation.getIdentifiers(wc.elements[1]);
				boolean first = true;
				for (int i = 0; i < partitionsAttr1.size(); i++) {
					Partition p1 = partitionsAttr1.get(i);
					for (int j = 0; j < partitionsAttr2.size(); j++) {
						Partition p2 = partitionsAttr2.get(j);
						if (p1.containsElementsGraterThan(p2)) {
							if (first) {
								resultQuery += "where (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
								first = false;
							} else {
								resultQuery += " OR (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
							}
						}
					}
				}
			} else {
				List<Partition> partitions = relation.getPartitions(wc.elements[0]);
				List<Identifier> identifiers = relation.getIdentifiers(wc.elements[0]);
				boolean first = true;
				for (int i = 0; i < partitions.size(); i++) {
					Partition p = partitions.get(i);
					if (p.getUpperBound() >= value) {
						if (first) {
							resultQuery += "where " + wc.elements[0] + "_s=" + identifiers.get(i).getValue();
							first = false;
						} else {
							resultQuery += " or " + wc.elements[0] + "_s=" + identifiers.get(i).getValue();
						}
					}
				}
			}
		} else if (wc.operator == '<') {
			if (wc.secondIsAttr) {
				List<Partition> partitionsAttr1 = relation.getPartitions(wc.elements[0]);
				List<Identifier> identifiers1 = relation.getIdentifiers(wc.elements[0]);
				List<Partition> partitionsAttr2 = relation.getPartitions(wc.elements[1]);
				List<Identifier> identifiers2 = relation.getIdentifiers(wc.elements[1]);
				boolean first = true;
				for (int i = 0; i < partitionsAttr1.size(); i++) {
					Partition p1 = partitionsAttr1.get(i);
					for (int j = 0; j < partitionsAttr2.size(); j++) {
						Partition p2 = partitionsAttr2.get(j);
						if (p1.containsElementsLessThan(p2)) {
							if (first) {
								resultQuery += "where (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
								first = false;
							} else {
								resultQuery += " OR (" + wc.elements[0] + "_s=" + identifiers1.get(i).getValue() + " AND " + 
										wc.elements[1] + "_s=" + identifiers2.get(j).getValue() + ")";
							}
						}
					}
				}
			} else {
				List<Partition> partitions = relation.getPartitions(wc.elements[0]);
				List<Identifier> identifiers = relation.getIdentifiers(wc.elements[0]);
				boolean first = true;
				for (int i = 0; i < partitions.size(); i++) {
					Partition p = partitions.get(i);
					if (p.getLowerBound() <= value) {
						if (first) {
							resultQuery += "where " + wc.elements[0] + "_s=" + identifiers.get(i).getValue();
							first = false;
						} else {
							resultQuery += " or " + wc.elements[0] + "_s=" + identifiers.get(i).getValue();
						}
					}
				}
			}
		} 
		return resultQuery;
	}

	public static WhereClause decodeWhereClause(String preparedSql, List<String> list) {
		String expression = preparedSql.replaceAll(".*(where|WHERE)", "");
		char operator = 0;
		boolean secondIsAttr = false;
		boolean secondIsString = false;
		String elements[] = null;
		if (expression.contains("=")) {
			operator = '=';
			elements = expression.split("=");
			elements[0] = elements[0].trim();
			elements[1] = elements[1].trim();
		} else if (expression.contains("<")) {
			operator = '<';
			elements = expression.split("<");
			elements[0] = elements[0].trim();
			elements[1] = elements[1].trim();
		} else if (expression.contains(">")) {
			operator = '>';
			elements = expression.split(">");
			elements[0] = elements[0].trim();
			elements[1] = elements[1].trim();
		}
		if (list.contains(elements[1])) {
			secondIsAttr = true;
		} else {
			if (elements[1].contains("\'") || elements[1].contains("\"")) {
				secondIsString = true;
				elements[1] = elements[1].replaceAll("\'", "").replaceAll("\"", "");
			}
		}
		return new WhereClause(operator, elements, secondIsAttr, secondIsString);
	}

}
