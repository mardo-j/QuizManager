package fr.epita.quiz.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import fr.epita.logger.Logger;
import fr.epita.quiz.datamodel.Answer;
import fr.epita.quiz.datamodel.Encryption;
import fr.epita.quiz.datamodel.MCQAnswer;
import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.Student;
import fr.epita.quiz.datamodel.User;
import fr.epita.quiz.services.AnswerDAO;
import fr.epita.quiz.services.MCQQuestionDAO;
import fr.epita.quiz.services.QuestionJDBCDAO;
import fr.epita.quiz.services.QuizJDBCDAO;
import fr.epita.quiz.services.UserJDBCDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
public class LoginPaneController{
	private static final String CREATE = "Create";
	private static final String ERROR = "ERROR";
	private static final String UPDATE="Update";
	@FXML private Pane topicsPane;
	@FXML private Button startFreeTest;
	@FXML private Slider diffSlider;
	@FXML private TextField usernameField;
	@FXML private TextField quizName;
	@FXML private TextField freeName;
	@FXML private Pane loginPane;
	@FXML private Pane passwordPane;
	@FXML private PasswordField pf;
	@FXML private AnchorPane profLoginPane;
	@FXML private AnchorPane viewQuestionsScrollPane;
	@FXML private TabPane professorMainPane;
	@FXML private TabPane mainTabPane;
	@FXML private Label usernameLabel;
	@FXML private Label passwordLabel;
	@FXML private Button buttonLogin;
	
	@FXML private TextArea newQuestionArea;
	@FXML private TextField newTopicsField;
	@FXML private AnchorPane newChoicesPane;
	@FXML private Slider newDifficultySlider;
	@FXML private RadioButton newSimpleAnswerRB;
	@FXML private RadioButton newMCQRB;
	@FXML private Button newAddChoiceBtn;
	@FXML private Button newSelectImageBtn;
	@FXML private Button newCreateBtn;
	@FXML private Button createCloseImageBtn;
	@FXML private ImageView newImageLabel;
	@FXML private Label newImagePathLabel;
	@FXML private ToggleButton newQuizTitleBtn;
	@FXML private ToggleButton newQuestionTitleBtn;	
	@FXML private ToggleButton viewQuizTitleBtn;	
	@FXML private Button newCreateQuizBtn;

	@FXML private Label updateQuizIdLabel;
	@FXML private TextFlow textQuestionFlow;
	private int updateQuizIdInteger;
	private int updateQuestionIdInteger;

	@FXML private TextField newQuizNameTextField;
	@FXML private TextField viewQuizSearchTextField;

	@FXML private AnchorPane createQuizQuestionsPane;
	@FXML private AnchorPane viewQuizScrollPane;

	@FXML private AnchorPane quizContentPane;
	@FXML private Pane studentLoginPane;
	
	@FXML private TextField viewSearchTextField;
	@FXML private Tab createQuestionTab;
	@FXML private TitledPane quizTitledPane;
	private ListView<Question> questionsList;
	ListView<String> questionsStringList;
	private ListView<CheckBox> newChoice;
	private List<CheckBox> quizBox=new ArrayList<>();
	private List<Question> questions;
	private ListView<Quiz> quizList;
	private ListView<String> quizListString;
	private Quiz currentQuiz;
	private User currentStudent;
	private Question currentQuestion;
	@FXML private Label quizQuestionLabel;
	@FXML private AnchorPane quizAnswerPane;
	@FXML private ImageView quizImageViewLabel;
	@FXML private Button nextQuestionBtn;
	@FXML private TextArea quizAnswerTextArea;
	@FXML private Tab freeTestTabBtn;
	@FXML private Tab professorTabBtn;

