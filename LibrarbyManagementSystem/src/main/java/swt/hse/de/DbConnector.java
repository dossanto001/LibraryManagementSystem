package swt.hse.de;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {

	private static Connection connection;
	private Statement statement;
	private String root = "postgres", rootPassword = "postgrespw";
	private static ResultSet resSet;
	public String connectionString;

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
		res = statement.executeQuery("SELECT * FROM books WHERE title='" + title + "';");
		int bookAvailable = 187;
		while (res.next())
			bookAvailable = res.getInt("bookAvailable");
		return bookAvailable;
	}

	public int getInStock(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT inStock FROM books WHERE title='" + title + "'");
		int inStock = 187;
		while (res.next())
			inStock = res.getInt("inStock");
		return inStock;
	}

	public int getBorrowCount(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT borrowCount FROM books WHERE title='" + title + "'");
		int borrowCount = 420;
		while (res.next())
			borrowCount = res.getInt("borrowCount");
		return borrowCount;
	}

	public double getRating(String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		ResultSet res = resSet;
		statement = connection.createStatement();
		res = statement.executeQuery("SELECT rating FROM books WHERE title='" + title + "'");
		double rating = 69.420;
		while (res.next())
			rating = res.getInt("rating");
		return rating;
	}

	public boolean createBook(String title, String author, int year, int edition, String publisher, int inStock)
			throws SQLException {
		String query = null;
		Connection connection = createConnectionToDatabase(root, rootPassword);
		if (bookExisting(title)) {
			query = "UPDATE books SET (title, author, year, edition, publisher, inStock, bookAvailable, borrowCount, rating) "
					+ "VALUES ('" + title + "','" + author + "','" + year + "','" + edition + "','" + publisher + "','"
					+ inStock + "',0 ,0 ,0);";
		} else {
			query = "INSERT INTO books (title, author, year, edition, publisher, inStock, bookAvailable, borrowCount, rating) "
					+ "VALUES ('" + title + "','" + author + "','" + year + "','" + edition + "','" + publisher + "','"
					+ inStock + "',0 ,0 ,0);";
		}
		statement = connection.createStatement();
		statement.executeUpdate(query);
		closeConnectionToDatabase();
		return true;
	}
	
	public void createMultipleBooks(String title, String author, int year, int edition, String publisher, int inStock) {
		
	}

	public void borrowBook(String nameOfCustomer, String title) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		statement = connection.createStatement();
		String query = "";
		if (getBookAvailable(title) == 0 && getInStock(title) != 1) {
			query = "UPDATE books SET borrowCount = borrowCount + 1, inStock = inStock -1 " + "WHERE title='"
					+ title + "' AND NOT inStock = 0;";
		} else if (getBookAvailable(title) == 0 && getInStock(title) == 1) {
			query = "UPDATE books SET borrowCount = borrowCount + 1, inStock = inStock -1, bookAvailable = 0 "
					+ "WHERE title='" + title + "' AND NOT inStock = 0 ;";
		} else
			query = "";
		statement.executeUpdate(query);
		closeConnectionToDatabase();
	}

	public void returnBook(String title, double rating) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		double oldRating = getRating(title);
		int count = getBorrowCount(title);
		double newRating = (oldRating * ((double) count - 1.0) + rating) / (double) count;

		String query = "UPDATE books SET inStock = inStock + 1, rating = '" + newRating + "' WHERE title='" + title
				+ "';";
		statement = connection.createStatement();
		statement.executeUpdate(query);
		closeConnectionToDatabase();
	}

	public boolean deleteBook(String title, int amount) throws SQLException {
		Connection connection = createConnectionToDatabase(root, rootPassword);
		if (getBookAvailable(title) == 0) {
			String query = "UPDATE books SET inStock = inStock - '" + amount + "' WHERE title= '" + title + "';";
			statement = connection.createStatement();
			statement.executeUpdate(query);
			query = "UPDATE books SET inStock= 0 WHERE inStock < 0;";
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
		res = statement.executeQuery("SELECT * FROM books");
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
		res = statement.executeQuery("SELECT * FROM books");
		String resultOfQuery = "Book Title\t\tAmount stocked\t\t Rating\n";
		while (res.next())
			if (name.equals(res.getString("title"))) {
				return true;
			}
		return false;
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
