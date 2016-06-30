package com.cooksys.ftd.assessment.dao;

import java.sql.Connection;

public abstract class AbstractDao {
	
	private Connection conn;
	
	public void setconn(Connection conn) {
		this.conn = conn;
	}
}
