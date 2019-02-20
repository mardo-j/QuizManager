package fr.epita.quiz.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import fr.epita.logger.Logger;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;

public class QuizJDBCDAO {

	public void create(Quiz quiz) {
		String sqlCommand = "INSERT INTO QUIZ(title) VALUES (?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, quiz.getTitle());
			
			insertStatement.execute();
			int insertID = 0;
			insertID = getLastInsertID(connection);
			int i=0;
			while(i<quiz.getQuestions().size()) {
				tryPrepareStatementCreateQuiz(quiz.getQuestions().get(i).getId(), connection, insertID);
				i++;
			}
		} catch (SQLException e) {
			Logger.logMessage("Error inserting quiz questions");
		}

	}

	private int getLastInsertID(Connection connection) {
		try (PreparedStatement statement = connection.prepareStatement("CALL SCOPE_IDENTITY();");) {
		    
			return lastInsertID(statement);
		}catch (SQLException e) {
			Logger.logMessage("Error getting last insert ID");
		}
		return 0;
	}

	private int lastInsertID(PreparedStatement statement) {
		try(ResultSet results = statement.executeQuery();){
			results.next();
			return results.getInt(1);
		} catch(SQLException e) {
			Logger.logMessage("Error getLastInsertID");
		}
		return 0;
	}

	private void tryPrepareStatementCreateQuiz(int questionId, Connection connection, int insertID) {
		String sqlCommand1="INSERT INTO QUIZ_QUESTIONS (QUIZ_ID,QUESTION_ID) VALUES (?,?)";
		try (PreparedStatement insertStatement1 = connection.prepareStatement(sqlCommand1);) {

			insertStatement1.setInt(1, insertID);
			insertStatement1.setInt(2, questionId);
			
			insertStatement1.execute();
			
		}catch(SQLException e) {
			Logger.logMessage("Error tryPrepareStatementCreateQuiz quiz");
		}
	}

	public void update(Quiz quiz) {
		if(quiz.getTitle().isEmpty()) {
			delete(quiz);
		}else {
			String updateQuery = "UPDATE QUIZ SET title=? WHERE ID=?";
			try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
				updateStatement.setString(1, quiz.getTitle());
				updateStatement.setInt(2, quiz.getId());
				updateStatement.execute();
				deleteQuizQuestions(quiz, connection);
				int i=0;
				while(i<quiz.getQuestions().size()) {
					tryPrepareStatementCreateQuiz(quiz.getQuestions().get(i).getId(), connection, quiz.getId());
					i++;
				}
			}catch (SQLException e) {
				Logger.logMessage("Error updating quiz");
			}
		}
	}

	private void deleteQuizQuestions(Quiz quiz, Connection connection) {
		try (PreparedStatement updateStatement = connection.prepareStatement("DELETE from QUIZ_QUESTIONS WHERE QUIZ_ID=?")){
				updateStatement.setInt(1, quiz.getId());
				updateStatement.execute();
		}catch(SQLException e) {
			Logger.logMessage("Error deleting quiz questions in update method");
		}
	}

	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	public void delete(Quiz quiz) {
		String deleteQuery = "DELETE QUIZ WHERE ID=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
			deleteStatement.setInt(1, quiz.getId());
			deleteStatement.execute();
			deleteQuizQuestions(quiz,connection);
		}catch (SQLException e) {
			Logger.logMessage("Error deleting quiz");
		}
	}

	public List<Quiz> search(Quiz quiz) {
		List<Quiz> resultList = new ArrayList<>();
		String selectQuery = "select id,title from QUIZ WHERE TITLE like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+quiz.getTitle()+"%");
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error searching quiz "+e.getMessage());
		}
		return resultList;
	}

	private void prepareTryStatement(List<Quiz> resultList, PreparedStatement preparedStatement) throws SQLException {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		} catch(SQLException e) {
			Logger.logMessage("Error prepareTryStatement quiz");
		}
	}

	private void addToResultList(List<Quiz> resultList, ResultSet results) throws SQLException {
		List<Question> questions = new ArrayList<>();
		String selectQuery = "select question_id from QUIZ_QUESTIONS WHERE QUIZ_ID=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setInt(1, results.getInt("id"));
			List<Integer> questionsId = new ArrayList<>();
			getQuestionsOfQuiz(preparedStatement, questionsId);
			
			QuestionJDBCDAO dao = new QuestionJDBCDAO();
			for(Integer q : questionsId) {
				questions.add(dao.getQuestionById(q));
			}
		} catch (Exception e) {
			Logger.logMessage("Error searching quiz");
		}
		Quiz quiz = new Quiz(
				results.getString("title"),
				questions
				);
		quiz.setId(results.getInt("id"));
		resultList.add(quiz);
	}

	private void getQuestionsOfQuiz(PreparedStatement preparedStatement, List<Integer> questionsId) {
		try(ResultSet results1 = preparedStatement.executeQuery();){
			while(results1.next()) {
				questionsId.add(results1.getInt("question_id"));
			}
		}catch(SQLException e) {
			Logger.logMessage("Error getting questions of quiz");
		}
	}
	public boolean checkQuiz(String title) {
		boolean b=false;
		String selectQuery = "select id from QUIZ WHERE title=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setString(1, title);
			b=prepareTryQuestionStatement(preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error selecting from quiz");
		}
		return b;
	}
	private boolean prepareTryQuestionStatement(PreparedStatement preparedStatement) {
		boolean b=false;
		try(ResultSet results = preparedStatement.executeQuery();){
			if(results.next() && results.getInt("id")>0) {
				b=true;
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing try statement query execution");
		}
		return b;
	}
	
	
}
