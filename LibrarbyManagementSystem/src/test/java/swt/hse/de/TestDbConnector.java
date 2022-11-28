package swt.hse.de;

import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.postgresql.util.PSQLException;

import static org.junit.Assert.*;

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

	@Test
	public void testAddBorrowInformation() throws SQLException  {
		assertEquals(true, db.addBorrowInformation("name", "title"));
	}

	@Test
	public void testReturnBookInformation() throws SQLException  {
		db.addBorrowInformation("name", "title2");
		assertEquals(true, db.returnBookInformation("name", "title2"));
	}

	@Test
	public void testAlreadyBorrowedTrue() throws SQLException  {
		db.addBorrowInformation("name", "title3");
		assertEquals(true, db.alreadyBorrowed("title3", "name"));
	}

	@Test
	public void testAlreadyBorrowedFalse() throws SQLException  {
		assertEquals(false, db.alreadyBorrowed("title4", "name"));
	}

	@Test
	public void testGetDueDate() throws SQLException  {
		db.addBorrowInformation("name", "title5");
		String date = db.getDueDate("name", "title5");
		//checking to see if its a date
		String regex = "^\\d{2}-\\d{2}-\\d{4}$";
		assertTrue(date.matches(regex));
	}

	@Test
	public void testIsOnTimeTrue() throws SQLException, ParseException {
		db.addBorrowInformation("name", "title6");
		assertEquals(true, db.isOnTime("name", "title6"));
	}

	@Test
	public void testIsOnTimeFalse() throws SQLException, ParseException{

	}

}
