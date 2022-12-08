package swt.hse.de;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DbBorrowFunctions {
    boolean alreadyBorrowed(String title, String customerName, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        DbConnector.setResSet(dbConnector.getStatement().executeQuery("SELECT * FROM library.borrowed"));
        while (DbConnector.getResSet().next())
            if (title.equals(DbConnector.getResSet().getString("book")) && customerName.equalsIgnoreCase(DbConnector.getResSet().getString("customer"))) {
                return true;
            }
        return false;
    }

    boolean addBorrowInformation(String nameOfCustomer, String title, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        String dueDate = dbConnector.getBt().borrowForTime(7);
        dbConnector.setQuery("INSERT INTO library.borrowed (customer, book, duedate) VALUES ('" + nameOfCustomer + "','" + title + "','"
                +  dueDate + "');");
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
        return true;
    }

    boolean returnBookInformation(String nameOfCustomer, String title, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        dbConnector.setQuery("DELETE FROM library.borrowed WHERE customer='" + nameOfCustomer + "' AND book='" + title + "';");
        dbConnector.getStatement().executeUpdate(dbConnector.getQuery());
        return true;
    }

    String getDueDate(String nameOfCustomer, String title, DbConnector dbConnector) throws SQLException {
        DbConnector.setConnection(dbConnector.createConnectionToDatabase(dbConnector.getRoot(), dbConnector.getRootPassword()));
        dbConnector.setStatement(DbConnector.getConnection().createStatement());
        dbConnector.setQuery("SELECT duedate FROM library.borrowed WHERE customer='" + nameOfCustomer + "' AND book='" + title + "';");
        DbConnector.setResSet(dbConnector.getStatement().executeQuery(dbConnector.getQuery()));
        String dueDate;
        if(DbConnector.getResSet().next()){
            dueDate = DbConnector.getResSet().getString("duedate");
        } else
            dueDate = DbConnector.getResSet().getString("duedate");
        return dueDate;
    }

    boolean isOnTime(String nameOfCustomer, String title, DbConnector dbConnector) throws SQLException, ParseException {
        String dueDate = dbConnector.getDueDate(nameOfCustomer, title);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String today = dateFormat.format(date);
        if(dateFormat.parse(today).before(dateFormat.parse(dueDate)) ||
                dateFormat.parse(today).equals(dateFormat.parse(dueDate))){
            return true;
        } else
            return false;
    }
}