	@FXML
	protected void newCreateQuizBtn() {
		List<Question> selectedQuestions = new ArrayList<>();
		for(int i=0;i<questions.size();i++) {
			if(quizBox.get(i).isSelected()) {
				selectedQuestions.add(questions.get(i));
			}
		}
		if(selectedQuestions.isEmpty()) {
			alertInfo(AlertType.ERROR, "Error", "Select at least one question to create/update a quiz");
		}else {
			QuizJDBCDAO qdao = new QuizJDBCDAO();
			Quiz q1 = new Quiz(newQuizNameTextField.getText(), selectedQuestions);
			q1.setId(updateQuizIdInteger);
			if(newCreateQuizBtn.getText().equals(CREATE)) {
				qdao.create(q1);
			}else {
				qdao.update(q1);
			}
		}
	}
	@FXML
	protected void viewQuizCreateBtn() {
		createQuizTab();
		newQuizNameTextField.setText("");
		newCreateQuizBtn.setText(CREATE);
		professorMainPane.getSelectionModel().select(2);
	}
	@FXML
	protected void createQuizTab(){
		boolean b=false;
		for(int i=0;i<quizBox.size();i++) {
			if(quizBox.get(i).isSelected())
				b=true;
		}
		if(!b || newQuizNameTextField.getText().isEmpty()) {
			QuestionJDBCDAO dao = new QuestionJDBCDAO();
			questions = dao.search(new Question());
			quizBox.clear();
			for(Question question : questions) {
				if(newQuizTitleBtn.isSelected())
					quizBox.add(new CheckBox(question.getQuestion()));
				else
					quizBox.add(new CheckBox(question.toString()));
			}
			
			ListView<CheckBox> checkBoxList = new ListView<>(FXCollections.observableArrayList(quizBox));
			checkBoxList.setPrefHeight(quizBox.size() * (double)24 + 24);
			checkBoxList.setPrefWidth((double)440);
			createQuizQuestionsPane.getChildren().clear();
			createQuizQuestionsPane.getChildren().add(checkBoxList);
		}
	}
	@FXML
	protected void viewQuizEditBtn(){

//		Quiz quiz=quizList.getSelectionModel().getSelectedItem();
		Quiz quiz;
		if(newQuizTitleBtn.isSelected()) {
			quiz=quizList.getItems().get(quizListString.getSelectionModel().getSelectedIndex());
		}else {
			quiz=quizList.getSelectionModel().getSelectedItem();
		}
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		questions = dao.search(new Question());
		
		quizBox.clear();
		for(Question question : questions) {
			if(newQuizTitleBtn.isSelected()) {
				quizBox.add(new CheckBox(question.getQuestion()));
			}else {
				quizBox.add(new CheckBox(question.toString()));
			}
			boolean b = false;
			int k=0;
			while(!b&&k<quiz.getQuestions().size()) {
				if(question.getId()==quiz.getQuestions().get(k).getId()) {
					b=true;
					quizBox.get(quizBox.size()-1).setSelected(b);
				}
				k++;
			}
		}
		newQuizNameTextField.setText(quiz.getTitle());
		newCreateQuizBtn.setText(UPDATE);
		updateQuizIdInteger=quiz.getId();
		ListView<CheckBox> checkBoxList = new ListView<>(FXCollections.observableArrayList(quizBox));
		checkBoxList.setPrefHeight(quizBox.size() * (double)24 + 24);
		checkBoxList.setPrefWidth((double)440);
		createQuizQuestionsPane.getChildren().clear();
		createQuizQuestionsPane.getChildren().add(checkBoxList);
		professorMainPane.getSelectionModel().select(2);
	}
	@FXML
	protected void viewQuizDeleteBtn(){
		Quiz quiz=quizList.getSelectionModel().getSelectedItem();
		Optional<ButtonType> result = confirmDialog("Are you sure you want to delete the Quiz: "+quiz.getTitle());
		if (result.isPresent()&&result.get() == ButtonType.OK){
			QuizJDBCDAO quidao = new QuizJDBCDAO();
			quidao.delete(quiz);
			alertInfo(AlertType.INFORMATION,getQuizName("Deleted"),getQuizName(quiz.getTitle())+" is deleted successfully");
			quizList.getItems().remove(quizList.getSelectionModel().getSelectedIndex());
		} else {
			alertInfo(AlertType.INFORMATION, "Thanks there is confirmation", "I knew you did it unintentionally");
		}
		
	}
	private Optional<ButtonType> confirmDialog(String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Please Confirm");
		alert.setContentText(content);
		return alert.showAndWait();
	}

	@FXML
	protected void viewQuizSearchBtn(){
		viewGetQuizzes(viewQuizSearchTextField.getText());
	}
	
