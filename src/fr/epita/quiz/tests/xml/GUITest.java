package fr.epita.quiz.tests.xml;

import fr.epita.quiz.datamodel.GUI;

public class GUITest {
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.dao.initialize(gui);
	
	}
}
