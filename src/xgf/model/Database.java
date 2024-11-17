package xgf.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

	private String name;
	
	public Database(String name) {
		super();
		this.name = name;
	}

	
	public Connection connectToDatabase(String userName, String userPassword) throws SQLException {
		
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + name, userName, userPassword);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getUserType(Connection connection, String userName) throws SQLException {
		
		String type = "";
		
		PreparedStatement psSelect = connection.prepareStatement("SELECT type FROM users WHERE login = ?");
		psSelect.setString(1, userName);
		
		ResultSet result = psSelect.executeQuery();
		
		if(result.next()) {
			
			type = result.getString("type");
		}
		
		result.close();
		psSelect.close();
		
		return type;
	}
	
	public boolean insertClient(Connection connection, String userName, String password) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users (login, password, type) VALUES (?, ?, client)");
		psInsert.setString(1, userName);
		psInsert.setString(2, password);
		
//		ResultSet result = psSelect.executeQuery();
//		
//		if(result.next()) {
//			
//			type = result.getString("type");
//		}
//		
//		result.close();
//		psSelect.close();			
		
		return isSuccess;
	}


}
