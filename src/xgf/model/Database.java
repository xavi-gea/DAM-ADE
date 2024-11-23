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

/**
 * @author Xavi
 */
public class Database {

	private String name;
	private static String[] currentQueryHeader;
	private static String[][] currentQueryRows;
	
	public Database(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Tries to connect to the database used by the application 
	 * @param userName Name of the user that is trying to log-in
	 * @param userPassword Password of the user that is trying to log-in
	 * @return If successful, the connection to the database 
	 * @throws SQLException When a connection to the database is not able to be made
	 * @throws ClassNotFoundException When the specified driver is not found 
	 */
	public Connection connectToDatabase(String userName, String userPassword) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + name, userName, userPassword);
	}

	/**
	 * Executes a select statement to obtain the type of the provided userName
	 * @param connection Connection to be used for the SELECT statement
	 * @param userName Name of the target user
	 * @return Return the type of the provided user
	 * @throws SQLException When Connection cannot be interacted with
	 */
	public String getUserType(Connection connection, String userName) throws SQLException {
		
		String type = "client";
		
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
	
	/**
	 * Checks if the provided query is valid
	 * @param query Query to be checked
	 * @return Boolean with the results of the check
	 */
	public static boolean isValidQuery(String query) {

		if (!query.contains("SELECT") || !query.contains("FROM")) {
			
			return false;
		}
		
		return true;
	}

	/**
	 * Calls the relevant methods to create, grant, insert and update a new user
	 * @param connection Connection to be used for the create statement
	 * @param userName Name of the new user
	 * @param password Password of the new user
	 * @return Boolean with the result of the methods that can have a return
	 * @throws SQLException When Connection cannot be interacted with
	 */
	public static boolean setUpClient(Connection connection, String userName, String password) throws SQLException {
		
		createClient(connection, userName, password);
		grantClient(connection, userName);
		boolean isInsertSuccess = insertClient(connection, userName, password);
		boolean isUpdateSuccess = updateClient(connection, userName);
		
		return (isInsertSuccess && isUpdateSuccess) ? true : false;
	}

	/**
	 * Tries to create a user that will be able to interact with the database
	 * @param connection Connection to be used for the create statement
	 * @param userName Name of the new user
	 * @param password Password of the new user
	 * @throws SQLException When Connection cannot be interacted with
	 */
	private static void createClient(Connection connection, String userName, String password) throws SQLException {
		
		Statement createStatement = connection.createStatement();
		
		String statement = String.format("CREATE USER '%s' IDENTIFIED BY '%s'", userName, password);

		createStatement.execute(statement);
		
		createStatement.close();
	}
	
	/**
	 * Tries to grant privileges to the provided user 
	 * @param connection Connection to be used for the grant
	 * @param userName Name of the target user
	 * @throws SQLException When Connection cannot be interacted with
	 */
	private static void grantClient(Connection connection, String userName) throws SQLException {
				
		Statement grantStatement = connection.createStatement();
		
		String statement = String.format("GRANT SELECT ON population.population TO '%s'", userName);
		
		grantStatement.executeUpdate(statement);
		
		grantStatement.close();
	}

	/**
	 * Tries to insert a new user in the USERS table
	 * @param connection Connection to be used for the insert
	 * @param userName Name of the user to be inserted
	 * @param password Password of the user to be inserted
	 * @return Boolean with the insert result
	 * @throws SQLException When Connection cannot be interacted with
	 */
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


	/**
	 * Updates the type of user that resides in the USERS table
	 * @param connection Connection to be used for the update
	 * @param userName Name of the targeted user
	 * @return Boolean with the update result
	 * @throws SQLException When Connection cannot be interacted with
	 */
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
	
	/**
	 * Tries to remove a table with the provided name
	 * @param connection Connection to be used for the drop statement
	 * @param tableName Name of the targeted table
	 * @throws SQLException When Connection cannot be interacted with
	 */
	public static void removeTable (Connection connection, String tableName) throws SQLException {
		
		Statement dropStatement = connection.createStatement();

		dropStatement.execute(String.format("DROP TABLE IF EXISTS %s", tableName));

		dropStatement.close();
	}
	
	/**
	 * Tries to create a table with the provided name and columns
	 * @param connection Connection to be used for the create statement
	 * @param tableName Name of the new table
	 * @param tableColumns Columns of the new table
	 * @throws SQLException When Connection cannot be interacted with
	 */
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

	/**
	 * Execute an insert statement and return it's successfulness
	 * @param connection Connection to be used for the insert
	 * @param fileValues Values to be inserted
	 * @return Boolean with the insert result
	 * @throws SQLException When Connection cannot be interacted with
	 */
	public static boolean insertPopulation(Connection connection, List<String> fileValues) throws SQLException {
		
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

	/**
	 * Given the provided statement, connect to the database and execute a query returning 
	 * the results in a matrix
	 * @param connection Connection to be used for the query
	 * @param statement
	 * @return Matrix with the contents of the query
	 * @throws SQLException When Connection cannot be interacted with
	 */
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

	/**
	 * Given the provided ResultSet and ResultSetMetaData, construct a matrix with the column 
	 * names already present
	 * @param result Source used to obtain the total number of rows
	 * @param metadata Source used to obtain the name of the columns
	 * @return String[][] Array to be used for query data storage
	 * @throws SQLException When ResultSet or ResultSetMetaData cannot be interacted with
	 */
	private static String[][] getColumnNames(ResultSet result, ResultSetMetaData metadata) throws SQLException {

		String[][] columnNames = new String[getNumberOfRows(result) + 1][metadata.getColumnCount()];
		
		for (int i = 0; i < metadata.getColumnCount(); i++) {
			
			columnNames[0][i] = metadata.getColumnName(i + 1);
		}
		
		return columnNames;
	}

	/**
	 * Get number of rows from the given ResultSet
	 * @param result Source used to obtain the number of rows
	 * @return integer Total number of rows
	 * @throws SQLException When ResultSet cannot be interacted with
	 */
	private static int getNumberOfRows(ResultSet result) throws SQLException {
			
		int rows = 0;
			
		result.last();
		
		rows = result.getRow();
		
		result.beforeFirst();
		
		return rows;
	}
	
	/**
	 * Sets static variables currentQueryHeader and currentQueryRows to null
	 */
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
