package fr.epita.quiz.datamodel;

public class Answer {

	private String text;
	private int questionId;
	private int id;
	private String student;
	
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

	@Override
	public String toString() {
		return "Answer [text=" + text + ", question_id=" + questionId + "]";
	}

}
