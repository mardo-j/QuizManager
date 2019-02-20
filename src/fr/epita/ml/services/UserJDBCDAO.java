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
import fr.epita.ml.datamodel.Answer;
import fr.epita.ml.datamodel.MCQAnswer;
import fr.epita.ml.datamodel.MCQChoice;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.Quiz;
import fr.epita.ml.datamodel.Student;
import fr.epita.ml.datamodel.User;

public class UserJDBCDAO {
	
	/**
	 * UserJDBCDAO class with methods to query the database
	 * @author Mardo.Lucas
	 *
	 */
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

	public List<Student> search(User user) {
		List<Student> resultList = new ArrayList<>();
		String selectQuery = "select user.id,user.name,student_quiz.quiz_name from USER left join student_quiz on user.name=student_quiz.student_name WHERE user.name like ? and admin<>1 order by user.id desc";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, "%"+user.getName()+"%");
			prepareSearchMethod(resultList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error searching user "+e.getMessage());
		}
		return resultList;
	}

	private void prepareSearchMethod(List<Student> resultList, PreparedStatement preparedStatement) {
		
		try (ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				addToResultList(resultList, results);
			}
		} catch (SQLException e) {
			Logger.logMessage("Error prepareSearchMethod user");
		}
	}

	private void addToResultList(List<Student> resultList, ResultSet results) throws SQLException {
		User student = new Student(results.getString("name"));
		student.setId(results.getInt("id"));
		student.setQuiz(new Quiz(results.getString("quiz_name")));
		List<Answer> answersList = new ArrayList<>();
		student.setQuestions(getStudentQuestions(answersList,student));
		student.setAnswers(answersList);
		resultList.add((Student)student);
	}
	private List<Question> getStudentQuestions(List<Answer> answersList,User student) {
		List<Question> resultList = new ArrayList<>();
		String selectQuery = "SELECT QUESTION.LABEL, QUESTION.MCQ,QUESTION.IMAGE, ANSWER, VALID FROM STUDENT_ANSWERS LEFT JOIN QUESTION ON STUDENT_ANSWERS.QUESTION_ID = QUESTION.ID WHERE STUDENT_NAME=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {

			preparedStatement.setString(1, student.getName());
			prepareSearchQuestionMethod(resultList, answersList, preparedStatement);
		} catch (Exception e) {
			Logger.logMessage("Error getStudentQuestions user "+e.getMessage());
		}
		return resultList;
	}


		
	private void prepareSearchQuestionMethod(List<Question> resultList, List<Answer> answersList, PreparedStatement preparedStatement) {

		try (ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				Question q=new Question();
				q.setQuestion(results.getString("label"));
				q.setImage(results.getString("image"));
				q.setMCQ(results.getInt("mcq"));
				resultList.add(q);
				Answer answer;
				if(results.getInt("mcq")==1) {
					List<String> answersId=Arrays.asList(results.getString("answer").substring(1,results.getString("answer").length()-1).split(",")).stream().map(String :: trim).filter(e->Integer.parseInt(e)>0).collect(Collectors.toList());
					List<MCQChoice> resultList1 = new ArrayList<>();
					for(String s : answersId) {
						String selectQuery = "SELECT CHOICE, VALID FROM CHOICES WHERE ID=?";
						try (Connection connection = getConnection();
								PreparedStatement preparedStatement1 = connection.prepareStatement(selectQuery);
								) {

							preparedStatement1.setInt(1, Integer.parseInt(s));
							prepareSearchChoiceMethod(resultList1, preparedStatement1);
							
						} catch (Exception e) {
							Logger.logMessage("Error searching choices "+e.getMessage());
						}
					}
					answer = new MCQAnswer();
					answer.setChoices(resultList1);
					answer.setValid(results.getInt("valid")==1);
				}else {
					answer = new Answer();
					answer.setText(results.getString("answer"));
					
				}
				answersList.add(answer);
			}
		} catch (SQLException e) {
			Logger.logMessage("Error prepareSearchQuestionMethod user");
		}
	}
	private void prepareSearchChoiceMethod(List<MCQChoice> resultList, PreparedStatement preparedStatement) {

		try (ResultSet results = preparedStatement.executeQuery();){
			while (results.next()) {
				resultList.add(new MCQChoice(results.getString("choice"),(results.getInt("valid")==1)));
			}
		}catch (SQLException e) {
			Logger.logMessage("Error prepareSearchChoiceMethod user "+e.getMessage());
		}
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
