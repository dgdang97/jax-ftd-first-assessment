package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {

	String trueFalse;
	
	public Response() {
		super();
	}
	
	public Response(String trueFalse) {
		super();
		this.trueFalse = trueFalse;
	}
	
	public String getTrueFalse() {
		return trueFalse;
	}

	public void setTrueFalse(String trueFalse) {
		this.trueFalse = trueFalse;
	}
	
	@Override
	public String toString() {
		return "Response [trueFalse=" + trueFalse + "]";
	}
}
