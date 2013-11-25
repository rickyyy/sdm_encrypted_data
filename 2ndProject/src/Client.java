import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Client {
	private int id;
	private String name;
	private String contact;
	
	public Client(int idClient, String nm, String cnt){
		this.id = idClient;
		this.name = nm;
		this.contact = cnt;
	}
	
	public Client(String nm, String cnt){
		this.name = nm;
		this.contact = cnt;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public String getName (){
		return name;
	}
	
	public void setContact(String newContact){
		contact = newContact;
	}
	
	public String getContact(){
		return contact;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void persist() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement insertData = null;
		String insertString = "insert into "
				+ "client"
				+ "(name, contact) VALUES"
				+ "(?,?)";

		try {
			dbConnection = DBUtils.getDBConnection();
			insertData = dbConnection.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
			insertData.setString(1, name);
			insertData.setString(2, contact);

			// execute insert SQL statement
			insertData.execute();
			ResultSet generatedKeys = insertData.getGeneratedKeys();
			if (generatedKeys.next()) {
				setId(generatedKeys.getInt(1));
			}
			System.out.println("New client was persisted");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} finally {

			if (insertData != null) {
				insertData.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}
	
	public void delete() {
		if (getId() == DBUtils.ID_NOT_SET) { 
			//entity not yet persisted
			return;
		}
		Connection dbConnection = null;
		PreparedStatement sqlStatement = null;
		String sqlString = "delete from client where id = ?";

		try {
			dbConnection = DBUtils.getDBConnection();
			sqlStatement = dbConnection.prepareStatement(sqlString);
			sqlStatement.setInt(1, getId());
			sqlStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
