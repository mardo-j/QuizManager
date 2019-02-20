package fr.epita.ml.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Answer;
import fr.epita.ml.datamodel.MCQAnswer;
import fr.epita.ml.datamodel.MCQChoice;
/**
 * AnswerDAO class with methods to query the database
 * @author Mardo.Lucas
 *
 */
public class AnswerDAO {
	/**
	 * create method to insert the student's open answer into the database
	 * 
	 * @param answer User's open answer
	 */
	public void create(Answer answer) {
		String sqlCommand = "INSERT INTO STUDENT_ANSWERS(student_name,question_id,answer) VALUES (?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {

			insertStatement.setString(1, answer.getStudent());
			insertStatement.setInt(2, answer.getQuestionId());
			insertStatement.setString(3, answer.getText());
			
			insertStatement.execute();

		} catch (SQLException e) {
			Logger.logMessage("Error creating STUDENT_ANSWERS");
		}

	}
	/**
	 * create method to insert the student's MCQ answer into the database
	 * 
	 * @param answer User's MCQ answer
	 */
	public void create(MCQAnswer answer) {
		String sqlCommand = "INSERT INTO STUDENT_ANSWERS(student_name,question_id,answer) VALUES (?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {

			insertStatement.setString(1, answer.getStudent());
			insertStatement.setInt(2, answer.getQuestionId());
			List<Integer> choices = new ArrayList<>();
			for(MCQChoice choice : answer.getChoices()) {
				choices.add(choice.getId());
			}
			insertStatement.setString(3, choices.toString());
			
			insertStatement.execute();

		} catch (SQLException e) {
			Logger.logMessage("Error creating STUDENT_ANSWERS "+e.getMessage());
		}

	}


	/**
	 * getConnection method to to connect to the database
	 */
	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	/**
	 * search method to search for the student's answer(s) on the database
	 * 
	 * @param answer User's answer
	 */
	public List<Answer> search(Answer answer) {
		List<Answer> resultList = new ArrayList<>();
		String selectQuery = "select id,student_name,question_id,answer from STUDENT_ANSWERS WHERE label like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+answer.getText()+"%");
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error searching STUDENT_ANSWERS");
		}
		return resultList;
	}
	/**
	 * prepareTryStatement method to execute query on the database and retrieve results
	 * 
	 * @param resultList List of results from the query
	 * @param preparedStatement SQL query statement
	 */
	private void prepareTryStatement(List<Answer> resultList, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing try statement query execution STUDENT_ANSWERS");
		}
	}
	/**
	 * addToResultList method to add new results to a list of results
	 * 
	 * @param resultList List of results to receive new results
	 * @param results New results to be added to the existing list
	 */
	private void addToResultList(List<Answer> resultList, ResultSet results) throws SQLException {
			Answer q = new Answer();
			q.setText(results.getString("text"));
			q.setId(results.getInt("id"));
			q.setStudent(results.getString("student_name"));
			q.setQuestionId(results.getInt("question_id"));
			resultList.add(q);
		
	}

}
