package com.cooksys.ftd.assessment.filesharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.dao.FileDao;
import com.cooksys.ftd.assessment.dao.UserDao;

public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/sakila";
	private static String username = "root";
	private static String password = "bondstone";
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName(driver);
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			log.info("Starting server...");
			Server server = new Server();
			
			server.setExecutor(executor);
			
			FileDao fileDao = new FileDao();
			fileDao.setconn(conn);
			server.setFileDao(fileDao);
			
			UserDao userDao = new UserDao();
			userDao.setconn(conn);
			server.setUserDao(userDao);

			Future<?> serverFuture = executor.submit(server);
			
			serverFuture.get();
			
		} catch (SQLException | InterruptedException | ExecutionException e) {
			log.error("A connection error occured", e);
		} finally {
			log.info("Shutting down server...");
			executor.shutdown();
		}
	}
}
