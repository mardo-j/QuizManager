package fr.epita.quiz.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.logger.Logger;
import fr.epita.quiz.datamodel.Answer;
import fr.epita.quiz.datamodel.MCQAnswer;
import fr.epita.quiz.datamodel.MCQChoice;

public class AnswerDAO {

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

	public void update(Answer answer) {
			String updateQuery = "UPDATE STUDENT_ANSWERS SET student_name=?,question_id=?,answer=? WHERE ID=?";
			try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
				updateStatement.setString(1, answer.getStudent());
				updateStatement.setInt(2, answer.getQuestionId());
				updateStatement.setString(3, answer.getText());
				updateStatement.setInt(4, answer.getId());
				updateStatement.execute();
			}catch (SQLException e) {
				Logger.logMessage("Error updating STUDENT_ANSWERS");
			}
	}


	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	public void delete(Answer answer) {
		String deleteQuery = "DELETE STUDENT_ANSWERS WHERE ID=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
			deleteStatement.setInt(1, answer.getId());
			deleteStatement.execute();
		}catch (SQLException e) {
			Logger.logMessage("Error deleting STUDENT_ANSWERS");
		}
	}

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
	private void prepareTryStatement(List<Answer> resultList, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing try statement query execution STUDENT_ANSWERS");
		}
	}
	private void addToResultList(List<Answer> resultList, ResultSet results) throws SQLException {
			Answer q = new Answer(
				results.getString("text")
				);
			q.setId(results.getInt("id"));
			q.setStudent(results.getString("student_name"));
			q.setQuestionId(results.getInt("question_id"));
			resultList.add(q);
		
	}

}