import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext; 

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;

public class Send_Mail {
	public static void main(String[] args) {
		sendMail();   
	}
	
	public static void sendMail() {
		try {
			// Set up the mail server properties
			Properties properties = new Properties();
			properties.put("mail.smtp.host", "localhost");
			properties.put("mail.smtp.port", "25");

			// Create a session with no authentication
			Session session = Session.getInstance(properties);

			// Compose the email
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("labrat@localhost"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("labrat@localhost"));
			message.setSubject("Test Email");
			message.setText("This is a test email sent from localhost.");

			// Send the email
			Transport.send(message);

			System.out.println("Email sent successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
