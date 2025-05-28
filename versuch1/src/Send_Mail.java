import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
			// Look up the JavaMail session.
			InitialContext ic = new InitialContext();
			String snName = "java:comp/env/mail/MyMailSession";
			Session session = (Session) ic.lookup(snName);
			// Override the JavaMail session properties if necessary.
			Properties props = session.getProperties();
			props.put("mail.from", "user2@mailserver.com");
			// Create a MimeMessage
			Message msg = new MimeMessage(session);
			String msgSubject = "Test Subject";
			msg.setSubject(msgSubject);
			msg.setSentDate(new Date());
			msg.setFrom();
			String msgRecipient = "";
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(msgRecipient, false));
			String msgTxt = "Test message body";
			msg.setText(msgTxt);
			// Send the message.
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
