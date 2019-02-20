package fr.epita.ml.datamodel;
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
	 * @param text User's answer
	 */
	public Answer(String text) {
		this.text=text;
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
	@Override
	public String toString() {
		return "Answer [text=" + text + ", questionId=" + questionId + ", student=" + student + "]";
	}

	

}
