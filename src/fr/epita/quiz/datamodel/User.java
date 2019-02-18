package fr.epita.quiz.datamodel;


public class User {


	private int id;
	private int admin;
	
	private String name; 
	public User() {
		
	}
	public User(String name) {
		this.name=name;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
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
	public void incrementValidAnswers() {
	}
	public void incrementQuestionCounter() {
	}
	public void incrementMCQquestionCounter() {
	}
	public void incrementTotalAnswers() {
	}

	public int getQuestionCounter() {
		return 0;
	}

	public String getFinalScore() {
		// TODO Auto-generated method stub
		return null;
	}
}