	@FXML
	protected void viewCreateBtn() {
		clearCreateFields();
		professorMainPane.getSelectionModel().select(0);
	}
	@FXML
	protected void viewQuizTab(){
		viewGetQuizzes("");
//		
	}
	private void viewGetQuizzes(String quizTitle) {
		QuizJDBCDAO quizDAO = new QuizJDBCDAO();
		List<Quiz> quizz = quizDAO.search(new Quiz(quizTitle));
		quizList = new ListView<>(FXCollections.observableArrayList(quizz));
		List<String> quizString = new ArrayList<>();
		if(viewQuizTitleBtn.isSelected()) {
			for(Quiz quiz : quizz) {
				quizString.add(quiz.getTitle());
			}
			quizListString = new ListView<>(FXCollections.observableArrayList(quizString));
			quizListString.setPrefHeight(quizString.size() * (double)24 + 24);
			quizListString.setPrefWidth((double)440);
			viewQuizScrollPane.getChildren().clear();
			viewQuizScrollPane.getChildren().add(quizListString);
			
		}else {
			quizList.setPrefHeight(quizz.size() * (double)24 + 24);
			quizList.setPrefWidth((double)440);
			viewQuizScrollPane.getChildren().clear();
			viewQuizScrollPane.getChildren().add(quizList);
		}
	}
	
	@FXML
	protected void viewEditBtn(){
		clearCreateFields();
		Question question;
		if(newQuestionTitleBtn.isSelected()) {
			question=questions.get(questionsStringList.getSelectionModel().getSelectedIndex());
		}else {
			question=questions.get(questionsList.getSelectionModel().getSelectedIndex());
		}
		updateQuestionIdInteger=question.getId();
		newQuestionArea.setText(question.getQuestion());
		newTopicsField.setText(question.getTopics().toString().substring(1, question.getTopics().toString().length()-1));
		newDifficultySlider.setValue((double)question.getDifficulty());
		newSimpleAnswerRB.setSelected(!question.isMCQ());
		newMCQRB.setSelected(question.isMCQ());
		if(question.isMCQ()) {
			newChoices.clear();
			newChoicesPane.getChildren().clear();
			for(MCQChoice choice : question.getChoices()) {
				final CheckBox checkBox = new CheckBox(choice.getChoice());
				checkBox.setSelected(choice.isValid());
				checkBox.selectedProperty().addListener( (observable, oldValue, newValue) -> promptToAddNewChoice(checkBox));
				newChoices.add(checkBox);
			}
			newChoice = new ListView<>(FXCollections.observableArrayList(newChoices));
			newChoice.setPrefHeight(newChoices.size() * (double)24 + 24);
			newChoice.setPrefWidth((double)440);
			newChoicesPane.getChildren().add(newChoice);
			newChoicesPane.getParent().getParent().getParent().setVisible(true);
		}
		if(question.getImage()!=null&&!question.getImage().isEmpty()) {
			try {
				Image image1 = new Image(new FileInputStream(question.getImage()));
				newImageLabel.setImage(image1);
				newImagePathLabel.setText(question.getImage());
				createCloseImageBtn.setVisible(true);
			} catch (FileNotFoundException e) {
				Logger.logMessage("Error setting image");
			}
		}
		newCreateBtn.setText(UPDATE);
		professorMainPane.getSelectionModel().select(0);
	}

