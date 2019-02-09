package fr.epita.quiz.tests.xml;

import fr.epita.quiz.services.QuestionJDBCDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.epita.quiz.datamodel.Question;

public class JDBCDAOTest {

	public static void main(String[] args) {
		QuestionJDBCDAO jd = new QuestionJDBCDAO();
//		Question q = new Question();
//		q.setDifficulty(3);
//		q.setQuestion("What does the static keyword mean?");
//
//		List<String> topics = new ArrayList<>(Arrays.asList("Java","Keywords"));
//		topics.add("Java");
//		topics.add("Keywords");
//		q.setTopics(topics);
//		
//		jd.create(new Question("What does the static keyword mean?",new ArrayList<>(Arrays.asList("Java","Keywords")),3));
//		jd.create(new Question("What does \"<=\" mean for a java condition?",new ArrayList<>(Arrays.asList("Java","Syntax")),3));
		List<Question> lq = jd.search(new Question("",null,null));
		System.out.println(lq.toString());
		lq = jd.getQuestionsByDifficulty(3);
		System.out.println(lq.toString());
		lq = jd.getQuestionsByTopic(Arrays.asList("Java","Syntax"));
		System.out.println(lq.toString());
	}
}
