package com.cooksys.ftd.assessment.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cooksys.ftd.assessment.db.models.User;

public class UserDao extends AbstractDao {

	public boolean registerUser(User user) {
		try {
			String sql = "select username from user_auth " + "where username like ?";

			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setString(1, user.getUser());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (user.getUser() == rs.getString("username")) {
					return false;
				}
			}
			sql = "insert into user_auth " + "values(?, ?)";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, user.getUser());
			stmt.setString(2, user.getPassword());

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			log.error("SQL error", e);
			return false;
		}
	}

	public String loginUser(User user) {
		try {
			String sql = "select username, password " + "from user_auth " + "where username like ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUser());

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String username = rs.getString("username");
				if (user.getUser().contentEquals(username)) {
					String password = rs.getString("password");
					log.info(password);
					return password;
				}
			}
		} catch (SQLException e) {
			log.error("SQL error", e);
			return "Username not in use";
		}
		return "Username not in use";
	}
}
