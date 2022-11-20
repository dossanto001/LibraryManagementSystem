package swt.hse.de;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class JfxConnector {
	
	//create a new run config of type "maven", select this project folder as workspace target and
	//for goals use "clean javafx:run", then click Apply and after that -> run.
	
	DbConnector db = new DbConnector();
	BorrowTimer bt = new BorrowTimer();
	
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
		if(db.getInStock(title) > 0) {
			db.deleteBook(title, Integer.parseInt(amount.getText()));
			JOptionPane.showInternalMessageDialog(null, "book(s) have been deleted");
		}
		else
			JOptionPane.showInternalMessageDialog(null, "No books left to delete");
		searchBook.setText(db.printBookList());
	}

	@FXML
	public void addBookButton() throws SQLException {
		db.createBook(nameOfBook.getText(), author.getText(), Integer.parseInt(year.getText()), 
				Integer.parseInt(edition.getText()), publisher.getText(), Integer.parseInt(numberInStock.getText()));
		JOptionPane.showInternalMessageDialog(null, "book(s) have been added");
		searchBook.setText(db.printBookList());
	}

	@FXML
	public void borrowBookButton() throws SQLException {
		if(db.getBookAvailable(nameOfBook.getText())==1 || db.getInStock(nameOfBook.getText())==0) {
			JOptionPane.showInternalMessageDialog(null, "Book can't be borrowed");
		}
		else {
			db.borrowBook(nameOfCustomer.getText(), nameOfBook.getText());
			JOptionPane.showInternalMessageDialog(null, "Book has been borrowed by " + nameOfCustomer.getText() +" book must be returned by " +
					bt.borrowForTime(7));
		}
		searchBook.setText(db.printBookList());
		return;
	}
	
	public void returnBookButton() throws SQLException {
		db.returnBook(nameOfBook.getText(), Double.parseDouble(rating.getText()));
		JOptionPane.showInternalMessageDialog(null, "Book has been returned.");
		searchBook.setText(db.printBookList());
		return;
	}
	
}