	private void promptToAddNewChoice(final CheckBox checkBox) {
		TextInputDialog dialog = new TextInputDialog(checkBox.getText());
		 
		dialog.setTitle("Add new Choice");
		dialog.setHeaderText("Enter new choice: ");
		dialog.setContentText("Choice: ");
		 
		Optional<String> result = dialog.showAndWait();
		 
		result.ifPresent(choice -> {
			if(choice.isEmpty()) {
				newChoice.getItems().remove(checkBox);
				newChoices.remove(checkBox);
			}else {
				checkBox.setText(choice);
			}
		});
	}
	void clearCreateFields() {
		newQuestionArea.setText("");
		newTopicsField.setText("");
		newDifficultySlider.setValue((double)1);
		newSimpleAnswerRB.setSelected(true);
		newMCQRB.setSelected(false);
		newChoices.clear();
		newChoicesPane.getChildren().clear();
		newImageLabel.setImage(null);
		newImagePathLabel.setText("");
		newCreateBtn.setText(CREATE);
		newAddChoiceBtn.setVisible(false);
		newChoicesPane.getParent().getParent().getParent().setVisible(false);
		createCloseImageBtn.setVisible(false);
	}
	@FXML
	protected void viewDeleteBtn(){
		Question question;
		if(newQuestionTitleBtn.isSelected()) {
			question=questions.get(questionsStringList.getSelectionModel().getSelectedIndex());
		}else {
			question=questions.get(questionsList.getSelectionModel().getSelectedIndex());
		}
		Optional<ButtonType> result = confirmDialog("Are you sure you want to delete the Quiz: "+question.getQuestion());
		if (result.isPresent()&&result.get() == ButtonType.OK){
			QuestionJDBCDAO dao = new QuestionJDBCDAO();
			dao.delete(question);
			alertInfo(AlertType.INFORMATION,"Question Deleted","Question "+question.getQuestion()+" is deleted successfully");
			questionsList.getItems().remove(questionsList.getSelectionModel().getSelectedIndex());
		} else {
			alertInfo(AlertType.INFORMATION, "Thanks there is confirmation", "I knew you did it unintentionally");
		}
	}
	@FXML
	protected void viewSearchBtn(){

		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		questions=dao.search(new Question(viewSearchTextField.getText(),null,1,1));
		List<String> questionsString = new ArrayList<>();
		if(newQuestionTitleBtn.isSelected())
			for(Question question : questions) {
				questionsString.add(question.getQuestion());
			}
		questionsStringList = new ListView<>(FXCollections.observableArrayList(questionsString));
		questionsStringList.setPrefHeight(questions.size() * (double)24 + 24);
		questionsStringList.setPrefWidth((double)440);
		questionsList = new ListView<>(FXCollections.observableArrayList(questions));
		questionsList.setPrefHeight(questions.size() * (double)24 + 24);
		questionsList.setPrefWidth((double)440);

		viewQuestionsScrollPane.getChildren().clear();
		if(newQuestionTitleBtn.isSelected())
			viewQuestionsScrollPane.getChildren().add(questionsStringList);
		else
			viewQuestionsScrollPane.getChildren().add(questionsList);
			
		
	}
	
	public final List<MCQChoice> resultlist = new ArrayList<>();

	List<CheckBox> cb= new ArrayList<>();
	List<CheckBox> newChoices= new ArrayList<>();
	List<MCQChoice> choicesId = new ArrayList<>();
	@FXML
	protected void newSelectImage() {
		JFileChooser jfc = new JFileChooser();
	    jfc.showDialog(null,"Select the File");
	    jfc.setVisible(true);
	    File filename = jfc.getSelectedFile();
	    if(filename!=null) {
		    try {
				Image image1 = new Image(new FileInputStream(filename.getAbsolutePath()));
				newImageLabel.setImage(image1);
				newImagePathLabel.setText(filename.getAbsolutePath());
				createCloseImageBtn.setVisible(true);
			} catch (FileNotFoundException e) {
				Logger.logMessage("Error select new image");
			}
	    }
	}
	@FXML
	protected void createCloseImageBtn() {
		newImageLabel.setImage(null);
		newImagePathLabel.setText("");
		createCloseImageBtn.setVisible(false);
	}

	@FXML
    protected void newCreateBtn() {
		if(!newQuestionArea.getText().isEmpty()&&!newTopicsField.getText().isEmpty()) {
			
			final List<String> result = Arrays.asList(newTopicsField.getText().split(",")).stream()
			        .map(String::trim)
			        .collect(Collectors.toList());
			boolean b=false;
			if(newMCQRB.isSelected()) {
				b=createNewMCQQuestion(result);
			}else {
				b=true;
				createNewQuestion(result);
			}
			if(b) {
				alertInfo(AlertType.INFORMATION,newCreateBtn.getText()+" New Question", "New question "+newCreateBtn.getText()+"d successfully");
				clearCreateFields();
			}
		}else {
				alertInfo(AlertType.ERROR,"Error validating question","Fill all required fields(Label,Topic)");
			
		}
		
	}

