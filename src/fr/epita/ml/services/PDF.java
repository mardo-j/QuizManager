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
import fr.epita.ml.datamodel.MCQChoice;
import fr.epita.ml.datamodel.Question;
import fr.epita.ml.datamodel.Quiz;


public class PDF {
	public void exportPDF(Quiz quiz) {
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
			System.out.println(question.getQuestion());
			Section section1 = chapter1.addSection(title11);
			if(question.getImage()!=null&&!question.getImage().isEmpty()) {
				System.out.println(question.getImage());
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
				System.out.println(question.getChoices());
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
}
