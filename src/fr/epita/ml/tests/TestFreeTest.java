package fr.epita.quiz.tests.xml;

import fr.epita.quiz.services.GUIDAO;

public class TestFreeTest {

	public static void main(String[] args) {
		System.setProperty("conf.location","app.properties");
		GUIDAO dao = new GUIDAO();
		dao.initialize();
		dao.freeTrialTab();
		
		
	}

}
