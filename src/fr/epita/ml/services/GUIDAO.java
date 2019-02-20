package fr.epita.ml.services;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;

import fr.epita.ml.datamodel.Answer;
import fr.epita.ml.datamodel.Encryption;
import fr.epita.ml.datamodel.MCQAnswer;
import fr.epita.ml.datamodel.MCQChoice;
import fr.epita.ml.datamodel.MCQQuestion;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.Quiz;
import fr.epita.ml.datamodel.Student;
import fr.epita.ml.datamodel.User;

/**
 * 
 * @author Mardo.Lucas
 *
 */
public class GUIDAO implements ActionListener {
	private static final String SEARCH = "Search";
	JFrame f;
	JButton b1;
	JButton b2;
	JButton b3;
	Dimension dim;
	JPanel p1;
	JPanel p3;
	JSpinner spinner1;
	JToggleButton tb;
	Font f50;
	Font f30;
	String warning = "Warning";
	String selectImage = "Select an image";
	String info = "Info";
	String update = "Update";
	int questionCounter;
	int mCQquestionCounter;
	int rightAnswers;
	
	public GUIDAO() {
		f=new JFrame();
        dim = Toolkit.getDefaultToolkit().getScreenSize();
		p1 = new JPanel();
		f30=new Font("Copperplate Gothic Bold", 1,30);
		f50=new Font("Copperplate Gothic Bold", 1,50);
	}
	
