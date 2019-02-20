package fr.epita.ml.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.MCQChoice;
import fr.epita.ml.datamodel.MCQQuestion;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.Quiz;

public class QuestionJDBCDAO {
	String topics="topics";
	String label="label";
	String difficulty="difficulty";

	public void create(Question question) {
		String sqlCommand = "INSERT INTO QUESTION(label,difficulty,topics,image) VALUES (?,?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, question.getQuestion());
			insertStatement.setInt(2, question.getDifficulty());
			insertStatement.setString(3, question.getTopics().toString());
			insertStatement.setString(4, question.getImage());
			
			insertStatement.execute();

		} catch (SQLException e) {
			Logger.logMessage("Error creating question "+e.getMessage());
		}

	}

	public void update(Question question) {
		if(question.getQuestion().isEmpty()) {
			delete(question);
		}else {
			String updateQuery = "UPDATE QUESTION SET LABEL=?,DIFFICULTY=?,TOPICS=?,MCQ=?,IMAGE=? WHERE ID=?";
			try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
				updateStatement.setString(1, question.getQuestion());
				updateStatement.setInt(2, question.getDifficulty());
				updateStatement.setString(3, question.getTopics().toString());
				updateStatement.setInt(4, 0);
				updateStatement.setString(5, question.getImage());
				updateStatement.setInt(6, question.getId());
				updateStatement.execute();
				//If current Questions was MCQ, then it deletes its choices, if not, nothing will be deleted
				deleteChoicesFromDB(question, connection);
			}catch (SQLException e) {
				Logger.logMessage("Error updating question");
			}
		}
	}

	private void deleteChoicesFromDB(Question question, Connection connection) {
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM CHOICES WHERE RELATION=? AND ID>0");) {
		    statement.setInt(1, question.getId());
			statement.execute();
			
		}catch (SQLException e) {
			Logger.logMessage("Error deleting question choices");
		}
	}

	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	public void delete(Question question) {
		String deleteQuery = "DELETE QUESTION WHERE ID=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
			deleteStatement.setInt(1, question.getId());
			deleteStatement.execute();
		}catch (SQLException e) {
			Logger.logMessage("Error deleting question");
		}
	}

	public List<Question> search(Question question) {
		List<Question> resultList = new ArrayList<>();
		String selectQuery = "select id,difficulty,label,topics,mcq,image from QUESTION WHERE label like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+question.getQuestion()+"%");
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error searching question");
		}
		return resultList;
	}

	public List<Question> getClassQuestions(Quiz quiz) {
		List<Question> resultList = new ArrayList<>();
		
		String selectQuery = "select id,difficulty,label,topics,mcq,image from QUESTION where id in "
				+ "(select question_id from quiz_questions where quiz_id in (select id from quiz where title like ?))";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setString(1, quiz.getTitle());
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error in getting quiz questions");
		}
		return resultList;
	}
	public List<Question> getQuizQuestions(Question question) {
		List<Question> resultList = new ArrayList<>();
		StringBuilder temp = new StringBuilder();
		temp.append("( topics like ?");
		int topicsSize=question.getTopics().size();
		for(int i=1;i<topicsSize;i++) {
			temp.append(" or topics like ?");
		  }
		temp.append(")");
		String selectQuery = "select id,difficulty,label,topics,mcq,image from QUESTION WHERE difficulty=? and "+temp.toString();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setInt(1, question.getDifficulty());
			for(int i=0;i<topicsSize;i++) {
				preparedStatement.setString(i+2, "%"+question.getTopics().get(i)+"%");
			}
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error in getting quiz questions");
		}
		return resultList;
	}



	private List<MCQChoice> getChoices(int id) {
		String selectQuery = "select id,choice,valid from CHOICES WHERE relation=?";
		List<MCQChoice> choices = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setInt(1, id);
			prepareTryChoicesStatement(choices, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error selecting choices");
		}
		return choices;
	}

	
	
	
	
	public List<Question> getQuestionsByDifficulty(int difficulty) throws SQLException {
		List<Question> resultList = new ArrayList<>();
		
		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE difficulty=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setInt(1, difficulty);
			
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error getting questions by difficulty");
		}
		return resultList;
	}

	private void prepareTryStatement(List<Question> resultList, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing try statement query execution");
		}
	}
	private void prepareTryChoicesStatement(List<MCQChoice> choices, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				MCQChoice choice = new MCQChoice(results.getString("choice"), results.getInt("valid") == 1 );
				choice.setId(results.getInt("id"));				

				choices.add(choice);
			}
		} catch(SQLException e) {
			Logger.logMessage("Error in prepareTryChoicesStatement QuestionJDBCDAO");
		}
	}
	private void addToResultList(List<Question> resultList, ResultSet results) throws SQLException {
		if(results.getInt("mcq")==1) {
			List<MCQChoice> choices = getChoices(results.getInt("id"));
			Question q = new MCQQuestion(
				results.getString(label),
				topicsStringToArray(results.getString(topics)),
				Integer.parseInt(results.getString(difficulty)), 1, choices
				);
			q.setId(results.getInt("id"));
			q.setImage(results.getString("image"));
			resultList.add(q);
		}else {
			Question q = new Question(
				results.getString(label),
				topicsStringToArray(results.getString(topics)),
				Integer.parseInt(results.getString(difficulty)), 0
				);
			q.setId(results.getInt("id"));
			q.setImage(results.getString("image"));
			resultList.add(q);
		}
	}
	public List<Question> getQuestionsByTopic(List<String> topics) {
		List<Question> resultList = new ArrayList<>();
		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE topics like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setString(1, topics.toString());
		
			prepareTryStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error getting questions by topic");
		}
		return resultList;
	}
	public List<String> getTopics() {
		List<String> resultList = new ArrayList<>();
		String selectQuery = "select topics from QUESTION";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {		

			prepareTryTopicsStatement(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error selecting topics from questions "+e.getMessage());
		}
		return resultList;
	}
	private void prepareTryTopicsStatement(List<String> resultList, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addTopicsToResult(resultList, results);
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing try topics query execution");
		}
	}
	private void addTopicsToResult(List<String> resultList, ResultSet results) throws SQLException {
		List<String> a= topicsStringToArray(results.getString(topics));
		for(int i=0;i<a.size();i++) {
			int j=0;
			boolean b=false;
			while(!b&&j<resultList.size()){
				if(a.get(i).equals(resultList.get(j))) {
					b=true;
				}
				j++;
			}
			if(!b)
				resultList.add(a.get(i));
		}
	}
	private List<String> topicsStringToArray(String topics){
		final List<String> result = Arrays.asList(topics.replace("[","").replace("]","").split(","));
		return 	result.stream()
		        .filter(entity -> !entity.isEmpty())
				.map(String::trim)
		        .collect(Collectors.toList())
		        ;
		
	}
	
	
	public Question getQuestionById(Integer q) {
		Question resultList = new Question();
		String selectQuery = "select id,difficulty,label,topics,mcq,image from QUESTION WHERE ID=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setInt(1, q);
		
			resultList = addQuestionToResultList(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error getting questions by topic");
		}
		return resultList;
	}

	private Question addQuestionToResultList(Question resultList, PreparedStatement preparedStatement) {
		try(ResultSet results = preparedStatement.executeQuery();){
			if (results.next()) {
				if(results.getInt("mcq")==1) {
					resultList = new MCQQuestion(
							results.getString(label),
							topicsStringToArray(results.getString(topics)),
							results.getInt(difficulty),
							results.getInt("mcq"), getChoices(results.getInt("id")));
					resultList.setId(results.getInt("id"));
					resultList.setImage(results.getString("image"));
				}else {
					resultList = new Question(
							results.getString(label),
							topicsStringToArray(results.getString(topics)),
							results.getInt(difficulty),
							results.getInt("mcq"));
					resultList.setId(results.getInt("id"));
					resultList.setImage(results.getString("image"));
				}
			}
		}catch(SQLException e) {
			Logger.logMessage("Error preparing addQuestionToResultList");
		}
		return resultList;
	}
	
}
