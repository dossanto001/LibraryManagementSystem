package swt.hse.de;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.postgresql.util.PSQLException;

@RunWith(JUnit4.class)
public class TestDbConnector {

	DbConnector db;

	@Before
	public void setup() {
		db = new DbConnector();
	}

	public void testConnectionFail() throws SQLException  {
		assertEquals(null, db.createConnectionToDatabase("WrongUserName", "postgrespw"));
		db.closeConnectionToDatabase();
	}

	@Test
	public void testConnectionSuccess() throws SQLException {
		assertNotEquals(null, db.createConnectionToDatabase("postgres", "postgrespw"));
		db.closeConnectionToDatabase();
	}

	@Test
	public void testCreateBook() throws SQLException  {
		assertEquals(true, db.createBook("name", "auth", 1222, 2, "pub", 0));
	}

	@Test
	public void testCreateExistingBook() throws SQLException {
		assertEquals(true, db.createBook("name2", "auth", 1222, 2, "pub", 0));
		try {
			assertEquals(true, db.createBook("name2", "auth", 1222, 2, "pub", 1));
		} catch(PSQLException ex){

		}
	}

	@Test
	public void testDeleteBook() throws SQLException{
		assertEquals(true, db.deleteBook("name", 1));
	}

	@Test
	public void testDeleteBookFail() throws SQLException  {
		assertEquals(false, db.deleteBook("no book with this name", 1));
	}

	@Test
	public void testBookExistingSuccess() throws SQLException  {
		assertEquals(true, db.bookExisting("name"));
	}

	@Test
	public void testBookExistingFail() throws SQLException {
		assertEquals(false, db.bookExisting("this book does not exist"));
	}

}
