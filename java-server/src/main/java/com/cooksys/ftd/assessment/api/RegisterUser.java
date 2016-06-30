package com.cooksys.ftd.assessment.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RegisterUser")
public class RegisterUser {
	
	String user;
	String password;

	public RegisterUser() {
		super();
	}

	public RegisterUser(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterUser [user=" + user + ", password=" + password + "]";
	}



}
