package fr.epita.quiz.datamodel;

import java.util.List;

public class MCQAnswer {
	
	private List<MCQChoice> choices;
	private int questionId;
	private String student;
	private boolean valid;
	
	
	public MCQAnswer(List<MCQChoice> choices) {
		this.choices=choices;
	}
	
	public List<MCQChoice> getChoices() {
		return choices;
	}
	public void setChoices(List<MCQChoice> choices) {
		this.choices = choices;
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
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "MCQAnswer [choices=" + choices + ", questionId=" + questionId + ", student=" + student + "]";
	}

}
