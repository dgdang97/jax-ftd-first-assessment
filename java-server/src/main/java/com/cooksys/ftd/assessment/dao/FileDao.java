package com.cooksys.ftd.assessment.dao;

import java.util.List;

public class FileDao extends AbstractDao {
	
	public List<String> listFiles () {
		String sql = "select filename, filepath";
		return null;
	}
	
	public boolean uploadFiles() {
		return false;
	}
	
	public void downloadFile () {
	}
}
