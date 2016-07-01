package com.cooksys.ftd.assessment.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	public boolean uploadFiles(FileData fileData) {
		try {
			File file = new File(fileData.getFilePath());
			byte[] byteArray = new byte[(int) file.length()];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			
			int c;
			int b = 0;
			while ((c = in.read()) != -1) {
				byteArray[b] = (byte) c;
				b++;
			}

			Blob blobArray = conn.createBlob();
			blobArray.setBytes(1, byteArray);
			String sql = "insert into files (file_path, file_data, file_user) " + "values (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setString(1, fileData.getFilePath());
			stmt.setBlob(2, blobArray);
			stmt.setString(3, fileData.getUsername());

			stmt.executeUpdate();
			in.close();
			
			return true;
		} catch (SQLException | IOException e) {
			log.error("Upload failure", e);
			return false;
		}
	}

	public boolean downloadFile(FileData fileData) {
		try {

			Blob blobArray = conn.createBlob();
			String sql = "select file_data, file_user, file_path from files where file_id = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setInt(1, fileData.getFileID());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String fileUser = rs.getString("file_user");
				if (fileData.getUsername().equals(fileUser)) {
					blobArray = rs.getBlob("file_data");
					FileOutputStream out;
					if (fileData.getAltPath() != null) {
						out = new FileOutputStream(fileData.getAltPath());
					} else {
						String filePath = rs.getString("file_path");
						out = new FileOutputStream(filePath);
					}
					int blobLength = (int) blobArray.length();
					byte[] blobBytes = blobArray.getBytes(1, blobLength);
					out.write(blobBytes);
					out.close();
					return true;
				}
			}
		} catch (IOException | SQLException e) {
			log.error("Download error", e);
			return false;
		}
		return false;
	}
}
