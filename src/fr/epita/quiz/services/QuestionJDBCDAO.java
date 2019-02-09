package fr.epita.quiz.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.epita.quiz.datamodel.Question;

public class QuestionJDBCDAO {
	

	public void create(Question question) {
		String sqlCommand = "INSERT INTO QUESTION(label,difficulty,topics) VALUES (?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, question.getQuestion());
			insertStatement.setInt(2, question.getDifficulty());
			insertStatement.setString(3, question.getTopics().toString());
			
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void update(Question question) {
		String updateQuery = "UPDATE QUESTION SET  WHERE ";

		
		try (Connection connection = getConnection();
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
//		System.out.println(jdbcUrl);
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}

	public void delete(Question Question) {
		String deleteQuery = "DELETE QUESTION WHERE NAME=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Question> search(Question Question) {
		List<Question> resultList = new ArrayList<Question>();
		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE label like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+Question.getQuestion()+"%");
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				addToResultList(resultList, results);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private void addToResultList(List<Question> resultList, ResultSet results) throws SQLException {
		resultList.add(new Question(
				results.getString("label"),
				Arrays.asList(results.getString("topics").replace("[","").replace("]","").split(",")),
				Integer.parseInt(results.getString("difficulty"))
				));
	}

	public List<Question> getQuestionsByDifficulty(int difficulty) {
		List<Question> resultList = new ArrayList<Question>();
		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE difficulty=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {


			preparedStatement.setInt(1, difficulty);
			
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				addToResultList(resultList, results);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	public List<Question> getQuestionsByTopic(List<String> topics) {
		List<Question> resultList = new ArrayList<Question>();
		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE topics like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setString(1, topics.toString());
		
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				addToResultList(resultList, results);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public static void main(String[] args) {
		QuestionJDBCDAO jd= new QuestionJDBCDAO();
		jd.search(new Question("java", null,null));
	}
}
