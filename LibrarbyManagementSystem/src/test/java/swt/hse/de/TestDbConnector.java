package swt.hse.de;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestDbConnector {
	
	DbConnector db;
	
	@Before
	public void setup() {
		db = new DbConnector();
	}
	
	@Test
	public void testConnectionFail() throws SQLException {
		assertEquals(null, db.createConnectionToDatabase("WrongUserName", "postgrespw")); 
		db.closeConnectionToDatabase();
	}
	
	@Test
	public void testConnectionSuccess() throws SQLException {
		assertNotEquals(null, db.createConnectionToDatabase("postgres", "postgrespw")); 
		db.closeConnectionToDatabase();
	}

}
