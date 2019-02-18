package fr.epita.quiz.datamodel;

public class Student extends User{

	 
	private double totalgrade;
	private int validAnswers;
	
	private int questionCounter;
	private int mCQquestionCounter;
	public Student(String name) {
		super(name);
		this.totalgrade=0;
		this.validAnswers=0;
		this.questionCounter=0;
		this.mCQquestionCounter=0;
	}
	
	
	public double getTotalgrade() {
		return totalgrade;
	}
	
	public void addToGrade(double result) {
		if(result>0)
			validAnswers++;
	}
	
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
		return "Your score is: "+this.totalgrade+"% "
		+ "\n"+this.validAnswers+"/"+this.mCQquestionCounter+" right MCQ answers"
				+ "\nThe other "+(this.questionCounter-this.mCQquestionCounter)+" will be corrected by the Professor";
	}
}
