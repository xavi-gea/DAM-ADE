package xgf.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Database {

	private String name;
	private static String[] currentQueryHeader;
	private static String[][] currentQueryRows;
	
	public Database(String name) {
		super();
		this.name = name;
	}

	
	public Connection connectToDatabase(String userName, String userPassword) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + name, userName, userPassword);
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
	
	public static boolean isValidQuery(String query, String userType) {

		if (!query.contains("SELECT") || !query.contains("FROM")) {
			
			return false;
		}
		
		if (query.contains("USERS") && userType.equals("client")) {
			
			return false;
		}
		
		if (!query.contains("USERS") && !query.contains("POPULATION")) {
			
			return false;
		}
		
		return true;
	}


	public static boolean setUpClient(Connection connection, String userName, String password, String tableName) throws SQLException {
		
		createClient(connection, userName, password);
		grantClient(connection, userName);
		boolean isInsertSuccess = insertClient(connection, userName, password);
		boolean isUpdateSuccess = updateClient(connection, userName);
		
		return (isInsertSuccess && isUpdateSuccess) ? true : false;
	}

	private static void createClient(Connection connection, String userName, String password) throws SQLException {
		
		Statement createStatement = connection.createStatement();
		
		String statement = String.format("CREATE USER '%s' IDENTIFIED BY '%s'", userName, password);

		createStatement.execute(statement);
		
		createStatement.close();
	}
	
	private static void grantClient(Connection connection, String userName) throws SQLException {
				
		Statement grantStatement = connection.createStatement();
		
		String statement = String.format("GRANT SELECT ON population.population TO '%s'", userName);
		
		grantStatement.executeUpdate(statement);
		
		grantStatement.close();
	}


	private static boolean insertClient(Connection connection, String userName, String password) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users ("
				+ "login, "
				+ "password, "
				+ "type"
				+ ") VALUES (?, ?, ?)");
		
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


	private static boolean updateClient(Connection connection, String userName) throws SQLException {
		
		boolean isSuccess = false;
		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE USERS SET type = 'client' WHERE login = ?");
		psUpdate.setString(1, userName);
		
		int result = psUpdate.executeUpdate();
		
		if (result > 0) {
			
			isSuccess = true;
		}
		
		psUpdate.close();
		
		return isSuccess;
	}
	
	public static void removeTable (Connection connection, String tableName) throws SQLException {
		
		Statement dropStatement = connection.createStatement();

		dropStatement.execute(String.format("DROP TABLE IF EXISTS %s", tableName));

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
		
		createStatement.execute(statement);
		
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

	public static String[][] customSelect(Connection connection, String statement) throws SQLException {
		
		Statement customSelectStatement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, 
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet result = customSelectStatement.executeQuery(statement);
		
		ResultSetMetaData metadata = result.getMetaData();
		
		int numberOfColumns = metadata.getColumnCount();
		
		String[][] queryRows = getColumnNames(result, metadata);
		
		int currentRow = 1;
	
		while (result.next()) {
			
			for (int i = 0; i < numberOfColumns; i++) {
				
				queryRows[currentRow][i] = result.getString(i + 1);
			}
			
			currentRow++;
		}
		
		customSelectStatement.close();
		
		currentQueryHeader = queryRows[0];
		currentQueryRows = Arrays.copyOfRange(queryRows, 1, queryRows.length);
		
		return queryRows;
	}

	private static String[][] getColumnNames(ResultSet result, ResultSetMetaData metadata) throws SQLException {

		String[][] columnNames = new String[getNumberOfRows(result) + 1][metadata.getColumnCount()];
		
		for (int i = 0; i < metadata.getColumnCount(); i++) {
			
			columnNames[0][i] = metadata.getColumnName(i + 1);
		}
		
		return columnNames;
	}

	private static int getNumberOfRows(ResultSet result) throws SQLException {
			
		int rows = 0;
			
		result.last();
		
		rows = result.getRow();
		
		result.beforeFirst();
		
		return rows;
	}
	
	public static void emptyCurrentQuery() {
		
		currentQueryHeader = null;
		currentQueryRows = null;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public static String[] getCurrentQueryHeader() {
		return currentQueryHeader;
	}

	public static String[][] getCurrentQueryRows() {
		return currentQueryRows;
	}
}
