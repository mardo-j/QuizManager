package fr.epita.quiz.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.datamodel.Classes;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.User;

public class ClassesJDBCDAO {

	public void create(Classes Classes) {
		String sqlCommand = "INSERT INTO CLASSES(classname) VALUES (?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(sqlCommand);) {
			
			insertStatement.setString(1, Classes.getClassname());
			
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void update(Classes Classes) {
		String updateQuery = "UPDATE CLASSES SET  WHERE ";

		
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

	public void delete(Classes Classes) {
		String deleteQuery = "DELETE CLASSES WHERE classname=?";
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Classes> search(Classes Classes) {
		List<Classes> resultList = new ArrayList<>();
		String selectQuery = "select id,classname from CLASSES WHERE classname like ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+Classes.getClassname()+"%");
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

	private void addToResultList(List<Classes> resultList, ResultSet results) throws SQLException {
		resultList.add(new Classes(
				results.getString("classname")
				));
	}
	public boolean checkClass(String classname2) {
		// TODO Auto-generated method stub
		boolean b=false;
		List<Question> resultList = new ArrayList<Question>();
		String selectQuery = "select id from CLASSES WHERE classname = ?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {


			preparedStatement.setString(1, classname2);
			
			ResultSet results = preparedStatement.executeQuery();
			if(results.next()) {
				if(results.getInt("id")>0)
					b=true;
			}
//				while (results.next()) {
//					addToResultList(resultList, results);
//				}
			results.close();
			if(b)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
