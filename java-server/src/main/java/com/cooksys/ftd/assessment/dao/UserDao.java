package com.cooksys.ftd.assessment.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cooksys.ftd.assessment.db.models.User;

public class UserDao extends AbstractDao {
	
	
	public boolean registerUser(User user) {
		try {
			String sql = "select username from user_auth "
					+ "where username = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, user.getUser());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (user.getUser() == rs.getString("username")) {
					return false;
				}
			}
			
			sql = "insert into user_auth "
					+ "values(?, ?)";
				
			stmt.setString(1, user.getUser());
			stmt.setString(2, user.getPassword());
			
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			log.error("SQL error", e);
		}
		return false;
	}
	
	public boolean loginUser() {
		return false;
	}
}
