package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="File")
public class FileData {

	private String altPath;
	private String filePath;
	private Integer fileID;
	
	public FileData() {
		super();
	}
	
	public FileData(Integer fileID, String altPath, String filePath) {
		super();
		this.fileID = fileID;
		this.altPath = altPath;
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return altPath;
	}

	public void setFileName(String altPath) {
		this.altPath = altPath;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

}
