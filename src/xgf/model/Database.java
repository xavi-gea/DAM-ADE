package xgf.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class Database {

	private String name;
	
	public Database(String name) {
		super();
		this.name = name;
	}

	
	public Connection connectToDatabase(String userName, String userPassword) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
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
	
	public static boolean insertClient(Connection connection, String userName, String password) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users (login, password, type) VALUES (?, ?, ?)");
		psInsert.setString(1, userName);
		psInsert.setString(2, password);
		psInsert.setString(3, "client");
		
		int result = psInsert.executeUpdate();
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psInsert.close();
		
		return isSuccess;
	}
	
	public static void removeTable (Connection connection, String tableName) throws SQLException {
		
		Statement dropStatement = connection.createStatement();

		dropStatement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", tableName));

		dropStatement.close();
	}
	
	public static void createTable(Connection connection, String tableName, String[] tableColumns) throws SQLException {
		
		Statement createStatement = connection.createStatement();
		
		String statement = "CREATE TABLE " + tableName + " (";
		
		statement = statement.concat("id INT NOT NULL AUTO_INCREMENT, ");
		
		for (String column : tableColumns) {
			
			statement = statement.concat(column + " VARCHAR(30) , ");
		}
		
		statement = statement.concat("PRIMARY KEY (id))");
		
		createStatement.executeUpdate(statement);
		
		createStatement.close();
	}


	public static boolean insertPopulation(Connection connection, String string, List<String> fileValues) throws SQLException {
		
		boolean isSuccess = false;
		
		System.out.println(fileValues.toString());
		
		PreparedStatement psInsert = connection.prepareStatement("INSERT INTO population ("
				+ "country, "
				+ "population, "
				+ "density, "
				+ "area, "
				+ "fertility, "
				+ "age, "
				+ "urban, "
				+ "share"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		for (int i = 0; i < fileValues.size(); i++) {
			
			System.out.println("Size: " + fileValues.size());
			
			System.out.println("Se va a a insertar: " + fileValues.get(i));
			
			psInsert.setString(i + 1, fileValues.get(i));
		}
		
		int result = psInsert.executeUpdate();
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psInsert.close();
		
		return isSuccess;
		
	}


}
