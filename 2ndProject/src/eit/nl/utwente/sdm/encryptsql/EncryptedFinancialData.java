package eit.nl.utwente.sdm.encryptsql;


public class EncryptedFinancialData {
	private String etuple;
	private String idCons;
	private String idClient;
	private String interest;
	private String investment;
	private String statement;
	
	public EncryptedFinancialData (String et, String idConsEFD, String idClientEFD, String interestEFD, String investmentEFD, String statementEFD){
		this.etuple = et;
		this.idCons = idConsEFD;
		this.idClient = idClientEFD;
		this.interest = interestEFD;
		this.investment = investmentEFD;
		this.statement = statementEFD;
	}

	public String getEtuple() {
		return etuple;
	}

	public void setEtuple(String etuple) {
		this.etuple = etuple;
	}

	public String getIdCons() {
		return idCons;
	}

	public void setIdCons(String idCons) {
		this.idCons = idCons;
	}

	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getInvestment() {
		return investment;
	}

	public void setInvestment(String investment) {
		this.investment = investment;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
}
