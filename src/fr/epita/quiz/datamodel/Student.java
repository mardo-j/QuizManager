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
	private boolean valid;
	
	
	public Student(String name) {
		super(name);
		this.totalgrade=0;
		this.validAnswers=0;
		this.questionCounter=0;
		this.mCQquestionCounter=0;
		this.valid=true;
	}
	@Override
	public boolean isValid() {
		return this.valid;
	}
	@Override
	public void setValid(boolean valid) {
		this.valid = valid;
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
