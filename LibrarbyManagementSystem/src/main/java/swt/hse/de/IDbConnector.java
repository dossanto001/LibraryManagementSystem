package swt.hse.de;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public interface IDbConnector {
    Connection createConnectionToDatabase(String name, String password);

    int getBookAvailable(String title) throws SQLException;

    int getInStock(String title) throws SQLException;

    int getBorrowCount(String title) throws SQLException;

    double getRating(String title) throws SQLException;

    boolean createBook(Book book)
            throws SQLException;

    boolean alreadyBorrowed(String title, String customerName) throws SQLException;

    boolean addBorrowInformation(String nameOfCustomer, String title) throws SQLException;

    boolean returnBookInformation(String nameOfCustomer, String title) throws SQLException;

    String getDueDate(String nameOfCustomer, String title) throws SQLException;

    boolean isOnTime(String nameOfCustomer, String title) throws SQLException, ParseException;

    void borrowBook(String nameOfCustomer, String title) throws SQLException;

    void returnBook(String title, double rating, String nameOfCustomer) throws SQLException;

    boolean deleteBook(String title, int amount) throws SQLException;

    String printBookList() throws SQLException;

    boolean bookExisting(String name) throws SQLException;

    void closeConnectionToDatabase() throws SQLException;
}
