package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {

	String trueFalse;
	String hash;


	public Response() {
		super();
	}
	
	public Response(String trueFalse, String hash) {
		super();
		this.trueFalse = trueFalse;
		this.hash = hash;
	}
		
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTrueFalse() {
		return trueFalse;
	}

	public void setTrueFalse(String trueFalse) {
		this.trueFalse = trueFalse;
	}
	
	@Override
	public String toString() {
		return "Response [trueFalse=" + trueFalse + ", hash=" + hash + "]";
	}
}
