package fr.epita.quiz.datamodel;

import java.util.List;

public class MCQQuestion extends Question{
	private List<MCQChoice> choices;
	public MCQQuestion(String question, List<String> topics, int difficulty, int mcq,List<MCQChoice> choices){
		super(question, topics, difficulty, mcq);
		this.choices=choices;
	}
	
	@Override
	public String toString() {
		return "MCQQuestion [" + super.toString() + ", choices=" + choices + "]";
	}
	@Override
	public List<MCQChoice> getChoices() {
		return choices;
	}
	public void setChoices(List<MCQChoice> choices) {
		this.choices = choices;
	}
}
