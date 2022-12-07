package swt.hse.de;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DbConnector {

	private static Connection connection;
	private Statement statement;
	private final String root = "postgres", rootPassword = "postgrespw";
	private static ResultSet resSet;
	public String connectionString;
	private BorrowTimer bt = new BorrowTimer();

	public Connection createConnectionToDatabase(String name, String password) {
		try {
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:49153/library", name, password);
			connectionString = con.toString();
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getBookAvailable(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT * FROM library.books WHERE title='" + title + "';");
		int bookAvailable = 187;
		while (res.next())
			bookAvailable = res.getInt("bookAvailable");
		return bookAvailable;
	}

	public int getInStock(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT inStock FROM library.books WHERE title='" + title + "'");
		int inStock = 187;
		while (res.next())
			inStock = res.getInt("inStock");
		return inStock;
	}

	public int getBorrowCount(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT borrowCount FROM library.books WHERE title='" + title + "'");
		int borrowCount = 420;
		while (res.next())
			borrowCount = res.getInt("borrowCount");
		return borrowCount;
	}

	public double getRating(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT rating FROM library.books WHERE title='" + title + "'");
		double rating = 69.420;
		while (res.next())
			rating = res.getInt("rating");
		return rating;
	}

	public boolean createBook(Book book)
			throws SQLException {
		String query = null;
		Connection connection = createConnectionToDatabase(root, rootPassword);
		if (bookExisting(book.getTitle())) {
			query = "UPDATE library.books SET title='" + book.getTitle() + "', author='" + book.getAuthor() +
					"', year='" + book.getYear() + "', edition='" + book.getEdition() + "', publisher='" +
					book.getPublisher() + "', instock='"+ book.getInStock() + "' WHERE title='"+ book.getTitle() +"'";
		} else {
			query = "INSERT INTO library.books (title, author, year, edition, publisher, inStock, bookAvailable, borrowCount, rating) "
					+ "VALUES ('" + book.getTitle() + "','" + book.getAuthor() + "','" + book.getYear() + "','" + book.getEdition() + "','" + book.getPublisher() + "','"
					+ book.getInStock() + "',0 ,0 ,0);";
		}
		statement = connection.createStatement();
		statement.executeUpdate(query);
		closeConnectionToDatabase();
		return true;
	}

	public boolean alreadyBorrowed(String title, String customerName)throws SQLException{
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT * FROM library.borrowed");
		while (res.next())
			if (title.equals(res.getString("book")) && customerName.equalsIgnoreCase(res.getString("customer"))) {
				return true;
			}
		return false;
	}

	public boolean addBorrowInformation(String nameOfCustomer, String title) throws SQLException{
		Connection connection = createConnectionToDatabase(root, rootPassword);
		statement = connection.createStatement();
		String dueDate = bt.borrowForTime(7);
		String query = "INSERT INTO library.borrowed (customer, book, duedate) VALUES ('" + nameOfCustomer + "','" + title + "','"
				+  dueDate + "');";
		statement = connection.createStatement();
		statement.executeUpdate(query);
		return true;
	}

	public boolean returnBookInformation(String nameOfCustomer, String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		statement = connection.createStatement();
		String query = "DELETE FROM library.borrowed WHERE customer='" + nameOfCustomer + "' AND book='" + title + "';";
		statement.executeUpdate(query);
		return true;
	}

	public String getDueDate(String nameOfCustomer, String title) throws SQLException{
		Connection connection = createConnectionToDatabase(root, rootPassword);
		statement = connection.createStatement();
		ResultSet res = resSet;
		String query = "SELECT duedate FROM library.borrowed WHERE customer='" + nameOfCustomer + "' AND book='" + title + "';";
		res = statement.executeQuery(query);
		String dueDate;
		if(res.next()){
			dueDate = res.getString("duedate");
		} else {
			dueDate = res.getString("duedate");
		}

		return dueDate;

	}

	public boolean isOnTime(String nameOfCustomer, String title) throws SQLException, ParseException {
		String dueDate = getDueDate(nameOfCustomer,title);
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String today = dateFormat.format(date);
		if(dateFormat.parse(today).before(dateFormat.parse(dueDate)) ||
				dateFormat.parse(today).equals(dateFormat.parse(dueDate))){
			return true;
		} else {
			return false;
		}

	}

	public void borrowBook(String nameOfCustomer, String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		statement = connection.createStatement();
		String query = "";
		if (getBookAvailable(title) == 0 && getInStock(title) != 1) {
			query = "UPDATE library.books SET borrowCount = borrowCount + 1, inStock = inStock -1 " + "WHERE title='"
					+ title + "' AND NOT inStock = 0;";
			addBorrowInformation(nameOfCustomer,title);
		} else if (getBookAvailable(title) == 0 && getInStock(title) == 1) {
			query = "UPDATE library.books SET borrowCount = borrowCount + 1, inStock = inStock -1, bookAvailable = 0 "
					+ "WHERE title='" + title + "' AND NOT inStock = 0 ;";
			addBorrowInformation(nameOfCustomer,title);
		} else
			query = "";
		statement.executeUpdate(query);
		closeConnectionToDatabase();
	}

	public void returnBook(String title, double rating, String nameOfCustomer) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		double oldRating = getRating(title);
		int count = getBorrowCount(title);
		double newRating = (oldRating * ((double) count - 1.0) + rating) / (double) count;

		String query = "UPDATE library.books SET inStock = inStock + 1, rating = '" + newRating + "' WHERE title='" + title
				+ "';";
		returnBookInformation(nameOfCustomer, title);
		statement = connection.createStatement();
		statement.executeUpdate(query);
		closeConnectionToDatabase();
	}

	public boolean deleteBook(String title, int amount, int option) throws SQLException {
		if (option == 3) {
			option = JOptionPane.showConfirmDialog(null, "Do you want to delete " +
					amount + " copies of " + title + "?");
		}
		if (option == 1 || option == 2) {
			return false;
		}
		Connection connection = createConnectionToDatabase(root, rootPassword);
		if (getBookAvailable(title) == 0) {
			String query = "UPDATE library.books SET inStock = inStock - '" + amount + "' WHERE title= '" + title + "';";
			statement = connection.createStatement();
			statement.executeUpdate(query);
			query = "UPDATE library.books SET inStock= 0 WHERE inStock < 0;";
			statement = connection.createStatement();
			statement.executeUpdate(query);
			closeConnectionToDatabase();
			return true;
		} else {
			closeConnectionToDatabase();
			return false;
		}
	}

	public String printBookList() throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT * FROM library.books");
		String resultOfQuery = "Book Title\t\tAmount stocked\t\t Rating\n";
		while (res.next())
			resultOfQuery += res.getString("title") + "   \t\t" + res.getString("inStock") + " \t"
					+ res.getDouble("rating") + "\n";
		return resultOfQuery;
	}

	public boolean bookExisting(String name) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT * FROM library.books");
		String resultOfQuery = "Book Title\t\tAmount stocked\t\t Rating\n";
		while (res.next())
			if (name.equals(res.getString("title"))) {
				return true;
			}
		return false;
	}

	public void truncateTable() {
		Connection c = connection;
		Statement stmt = statement;
		try {
			c = createConnectionToDatabase(root, rootPassword);
			stmt = c.createStatement();
			stmt.executeUpdate("truncate library.books;");
			closeConnectionToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnectionToDatabase() throws SQLException {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				System.out.println(connection + " left open (connection)");
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();

			} catch (Exception e) {
				System.out.println(statement + " left open (statement)");
				e.printStackTrace();
			}
		}
		if (resSet != null) {
			try {
				resSet.close();
			} catch (Exception e) {
				System.out.println(resSet + " left open (result set)");
				e.printStackTrace();
			}
		}
	}
}
