package fr.epita.ml.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Quiz;
import fr.epita.ml.datamodel.Student;
import fr.epita.ml.datamodel.User;

public class UserJDBCDAO {
	

	public void create(User user) {
		String sqlCommand = "INSERT INTO USER(name,admin,password) VALUES (?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, user.getName());
			insertStatement.setInt(2, user.getAdmin());
			insertStatement.setString(3, user.getPassword());
			
			insertStatement.execute();

		} catch (SQLException e) {
			Logger.logMessage("Error creating new user");
		}

	}

	public void update(User user) {
		String updateQuery = "UPDATE USER SET NAME=?,ADMIN=? WHERE ID=?";

		
		try (Connection connection = getConnection();
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
			updateStatement.setString(1, user.getName());
			updateStatement.setInt(2, user.getAdmin());
			updateStatement.setInt(3, user.getId());
			updateStatement.execute();
		}catch (SQLException e) {
			Logger.logMessage("Error creating new user");
		}
		
	}

	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	public void delete(User user) {
		String deleteQuery = "DELETE USER WHERE ID=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
			deleteStatement.setInt(1, user.getId());
		}catch (SQLException e) {
			Logger.logMessage("Error deleting user");
		}
	}

	public List<User> search(User user) {
		List<User> resultList = new ArrayList<>();
		String selectQuery = "select id,name,student_quiz.quiz_name from USER left join student_quiz on user.name=student_quiz.student_name WHERE user.name like ? order by user.id desc";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+user.getName()+"%");
			prepareSearchMethod(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error searching user");
		}
		return resultList;
	}

	private void prepareSearchMethod(List<User> resultList, PreparedStatement preparedStatement) {
		
		try (ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		} catch (SQLException e) {
			Logger.logMessage("Error prepareSearchMethod user");
		}
	}

	private void addToResultList(List<User> resultList, ResultSet results) throws SQLException {
		User student = new Student(results.getString("name"));
		student.setId(results.getInt("id"));
		student.setQuiz(new Quiz(results.getString("quiz_name")));
//		student.set
		resultList.add(student);
	}
	public void studentQuizTaken(User user,Quiz quiz) {
		String sqlCommand = "INSERT INTO STUDENT_QUIZ (STUDENT_NAME,QUIZ_NAME) VALUES (?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, user.getName());
			insertStatement.setString(2, quiz.getTitle());
			
			insertStatement.execute();

		} catch (SQLException e) {
			Logger.logMessage("Error creating new student quiz "+e.getMessage());
		}

	}
	public boolean validateStudent(User user) {
		boolean b=false;
		String selectQuery = "select id from USER WHERE name= ? and admin=0";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			preparedStatement.setString(1, user.getName());
			b = prepareAuthenticateAdmin(preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error validating user");
		}
		return b;
	}
	public boolean authenticateAdmin(String username,String password) {
		boolean b=false;
		String selectQuery = "select id from USER WHERE name= ? and password= ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			b=prepareAuthenticateAdmin(preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error authenticating user "+e.getMessage());
		}
		return b;
	}

	private boolean prepareAuthenticateAdmin(PreparedStatement preparedStatement){
		try(ResultSet results = preparedStatement.executeQuery();){
			if(results.next() && results.getInt("id")>0) {
					return true;
			}
		}catch(SQLException e) {
			Logger.logMessage("Error prepareAuthenticateAdmin user");
		}
			return false;
	}


}
