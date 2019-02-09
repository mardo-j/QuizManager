package fr.epita.quiz.datamodel;


public class User {

	
	private int id;
	
	private String name; 
	private int admin;	 
	private String password;

	public User() {
		
	}
	public User(String name,int admin,String password) {
		this.name=name;
		this.admin=admin;
		this.password=password;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", admin=" + admin + ", password=" + password + "]";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
}
