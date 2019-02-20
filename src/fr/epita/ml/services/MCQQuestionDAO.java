package fr.epita.ml.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.MCQQuestion;
/**
 * MCQQuestionDAO class with methods to query the database
 * @author Mardo.Lucas
 *
 */
public class MCQQuestionDAO {
	
	/**
	 * create method to insert the question into the database
	 * 
	 * @param question New question
	 */
	public void create(MCQQuestion question) {
		String sqlCommand = "INSERT INTO QUESTION(label,difficulty,topics,mcq,image) VALUES (?,?,?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			insertStatement.setString(1, question.getQuestion());
			insertStatement.setInt(2, question.getDifficulty());
			insertStatement.setString(3, question.getTopics().toString());
			insertStatement.setInt(4, 1);
			insertStatement.setString(5, question.getImage());
			insertStatement.execute();
			int insertID = getLastInsertID(connection);
			int resultlength=question.getChoices().size();
			for(int i=0;i<resultlength;i++) {
				String sqlCommand1 = "INSERT INTO CHOICES(CHOICE,VALID,RELATION) VALUES (?,?,?)";
				insertChoicesToDB(question, connection, insertID, i, sqlCommand1);
			}
		} catch (SQLException e) {
			Logger.logMessage("Error create MCQQuestion");
		}
	}
	/**
	 * update method to update the question on the database
	 * 
	 * @param question New question
	 */
	public void update(MCQQuestion question) {
		if(question.getQuestion().length()==0) {
			delete(question);
		}else {
			String updateQuery = "UPDATE QUESTION SET LABEL=?,DIFFICULTY=?,TOPICS=?,MCQ=?,IMAGE=? WHERE ID=?";
			try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
				updateStatement.setString(1, question.getQuestion());
				updateStatement.setInt(2, question.getDifficulty());
				updateStatement.setString(3, question.getTopics().toString());
				updateStatement.setInt(4, 1);
				updateStatement.setString(5, question.getImage());
				updateStatement.setInt(6, question.getId());
				updateStatement.execute();
				deleteChoicesFromDB(question, connection);
				int resultlength=question.getChoices().size();
				for(int i=0;i<resultlength;i++) {
					String sqlCommand1 = "INSERT INTO CHOICES(CHOICE,VALID,RELATION) VALUES (?,?,?)";
					insertChoicesToDB(question, connection, question.getId(), i, sqlCommand1);
				}
			}catch (SQLException e) {
				Logger.logMessage("Error updating MCQQuestion");
			}
		}
	}
	/**
	 * delete method to delete the question from the database
	 * 
	 * @param question Question to be deleted
	 */
	public void delete(MCQQuestion question) {
		String deleteQuery = "DELETE QUESTION WHERE ID=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
			deleteStatement.setInt(1, question.getId());
			deleteStatement.execute();
			deleteChoicesFromDB(question, connection);
		}catch (SQLException e) {
			Logger.logMessage("Error delete MCQQuestion");
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
	 * insertChoicesToDB method to insert MCQ choices for a question on the database
	 * 
	 * @param question Question to receive the choices
	 * @param connection Connection parameters to connect to the database
	 * @param question Question to receive the choices 
	 * @param insertID ID of the question to receive the choices
	 * @param i Order of the choice
	 */
	private void insertChoicesToDB(MCQQuestion question, Connection connection, int insertID, int i,
			String sqlCommand) {
		try (PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {

			insertStatement.setString(1, question.getChoices().get(i).getChoice());
			insertStatement.setInt(2, question.getChoices().get(i).isValid() ? 1: 0);
			insertStatement.setInt(3, insertID);
			
			insertStatement.execute();
		} catch (SQLException e) {
			Logger.logMessage("Error creating Choices");
		}
	}
	/**
	 * deleteChoicesFromDB method to delete the choices for a question on the database
	 * 
	 * @param question Question which choices will be deleted
	 * @param connection Connection parameters to connect to the database
	 */
	private void deleteChoicesFromDB(MCQQuestion question, Connection connection) {
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM CHOICES WHERE RELATION=? AND ID>0");) {
		    statement.setInt(1, question.getId());
			statement.execute();
			
		}catch (SQLException e) {
			Logger.logMessage("Error updating MCQQuestion");
		}
	}
	/**
	 * getLastInsertID method to to get the ID of the last inserted question
	 * 
	 * @param connection Connection parameters to connect to the database
	 */
	private int getLastInsertID(Connection connection) {
		int insertID=0;
		try (PreparedStatement statement = connection.prepareStatement("CALL SCOPE_IDENTITY();");
				ResultSet results = statement.executeQuery();) {
			results.next();
			insertID=results.getInt(1);
		}catch (SQLException e) {
			Logger.logMessage("Error getting last inserted ID");
		}
		return insertID;
	}
	
}
