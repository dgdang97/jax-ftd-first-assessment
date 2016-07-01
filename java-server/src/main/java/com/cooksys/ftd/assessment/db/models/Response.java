package com.cooksys.ftd.assessment.db.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {

	String trueFalse;
	String hash;
	List<FileData> listFiles;

	public List<FileData> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<FileData> listFiles) {
		this.listFiles = listFiles;
	}

	public Response() {
		super();
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
