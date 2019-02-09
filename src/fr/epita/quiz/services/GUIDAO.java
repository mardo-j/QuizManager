package fr.epita.quiz.services;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.epita.quiz.datamodel.GUI;

public class GUIDAO {
	
	public void initialize(GUI gui) {
		// TODO Auto-generated method stub
		
		gui.op=new JOptionPane();
		gui.setLayout(new BorderLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        loginTab(gui);
		gui.setSize(200,300);
		gui.setLocation(dim.width/2-gui.getSize().width/2, dim.height/2-gui.getSize().height/2);
		gui.setUndecorated(true);
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void loginTab(GUI gui) {
		gui.p1 = new JPanel();
        gui.add(gui.p1,BorderLayout.CENTER);
		gui.p1.setLayout(new GridLayout(0,1));
		gui.b1 = new JButton("Student Login");
		gui.b2 = new JButton("Professor Login");
		gui.b1.addActionListener(gui);
		gui.b2.addActionListener(gui);
		gui.p1.add(gui.b1);
		gui.p1.add(gui.b2);
	}
	
	public void adminTab(GUI gui) {
		
	}
}
