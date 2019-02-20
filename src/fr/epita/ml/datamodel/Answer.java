package fr.epita.ml.datamodel;

import java.util.Collections;
import java.util.List;

/**
 * Answer class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */
public class Answer {

	private String text;
	private int questionId;
	
	private int id;
	private String student;
	/**
	 * Answer class Constructor
	 * 
	 * 
	 */
	public Answer() {
		//Overridden in other class
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}

	/**
	 * Answer toString method
	 */
	public String toString() {
		return "Answer [text=" + text + ", student=" + student + "]";
	}

	public void setChoices(List<MCQChoice> choices) {
		//Overridden in other class
	}



	public List<MCQChoice> getChoices() {
		return Collections.emptyList(); 
	}

	public void setValid(boolean b) {
		//Overridden in other class
	}

	public boolean isValid() {
		return false;
	}

	

}
