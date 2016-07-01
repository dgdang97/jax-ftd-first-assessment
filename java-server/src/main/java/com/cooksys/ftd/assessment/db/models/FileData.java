package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="File")
public class FileData {

	private String fileName;
	private String filePath;
	private Integer fileID;
	
	public FileData() {
		super();
	}
	
	public FileData(Integer fileID, String fileName, String filePath) {
		super();
		this.fileID = fileID;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

}
