package fr.epita.quiz.launcher;

import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.User;
import fr.epita.quiz.services.QuizJDBCDAO;
import fr.epita.quiz.services.UserJDBCDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		//The primaryStage is the top-level container
		try {
			System.setProperty("conf.location","app.properties");
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_pane.fxml"));
	        Parent root1 =  fxmlLoader.load();
	        
	        
	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root1));  
	        stage.setTitle("QuizManager 1.0");
	        stage.show();
	        
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
//				primaryStage.setTitle("example Gui");
//				//The BorderPane has the same areas laid out as the
//				//BorderLayout layout manager
//				BorderPane componentLayout = new BorderPane();
//				componentLayout.setPadding(new Insets(20,0,20,20));
//				//The FlowPane is a conatiner that uses a flow layout
//				final FlowPane choicePane = new FlowPane();
//				choicePane.setHgap(100);
//				Label choiceLbl = new Label("Fruits");
//				//The choicebox is populated from an observableArrayList
//				
//				QuizJDBCDAO quizDAO = new QuizJDBCDAO();
//				UserJDBCDAO userDAO = new UserJDBCDAO();
//				List<Quiz> quiz=quizDAO.search(new Quiz(""));
//				List<User> users=userDAO.search(new User(""));
//				System.out.println(users);
//				List<String> quizString=new ArrayList<>();
//				for(Quiz q : quiz) {
//					quizString.add(q.getTitle());
//				}
//				System.out.println(quiz.toString());
//				ChoiceBox<String> fruits = new ChoiceBox<>(FXCollections.observableArrayList(quizString));
//				//Add the label and choicebox to the flowpane
//				choicePane.getChildren().add(choiceLbl);
//				choicePane.getChildren().add(fruits);
//				//put the flowpane in the top area of the BorderPane
//				componentLayout.setTop(choicePane);
//				final FlowPane listPane = new FlowPane();
//				listPane.setHgap(100);
//				Label listLbl = new Label("Vegetables");
//				ListView<User> vegetables = new ListView<>(FXCollections.observableArrayList(users));
//				listPane.getChildren().add(listLbl);
//				listPane.getChildren().add(vegetables);
//				listPane.setVisible(false);
//				componentLayout.setCenter(listPane);
//				//The button uses an inner class to handle the button click event
//				
//				//Add the BorderPane to the Scene
//				
//				final FlowPane loginPane = new FlowPane();
//				Button btn1 = new Button();
//				Button btn2 = new Button();
//				Button btn3 = new Button();
//				ListView<Button> buttons = new ListView<>(FXCollections.observableArrayList(btn1,btn2,btn3));
//
//				componentLayout.setBottom(buttons);
//				
//				
//				Button vegFruitBut = new Button("Fruit or Veg");
//				vegFruitBut.setOnAction(e ->{
//					//switch the visibility for each FlowPane
//					choicePane.setVisible(!choicePane.isVisible());
//					listPane.setVisible(!listPane.isVisible());
//					buttons.setVisible(!buttons.isVisible());
//				});
//
//				componentLayout.setBottom(vegFruitBut);
//				Scene appScene = new Scene(componentLayout,500,500);
//				//Add the Scene to the Stage
//				primaryStage.setScene(appScene);
//				primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
