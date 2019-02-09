package fr.epita.quiz.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Classes {
	int id;
	String classname;
	public Classes() {
		
	}
	public Classes(String classname) {
		this.classname=classname;
	}
	
	@Override
	public String toString() {
		return "Classes [id=" + id + ", classname=" + classname + "]";
	}
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public int getId() {
		return id;
	}
	
}
