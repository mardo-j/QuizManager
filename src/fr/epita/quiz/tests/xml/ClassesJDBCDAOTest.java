package fr.epita.quiz.tests.xml;

import fr.epita.quiz.datamodel.Classes;
import fr.epita.quiz.services.ClassesJDBCDAO;

public class ClassesJDBCDAOTest {

	public static void main(String[] args) {
		ClassesJDBCDAO jd = new ClassesJDBCDAO();
		jd.create(new Classes("TestClass"));
//		System.out.println(jd.checkClass("Tes1tClass"));
	}
}
