package fr.epita.ml.datamodel;

import java.util.Collections;
import java.util.List;
/**
 * Question class with getters and setters for private variables
 * @author Mardo.Lucas
 *
 */
public class Question {

	private int id;
	
	private String label; 
	private List<String> topics;	 
	private Integer difficulty; 
	private Integer mcq;
	private String image;
	

	/**
	 * Question class with no parameters will be constructed as empty question
	 * It serves for search and new questions before initializing them
	 */
	public Question() {
		this.id=0;
		this.label = "";
		this.topics = null;
		this.difficulty = 1;
		this.mcq = 0;
	}
	/**
	 * Question constructor
	 * @param question
	 * @param topics
	 * @param difficulty
	 * @param mcq
	 */
	public Question(String question, List<String> topics, Integer difficulty, int mcq) {
		this.label = question;
		this.topics = topics;
		this.difficulty = difficulty;
		this.mcq = mcq;
	}
	
	
	public String getQuestion() {
		return label;
	}
	public void setQuestion(String question) {
		this.label = question;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	public Integer getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMCQ(Integer mcq) {
		this.mcq=mcq;
	}
	public boolean isMCQ() {
		return this.mcq == 1;
	}
	public List<MCQChoice> getChoices() {
		return Collections.emptyList();
	}

	public void setImage(String image) {
		this.image=image;
	}
	public String getImage() {
		return this.image;
	}
	public void setChoices(List<MCQChoice> choices) {
	}
	/**
	 * Question toString method
	 */
	@Override
	public String toString() {
		return "Question [id=" + id + ", question=" + label + ", topics=" + topics + ", difficulty=" + difficulty
				+ ", mcq=" + mcq + ", image=" + image + "]";
	}
}
