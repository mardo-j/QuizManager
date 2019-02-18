package fr.epita.quiz.launcher;

import fr.epita.quiz.services.GUIDAO;

public class Launcher {

	public static void main(String[] args) {

		System.setProperty("conf.location","app.properties");
		GUIDAO gui = new GUIDAO();
		gui.initialize();
	}

}
