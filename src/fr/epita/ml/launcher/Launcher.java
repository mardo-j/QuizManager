package fr.epita.ml.launcher;

import fr.epita.logger.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {

	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_pane.fxml"));
	        Parent root1 =  fxmlLoader.load();
	        
	        
	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root1));  
	        stage.setTitle("QuizManager 1.0");
	        stage.show();
	        
	    } catch(Exception e) {
	        Logger.logMessage("ERROR starting GUI "+e.getMessage());
	    }

	}

	public static void main(String[] args) {
		launch(args);
	}
}