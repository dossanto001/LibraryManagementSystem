package swt.hse.de;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFunctions {
    public static Connection createConnectionToDatabase(String name, String password, DbConnector dbConnector) {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:55000/library", name, password);
            dbConnector.connectionString = con.toString();
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnectionToDatabase(DbConnector dbConnector) {
        if (DbConnector.getConnection() != null) {
            try {
                DbConnector.getConnection().close();
            } catch (Exception e) {
                System.out.println(DbConnector.getConnection() + " left open (connection)");
                e.printStackTrace();
            }
        }
        if (dbConnector.getStatement() != null) {
            try {
                dbConnector.getStatement().close();

            } catch (Exception e) {
                System.out.println(dbConnector.getStatement() + " left open (statement)");
                e.printStackTrace();
            }
        }
        if (DbConnector.getResSet() != null) {
            try {
                DbConnector.getResSet().close();
            } catch (Exception e) {
                System.out.println(DbConnector.getResSet() + " left open (result set)");
                e.printStackTrace();
            }
        }
    }
}
