package com.cooksys.ftd.assessment.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cooksys.ftd.assessment.db.models.FileData;
import com.cooksys.ftd.assessment.db.models.User;

public class FileDao extends AbstractDao {

	public List<FileData> listFiles(User user) {
		try {
			List<FileData> fileList = new ArrayList<>();

			String sql = "select file_id, file_path from files where file_user like ?";

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

	public boolean uploadFiles(FileData fileData, String user) {
		try (FileInputStream in = new FileInputStream(fileData.getFilePath())) {
			byte[] byteArray = new byte[9001];
			int c;
			while ((c = in.read()) != -1) {
				byteArray[byteArray.length] = (byte) c;
			}
			Blob blobArray = conn.createBlob();
			blobArray.setBytes(1, byteArray, 0, byteArray.length);
			String sql = "insert into files (file_path, file_data, file_user) "
					+ "values (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, fileData.getFilePath());
			stmt.setBlob(2, blobArray);
			stmt.setString(3, user);
			
			stmt.executeUpdate();
			
			return true;
		} catch (IOException| SQLException e) {
			log.error("Upload failure", e);
			return false;
		}
	}

	public void downloadFile() {
	}
}