	private void createNewQuestion(final List<String> result) {
		Question question = new Question(newQuestionArea.getText(),result,(int)newDifficultySlider.getValue(),0);
		question.setId(updateQuestionIdInteger);
		if(!newImagePathLabel.getText().isEmpty())
			question.setImage(newImagePathLabel.getText());
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		if(newCreateBtn.getText().equals(UPDATE))
			dao.update(question);
		else
			dao.create(question);
	}

	private boolean createNewMCQQuestion(final List<String> result) {
		List<MCQChoice> choices = new ArrayList<>();
		boolean b=false;
		for(CheckBox c : newChoices) {
			choices.add(new MCQChoice(c.getText(),c.isSelected()));
			if(c.isSelected())
				b=true;
		}
		if(!b)
			alertInfo(AlertType.ERROR,"Error validating question","Please select at least 1 choice as valid");
		else {
			MCQQuestion question = new MCQQuestion(newQuestionArea.getText(),result,(int)newDifficultySlider.getValue(),1,choices);
			question.setId(updateQuestionIdInteger);
			if(!newImagePathLabel.getText().isEmpty())
				question.setImage(newImagePathLabel.getText());
			MCQQuestionDAO dao = new MCQQuestionDAO();
			if(newCreateBtn.getText().equals(UPDATE))
				dao.update(question);
			else
				dao.create(question);
		}
		return b;
	}

	private void alertInfo(Alert.AlertType type,String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	@FXML
    protected void simpleAnswerBtn() {

		newSimpleAnswerRB.setSelected(true);
		newMCQRB.setSelected(false);
		newChoicesPane.getParent().getParent().getParent().setVisible(false);
		newAddChoiceBtn.setVisible(false);
	}
	@FXML
    protected void mCQBtn() {
		newMCQRB.setSelected(true);
		newSimpleAnswerRB.setSelected(false);
		newChoicesPane.getParent().getParent().getParent().setVisible(true);
		newAddChoiceBtn.setVisible(true);
	}
	@FXML
    protected void newAddChoice() {
		
		 
		Optional<String> result = userInputDialog("", "Add new Choice", "Enter new choice", "Choice: ");
		 
		result.ifPresent(choice -> {
			final CheckBox checkBox = new CheckBox(choice);
			checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> promptToAddNewChoice(checkBox));
			Logger.logMessage("Error select new image");
			newChoices.add(checkBox);
		});
		newChoice = new ListView<>(FXCollections.observableArrayList(newChoices));
		newChoice.setPrefHeight(newChoices.size() * (double)24 + 24);
		newChoice.setPrefWidth((double)440);
		newChoicesPane.getChildren().add(newChoice);
	}
	@FXML
    protected void viewQuestionsTab() {

		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		questions=dao.search(new Question());
		
		questionsList = new ListView<>(FXCollections.observableArrayList(questions));
		questionsList.setPrefHeight(questions.size() * (double)24 + 24);
		questionsList.setPrefWidth((double)440);
		
		viewQuestionsScrollPane.getChildren().add(questionsList);
		newQuestionTitleBtn.setSelected(false);
	}
	private String getQuizName(String quizName) {
		return "Quiz "+quizName;
	}
	@FXML
    protected void startQuiz() {
		QuizJDBCDAO quizDAO = new QuizJDBCDAO();
		if(quizDAO.checkQuiz(quizName.getText())){
			Optional<String> result=userInputDialog("",getQuizName(quizName.getText())+" found, get ready to start", "Enter your name", "Full name: ");
			result.ifPresent( name -> {
				studentLoginPane.setVisible(false);
				
				quizTitledPane.setExpanded(true);
				quizTitledPane.setText(quizName.getText()+" - "+name);
				QuestionJDBCDAO dao = new QuestionJDBCDAO();
				currentQuiz = new Quiz(quizName.getText());
				currentQuiz.setQuestions(dao.getClassQuestions(currentQuiz));
				currentStudent=new Student(name);
				UserJDBCDAO userdao = new UserJDBCDAO();
				boolean userfound=userdao.validateStudent(currentStudent);
				if(currentQuiz.getQuestions().isEmpty()){
					alertInfo(AlertType.ERROR,"No questions for these criterias",ERROR);
				}else if(userfound){
					alertInfo(AlertType.ERROR,"Name already exists",ERROR);
					startQuiz();
				}else{
					userdao.create(currentStudent);
					userdao.studentQuizTaken(currentStudent, currentQuiz);

					freeTestTabBtn.setDisable(true);
					professorTabBtn.setDisable(true);
					
					nextQuestionBtn();
				}
			});
		}else {
			Optional<String> result = userInputDialog(quizName.getText(), getQuizName(quizName.getText())+" not found","Enter new quiz name: ",getQuizName("name: "));
			result.ifPresent(choice -> {
				quizName.setText(choice);
				startQuiz();
			});
		}
	}

