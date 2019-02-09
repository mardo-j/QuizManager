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
import fr.epita.quiz.datamodel.User;

public class UserJDBCDAO {
	

	public void create(User User) {
		String sqlCommand = "INSERT INTO USER(name,admin,password) VALUES (?,?,?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, User.getName());
			insertStatement.setInt(2, User.getAdmin());
			insertStatement.setString(3, User.getPassword());
			
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void update(Question question) {
		String updateQuery = "UPDATE USER SET  WHERE ";

		
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
		String deleteQuery = "DELETE USER WHERE NAME=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> search(User User) {
		List<User> resultList = new ArrayList<>();
		String selectQuery = "select id,name,admin,password from USER WHERE name like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+User.getName()+"%");
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

	private void addToResultList(List<User> resultList, ResultSet results) throws SQLException {
		resultList.add(new User(
				results.getString("name"),
				Integer.parseInt(results.getString("admin")),
				results.getString("password")
				));
	}

	public boolean authenticateAdmin(String username,String password) {
		boolean b=false;
		List<Question> resultList = new ArrayList<Question>();
		String selectQuery = "select id from USER WHERE name= ? and password= ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {


			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			ResultSet results = preparedStatement.executeQuery();
			if(results.next()) {
				if(results.getInt("id")>0)
					b=true;
			}
//			while (results.next()) {
//				addToResultList(resultList, results);
//			}
			results.close();
			if(b)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
//	public List<Question> getQuestionsByTopic(List<String> topics) {
//		List<Question> resultList = new ArrayList<Question>();
//		String selectQuery = "select id,difficulty,label,topics from QUESTION WHERE topics like ?";
//		try (Connection connection = getConnection();
//				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
//				) {
//			preparedStatement.setString(1, topics.toString());
//		
//			ResultSet results = preparedStatement.executeQuery();
//			while (results.next()) {
//				addToResultList(resultList, results);
//			}
//			results.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultList;
//	}

	public static void main(String[] args) {
		UserJDBCDAO jd= new UserJDBCDAO();
		jd.search(new User("mardo", 1,"mardo"));
	}
}
