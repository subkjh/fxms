package fxms.bas.impl.dpo;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.co.log.Logger;

public class SendMail {

	private Logger logger = Logger.createLogger(Logger.logger.getFolder(), FxServiceImpl.serviceName + "-mail");

	private String from = "fxms@thingspire.com";

	public void sendmail(String to, String subject, String text, String filename) {

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", "localhost");
		// properties.put("mail.smtp.port", 465); // 네이버포트
		// properties.put("mail.transport.protocol", "smtp");
		// properties.put("mail.smtp.starttls.enable", "true");
		// properties.put("mail.smtp.auth", "true");
		// properties.setProperty("mail.user", "subkjh");
		// properties.setProperty("mail.password", "jonghoon@@K");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		Logger.logger.debug("from={},to={},subject={}", from, to, subject);
		logger.debug("from={},to={},subject={}", from, to, subject);
		logger.debug(text);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(text);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if (filename != null) {
				messageBodyPart = new MimeBodyPart();

				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));

				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);

			}

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			logger.debug("Sent message successfully....");
			Logger.logger.debug("Sent message successfully....");
		} catch (MessagingException mex) {
			Logger.logger.error(mex);
			logger.error(mex);
		}
	}

	private final String FROM = "subkjh@naver.com";
	private final String FROMNAME = "FxMS";
	private final String SMTP_USERNAME = "subkjh";
	private final String SMTP_PASSWORD = "jonghoon@@K";
	private final String CONFIGSET = "ConfigSet";
	private final String HOST = "smtp.naver.com";
	private final int PORT = 587;
	private final String BODY = "<h1>Alarm Message</h1>\n";

	public boolean sendmail(String to, String subject, String text) {

		try {
			logger.debug("from={},to={},subject={}", from, to, subject);
			send(to, subject, text);
			Logger.logger.debug("from={},to={},subject={},result=ok", from, to, subject);
			return true;
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}
	}

	private void send(String to, String subject, String text) throws Exception {

		Session session = null;

		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		session = Session.getDefaultInstance(props);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject, "utf-8");
		msg.setContent(BODY + text, "text/html; charset=utf-8");

		msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

		Transport transport = session.getTransport();
		try {

			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			transport.sendMessage(msg, msg.getAllRecipients());

		} catch (Exception ex) {
			throw ex;
		} finally {
			transport.close();
		}
	}

}
