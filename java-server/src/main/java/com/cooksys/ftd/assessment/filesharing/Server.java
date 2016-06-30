package com.cooksys.ftd.assessment.filesharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.dao.FileDao;
import com.cooksys.ftd.assessment.dao.UserDao;

public class Server implements Runnable{
	
	private Logger log = LoggerFactory.getLogger(Server.class);
	private Executor executor;
	
	private FileDao fileDao;
	private UserDao userDao;
	
	private ServerSocket serverSocket;
	
	
	@Override
	public void run() {
		try {
			this.serverSocket = new ServerSocket(667);
			log.debug("Server started. Waiting for client.");
			while (true) {
				Socket socket = serverSocket.accept();
				ClientHandler handler = createClientHandler(socket);
				this.executor.execute(handler);
		}} catch (IOException e) {
			log.error("An error occured while obtaining a client", e);
		} finally {
			
		}
	}


	private ClientHandler createClientHandler(Socket socket) throws IOException {
		ClientHandler handler = new ClientHandler();
		handler.setFileDao(fileDao);
		handler.setUserDao(userDao);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		handler.setReader(reader);
		
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		handler.setWriter(writer);
		
		//handler.createJAXB();
		return handler;
	}


	public FileDao getFileDao() {
		return fileDao;
	}


	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}


	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}
