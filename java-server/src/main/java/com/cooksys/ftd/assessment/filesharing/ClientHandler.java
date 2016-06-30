package com.cooksys.ftd.assessment.filesharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.api.RegisterUser;
import com.cooksys.ftd.assessment.dao.FileDao;
import com.cooksys.ftd.assessment.dao.UserDao;
import com.cooksys.ftd.assessment.db.models.User;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private BufferedReader reader;
	private PrintWriter writer;
	private FileDao fileDao;
	private UserDao userDao;

	private JAXBContext jc;
	private Marshaller marshal;
	private Unmarshaller uMarshall;

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void createJAXB() {
		try {
			this.jc = JAXBContext.newInstance("com.cooksys.ftd.assessment.api");

			this.marshal = this.jc.createMarshaller();
			this.marshal.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
			this.marshal.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
			
			this.uMarshall = jc.createUnmarshaller();
			this.uMarshall.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
			this.uMarshall.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);

		} catch (JAXBException e) {
			log.error("An error occurred creating JAXB", e);
		}

	}

	@Override
	public void run() {
		log.info("Client connected. Awaiting command.");
		try {
			String command = this.reader.readLine();
			log.info("Received command");
			if (command.equals("RegisterUser")) {
			StringReader sr = new StringReader(this.reader.readLine());
			RegisterUser registerUser = (RegisterUser) uMarshall.unmarshal(sr);
			//String command = this.reader.readLine();
			User user = new User(registerUser.getUser(), registerUser.getPassword());
			Boolean loggedIn = this.userDao.registerUser(user);
			this.writer.print(loggedIn.toString());
			this.writer.flush();
			}
		} catch (IOException | JAXBException  e){
			log.error("An error occurred while obtaining a client command", e);
		}

	}

}
