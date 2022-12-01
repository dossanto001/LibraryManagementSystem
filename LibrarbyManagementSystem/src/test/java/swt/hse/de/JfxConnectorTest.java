package swt.hse.de;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class JfxConnectorTest extends ApplicationTest {
	Stage stage;

	@Before
	public void setUpClass() throws Exception {
		ApplicationTest.launch(App.class);
	}
	
	@After
	public void afterEachTest() throws TimeoutException {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
	}
	
//	@Test
//	@Ignore
//	public void addBookButtonTest() throws InterruptedException, TimeoutException {
//		clickOn("#amount").write("4");
//		clickOn("#author").write("Sapkowski");
//		clickOn("#publisher").write("SuperNowa");
//		clickOn("#edition").write("3");
//		clickOn("#year").write("2013");
//		clickOn("#nameOfBook").write("The Witcher: Time Of Contempt");
//		clickOn("#numberInStock").write("4");
//		clickOn("#addBookButton");
//	}
//	
//	@Test
//	@Ignore
//	public void borrowBookButtonSadTest() {
//		clickOn("#nameOfCustomer").write("Hugo");
//		clickOn("#nameOfBook").write("name");
//		clickOn("#borrowBookButton");
//	}
//	
//	@Test
//	@Ignore
//	public void borrowAndReturnBookButton() {
//		clickOn("#nameOfCustomer").write("Chad");
//		clickOn("#nameOfBook").write("The Witcher: Time of Contempt");
//		clickOn("#borrowBookButton");
//		clickOn("#rating").write("4.5");
//		clickOn("#returnBookButton");
//	}

//	@Test
//	public void borrowBookButtonTest() throws SQLException {
//		jfx.nameOfCustomer.setText("Gregor");
//		jfx.nameOfBook.setText("The Witcher: Time Of Contempt");
//		jfx.borrowBookButton();
//		
//	}
//
//	@Test
//	public void editionInputHappyTest() {
//		jfxTest.edition.setText("2");
//		assertTrue(jfxTest.edition.getText().contains("[0-9]+"));
//	}
//	
//	@Test
//	public void editionInputBadTest() {
//		jfxTest.edition.setText("a");
//		assertFalse(jfxTest.edition.getText().contains("[0-9]+"));
//	}
//
//	@Test
//	public void ratingInputHappyTest() {
//		assertTrue(jfxTest.publisher.getText().contains("[0-9]\\.[0-9]"));
//	}
//
//	@Test
//	public void nameOfCustomerHappyTest() {
//		jfxTest = mock(JfxConnector.class);
//		jfxTest.nameOfCustomer.setText("keggo");
//		System.out.print(jfxTest.nameOfCustomer.getText());
//		assertTrue(jfxTest.nameOfCustomer.getText().contains("[a-zA-Z]*"));
//	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
