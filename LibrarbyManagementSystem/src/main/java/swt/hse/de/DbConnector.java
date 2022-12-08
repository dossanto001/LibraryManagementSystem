package swt.hse.de;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class DbConnector implements IDbConnector {
	private static Connection connection;
	private Statement statement;
	private final String root = "postgres";
	private final String rootPassword = "postgrespw";
	private static ResultSet resSet;
	public String connectionString;
	private BorrowTimer bt = new BorrowTimer();
	private DbBorrowFunctions dbBorrowFunctions = new DbBorrowFunctions();
	private String query;

	private DbBookFunctions dbF = new DbBookFunctions();

	@Override
	public Connection createConnectionToDatabase(String name, String password) {
		return DbConnectionFunctions.createConnectionToDatabase(name, password, this);
	}

	@Override
	public void closeConnectionToDatabase() {
		DbConnectionFunctions.closeConnectionToDatabase(this);
	}

	@Override
	public int getBookAvailable(String title) throws SQLException {
		return DbBookFunctions.getValuesFromBook("bookAvailable", title, this);
	}

	@Override
	public int getInStock(String title) throws SQLException {
		return DbBookFunctions.getValuesFromBook("inStock", title, this);
	}

	@Override
	public int getBorrowCount(String title) throws SQLException {
		return DbBookFunctions.getValuesFromBook("borrowCount", title, this);
	}

	@Override
	public double getRating(String title) throws SQLException {
		return DbBookFunctions.getValuesFromBook("rating", title, this);
	}

	@Override
	public boolean createBook(Book book) throws SQLException {
		return DbBookFunctions.createBook(book, this);
	}

	@Override
	public boolean borrowBook(String nameOfCustomer, String title) throws SQLException {
		return DbBookFunctions.borrowBook(nameOfCustomer, title, this);
	}

	@Override
	public double returnBook(String title, double rating, String nameOfCustomer) throws SQLException {
		return dbF.returnBook(title, rating, nameOfCustomer, this);
	}

	@Override
	public boolean deleteBook(String title, int amount, int option) throws SQLException {
		return dbF.deleteBook(title, amount, this, option);
	}

	@Override
	public String printBookList() throws SQLException {
		return DbBookFunctions.printBookList(this);
	}

	@Override
	public boolean bookExisting(String name) throws SQLException {
		return DbBookFunctions.bookExisting(name, this);
	}

	@Override
	public boolean alreadyBorrowed(String title, String customerName) throws SQLException {
		return dbBorrowFunctions.alreadyBorrowed(title, customerName, this);
	}

	@Override
	public boolean addBorrowInformation(String nameOfCustomer, String title) throws SQLException{
		return dbBorrowFunctions.addBorrowInformation(nameOfCustomer, title, this);
	}

	@Override
	public boolean returnBookInformation(String nameOfCustomer, String title) throws SQLException {
		return dbBorrowFunctions.returnBookInformation(nameOfCustomer, title, this);
	}

	@Override
	public String getDueDate(String nameOfCustomer, String title) throws SQLException{
		return dbBorrowFunctions.getDueDate(nameOfCustomer, title, this);
	}

	@Override
	public boolean isOnTime(String nameOfCustomer, String title) throws SQLException, ParseException {
		return dbBorrowFunctions.isOnTime(nameOfCustomer, title, this);
	}

	@Override
	public void truncateTable() {
		DbBookFunctions.truncateTable(this);
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public static ResultSet getResSet() {
		return resSet;
	}

	public static void setResSet(ResultSet resSet) {
		DbConnector.resSet = resSet;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		DbConnector.connection = connection;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getRoot() {
		return root;
	}

	public String getRootPassword() {
		return rootPassword;
	}

	public BorrowTimer getBt() {
		return bt;
	}

	public void setBt(BorrowTimer bt) {
		this.bt = bt;
	}
}
