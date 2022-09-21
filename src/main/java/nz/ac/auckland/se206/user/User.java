package nz.ac.auckland.se206.user;

public class User {
	private String name;
	
	public User(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Name : " + this.name;
	}
}
