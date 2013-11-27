package eit.nl.utwente.sdm.encryptsql;


public class EncryptedFinancialData {
	
	private int id;
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
	
	public EncryptedFinancialData (int id, String et, String idConsEFD, String idClientEFD, String interestEFD, String investmentEFD, String statementEFD){
		this.id = id;
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

	public int getIdCons() {
		int id = Integer.parseInt(idCons);
		return id;
	}

	@Override
	public String toString() {
		return "EncryptedFinancialData [etuple=" + etuple + ", idCons="
				+ idCons + ", idClient=" + idClient + ", interest=" + interest
				+ ", investment=" + investment + ", statement=" + statement
				+ "]";
	}

	public void setIdCons(String idCons) {
		this.idCons = idCons;
	}

	public int getIdClient() {
		int id = Integer.parseInt(this.idClient);
		return id;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public int getInterest() {
		int id = Integer.parseInt(this.interest);
		return id;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public int getInvestment() {
		int id = Integer.parseInt(this.investment);
		return id;	
	}

	public void setInvestment(String investment) {
		this.investment = investment;
	}

	public int getStatement() {
		int id = Integer.parseInt(this.statement);
		return id;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public void setId(int int1) {
		this.id = int1;
	}
	
	public int getId(){
		return this.id;
	}
	
}
