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

	@Test
	public void testConnectionFail() throws SQLException  {
		assertNull(db.createConnectionToDatabase("WrongUserName", "postgrespw"));
		db.closeConnectionToDatabase();
	}

	@Test
	public void testConnectionSuccess() throws SQLException {
		assertNotEquals(null, db.createConnectionToDatabase("postgres", "postgrespw"));
		db.closeConnectionToDatabase();
	}

	@Test
	public void testCreateBook() throws SQLException  {
		assertTrue(db.createBook(new Book("name2", "auth", 1222, 2, "pub", 0)));
	}

	@Test
	public void testCreateExistingBook() throws SQLException {
		assertTrue(db.createBook(new Book("name2", "auth", 1222, 2, "pub", 0)));
		try {
			assertTrue(db.createBook(new Book("name2", "auth", 1222, 2, "pub", 0)));
		} catch(PSQLException ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testDeleteBook() throws SQLException{
		assertTrue(db.deleteBook("name", 1));
	}

	@Test
	public void testDeleteBookFail() throws SQLException  {
		assertFalse(db.deleteBook("no book with this name", 1));
	}

	@Test
	public void testBookExistingSuccess() throws SQLException  {
		assertTrue(db.bookExisting("name"));
	}

	@Test
	public void testBookExistingFail() throws SQLException {
		assertFalse(db.bookExisting("this book does not exist"));
	}

	@Test
	public void testAddBorrowInformation() throws SQLException  {
		assertTrue(db.addBorrowInformation("name", "title"));
	}

	@Test
	public void testReturnBookInformation() throws SQLException  {
		db.addBorrowInformation("name", "title2");
		assertTrue(db.returnBookInformation("name", "title2"));
	}

	@Test
	public void testAlreadyBorrowedTrue() throws SQLException  {
		db.addBorrowInformation("name", "title3");
		assertTrue(db.alreadyBorrowed("title3", "name"));
	}

	@Test
	public void testAlreadyBorrowedFalse() throws SQLException  {
		assertFalse(db.alreadyBorrowed("title4", "name"));
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
		assertTrue(db.isOnTime("name", "title6"));
	}

}
