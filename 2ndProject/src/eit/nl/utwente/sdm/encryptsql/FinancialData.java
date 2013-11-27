package eit.nl.utwente.sdm.encryptsql;


public class FinancialData {
	
	public final int id;
	public final int idCons;
	public final int idClient;
	public final int interest;
	public final int investment;
	public final String statement;
	

	public FinancialData(int id, int idCons, int idClient, int interest, int investment, String statement) {
		this.id = id;
		this.interest = interest;
		this.investment = investment;
		this.statement = statement;
		this.idCons = idCons;
		this.idClient = idClient;
	}

}
