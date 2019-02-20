package fr.epita.ml.datamodel;

import java.util.List;
/**
 * Quiz class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */

public class Quiz {

	String title;
	int id;
	List<Question> questions;
	
	public Quiz(String title) {
		this.title=title;
	}
	
	
	public Quiz(String title,List<Question> questions) {
		this.title=title;
		this.questions=questions;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * Quiz toString method
	 */
	@Override
	public String toString() {
		return "Quiz [title=" + title + ", id=" + id + ", questions=" + questions.toString() + "]";
	}

	
	
}
