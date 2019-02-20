package fr.epita.quiz.datamodel;

import java.util.List;
/**
 * MCQQuestion class with getters and setters for private variables
 * MCQQuestion is inherited from Question
 * @author Mardo.Lucas
 *
 */
public class MCQQuestion extends Question{
	private List<MCQChoice> choices;
	/**
	 * MCQQuestion class Constructor sends first 4 parameters to super
	 * @param question
	 * @param topics
	 * @param difficulty
	 * @param mcq
	 * @param choices
	 */
	public MCQQuestion(String question, List<String> topics, int difficulty, int mcq,List<MCQChoice> choices){
		super(question, topics, difficulty, mcq);
		this.choices=choices;
	}
	/**
	 * MCQQuestion toString method
	 */
	@Override
	public String toString() {
		return "MCQQuestion [" + super.toString() + ", choices=" + choices + "]";
	}
	@Override
	public List<MCQChoice> getChoices() {
		return choices;
	}
	@Override
	public void setChoices(List<MCQChoice> choices) {
		this.choices = choices;
	}
	
}
