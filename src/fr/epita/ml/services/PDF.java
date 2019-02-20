package fr.epita.ml.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import fr.epita.logger.Logger;
import fr.epita.ml.datamodel.Answer;
import fr.epita.ml.datamodel.MCQAnswer;
import fr.epita.ml.datamodel.MCQChoice;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.Quiz;
import fr.epita.ml.datamodel.User;


public class PDF {
	public void exportQuizPDF(Quiz quiz) {
		Document document = new Document();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(quiz.getTitle()+".pdf");
			PdfWriter writer = PdfWriter.getInstance(document,fileOutputStream);
			writer.close();
		} catch (FileNotFoundException|DocumentException e) {
			Logger.logMessage("Error adding getInstance of new PDF "+e.getMessage());
		}
		
		document.open();
		int counter=1;
		for(Question question : quiz.getQuestions()) {
			Paragraph title1 = new Paragraph("Question "+counter,FontFactory.getFont(FontFactory.HELVETICA,18, Font.BOLDITALIC, new CMYKColor(0, 0, 0,255)));
			Chapter chapter1 = new Chapter(title1, 1);
			chapter1.setNumberDepth(0);
			Paragraph title11 = new Paragraph(question.getQuestion(),FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,new CMYKColor(0, 0, 0,255)));
			Section section1 = chapter1.addSection(title11);
			if(question.getImage()!=null&&!question.getImage().isEmpty()) {
				Image image2;
				try {
					image2 = Image.getInstance(question.getImage());
					image2.scaleAbsolute(300f, 240f);
					section1.add(image2);
				} catch (BadElementException|IOException e) {
					Logger.logMessage("Error adding image2 "+e.getMessage());
				}
			}
			if(question.isMCQ()) {
				List l = new List(true, false, 10);
				for(MCQChoice choice : question.getChoices()) {
					l.add(new ListItem(choice.getChoice()));
				}
				section1.add(l);
			}
			try {
				document.add(chapter1);
			} catch (DocumentException e) {
				Logger.logMessage("Error adding chapter1 "+e.getMessage());
			}
			counter++;
		}
		document.close();
	}
	public void exportStudentPDF(User student) {
		Document document = new Document();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(student.getQuiz().getTitle()+"-"+student.getName()+".pdf");
			PdfWriter writer = PdfWriter.getInstance(document,fileOutputStream);
			writer.close();
		} catch (FileNotFoundException|DocumentException e) {
			Logger.logMessage("Error adding getInstance of new PDF "+e.getMessage());
		}
		
		document.open();
		for(int i=0;i<student.getQuestions().size();i++) {
			getQuestionAnswers(student, document, i);
		}
		
		document.close();
	}
	private void getQuestionAnswers(User student, Document document, int i) {
		Question question = student.getQuestions().get(i);
		Paragraph title1 = new Paragraph("Question "+(i+1),FontFactory.getFont(FontFactory.HELVETICA,18, Font.BOLDITALIC, new CMYKColor(0, 0, 0,255)));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);
		Paragraph title11 = new Paragraph(question.getQuestion(),FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,new CMYKColor(0, 0, 0,255)));
		Section section1 = chapter1.addSection(title11);
		if(question.getImage()!=null&&!question.getImage().isEmpty()) {
			Image image2;
			try {
				image2 = Image.getInstance(question.getImage());
				image2.scaleAbsolute(300f, 240f);
				section1.add(image2);
			} catch (BadElementException|IOException e) {
				Logger.logMessage("Error adding image2 "+e.getMessage());
			}
		}
		if(question.isMCQ()) {
			List l = new List(true, false, 10);
			for(MCQChoice choice : student.getAnswers().get(i).getChoices()) {
				ListItem ll = new ListItem(choice.getChoice());
				if(choice.isValid()) {
					ll.setFont(FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,new CMYKColor(255, 0, 255,0)));
				}else {
					ll.setFont(FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,new CMYKColor(0, 255, 255,0)));
				}
				l.add(ll);
			}
			section1.add(l);
			
		}else {
			Paragraph title12 = new Paragraph(student.getAnswers().get(i).getText(),FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,new CMYKColor(0, 0, 0,255)));
			section1.add(title12);
		}
		try {
			document.add(chapter1);
		} catch (DocumentException e) {
			Logger.logMessage("Error adding chapter1 "+e.getMessage());
		}
	}
}
