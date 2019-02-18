package fr.epita.quiz.datamodel;

public class Professor extends User{

	private int admin;	 
	
	private String password;

	public Professor(String name,int admin,String password) {
		super(name);
		this.admin=admin;
		this.password=password;
	}

	
	@Override
	public String toString() {
		return "Professor [ " + super.toString() + "admin=" + admin + ", password=" + password + "]";
	}


	@Override
	public int getAdmin() {
		return admin;
	}
	@Override
	public String getPassword() {
		return password;
	}

	
}
