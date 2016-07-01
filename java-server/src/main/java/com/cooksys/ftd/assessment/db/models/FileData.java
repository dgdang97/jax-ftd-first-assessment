package com.cooksys.ftd.assessment.db.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileData")
public class FileData {

	private String username;
	private String altPath;
	private String filePath;
	private Integer fileID;

	public FileData() {
		super();
	}

	public FileData(String username, String filePath, String altPath, Integer fileID) {
		super();
		this.username = username;
		this.altPath = altPath;
		this.filePath = filePath;
		this.fileID = fileID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAltPath() {
		return altPath;
	}

	public void setAltPath(String altPath) {
		this.altPath = altPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

	@Override
	public String toString() {
		return "FileData [username=" + username + ", altPath=" + altPath + ", filePath=" + filePath + ", fileID="
				+ fileID + "]";
	}

}
