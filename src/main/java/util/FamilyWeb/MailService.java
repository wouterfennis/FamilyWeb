package util.FamilyWeb;

import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import domain.FamilyWeb.User;

public class MailService {

	private User employee = null;
	private String message;
	private String subject;
	
	public MailService(User employee, String subject, String message) {
		this.employee = employee;
		this.subject = subject;
		this.message = message;
	}
	
	public boolean sendMail() {
		boolean sendStatus = false;
		Properties prop = new Properties();

		prop.put("mail.smtp.from", "info.familyweb@gmail.com");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.ssl.enable", true);

	    Session mailSession = Session.getDefaultInstance(prop, new Authenticator() {

	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication("info.familyweb@gmail.com", "familyweb12345");
	            }

	        });

		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress("info.familyweb@gmail.com", "FamilyWeb"));
			msg.setRecipients(Message.RecipientType.TO, this.employee.getEmail());
			msg.setSubject(subject);
			msg.setSentDate(Calendar.getInstance().getTime());
			// The method below is the "old" method to send a mail with just plain text
			//msg.setText("Beste " + employee.getForename() + ", \n\n" + message + "\n");
	        msg.setContent("<html><head><style>body{width: 100%;height: auto;background-color: white;font-family: 'Arial';}.wrapper{width: 90%;min-width: 365px;margin: 10px auto;padding: 10px;background: #49c0f0; /* Old browsers */background: -moz-linear-gradient(top,  #49c0f0 0%, #2cafe3 100%); /* FF3.6+ */background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#49c0f0), color-stop(100%,#2cafe3)); /* Chrome,Safari4+ */background: -webkit-linear-gradient(top,  #49c0f0 0%,#2cafe3 100%); /* Chrome10+,Safari5.1+ */background: -o-linear-gradient(top,  #49c0f0 0%,#2cafe3 100%); /* Opera 11.10+ */background: -ms-linear-gradient(top,  #49c0f0 0%,#2cafe3 100%); /* IE10+ */background: linear-gradient(to bottom,  #49c0f0 0%,#2cafe3 100%); /* W3C */filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#49c0f0', endColorstr='#2cafe3',GradientType=0 ); /* IE6-9 */-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;}.content{padding: 5px;background: rgb(249,252,247); /* Old browsers */background: -moz-linear-gradient(top,  rgba(249,252,247,1) 0%, rgba(245,249,240,1) 100%); /* FF3.6+ */background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(249,252,247,1)), color-stop(100%,rgba(245,249,240,1))); /* Chrome,Safari4+ */background: -webkit-linear-gradient(top,  rgba(249,252,247,1) 0%,rgba(245,249,240,1) 100%); /* Chrome10+,Safari5.1+ */background: -o-linear-gradient(top,  rgba(249,252,247,1) 0%,rgba(245,249,240,1) 100%); /* Opera 11.10+ */background: -ms-linear-gradient(top,  rgba(249,252,247,1) 0%,rgba(245,249,240,1) 100%); /* IE10+ */background: linear-gradient(to bottom,  rgba(249,252,247,1) 0%,rgba(245,249,240,1) 100%); /* W3C */filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f9fcf7', endColorstr='#f5f9f0',GradientType=0 ); /* IE6-9 */-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;}.bold_text{font-weight: bold;}.custom_table{margin-left: 20px;padding: 2px;background-color: #079FD9;}.row {padding: 5px;background-color: #71D1F5;}.data {width: 150px;}</style></head><body><div class='wrapper'><div class='header'><h1>FamilyWeb</h1></div><div class='content'> " + message + "</div></body></html>","text/html" );
			Transport.send(msg);
			sendStatus = true;
		} catch (Exception e) {
			Logger.getLogger("sp.lesson5").warning("send failed: " + e.getMessage());
		}
		return sendStatus;
	}
}
