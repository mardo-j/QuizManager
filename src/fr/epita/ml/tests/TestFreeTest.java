package fr.epita.ml.tests;

import fr.epita.ml.services.GUIDAO;

public class TestFreeTest {

	public static void main(String[] args) {

		GUIDAO dao = new GUIDAO();
		dao.initialize();
		dao.freeTrialTab();
		
		
	}

}
