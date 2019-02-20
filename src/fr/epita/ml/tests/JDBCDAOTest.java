package fr.epita.ml.tests;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.services.QuestionJDBCDAO;

public class JDBCDAOTest {

	public static void main(String[] args) throws SQLException {
		QuestionJDBCDAO jd = new QuestionJDBCDAO();

		List<Question> lq = jd.search(new Question("",null,null,0));
		Logger.logMessage(lq.toString());
		lq = jd.getQuestionsByDifficulty(3);
		Logger.logMessage(lq.toString());
		lq = jd.getQuestionsByTopic(Arrays.asList("Java","Syntax"));
		Logger.logMessage(lq.toString());
	}
}
