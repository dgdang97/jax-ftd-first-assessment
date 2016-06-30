package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="File")
public class File {

	private String filePath;

	public File() {
		super();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
