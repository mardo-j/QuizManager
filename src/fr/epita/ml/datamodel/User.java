package fr.epita.ml.datamodel;

import java.util.Collections;
import java.util.List;

/**
 * User class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */
public class User {


	private int id;
	private int admin;
	private String name; 

	static int bestNumber = 0;
	
	public User(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(int id) {
		this.id=id;
	}
	public int getId() {
		return id;
	}
	public int getAdmin() {
		return this.admin;
	}
	public String getPassword() {
		return null;
	}
	public void calculateTotal(int i) {
		//Overridden in other class
	}
	public double getTotalgrade() {

		return bestNumber;
	}
	public void addToGrade(double result) {
		//Overridden in other class
	}
	public void incrementValidAnswers() {
		//Overridden in other class
	}
	public void incrementQuestionCounter() {
		//Overridden in other class
	}
	public void incrementMCQquestionCounter() {
		//Overridden in other class
	}
	public void incrementTotalAnswers() {
		//Overridden in other class
	}
	public boolean isValid() {
		return false;
	}
	public void setValid(boolean valid) {
		//Overridden in other class
	}
	public boolean getChoicesValidity() {
		return false;
	}
	public int getQuestionCounter() {
		//Overridden in other class
		return (int)this.getTotalgrade();
	}
	public Quiz getQuiz() {
		return null;
	}
	public void setQuiz(Quiz quiz) {
		//Overridden in other class
	}
	public List<Question> getQuestions() {
		return Collections.emptyList();
	}
	public void setQuestions(List<Question> questions) {
		//Overridden in other class
	}
	
	public List<Answer> getAnswers() {
		return Collections.emptyList();
	}
	public void setAnswers(List<Answer> answers) {
		//Overridden in other class
	}
	

	public String getFinalScore() {
		return null;
	}
	/**
	 * User toString method
	 */
	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}

	
}
