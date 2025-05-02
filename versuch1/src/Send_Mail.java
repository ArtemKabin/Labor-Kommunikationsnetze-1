import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext; 
import java.util.*;

public class Send_Mail {
	public static void main(String[] args) {
		sendMail();   
	}
	
	public static void sendMail() {
		try {
			Properties props = new Properties();
   			props.put("mail.smtp.host", "localhost");
			Session session = Session.getInstance(props, null);
			// Create a Message
			Message msg = new MimeMessage(session);
			msg.setSubject("Test");
			msg.setSentDate(new Date());
			msg.setFrom();
			msg.setRecipients(Message.RecipientType.TO, 
  			InternetAddress.parse("labrat@localhost", false));
			msg.setText("Test ");
			// Send the message
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
