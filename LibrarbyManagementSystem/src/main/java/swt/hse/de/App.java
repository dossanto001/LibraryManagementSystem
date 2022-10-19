package swt.hse.de;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	public App() {
		
	}
	
	private static Scene scene;
	public Stage stage = new Stage();

    public void start(Stage stage) throws IOException {
    	this.stage = stage;
		URL url = App.class.getResource("/LibrarbyGui.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		Parent p = fxmlLoader.load();
		scene = new Scene(p);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Library Manager System");
		stage.show();
    }


	static void setRoot(String fxml) throws Exception {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}
}