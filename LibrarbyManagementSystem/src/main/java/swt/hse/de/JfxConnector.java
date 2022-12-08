package swt.hse.de;

import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class JfxConnector {
	
	//create a new run config of type "maven", select this project folder as workspace target and
	//for goals use "clean javafx:run", then click Apply and after that -> run.
	
	DbConnector db = new DbConnector();
	BorrowTimer bt = new BorrowTimer();

	PatternChecking pt = new PatternChecking();
	
	@FXML
	public TextArea booksInStock = new TextArea();
	@FXML 
	public TextArea searchBook = new TextArea();
	//searchBook.setText(db.printBookList());
	@FXML
	public TextField nameOfCustomer = new TextField();
	@FXML
	public TextField amount = new TextField();
	@FXML
	public TextField nameOfBook = new TextField();
	@FXML
	public TextField author = new TextField();
	@FXML
	public TextField publisher = new TextField();
	@FXML
	public TextField edition = new TextField();
	@FXML
	public TextField rating = new TextField();
	@FXML
	public TextField numberInStock = new TextField();
	@FXML
	public TextField year = new TextField();

	
	
	@FXML
	public void deleteBookButton() throws SQLException {
		String title = nameOfBook.getText();
		if(pt.checkAlphaNumeric(title)){
			if(db.getInStock(title) > 0) {
				db.deleteBook(title, Integer.parseInt(amount.getText()));
				JOptionPane.showInternalMessageDialog(null, "book(s) have been deleted");
			}
			else
				JOptionPane.showInternalMessageDialog(null, "No books left to delete");
			searchBook.setText(db.printBookList());
		}else{
			JOptionPane.showInternalMessageDialog(null,
					"Title does not follow Alphanumeric protocol");
		}

	}

	@FXML
	public void addBookButton() throws SQLException {
		Book book = new Book(nameOfBook.getText(), author.getText(), year.getText(),
				edition.getText(), publisher.getText(), numberInStock.getText());
		if(pt.checkAlphaNumeric(book.getTitle()) && pt.checkWord(book.getAuthor())
				&& pt.checkYear(year.getText()) && pt.checkNumber(edition.getText())
				&& pt.checkAlphaNumeric(book.getPublisher()) && pt.checkNumber(numberInStock.getText())){
			db.createBook(book);
			JOptionPane.showInternalMessageDialog(null, "book(s) have been added");
			searchBook.setText(db.printBookList());
		}else{
			JOptionPane.showInternalMessageDialog(null,
					"Incorrect pattern for book addition ");
		}

	}

	@FXML
	public void borrowBookButton() throws SQLException {
		if(db.getBookAvailable(nameOfBook.getText())==1 || db.getInStock(nameOfBook.getText())==0) {
			JOptionPane.showInternalMessageDialog(null, "Book can't be borrowed");
		} else if(db.alreadyBorrowed(nameOfBook.getText(), nameOfCustomer.getText())){
			JOptionPane.showInternalMessageDialog(null, nameOfCustomer.getText() + " has already borrowed this book");
		}
		else {
			db.borrowBook(nameOfCustomer.getText(), nameOfBook.getText());
			JOptionPane.showInternalMessageDialog(null, "Book has been borrowed by " + nameOfCustomer.getText() +" book must be returned by " +
					bt.borrowForTime(7));
		}
		searchBook.setText(db.printBookList());
		return;
	}
	
	public void returnBookButton() throws SQLException, ParseException {
		if(pt.checkNumber(rating.getText())){
			if(db.alreadyBorrowed(nameOfBook.getText(), nameOfCustomer.getText())){
				String dueDate = db.getDueDate(nameOfCustomer.getText(), nameOfBook.getText());
				boolean isPastDue = db.isOnTime(nameOfCustomer.getText(), nameOfBook.getText());
				db.returnBook(nameOfBook.getText(), Double.parseDouble(rating.getText()), nameOfCustomer.getText());
				if(isPastDue){
					JOptionPane.showInternalMessageDialog(null, "Book has been returned on time.");
				} else {
					JOptionPane.showInternalMessageDialog(null, "Book is late. Book was due on "
							+ dueDate + " A late fee of $100 will be owed");
				}

				searchBook.setText(db.printBookList());
			} else {
				JOptionPane.showInternalMessageDialog(null, "This book is not currently borrowed by " + nameOfCustomer.getText());
			}
		}else{
			JOptionPane.showInternalMessageDialog(null, "Incorrect rating pattern. Please try again");
		}


		return;
	}

	
}