	public void initialize(){
		
		f.setLayout(new BorderLayout());
		f.add(p1,BorderLayout.CENTER);
        loginTab();
		
        f.setUndecorated(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void loginTab() {
		p1.removeAll();
		p1.setLayout(new GridLayout(0,1));
		b1 = new JButton("Student Login");
		b2 = new JButton("Professor Login");
		b3 = new JButton("Free trial");
		b1.addActionListener(this);
		b3.addActionListener(this);
		b2.addActionListener(this);
		p1.add(b3);
		p1.add(b1);
		p1.add(b2);
		f.setSize(200,300);
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
		f.revalidate();
		f.repaint();
	}
	


	public void freeTrialTab() {
		p1.removeAll();
		JLabel l1 = new JLabel("Enter your name");
		JTextField t1 = new JTextField();
		JPanel p21 = new JPanel();
		p21.setLayout(new GridLayout(1,0));
		p21.add(l1);
		p21.add(t1);
		p1.add(p21,BorderLayout.NORTH);
		JPanel p22 = new JPanel();
		p22.setLayout(new GridLayout(1,0));
		JLabel l2 = new JLabel("Select Topics");
		p22.add(l2);
		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(0,1));
		JScrollPane scrollPane = new JScrollPane(p4,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		QuestionJDBCDAO qa = new QuestionJDBCDAO();
		List<String> topics = qa.getTopics();
		JPanel[] p = new JPanel[topics.size()];
		JCheckBox[] cb = new JCheckBox[topics.size()];
		JLabel[] lb = new JLabel[topics.size()];
		int i=0;
		
		for(String t : topics) {
			p[i]=new JPanel();
			p[i].setLayout(new BorderLayout());
			cb[i] = new JCheckBox();
			lb[i] = new JLabel(t.trim());
			p[i].add(cb[i],BorderLayout.WEST);
			p[i].add(lb[i],BorderLayout.CENTER);
			p4.add(p[i]);
			i++;
		}
		
		p22.add(scrollPane);
		p1.add(p22);
		JLabel l3 = new JLabel("Set difficulty");
		JPanel p23 = new JPanel();
		p23.setLayout(new GridLayout(1,0));
		p23.add(l3);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setSize(50,50);
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		p6.add(spinner,BorderLayout.CENTER);
		p23.add(p6);
		p1.add(p23);
		

		JPanel p7 = new JPanel();
		JButton b41 = new JButton("Start test");
		b41.addActionListener( e -> startFreeTestButton(t1, cb, lb, spinner));
		p7.add(b41);
		
		p1.add(p7);

		
		
		f.setSize(300,400);
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
		
		f.revalidate();
		f.repaint();
		
	}
	private void startFreeTestButton(JTextField t1,JCheckBox[] cb,JLabel[] lb,JSpinner spinner) {
		List<String> topics = new ArrayList<>();
		 int j=0;
		 for(int i=0;i<cb.length;i++) {
			 if(cb[i].isSelected()) {
				 topics.add(lb[i].getText());
				 j++;
			 }
		 }
		 if(j==0) {
			 JOptionPane.showMessageDialog(null, "Select at least one topic", warning, JOptionPane.ERROR_MESSAGE);
		 }else{
			 if(t1.getText().length()>0) {
				 
				 User student = new Student(t1.getText());
				 QuestionJDBCDAO dao = new QuestionJDBCDAO();
				 List<Question> questions = dao.getQuizQuestions(new Question("", topics,Integer.parseInt(((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().getText()), 0));
				 Quiz quiz = new Quiz("Free Test");
				 quiz.setQuestions(questions);
				 initializeFreeTest(student,quiz);
				 t1.setText("");
			 }
		 }
	}
	private void initializeFreeTest(User student, Quiz quiz) {
		UserJDBCDAO userdao = new UserJDBCDAO();
		List<Question> questions=quiz.getQuestions();
		boolean userfound=userdao.validateStudent(student);
		
		if(questions.isEmpty()){
			JOptionPane.showMessageDialog(null, "No questions for these criterias","ERROR", JOptionPane.ERROR_MESSAGE);
		}else if(userfound) {
			JOptionPane.showMessageDialog(null, "Name already exists", warning, JOptionPane.ERROR_MESSAGE);
		}else{
			p1.removeAll();
			userdao.create(student);
			userdao.studentQuizTaken(student, quiz);
			p1.setLayout(new BorderLayout());
			questionCounter=0;
			mCQquestionCounter=0;
			rightAnswers=0;
			JLabel lh = new JLabel(quiz.getTitle(),SwingConstants.CENTER);
			lh.setFont(f50);

			Map<Integer,JCheckBox> cb =new HashMap<>();
			JTextArea answerArea = new JTextArea(4,20);
			JPanel p2= new JPanel();
			JPanel p4 = new JPanel();
			JPanel p5 = new JPanel();
			JPanel temp = new JPanel();
			JLabel ll = new JLabel();
			JLabel imageLabel=new JLabel();
			JPanel p7 = new JPanel();
			List<JLabel> labels = new ArrayList<>();
			setLayoutsMethod(questions, p2, p4, p5, temp, p7,labels);
	
	

			JButton b11;
			
				b11=new JButton("Next");
			
				b11.addActionListener( e -> {
					if(questionCounter==questions.size()){
						checkPreviousQuestion(student,questions.get(questionCounter-1),cb,answerArea,labels);
						submitQuiz();
					}else if(questionCounter<questions.size()) {
	
						if((questionCounter==questions.size()-1))
							b11.setText("Submit");
						checkPreviousQuestion(student,questions.get(questionCounter-1),cb,answerArea,labels);
						changeQuestionPanelLabel(questions, cb, answerArea, ll, imageLabel, p7);


						questionCounter++;
					}
				} );
			
	
				if((questionCounter==questions.size()-1))
					b11.setText("Submit");
				changeQuestionPanelLabel(questions, cb, answerArea, ll, imageLabel, p7);
				questionCounter++;
				
				buildQuestionPanelMethod(p2, temp, ll, imageLabel, p7);
			addToMainPanelMethod(lh, p2, p5, b11);
			
			
			f.setSize(dim);
			f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
	        f.revalidate();
	        f.repaint();
		}
	}

	private void changeQuestionPanelLabel(List<Question> questions, Map<Integer, JCheckBox> cb, JTextArea answerArea,
			JLabel ll, JLabel imageLabel, JPanel p7) {
		if(questions.get(questionCounter).isMCQ()) {
			changeChoicesLabel((MCQQuestion)questions.get(questionCounter), cb, p7);
		}else {
			changeAnswerArea(p7,answerArea);
		}
		changeImageLabel(imageLabel, questions.get(questionCounter).getImage());
		changeQuestionLabel(ll, questions.get(questionCounter));
	}

	private void addToMainPanelMethod(JLabel lh, JPanel p2, JPanel p5, JButton b11) {
		b11.setFont(f30);
		p5.add(b11);
		p1.add(lh,BorderLayout.NORTH);
		p1.add(p2,BorderLayout.CENTER);
		p1.add(p5,BorderLayout.SOUTH);
	}

	private void buildQuestionPanelMethod(JPanel p2, JPanel temp, JLabel ll, JLabel imageLabel, JPanel p7) {
		ll.setFont(f30);
		temp.add(ll,BorderLayout.NORTH);
		temp.add(imageLabel,BorderLayout.WEST);
		temp.add(p7,BorderLayout.CENTER);
		
		
		p2.add(temp,BorderLayout.CENTER);
	}

	private void setLayoutsMethod(List<Question> questions, JPanel p2, JPanel p4, JPanel p5, JPanel temp, JPanel p7,List<JLabel> labels) {
		p2.setLayout(new BorderLayout());
		p4.setLayout(new GridLayout(1,10));
		temp.setLayout(new BorderLayout());
		p7.setLayout(new GridLayout(0,1));
		p5.setLayout(new GridLayout(1,3));
		p5.add(new JLabel());p5.add(new JLabel());
		
		for(int i=0;i<questions.size();i++) {
			JLabel l = new JLabel(i+1+"",SwingConstants.CENTER);
			if(questions.size()<15)
				l.setFont(f30);
			p4.add(l);
			labels.add(l);
		}
		

		p2.add(p4,BorderLayout.NORTH);
	}

	private void changeChoicesLabel(MCQQuestion question, Map<Integer, JCheckBox> cb, JPanel p7) {
		cb.clear();
		p7.removeAll();
		List<MCQChoice> choices = question.getChoices();
		
		for(MCQChoice choice : choices) {
			JPanel p8 = new JPanel();
			p8.setLayout(new BorderLayout());
			cb.put(choice.getId(),new JCheckBox());
			p8.add(cb.get(choice.getId()),BorderLayout.WEST);
			p8.add(new JLabel(choice.getChoice()),BorderLayout.CENTER);
			p7.add(p8);
		}
		p7.revalidate();
		p7.repaint();
	}

	private void submitQuiz() {
		JOptionPane.showMessageDialog(null, "Your quiz is submitted successfully","Thank you", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(null, "Your score is: "+rightAnswers*100/mCQquestionCounter+"% "
				+ "\n"+rightAnswers+"/"+mCQquestionCounter+" right MCQ answers"
						+ "\nThe other "+(questionCounter-mCQquestionCounter)+" will be corrected by the Professor","Thank you", JOptionPane.INFORMATION_MESSAGE);
		
		loginTab();
	}

	private void changeImageLabel(JLabel imageLabel, String image) {
			setImageIconOnLabel(imageLabel, image);
	}
	
	private void changeAnswerArea(JPanel p7,JTextArea answerArea){
		p7.removeAll();
		answerArea.setText("");
		p7.add(answerArea);
		p7.revalidate();
		p7.repaint();
	}
	private void changeQuestionLabel(JLabel ll, Question question) {
		
		ll.setText(question.getQuestion());
	
	}
	
	private void checkPreviousQuestion(User student, Question question, Map<Integer,JCheckBox>  cb, JTextArea answerarea,List<JLabel> labels) {
		if(question.isMCQ()) {
			createMCQAnswerMethod(student, question, cb, labels);
		}else {
			createAnswerMethod(student, question, answerarea);

			labels.get(questionCounter-1).setForeground(Color.BLUE);
		}
	}

	private void createMCQAnswerMethod(User student, Question question, Map<Integer, JCheckBox> cb, List<JLabel> labels) {
		List<MCQChoice> choicesId = new ArrayList<>();
		List<MCQChoice> choices = question.getChoices();
		MCQAnswer answer;
		getUserMCQAnswer(student,cb, choicesId, choices);
		mCQquestionCounter++;
		if(student.getChoicesValidity()) {
			labels.get(questionCounter-1).setForeground(Color.GREEN);
			rightAnswers++;
			JOptionPane.showMessageDialog(null, "The answer is right", "Right Answer",JOptionPane.INFORMATION_MESSAGE);
		}else {
			labels.get(questionCounter-1).setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "The answer is wrong", "Wrong Answer",JOptionPane.ERROR_MESSAGE);
		}
		answer=new MCQAnswer();
		answer.setChoices(choicesId);
		answer.setQuestionId(question.getId());
		answer.setStudent(student.getName());
		AnswerDAO dao = new AnswerDAO();
		dao.create(answer);
	}

	private void getUserMCQAnswer(User student,Map<Integer, JCheckBox> cb, List<MCQChoice> choicesId, List<MCQChoice> choices) {
		

		student.setValid(true);
		for (Map.Entry<Integer, JCheckBox> entry : cb.entrySet())
		{
			
			getUserChoiceValidity(student, choicesId, choices, entry);
		}
	}

	private void getUserChoiceValidity(User student, List<MCQChoice> choicesId, List<MCQChoice> choices,
			Map.Entry<Integer, JCheckBox> entry) {
		for(MCQChoice choice: choices) {
			if(choice.getId()==entry.getKey()) {
				if (entry.getValue().isSelected()) {
					choicesId.add(choice);
					if(!choice.isValid()) {
						student.setValid(false);
					}
				}else {
					if(choice.isValid()) {
						student.setValid(false);
					}
				}
			}
		}
	}

	private void createAnswerMethod(User student, Question question, JTextArea answerarea) {
		Answer answer = new Answer();
		answer.setText(answerarea.getText());
		answer.setQuestionId(question.getId());
		answer.setStudent(student.getName());
		AnswerDAO dao = new AnswerDAO();
		dao.create(answer);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==b1){
			 studentLogin();
		 }else if(e.getSource()==b2) {
			 professorLogin();
		 }else if(e.getSource()==b3) {
			 freeTrialTab();
		 }
	}

	private void searchingButton(JPanel p4,Question q) {
		
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		List<Question> questions=dao.search(q);
		
		p4.removeAll();
		p4.setLayout(new BoxLayout(p4,BoxLayout.Y_AXIS));		

		for(int i=0;i<questions.size();i++) {
			JPanel p11=new JPanel();
			p11.setLayout(new BorderLayout());
			p11.add(new JLabel(questions.get(i).toString()),BorderLayout.CENTER);
			JButton b15=new JButton(update);
			Question q1 = questions.get(i);
			b15.addActionListener( e -> questionPanel(q1) );
			p11.add(b15,BorderLayout.EAST);
			p11.setPreferredSize(new Dimension((int)dim.getWidth()/3*2,100));
			p4.add(p11);
		}
		f.revalidate();
		f.repaint();
	}

	private void searchQuestionButton(JPanel p3) {
		JPanel p11 = new JPanel();
		p11.setLayout(new BorderLayout());
		JLabel lh = new JLabel("Enter label to search");
		lh.setFont(f50);
		p11.add(lh,BorderLayout.NORTH);
		JPanel p5 = new JPanel();
		p5.setLayout(new BorderLayout());
		JLabel lq = new JLabel(SEARCH);
		lq.setFont(f30);
		JTextField tf = new JTextField();
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		p6.add(lq, BorderLayout.WEST);
		p6.add(tf, BorderLayout.CENTER);
		p5.add(p6,BorderLayout.NORTH);
		JPanel p4=new JPanel();		
		JScrollPane sp = new JScrollPane(p4,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		p5.add(sp,BorderLayout.CENTER);
		p11.add(p5,BorderLayout.CENTER);
		JButton b21 = new JButton(SEARCH);
		b21.addActionListener( e -> searchingButton(p4,new Question(tf.getText(),null,null,0)) );
		p6.add(b21,BorderLayout.EAST);
		p3.removeAll();
		p3.add(p11);
		f.revalidate();
		f.repaint();
	}

	private void studentLogin() {
		boolean b;
		String classname;
		 do {
			 do{
				 classname = JOptionPane.showInputDialog(null, "Enter quiz name:", "Quiz name", JOptionPane.INFORMATION_MESSAGE);
			 }while(classname!=null&&classname.equals(""));
			 if(classname!=null) {
				 QuizJDBCDAO classes = new QuizJDBCDAO();
				 
				 b = classes.checkQuiz(classname);
				 if(b) {
					 initializeStudentTab(classname);
				 }else {
					 JOptionPane.showMessageDialog(null, "Quiz not found", "Not found", JOptionPane.ERROR_MESSAGE);
				 }
			 }else b=true;
		 }while(!b);
	}
	private void initializeStudentTab(String classname) {
		String username="";
		do {
			username=JOptionPane.showInputDialog(null,"Enter your name:","Your Name",JOptionPane.INFORMATION_MESSAGE);
		}while(username!=null&&username.isEmpty());
		if(username!=null) {
			 QuestionJDBCDAO dao = new QuestionJDBCDAO();
			 Quiz quiz = new Quiz(classname);
			 quiz.setQuestions(dao.getClassQuestions(quiz));
			initializeFreeTest(new Student(username),quiz);
		}else {
			JOptionPane.showMessageDialog(null,"You must specify a name","ERROR",JOptionPane.ERROR_MESSAGE);
			loginTab();
		}
	}

	private void professorLogin() {
		String username="";
		 do{
			 username = JOptionPane.showInputDialog(null, "Enter username:", "Username", JOptionPane.INFORMATION_MESSAGE);
		 }while(username!=null&&username.isEmpty());
		 if(username!=null) {
			 String password="";
			 JPasswordField pf = new JPasswordField();
			 do{
				 password = getPasswordFromUser(pf);
			 }while(password!=null&&password.equals(""));
			 if(password!=null) {
				 UserJDBCDAO usr= new UserJDBCDAO();
				 if(usr.authenticateAdmin(username,password)) {
					 initializeProfessorTab();
				 }else {
					 JOptionPane.showMessageDialog(null, "Username and password don't match", "Incorrect username or password", JOptionPane.ERROR_MESSAGE);
				 }
			 }
		 }
	}

	private String getPasswordFromUser(JPasswordField pf) {
		String password;
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		 if (okCxl == JOptionPane.OK_OPTION) {
		    password = Encryption.md5(new String(pf.getPassword()));
		 }else {
			 password=null;
		 }
		return password;
	}

	public void initializeProfessorTab() {
		JPanel p32 = new JPanel();
		p1.removeAll();
		
		p1.setLayout(new BorderLayout());
		
		JPanel p2 = new JPanel();
		p2.setSize((int)dim.getWidth()/3,(int)dim.getHeight());
		p2.setLayout(new GridLayout(0,1));
		JButton b11=new JButton("Create Question");
		JButton b12=new JButton("Search Question");
		JButton b13=new JButton("Create Quiz");
		JButton b14=new JButton("View Quiz");
		JButton b15=new JButton("View Users");
		JButton b16=new JButton("Logout");
		
		b11.addActionListener( e -> questionPanel(new Question()));
		b12.addActionListener( e -> searchQuestionButton(p32));
		b13.addActionListener( e -> quizPanel(new Quiz("",new ArrayList<Question>())));
		b14.addActionListener( e -> viewQuizButton(p32));
		b15.addActionListener( e -> viewUserButton(p32));
		b16.addActionListener( e -> loginTab());
		p2.add(b11);
		p2.add(b12);
		p2.add(b13);
		p2.add(b14);
		p2.add(b15);
		p2.add(b16);
		p1.add(p2,BorderLayout.WEST);

		
		
		p32.setSize((int)dim.getWidth()/3*2,(int)dim.getHeight());
		p32.setLayout(new BorderLayout());
		p1.add(p32,BorderLayout.CENTER);
		f.setSize(dim);
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
        f.revalidate();
        f.repaint();
	}

	private void viewQuizButton(JPanel p3) {
		JPanel p11 = new JPanel();
		p11.setLayout(new BorderLayout());
		JLabel lh = new JLabel("View/Search a Quiz");
		lh.setFont(f50);
		p11.add(lh,BorderLayout.NORTH);
		JPanel p5 = new JPanel();
		p5.setLayout(new BorderLayout());
		JLabel lq = new JLabel("Search:");
		lq.setFont(f30);
		JTextField tf = new JTextField();
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		p6.add(lq, BorderLayout.WEST);
		p6.add(tf, BorderLayout.CENTER);
		p5.add(p6,BorderLayout.NORTH);
		JPanel p4=new JPanel();		
		JScrollPane sp = new JScrollPane(p4,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		p5.add(sp,BorderLayout.CENTER);
		p11.add(p5,BorderLayout.CENTER);
		JButton b21 = new JButton(SEARCH);
		b21.addActionListener( e -> searchingQuizButton(p4,new Quiz(tf.getText())));
		p6.add(b21,BorderLayout.EAST);
		p3.removeAll();
		p3.add(p11);
		
		f.revalidate();
        f.repaint();
	}

	private void viewUserButton(JPanel p3) {
		JPanel p11 = new JPanel();
		p11.setLayout(new BorderLayout());
		JLabel lh = new JLabel("View/Search a Student");
		lh.setFont(f50);
		p11.add(lh,BorderLayout.NORTH);
		JPanel p5 = new JPanel();
		p5.setLayout(new BorderLayout());
		JLabel lq = new JLabel("Search:");
		lq.setFont(f30);
		JTextField tf = new JTextField();
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		p6.add(lq, BorderLayout.WEST);
		p6.add(tf, BorderLayout.CENTER);
		p5.add(p6,BorderLayout.NORTH);
		JPanel p4=new JPanel();		
		JScrollPane sp = new JScrollPane(p4,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		p5.add(sp,BorderLayout.CENTER);
		p11.add(p5,BorderLayout.CENTER);
		JButton b21 = new JButton(SEARCH);
		b21.addActionListener( e -> searchingStudentButton(p4,new Student(tf.getText())));
		p6.add(b21,BorderLayout.EAST);
		p3.removeAll();
		p3.add(p11);
		
		f.revalidate();
        f.repaint();
	}

	private void searchingStudentButton(JPanel p4,User user) {
		UserJDBCDAO dao = new UserJDBCDAO();
		List<Student> users=dao.search(user);
		
		p4.removeAll();
		p4.setLayout(new BoxLayout(p4,BoxLayout.Y_AXIS));		

		for(int i=0;i<users.size();i++) {
			User u = users.get(i);
			JPanel p11=new JPanel();
			p11.setLayout(new BorderLayout());
			JPanel p12 = new JPanel();
			p12.setLayout(new GridLayout(0,1));
			JLabel l = new JLabel(u.toString());
			l.setFont(f30);
			p12.add(l);
			p11.add(p12,BorderLayout.CENTER);
			JButton b15=new JButton(update);
			p11.add(b15,BorderLayout.EAST);
			p11.setPreferredSize(new Dimension((int)dim.getWidth()/3*2,100));
			p4.add(p11);
		}
		f.revalidate();
		f.repaint();
	}


	private void searchingQuizButton(JPanel p4,Quiz q) {
		QuizJDBCDAO dao = new QuizJDBCDAO();
		List<Quiz> quiz=dao.search(q);
		
		p4.removeAll();
		p4.setLayout(new BoxLayout(p4,BoxLayout.Y_AXIS));		

		for(int i=0;i<quiz.size();i++) {
			Quiz q1 = quiz.get(i);
			JPanel p11=new JPanel();
			p11.setLayout(new BorderLayout());
			JPanel p12 = new JPanel();
			p12.setLayout(new GridLayout(0,1));
			JLabel l = new JLabel(q1.toString());
			l.setFont(f30);
			p12.add(l);
			for(Question qu : q1.getQuestions()) {
				p12.add(new JLabel(qu.toString()));
			}
			p11.add(p12,BorderLayout.CENTER);
			JPanel pbuttons = new JPanel();
			pbuttons.setLayout(new GridLayout(1,2));
			JButton b15=new JButton(update);
			b15.addActionListener( e -> quizPanel(q1) );
			JButton b16=new JButton("Export");
			PDF pdf = new PDF();
			b16.addActionListener( e -> pdf.exportQuizPDF(q1) );
			pbuttons.add(b16);
			pbuttons.add(b15);
			p11.add(pbuttons,BorderLayout.EAST);
			p11.setPreferredSize(new Dimension((int)dim.getWidth()/3*2,100));
			p4.add(p11);
		}
		f.revalidate();
		f.repaint();
	}

	private void quizPanel(Quiz q) {
		JFrame f1 = new JFrame();
		JPanel p21 = new JPanel();
		JPanel p22 = new JPanel();
		p21.setLayout(new BorderLayout());
		p22.setLayout(new BorderLayout());
		JLabel lh = new JLabel("Create a new Quiz");
		lh.setFont(f50);
		
		JPanel p23 = new JPanel();
		JLabel lq = new JLabel("Quiz name:");
		lq.setFont(f30);
		JTextField tf1 = new JTextField(32);
		tf1.setFont(f30);
		tf1.setText(q.getTitle());
		p23.add(lq);
		p23.add(tf1);
		p22.add(lh,BorderLayout.NORTH);
		p22.add(p23,BorderLayout.CENTER);
		p21.add(p22,BorderLayout.NORTH);
		JPanel p4 = new JPanel();
		p4.setLayout(new BorderLayout());
		JLabel lc = new JLabel("Select Questions from the list");
		lc.setFont(f30);
		JPanel p6 = new JPanel();
		p6.setLayout(new BoxLayout(p6,BoxLayout.Y_AXIS));
		JScrollPane sp = new JScrollPane(p6,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		p4.add(lc,BorderLayout.NORTH);
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		List<Question> questions = dao.search(new Question("",null,0,0));
		List<JCheckBox> cb1 = addChoices(q, p6, questions);
		
		p4.add(sp,BorderLayout.CENTER);
		JButton save = new JButton("Save");
		save.setFont(f30);
		save.addActionListener( e -> createUpdateMethod(q, f1, tf1, questions, cb1));
		p4.add(save,BorderLayout.SOUTH);
		p21.add(p4,BorderLayout.CENTER);
		f1.add(p21);

		f1.setSize(dim);
		f1.setLocation(dim.width/2-f1.getSize().width/2, dim.height/2-f1.getSize().height/2);
		f1.setLocationRelativeTo(null);
		f1.setVisible(true);
	}

	private void createUpdateMethod(Quiz q, JFrame f1, JTextField tf1, List<Question> questions, List<JCheckBox> cb1) {
		List<Question> selectedQuestions = new ArrayList<>();
		for(int i=0;i<questions.size();i++) {
			if(cb1.get(i).isSelected()) {
				selectedQuestions.add(questions.get(i));
			}
		}
		if(selectedQuestions.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Select at least one question to create/update a quiz",warning,JOptionPane.ERROR_MESSAGE);
		}else {
			QuizJDBCDAO qdao = new QuizJDBCDAO();
			Quiz q1 = new Quiz(tf1.getText(), selectedQuestions);
			if(q.getId()==0) {
				qdao.create(q1);
				JOptionPane.showMessageDialog(null, "Quiz created successfully","Info",JOptionPane.INFORMATION_MESSAGE);
			}else {
				q1.setId(q.getId());
				qdao.update(q1);
				if(q1.getTitle().isEmpty())
					JOptionPane.showMessageDialog(null, "Quiz deleted successfully","Info",JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Quiz updated successfully","Info",JOptionPane.INFORMATION_MESSAGE);
			}
			
			f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));
		}
	}

	private List<JCheckBox> addChoices(Quiz q, JPanel p6, List<Question> questions) {
		List<JCheckBox> cb1=new ArrayList<>();
		for(int i=0;i<questions.size();i++) {
			JPanel p0 = new JPanel();
			p0.setLayout(new BorderLayout());
			p0.setPreferredSize(new Dimension((int)dim.getWidth()/3*2,100));
			cb1.add(new JCheckBox());
			boolean b = false;
			int k=0;
			while(!b&&k<q.getQuestions().size()) {
				if(questions.get(i).getId()==q.getQuestions().get(k).getId()) {
					b=true;
					cb1.get(i).setSelected(b);
				}
				k++;
			}
			
			p0.add(cb1.get(i),BorderLayout.WEST);
			JLabel l = new JLabel(questions.get(i).getQuestion());
			l.setFont(f30);
			if(questions.get(i).isMCQ()) {
				JPanel pq = new JPanel();
				JPanel pqu=new JPanel();
				pqu.setLayout(new BorderLayout());
				
				pq.setLayout(new BoxLayout(pq,BoxLayout.Y_AXIS));
				int choicessize=questions.get(i).getChoices().size();
				for(int j=0;j<choicessize;j++) {
					pq.add(new JLabel(questions.get(i).getChoices().get(j).getChoice()));
				}

				pqu.add(l,BorderLayout.NORTH);
				pqu.add(pq,BorderLayout.CENTER);
				p0.add(pqu);
			}else {
				p0.add(l);
			}
			p6.add(p0);
		}
		return cb1;
	}

	private void questionPanel(Question question) {
		JFrame f1 = new JFrame();
		f1.setLayout(new BorderLayout());
		JPanel p4 = new JPanel();
		JLabel lh=new JLabel("Question Tab");
		JPanel p15 = new JPanel();			
		JTextArea ta1 = new JTextArea(4,2);
		JScrollPane sp = new JScrollPane(ta1,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JSpinner spinner11 = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
		JTextField tf1 = new JTextField(20);

		questionPanelDesign(question, p4, lh, p15, ta1, sp, spinner11);
		tf1.setFont(f30);
		p15.add(tf1);		
		f1.add(p4,BorderLayout.NORTH);
		f1.add(p15,BorderLayout.CENTER);
		
		JPanel p6 = new JPanel();
		p6.setLayout(new GridLayout(0,3));
		List<JLabel> l = new ArrayList<>();
		List<JTextField> choices = new ArrayList<>();
		JCheckBox[] cb2 = new JCheckBox[4];
		JToggleButton tb1 = questionPanelp6(question, p15, tf1, p6, l, choices, cb2);
		JButton b14;
		JButton file = new JButton(selectImage);
        JLabel ll = new JLabel();
		imageMethodQuestionPanel(question, file, ll);
		if(question.getId()>0) {
			b14 = new JButton(update);
			b14.addActionListener( e -> {
				addUserInputToQuestion(question, ta1, spinner11, tf1);
				if(tb1.isSelected()) {
					 updateDeleteMethodIfTrue(question, f1, choices, cb2, file);
				}else {
					updateDeleteMethodIfFalse(question, f1, file);
				}
			} );
		}else {
			b14 = new JButton("Create");
			b14.addActionListener( e -> {
				if(ta1.getText().length()>0) {
					addUserInputToQuestion(question, ta1, spinner11, tf1);
					if(tb1.isSelected()) {
						 createMCQQuestionMethod(question, f1, choices, cb2, file);
					}else {
						createQuestionMethod(question, f1, file);
					}
				}else {
					JOptionPane.showMessageDialog(null, "Enter your question label at least", warning, JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		p15.add(file);
		p15.add(b14);
        p15.add(ll);
		f1.setSize(700,1000);
		f1.setLocationRelativeTo(null);
		f1.setVisible(true);
	}

	private void imageMethodQuestionPanel(Question question, JButton file, JLabel ll) {
		file.addActionListener( e -> imageFileMethod(selectImage, file, ll) );
		setImageIconOnLabel(ll, question.getImage());
		file.setText(question.getImage()==null ? selectImage : question.getImage());
	}

	private void addUserInputToQuestion(Question question, JTextArea ta1, JSpinner spinner11, JTextField tf1) {
		question.setQuestion(ta1.getText());
		final List<String> result = Arrays.asList(tf1.getText().split(",")).stream()
		        .map(String::trim)
		        .collect(Collectors.toList());
		question.setTopics(result);
		question.setDifficulty(Integer.parseInt(((JSpinner.DefaultEditor) spinner11.getEditor()).getTextField().getText()));
	}

	private JToggleButton questionPanelp6(Question question, JPanel p15, JTextField tf1, JPanel p6, List<JLabel> l,
			List<JTextField> choices, JCheckBox[] cb2) {
		boolean b=question.isMCQ();
		for(int i=0;i<4;i++) {
			cb2[i]=new JCheckBox();
			l.add(new JLabel("Choice "+i));
			p6.add(l.get(i));
			if(b){
				choices.add(new JTextField(question.getChoices().get(i).getChoice()));
				cb2[i].setSelected(question.getChoices().get(i).isValid());
			}else {
				choices.add(new JTextField());
			}
			p6.add(choices.get(i));
			p6.add(cb2[i]);
		}
		JToggleButton tb1;
		if(question.isMCQ()) {		
			tf1.setText(question.getTopics().toString().substring(1,question.getTopics().toString().length()-1));
			tb1 = new JToggleButton("MCQ",true);
			p6.setVisible(true);
		}else {
			if(question.getQuestion().length()>0)			
				tf1.setText(question.getTopics().toString().substring(1,question.getTopics().toString().length()-1));
			tb1 = new JToggleButton("MCQ",false);
			p6.setVisible(false);
		}
		tb1.addActionListener( e -> p6.setVisible(tb1.isSelected()));
		tb1.setFont(f30);
		p15.add(tb1);
		p15.add(p6);
		return tb1;
	}

	private void questionPanelDesign(Question question, JPanel p4, JLabel lh, JPanel p15, JTextArea ta1,
			JScrollPane sp, JSpinner spinner11) {
		lh.setFont(f50);
		p4.add(lh);
		p15.setLayout(new GridLayout(0,2));
		p15.add(new JLabel("Question:"));
		ta1.setFont(f30);
		ta1.setText(question.getQuestion());
		ta1.setLineWrap(true);
		ta1.setWrapStyleWord(true);
		p15.add(sp);
		p15.add(new JLabel("Difficulty:"));
		spinner11.setFont(f30);
		((JSpinner.DefaultEditor) spinner11.getEditor()).getTextField().setEditable(false);
		((JSpinner.DefaultEditor) spinner11.getEditor()).getTextField().setText(question.getDifficulty().toString());
		p15.add(spinner11);
		p15.add(new JLabel("Topics:"));
		
	}

	private void updateDeleteMethodIfTrue(Question question, JFrame f1, List<JTextField> choices, JCheckBox[] cb2, JButton file) {
		List<MCQChoice> mcqchoices= new ArrayList<>();
		 boolean bool=false;
		 for(int i=0;i<4;i++) {
			 mcqchoices.add(new MCQChoice(choices.get(i).getText(),cb2[i].isSelected()));
			 if(cb2[i].isSelected())
				 bool=true;
		 }
		 if(bool) {
			 MCQQuestionDAO dao = new MCQQuestionDAO();
			 Question q=new MCQQuestion(
					 question.getQuestion(),
					 question.getTopics(),
					 question.getDifficulty(),
					 1,
					 mcqchoices);
			 q.setId(question.getId());
			 if(!file.getText().equals(selectImage)) {
				 q.setImage(file.getText());
			 }
			 dao.update((MCQQuestion)q);
			if(question.getQuestion().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Question deleted successfully", warning, JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Question updated successfully", info, JOptionPane.INFORMATION_MESSAGE);
			}
			f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));	
		 }else {
			 JOptionPane.showMessageDialog(null, "Select at least 1 choice as valid option",warning,JOptionPane.ERROR_MESSAGE);
		 }
	}

	private void createQuestionMethod(Question question, JFrame f1, JButton file) {
				 QuestionJDBCDAO dao = new QuestionJDBCDAO();
				 Question q = new Question(
						 question.getQuestion(),
						 question.getTopics(),
						 question.getDifficulty(),
						 0
						 );
				 if(!file.getText().equals(selectImage)) {
					 q.setImage(file.getText());
				 }
				 dao.create(q);
				JOptionPane.showMessageDialog(null, "Question created successfully", info, JOptionPane.INFORMATION_MESSAGE);
				 f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));
	}

	
	private void createMCQQuestionMethod(Question question, JFrame f1, List<JTextField> choices,
			JCheckBox[] cb2,  JButton file) {
		List<MCQChoice> mcqchoices= new ArrayList<>();
		 boolean bool = false;
		 for(int i=0;i<4;i++) {
			 mcqchoices.add(new MCQChoice(choices.get(i).getText(),cb2[i].isSelected()));
			 if(cb2[i].isSelected())
				 bool=true;
		 }
		 if(bool) {
			 MCQQuestionDAO dao = new MCQQuestionDAO();
			 Question q = new MCQQuestion(
					 question.getQuestion(),
					 question.getTopics(),
					 question.getDifficulty(),
					 1,
					 mcqchoices);
			 if(!file.getText().equals(selectImage)) {
				 q.setImage(file.getText());
			 }
			dao.create((MCQQuestion)q);
			JOptionPane.showMessageDialog(null, "MCQ Question created successfully", info, JOptionPane.INFORMATION_MESSAGE);
			f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));	
		 }else {
			JOptionPane.showMessageDialog(null, "Select at least 1 choice as valid option",warning,JOptionPane.ERROR_MESSAGE);
		 }
	}

	private void updateDeleteMethodIfFalse(Question question, JFrame f1, JButton file) {
		
			 QuestionJDBCDAO dao = new QuestionJDBCDAO();
			 Question q = new Question(
					 question.getQuestion(),
					 question.getTopics(),
					 question.getDifficulty(),
					 0
					 );
			 q.setId(question.getId());
			 if(!file.getText().equals(selectImage)) {
				 q.setImage(file.getText());
			 }
			dao.update(q);	
			
			if(question.getQuestion().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Question deleted successfully", warning, JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Question updated successfully", info, JOptionPane.INFORMATION_MESSAGE);
			}
			f1.dispatchEvent(new WindowEvent(f1, WindowEvent.WINDOW_CLOSING));	
		
		 
	}

	private void imageFileMethod(String selectImage, JButton file, JLabel ll) {
		if(!file.getText().equals(selectImage)) {
			file.setText(selectImage);
			ll.setIcon(null);
		}else {
			JFileChooser jfc = new JFileChooser();
		    jfc.showDialog(null,"Select the File");
		    jfc.setVisible(true);
		    File filename = jfc.getSelectedFile();
		    file.setText(filename.getAbsolutePath());
		    setImageIconOnLabel(ll, filename.getAbsolutePath());
		}
	}

	private void setImageIconOnLabel(JLabel ll, String filename) {
		ImageIcon imageIcon = new ImageIcon(filename);
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(350,160,  java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg);
		imageIcon.getImage().flush();
		ll.setIcon(imageIcon);
	}
	
	

}
