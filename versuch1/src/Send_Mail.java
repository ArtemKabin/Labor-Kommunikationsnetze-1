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
			Properties props = new Properties();
    		props.put("mail.smtp.host", "localhost");
    		Session session = Session.getInstance(props, null);

			Message msg = new MimeMessage(session);
			msg.setSubject("First Mail");
			msg.setSentDate(new Date());
			msg.setFrom();
			msg.setRecipients(Message.RecipientType.TO, 
  				InternetAddress.parse("labrat@localhost", false));
			msg.setText("Hello this is a Test");

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
