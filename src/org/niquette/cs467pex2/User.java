// Create a user for the SIGNOUT system

// Written by: Dr. Wayne Brown, Fall 2014
// Modified by:

public class User {
	public String firstName;
	public String lastName;
	public String password;
	
	//-----------------------------------------------------------------
	public User(String firstName, String lastName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	//-----------------------------------------------------------------
	@Override
	public boolean equals(Object other) {
		if (other instanceof User) {
			User o = (User) other;
			if (firstName.compareToIgnoreCase(o.firstName) == 0 &&
				lastName.compareToIgnoreCase(o.lastName) == 0 &&
				password.compareToIgnoreCase(o.password) == 0) {
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------------
	public String toString() {
		String text = lastName + ", " + firstName + " pw:" + password + "\n";
		return text;
	}
}
