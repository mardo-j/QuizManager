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
	}
	public double getTotalgrade() {
		return 0;
	}
	public void addToGrade(double result) {
	}
	public void incrementValidAnswers() {
	}
	public void incrementQuestionCounter() {
	}
	public void incrementMCQquestionCounter() {
	}
	public void incrementTotalAnswers() {
	}
	public boolean isValid() {
		return false;
	}
	public void setValid(boolean valid) {
	}
	public boolean getChoicesValidity() {
		return false;
	}
	public int getQuestionCounter() {
		return 0;
	}
	public Quiz getQuiz() {
		return null;
	}
	public void setQuiz(Quiz quiz) {
	}
	public List<Question> getQuestions() {
		return Collections.emptyList();
	}
	public void setQuestions(List<Question> questions) {
	}
	
	public List<Answer> getAnswers() {
		return Collections.emptyList();
	}
	public void setAnswers(List<Answer> answers) {
	}
	

	public String getFinalScore() {
		return null;
	}
	/**
	 * User toString method
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

	
}
