package com.cooksys.ftd.assessment.dao;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao {
	
	Logger log = LoggerFactory.getLogger(AbstractDao.class);
	protected Connection conn;
	
	public void setconn(Connection conn) {
		this.conn = conn;
	}
}
