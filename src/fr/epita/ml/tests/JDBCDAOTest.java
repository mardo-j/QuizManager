package fr.epita.ml.tests;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.User;
import fr.epita.ml.services.QuestionJDBCDAO;
import fr.epita.ml.services.UserJDBCDAO;

public class JDBCDAOTest {

	public static void main(String[] args) throws SQLException {
		QuestionJDBCDAO jd = new QuestionJDBCDAO();

		Logger.logMessage(jd.search(new Question("",null,null,0)).toString());
		Logger.logMessage(jd.getQuestionsByDifficulty(1).toString());
		Logger.logMessage(jd.getQuestionsByTopic(Arrays.asList("Char","Double")).toString());
		
		
		UserJDBCDAO ud = new UserJDBCDAO();
		Logger.logMessage(ud.search(new User("")).toString());
	}
}
