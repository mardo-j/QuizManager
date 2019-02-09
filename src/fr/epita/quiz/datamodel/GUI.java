package fr.epita.quiz.datamodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import fr.epita.quiz.services.ClassesJDBCDAO;
import fr.epita.quiz.services.GUIDAO;
import fr.epita.quiz.services.UserJDBCDAO;

public class GUI extends JFrame implements ActionListener,KeyListener{
    public JPanel p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p25,pdu;
    public JButton b1,b2;
    public JOptionPane op;
    public GUIDAO dao;
	public GUI() {
//		initialize();
		dao = new GUIDAO();
		op=new JOptionPane();
		p1 = new JPanel();
		b1 = new JButton("Student Login");
		b2 = new JButton("Professor Login");
		
	}

//	private void initialize() {
//		// TODO Auto-generated method stub
//		setLayout(new BorderLayout());
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//		add(p1,BorderLayout.CENTER);
//		p1.setLayout(new GridLayout(0,1));
//        b1.addActionListener(this);
//        b2.addActionListener(this);
//		p1.add(b1);
//		p1.add(b2);
//		p1.remove(b1);
//		setSize(200,300);
//        this.setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
//        setUndecorated(true);
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean b=false;
		 if(e.getSource()==b1){
			 System.out.println("Button 1 clicked");
			 String classname;
			 do {
				 do{
					 classname = op.showInputDialog(null, "Enter class name:", "Class name", JOptionPane.INFORMATION_MESSAGE);
				 }while(classname!=null&&classname.equals(""));
				 if(classname!=null) {
					 System.out.println(classname);
					 ClassesJDBCDAO classes = new ClassesJDBCDAO();
					 b = classes.checkClass(classname);
					 if(b) {
						 System.out.println("Classname found in the database");
						 //TODO initialize Class GUI
						 
					 }else {
						 System.out.println("Not found");
						 op.showMessageDialog(null, "Class not found", "Not found", JOptionPane.ERROR_MESSAGE);

					 }
				 }else b=true;
			 }while(!b);
			 
		 }else if(e.getSource()==b2) {

			 System.out.println("Button 2 clicked");
			 String username="";
			 do{
				 System.out.println(username);
				 username = op.showInputDialog(null, "Enter username:", "Username", JOptionPane.INFORMATION_MESSAGE);
			 }while(username!=null&&username.equals(""));
			 if(username!=null) {
				 String password="";
				 JPasswordField pf = new JPasswordField();
				 
				 do{
					 int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

					 if (okCxl == JOptionPane.OK_OPTION) {
					    password = new String(pf.getPassword());
					   System.err.println("You entered: " + password);
					   
					 }else {
						 password=null;
					 }
				 }while(password!=null&&password.equals(""));
				 if(password!=null) {
					 UserJDBCDAO usr= new UserJDBCDAO();
					 if(usr.authenticateAdmin(username,password)) {
						 System.out.println("Welcome");
						 //TODO initialize Admin/Professor GUI
					 }else {
						 op.showMessageDialog(null, "Username and password don't match", "Incorrect username or password", JOptionPane.ERROR_MESSAGE);
						 System.out.println(username);
						 System.out.println(password);
					 }
				 }
			 }
		 }
		
	}
	
}
