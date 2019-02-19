package fr.epita.quiz.datamodel;
/**
 * Student class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */
public class Student extends User{

	 
	private double totalgrade;
	private int validAnswers;
	
	private int questionCounter;
	private int mCQquestionCounter;
	private int validChoice;
	
	private int userChoice;
	public Student(String name) {
		super(name);
		this.totalgrade=0;
		this.validAnswers=0;
		this.questionCounter=0;
		this.mCQquestionCounter=0;
		this.validChoice=0;
		this.userChoice=0;
	}
	@Override
	public boolean getChoicesValidity() {
		return this.validChoice==this.userChoice;
	}
	@Override
	public void clearChoices() {
		this.validChoice = 0;
		this.userChoice = 0;
	}
	
	
	@Override
	public double getTotalgrade() {
		return totalgrade;
	}
	@Override
	public void addToGrade(double result) {
		if(result>0)
			validAnswers++;
	}
	@Override
	public void calculateTotal(int over) {
		totalgrade=(double)validAnswers*over/mCQquestionCounter;
	}
	@Override
	public void incrementValidAnswers() {
		this.validAnswers++;
	}
	@Override
	public void incrementQuestionCounter() {
		this.questionCounter++;
	}
	@Override
	public void incrementMCQquestionCounter() {
		this.mCQquestionCounter++;
	}
	@Override
	public void incrementValidChoice() {
		this.validChoice++;
	}
	@Override
	public void incrementUserChoice() {
		this.userChoice++;
	}
	@Override
	public int getQuestionCounter() {
		return questionCounter;
	}
	@Override
	public String getFinalScore() {
		return "Your score is: "+String.format("%.2f",this.totalgrade)+"% "
		+ "\n"+this.validAnswers+"/"+this.mCQquestionCounter+" right MCQ answers"
				+ "\nThe other "+(this.questionCounter-this.mCQquestionCounter)+" will be corrected by the Professor";
	}
}
