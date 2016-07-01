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

import com.cooksys.ftd.assessment.dao.FileDao;
import com.cooksys.ftd.assessment.dao.UserDao;
import com.cooksys.ftd.assessment.db.models.Response;
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
			this.jc = JAXBContext.newInstance("com.cooksys.ftd.assessment.db.models");

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
			if (command.equals("registerUser")) {
				StringReader sr = new StringReader(this.reader.readLine());
				User registerUser = (User) uMarshall.unmarshal(sr);
				log.info(registerUser.getPassword());
				Boolean registerSuccess = true;//this.userDao.registerUser(registerUser);
				Response response = new Response();
				response.setTrueFalse(registerSuccess.toString());
				StringWriter sw = new StringWriter();
				this.marshal.marshal(response, sw);
				log.info(sw.toString());
				writer.print(sw.toString());
				writer.flush();
				log.info("command executed");
			} else if (command.equals("loginUser")) {
				StringReader sr = new StringReader(this.reader.readLine());
				User loginUser = (User) uMarshall.unmarshal(sr);
				Boolean loggedIn = this.userDao.loginUser(loginUser);
				log.info("command executed");
				this.writer.print(loggedIn.toString());
				this.writer.flush();
			}
		} catch (IOException | JAXBException e) {
			log.error("An error occurred while obtaining a client command", e);
		}

	}

}
