package com.cooksys.ftd.assessment.filesharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

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
import com.cooksys.ftd.assessment.db.models.FileData;
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

	private void output(String trueFalse, String hash) throws JAXBException {
		Response response = new Response();
		response.setTrueFalse(trueFalse);

		if (hash != null) {
			response.setHash(hash);
		}
		
		StringWriter sw = new StringWriter();
		this.marshal.marshal(response, sw);
		writer.print(sw.toString());
		writer.flush();
	}

	@Override
	public void run() {
		log.info("Client connected. Awaiting command.");
		try {
			String command = this.reader.readLine();
			log.info("Received command {}", command);
			StringReader sr = new StringReader(this.reader.readLine());

			if (command.equals("registerUser")) {
				User user = (User) uMarshall.unmarshal(sr);
				Boolean registerSuccess = this.userDao.registerUser(user);

				this.output(registerSuccess.toString(), null);

			} else if (command.equals("loginUser")) {
				User user = (User) uMarshall.unmarshal(sr);
				String isUser = this.userDao.loginUser(user);
				Boolean userCheck;

				if (isUser.equals("Username not in use")) {
					userCheck = false;
				} else {
					userCheck = true;
				}
				this.output(userCheck.toString(), isUser);

			} else if (command.equals("listFiles")) {

				User user = (User) uMarshall.unmarshal(sr);

				List<FileData> fileList = fileDao.listFiles(user);

				for (FileData fd : fileList) {
					StringWriter sw = new StringWriter();
					this.marshal.marshal(fd, sw);
					this.writer.print(sw.toString());
					this.writer.flush();
					this.reader.readLine();
				}
				
				StringWriter sw = new StringWriter();
				
				FileData fd = new FileData();
				fd.setFilePath("Stop");
				
				this.marshal.marshal(fd, sw);
				this.writer.print(sw.toString());
				this.writer.flush();
			
			} else if (command.equals("uploadFile")) {
				FileData upload = (FileData) uMarshall.unmarshal(sr);
				Boolean uploaded = fileDao.uploadFiles(upload);
				
				this.output(uploaded.toString(), null);
				
			} else if (command.equals("downloadFile")) {
				FileData download = (FileData) uMarshall.unmarshal(sr);
				Boolean downloaded = fileDao.downloadFile(download);
				
				this.output(downloaded.toString(), null);
			}
		} catch (IOException | JAXBException e) {
			log.error("An error occurred while obtaining a client command", e);
		}

	}


}
