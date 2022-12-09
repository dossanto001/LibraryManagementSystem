package swt.hse.de;

import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestDbConnector {

//	@Mock
//	private DbConnector mock = new DbConnector();

	private DbConnector db;
	IDbConnector dbI;

	@Before
	public void setUp() {
		db = new DbConnector();
	}

	@Test
	public void testConnectionFail() throws SQLException  {
		assertNull(db.createConnectionToDatabase("wrong", "pw"));
	}

//	@Test //Mock initial test for tryouts
//	public void testConnectionFailMock() throws SQLException  {
//		when(mock.createConnectionToDatabase("wrong", "pw")).thenReturn(null);
//	}

	@Test
	public void testConnectionSuccess() throws SQLException {
		assertNotEquals(null, db.createConnectionToDatabase("postgres", "postgrespw"));
		db.closeConnectionToDatabase();
	}

	@Test
	public void testCreateBook() throws SQLException  {
		db.truncateTable();
		assertTrue(db.createBook(new Book("name3", "auth", 1222, 2, "pub", 0)));
	}

	@Test
	public void testCreateExistingBook() throws SQLException {
		db.createBook(new Book("name2", "auth", 1222, 2, "pub", 0));
		assertTrue(db.createBook(new Book("name2", "auth", 1222, 3, "pub", 2)));
	}

	@Test
	public void testDeleteBookTrue() throws SQLException{
		db.createBook(new Book("name", "auth", 1222, 2, "pub", 0));
		assertTrue(db.deleteBook("name", 1, 0));
	}

	@Test
	public void testDeleteBookDecline() throws SQLException{
		assertFalse(db.deleteBook("name", 1, 1));
	}

	@Test
	public void testDeleteBookFail() throws SQLException  {
		assertFalse(db.deleteBook("no book with this name", 1, 0));
	}

	@Test
	public void testBookExistingSuccess() throws SQLException  {
		db.createBook(new Book("name", "auth", 1222, 2, "pub", 0));
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

	@Test
	public void testBorrowBookFail () throws SQLException {
		assertFalse(db.borrowBook("test", "notABook"));
	}

	@Test
	public void testBorrowBookTrue() throws SQLException {
		db.createBook(new Book("name", "auth", 1222, 2, "pub", 2));
		assertTrue(db.borrowBook("Customer", "name"));
	}

	@Test
	public void testBorrowBookTrueAnd1inStock() throws SQLException {
		db.createBook(new Book("name", "auth", 1222, 2, "pub", 1));
		assertTrue(db.borrowBook("Customer", "name"));
	}

	@Test
	public void testPrintBookList() throws SQLException {
		db.truncateTable();
		assertEquals("Book Title\t\tAmount stocked\t\t Rating\n", db.printBookList());
	}

	@Test
	public void testBookCount() throws SQLException {
		db.truncateTable();
		db.createBook(new Book("count", "auth", 1222, 2, "pub", 4));
		for(int i = 0; i < 4; i++) {
			db.borrowBook("Customer", "count");
		}
		assertEquals(4, db.getBorrowCount("count"));
	}
	@Test
	public void testGetRating() throws SQLException {
		db.truncateTable();
		db.createBook(new Book("rating", "auth", 1222, 2, "pub", 3));
		db.borrowBook("Customer", "rating");
		db.returnBook("rating", 3.5, "Customer");
		assertEquals(3.5,db.returnBook("rating", 3.5, "Customer"), 0.1);
	}
}
