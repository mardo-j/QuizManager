package fr.epita.ml.tests;

import java.sql.SQLException;
import java.util.Arrays;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.User;
import fr.epita.ml.services.QuestionJDBCDAO;
import fr.epita.ml.services.UserJDBCDAO;
/**
 * JDBCDAO test class test QuestionJDBCDAO and UserJDBCDAO methods
 * @author Mardo.Lucas
 *
 */
public class JDBCDAOTest {

	public static void main(String[] args) throws SQLException {
		QuestionJDBCDAO jd = new QuestionJDBCDAO();
		//get all questions
		Logger.logMessage(jd.search(new Question("",null,null,0)).toString());
		//get all questions by difficulty
		Logger.logMessage(jd.getQuestionsByDifficulty(1).toString());
		//get all questions by selected topics
		Logger.logMessage(jd.getQuestionsByTopic(Arrays.asList("Char","Double")).toString());
		//get all quiz questions by topics and difficulty
		int diff=1;
		Logger.logMessage(jd.getQuizQuestions(new Question("",Arrays.asList("Char","Double"),diff,0)).toString());
		
		
		UserJDBCDAO ud = new UserJDBCDAO();
		//get all students who took quiz
		Logger.logMessage(ud.search(new User("")).toString());
	}
}
