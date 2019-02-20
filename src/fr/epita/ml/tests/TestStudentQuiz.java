package fr.epita.ml.tests;

import fr.epita.ml.services.GUIDAO;
/**
 * Student login to quiz test class
 * @author Mardo.Lucas
 *
 */
public class TestStudentQuiz {
	public static void main(String[] args) {

		GUIDAO dao = new GUIDAO();
		dao.initialize();
		dao.studentLogin();
	}

}
