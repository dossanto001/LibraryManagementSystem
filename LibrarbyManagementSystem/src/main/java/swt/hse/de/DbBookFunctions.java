package swt.hse.de;

import javax.swing.*;
import java.sql.SQLException;

public class DbBookFunctions {
    boolean createBookQuery(Book book, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        if (dbConnector.bookExisting(book.getTitle())) {
            dbConnector.setQuery("UPDATE library.books SET title='" + book.getTitle() + "', author='" + book.getAuthor() +
                    "', year='" + book.getYear() + "', edition='" + book.getEdition() + "', publisher='" +
                    book.getPublisher() + "', instock='"+ book.getInStock() + "'");
        } else {
            dbConnector.setQuery("INSERT INTO library.books (title, author, year, edition, publisher, inStock, bookAvailable, borrowCount, rating) "
                    + "VALUES ('" + book.getTitle() + "','" + book.getAuthor() + "','" + book.getYear() + "','" + book.getEdition() + "','" + book.getPublisher() + "','"
                    + book.getInStock() + "',0 ,0 ,0);");
        }
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
        dbConnector.closeConnectionToDatabase();
        return true;
    }

    int getValuesFromBookQuery(String selectValue, String title, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        DbConnector.setResSet(dbConnector.getStatement().executeQuery("SELECT " + selectValue + " FROM library.books WHERE title='" + title + "';"));
        int queryResult = 187;
        while (DbConnector.getResSet().next())
            queryResult = DbConnector.getResSet().getInt(selectValue);
        return queryResult;
    }

    void borrowBookQuery(String nameOfCustomer, String title, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        if (dbConnector.getBookAvailable(title) == 0 && dbConnector.getInStock(title) != 1) {
            dbConnector.setQuery("UPDATE library.books SET borrowCount = borrowCount + 1, inStock = inStock -1 " + "WHERE title='"
                    + title + "' AND NOT inStock = 0;");
            dbConnector.addBorrowInformation(nameOfCustomer, title);
        } else if (dbConnector.getBookAvailable(title) == 0 && dbConnector.getInStock(title) == 1) {
            dbConnector.setQuery("UPDATE library.books SET borrowCount = borrowCount + 1, inStock = inStock -1, bookAvailable = 0 "
                    + "WHERE title='" + title + "' AND NOT inStock = 0 ;");
            dbConnector.addBorrowInformation(nameOfCustomer, title);
        } else
            dbConnector.setQuery("");
        dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
        dbConnector.closeConnectionToDatabase();
    }

    void returnBookQuery(String title, double rating, String nameOfCustomer, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        double oldRating = dbConnector.getRating(title);
        int count = dbConnector.getBorrowCount(title);
        double newRating = (oldRating * ((double) count - 1.0) + rating) / (double) count;

        dbConnector.setQuery("UPDATE library.books SET inStock = inStock + 1, rating = '" + newRating + "' WHERE title='" + title
                + "';");
        dbConnector.returnBookInformation(nameOfCustomer, title);
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
        dbConnector.closeConnectionToDatabase();
    }

    boolean deleteBookQuery(String title, int amount, DbConnector dbConnector) throws SQLException {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete " +
                amount + " pieces of " + title + "?");
        if (option == 1 || option == 2) {
            return false;
        }
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        if (dbConnector.getBookAvailable(title) == 0) {
            dbConnector.setQuery("UPDATE library.books SET inStock = inStock - '" + amount + "' WHERE title= '" + title + "';");
            dbConnector.setStatement(DbConnector.getConnection().createStatement());
            dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
            dbConnector.setQuery("UPDATE library.books SET inStock= 0 WHERE inStock < 0;");
            dbConnector.setStatement(DbConnector.getConnection().createStatement());
            dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
            dbConnector.closeConnectionToDatabase();
            return true;
        } else {
            dbConnector.closeConnectionToDatabase();
            return false;
        }
    }

    String printBookListQuery(DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        DbConnector.setResSet(dbConnector.getStatement().executeQuery("SELECT * FROM library.books"));
        String resultOfQuery = "Book Title\t\tAmount stocked\t\t Rating\n";
        while (DbConnector.getResSet().next())
            resultOfQuery += DbConnector.getResSet().getString("title") + "   \t\t" + DbConnector.getResSet().getString("inStock") + " \t"
                    + DbConnector.getResSet().getDouble("rating") + "\n";
        return resultOfQuery;
    }

    boolean bookExistingQuery(String name, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        DbConnector.setResSet(dbConnector.getStatement().executeQuery("SELECT * FROM library.books"));
        //String resultOfQuery = "Book Title\t\tAmount stocked\t\t Rating\n";
        while (DbConnector.getResSet().next())
            if (name.equals(DbConnector.getResSet().getString("title")))
                return true;
        return false;
    }
}
