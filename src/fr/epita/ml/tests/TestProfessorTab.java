package fr.epita.ml.tests;

import fr.epita.ml.services.GUIDAO;

public class TestProfessorTab {

	public static void main(String[] args) {

		System.setProperty("conf.location","app.properties");
		GUIDAO dao = new GUIDAO();
		dao.initialize();
		dao.initializeProfessorTab();
	}

}