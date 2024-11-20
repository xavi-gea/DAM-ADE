package xgf.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public static boolean setUpClient(Connection connection, String userName, String password, String tableName) throws SQLException {
		
		boolean isCreateSuccess = createClient(connection, userName, password);
		boolean isGrantSuccess = grantClient(connection, userName, tableName);
		boolean isUpdateSuccess = updateClient(connection, userName);
		
		return (isCreateSuccess && isGrantSuccess && isUpdateSuccess) ? true : false;
	}

	private static boolean createClient(Connection connection, String userName, String password) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psCreate = connection.prepareStatement("CREATE USER ?@localhost IDENTIFIED BY ?");
		psCreate.setString(1, userName);
		psCreate.setString(2, password);
		
		int result = psCreate.executeUpdate();
		
		System.out.println("result: " + result);
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psCreate.close();
		
		System.out.println("create: " + isSuccess);
		
		return isSuccess;
	}
	
	private static boolean grantClient(Connection connection, String userName, String tableName) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psGrant = connection.prepareStatement("GRANT SELECT ON population.population TO ?");
		psGrant.setString(1, userName);
		
		int result = psGrant.executeUpdate();
		
		System.out.println("result: " + result);
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psGrant.close();
		
		System.out.println("grant: " + isSuccess);
		
		return isSuccess;
	}


	private static boolean updateClient(Connection connection, String userName) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE USERS SET type = 'client' WHERE login = ?");
		psUpdate.setString(1, userName);
		
		int result = psUpdate.executeUpdate();
		
		System.out.println("result: " + result);
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psUpdate.close();
		
		System.out.println("update: " + isSuccess);
		
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
