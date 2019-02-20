package fr.epita.ml.datamodel;

import java.util.List;

/**
 * MCQAnswer class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */
public class MCQAnswer extends Answer{
	
	private List<MCQChoice> choices;
	private int questionId;
	private String student;
	private boolean valid;
	
	/**
	 * MCQAnswer constructor
	 * 
	 */
	public MCQAnswer() {
		//Overriden constructor
	}
	@Override
	public List<MCQChoice> getChoices() {
		return choices;
	}
	@Override
	public void setChoices(List<MCQChoice> choices) {
		this.choices = choices;
	}
	@Override
	public int getQuestionId() {
		return questionId;
	}
	@Override
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	@Override
	public String getStudent() {
		return student;
	}
	@Override
	public void setStudent(String student) {
		this.student = student;
	}
	@Override
	public boolean isValid() {
		return valid;
	}
	@Override
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	/**
	 * MCQAnswer toString method
	 */
	@Override
	public String toString() {
		return "MCQAnswer [choices=" + choices + "]";
	}

}