	@FXML
	protected void nextQuestionBtn() {
		if(currentStudent.getQuestionCounter()!=0)
			checkPreviousQuestion();
		if(currentStudent.getQuestionCounter()<currentQuiz.getQuestions().size()) {
			nextQuestionQuiz();
		}else {
			submitQuiz();
		}
	}
	private void nextQuestionQuiz() {
		if(currentStudent.getQuestionCounter()==currentQuiz.getQuestions().size()-1) {
			nextQuestionBtn.setText("Submit");
		}
		
		currentQuestion = currentQuiz.getQuestions().get(currentStudent.getQuestionCounter());
		quizQuestionLabel.setText(currentQuestion.getQuestion());
		if(currentQuestion.isMCQ()) {
			quizAnswerTextArea.setVisible(false);
			newChoices.clear();
			newChoicesPane.getChildren().clear();
			for(MCQChoice choice : currentQuestion.getChoices()) {
				final CheckBox checkBox = new CheckBox(choice.getChoice());
				newChoices.add(checkBox);
			}
			newChoice = new ListView<>(FXCollections.observableArrayList(newChoices));
			newChoice.setPrefHeight(newChoices.size() * (double)24 + 24);
			newChoice.setPrefWidth((double)440);
			quizAnswerPane.getChildren().add(newChoice);
			quizAnswerPane.getParent().getParent().getParent().setVisible(true);
		}else {
			quizAnswerPane.getChildren().remove(newChoice);
			quizAnswerTextArea.setVisible(true);
			quizAnswerTextArea.setText("");
		}
		if(currentQuestion.getImage()!=null&&!currentQuestion.getImage().isEmpty()) {
			try {
				Image image1 = new Image(new FileInputStream(currentQuestion.getImage()));
				quizImageViewLabel.setImage(image1);
			} catch (FileNotFoundException e) {
				Logger.logMessage("Error setting image to question");
			}
		}else {
			quizImageViewLabel.setImage(null);
		}
		currentStudent.incrementQuestionCounter();
	}
	private void submitQuiz() {
		alertInfo(AlertType.INFORMATION,"Thank you","Your quiz is submitted successfully");
		currentStudent.calculateTotal(100);
		alertInfo(AlertType.INFORMATION,"Thank you",currentStudent.getFinalScore());
		studentLoginPane.setVisible(true);
		quizTitledPane.setExpanded(false);
		quizTitledPane.setText("");

		freeTestTabBtn.setDisable(false);
		professorTabBtn.setDisable(false);
	}

