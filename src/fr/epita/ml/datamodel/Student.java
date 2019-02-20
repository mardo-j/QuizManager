package fr.epita.ml.datamodel;

import java.util.List;

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
	
	private Quiz quiz;
	
	private List<Question> questions;
	private List<Answer> answers;
	
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
	public Quiz getQuiz() {
		return quiz;
	}
	@Override
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	@Override
	public List<Question> getQuestions() {
		return questions;
	}
	@Override
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	@Override
	public List<Answer> getAnswers() {
		return answers;
	}
	@Override
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String getFinalScore() {
		return "Your score is: "+String.format("%.2f",this.totalgrade)+"% "
		+ "\n"+this.validAnswers+"/"+this.mCQquestionCounter+" right MCQ answers"
				+ "\nThe other "+(this.questionCounter-this.mCQquestionCounter)+" will be corrected by the Professor";
	}
}
