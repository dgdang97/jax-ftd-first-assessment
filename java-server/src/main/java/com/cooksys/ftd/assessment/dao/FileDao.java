package com.cooksys.ftd.assessment.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cooksys.ftd.assessment.db.models.FileData;
import com.cooksys.ftd.assessment.db.models.User;

public class FileDao extends AbstractDao {
	
	public List<FileData> listFiles (User user) {
		try {
			List<FileData> fileList = new ArrayList<>();
			
			String sql = "select file_id, file_path from files where username like ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, user.getUser());
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Integer fileID = rs.getInt("file_id");
				String filePath = rs.getString("file_path");
				FileData file = new FileData();
				file.setFileID(fileID);
				file.setFilePath(filePath);
				fileList.add(file);
			}
			return fileList;
		} catch (SQLException e) {
			log.error("SQL Error", e);
		}
		return null;
	}
	
	public boolean uploadFiles() {
		return false;
	}
	
	public void downloadFile () {
	}
}