	@FXML
	protected void checkPreviousQuestion() {
		if(currentQuestion.isMCQ()) {
			choicesId.clear();
			
			MCQAnswer answer;
			answer=getUserMCQAnswer();
			currentStudent.incrementMCQquestionCounter();
			if(answer.isValid()) {
				currentStudent.incrementValidAnswers();
				alertInfo(AlertType.INFORMATION, "Right Answer", "The answer is right");
			}else {
				alertInfo(AlertType.ERROR, "Wrong Answer","The answer is wrong");
			}
			currentStudent.incrementTotalAnswers();
			AnswerDAO dao = new AnswerDAO();
			dao.create(answer);
		}else {
			Answer answer = new Answer(quizAnswerTextArea.getText());
			answer.setQuestionId(currentQuestion.getId());
			answer.setStudent(currentStudent.getName());
			currentStudent.incrementTotalAnswers();
			AnswerDAO dao = new AnswerDAO();
			dao.create(answer);
		}
	}
	private MCQAnswer getUserMCQAnswer() {
		List<MCQChoice> choices = currentQuestion.getChoices();
		boolean b;
		MCQAnswer answer;
		b = getAnswerValidity(choices);
		newChoices.clear();
		newChoicesPane.getChildren().clear();
		answer=new MCQAnswer(choicesId);
		answer.setQuestionId(currentQuestion.getId());
		answer.setStudent(currentStudent.getName());
		answer.setValid(b);
		
		return answer;
	}
	private boolean getAnswerValidity(List<MCQChoice> choices) {
		boolean b;
		currentStudent.clearChoices();
		for(CheckBox c : newChoices) 
		{
			getUserChoiceValidity(choices, c);
		}
		b=currentStudent.getChoicesValidity();
		return b;
	}
	private void getUserChoiceValidity(List<MCQChoice> choices, CheckBox c) {
		for(MCQChoice choice: choices) {
			if(choice.getChoice().equals(c.getText())) {
				if (c.isSelected() && choice.isValid()) {
					currentStudent.incrementUserChoice();
				}else if (c.isSelected() && !choice.isValid()) {
					choicesId.add(choice);
				}
				if(choice.isValid())
					currentStudent.incrementValidChoice();
			}
		}
	}
	private Optional<String> userInputDialog(String input,String title, String header, String content) {
		TextInputDialog dialog = new TextInputDialog(input);
		 
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		 
		return dialog.showAndWait();
	}
	@FXML
    protected void startFreeTest() {
		List<String> topics = new ArrayList<>();
		 for(CheckBox c : cb) {
			 if(c.isSelected()) {
				 topics.add(c.getText());
			 }
		 }
		 if(topics.isEmpty()) {
			 alertInfo(AlertType.ERROR, ERROR,"Select at least one topic");
		 }else{
			 if(freeName.getText()!=null&&!freeName.getText().isEmpty()) {
				 
				currentStudent = new Student(freeName.getText());
				QuestionJDBCDAO dao = new QuestionJDBCDAO();
				questions = dao.getQuizQuestions(new Question("", topics,(int)diffSlider.getValue(), 0));
				currentQuiz = new Quiz("Free Test");
				currentQuiz.setQuestions(questions);
				UserJDBCDAO userdao = new UserJDBCDAO();
				userdao.create(currentStudent);
				userdao.studentQuizTaken(currentStudent, currentQuiz);
				mainTabPane.getSelectionModel().select(1);
				studentLoginPane.setVisible(false);
				quizTitledPane.setExpanded(true);
				quizTitledPane.setText(quizName.getText()+"Free Test "+topics.toString()+" Diff: "+(int)diffSlider.getValue()+" By: "+freeName.getText());
				nextQuestionBtn();
				freeName.setText("");
				freeTestTabBtn.setDisable(true);
				professorTabBtn.setDisable(true);
			 }
		 }
	}
	    
	@FXML
    protected void initialize() {

		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		List<String> topics=dao.getTopics();
		int i=0;
		for(String topic : topics) {
			cb.add(new CheckBox());
			cb.get(i).setText(topic);
			i++;
		}

		ListView<CheckBox> topicsList = new ListView<>(FXCollections.observableArrayList(cb));
		topicsList.setPrefHeight((double)topics.size() * 24 + 2);
		topicsPane.getChildren().add(topicsList);
		pf = new PasswordField();
		pf.setFont(new Font(24));
		passwordPane.getChildren().add(pf);

	}
	@FXML
    protected void loginProfessor() {
		UserJDBCDAO usr= new UserJDBCDAO();
		if(usr.authenticateAdmin(usernameField.getText(),Encryption.md5(pf.getText()))) {
			alertInfo(AlertType.INFORMATION,"Welcome","Welcome "+usernameField.getText());
			
			usernameField.setText("");
			pf.setText("");
			professorMainPane.setVisible(true);
			loginPane.setVisible(false);
		}else {
			alertInfo(AlertType.ERROR,"Username or password wrong","Username and password don't match");
			
		}
	}
	@FXML
    protected void logoutProfessor() {
		professorMainPane.setVisible(false);
		loginPane.setVisible(true);
		professorMainPane.getSelectionModel().select(0);
	}

	
	
}