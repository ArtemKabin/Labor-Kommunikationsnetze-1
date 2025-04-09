import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}

	public static void fetchMail() {
		try {

			// POP3 server information
			String host = "localhost";
			String mailStoreType = "pop3";
			String username = "labrat";
			String password = "kn1lab";
			int port = 25;

			// create properties field
			Properties properties = new Properties();

			// properties.put("mail.pop3.host", host);
			// properties.put("mail.pop3.port", port);
			properties.put("mail.store.protocol", "pop3");
			properties.put("mail.pop3.debug", "true");
			properties.put("mail.pop3.debug.quote", "true");
			// properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getInstance(properties,null);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3");

			store.connect(host, username, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("Sent Date: " + message.getSentDate());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

			// InitialContext ic = new InitialContext();
			// // String snName = "java:kn1lab/home/labrat/Maildir";
			// String snName = "/home/labrat/Maildir";
			// // String snName = "java:comp/env/Maildir/new";

			// Session session = (javax.mail.Session) ic.lookup(snName);

			// // Set properties and get the session
			// Properties properties = new Properties();
			// properties.put("mail.pop3.host", host);
			// properties.put("mail.pop3.port", "25");
			// properties.put("mail.store.protocol", "pop3");
			// // debug
			// properties.put("mail.pop3.debug", "true");
			// properties.put("mail.pop3.debug.quote", "true");
			// // properties.put("mail.pop3.starttls.enable", "false");

			// // Properties props = session.getProperties();
			// properties.put("mail.from", "labrat@locahost");

			// Store store = session.getStore();
			// store.connect(host, username, password);

			// Folder folder = store.getFolder("/home/labrat/Maildir/new");

			// // Session emailSession = Session.getDefaultInstance(properties);

			// // Connect to the store
			// // Store store = emailSession.getStore(mailStoreType);
			// // store.connect(host, username, password);

			// // Open the inbox folder
			// // Folder emailFolder = store.getFolder("/home/labrat/Maildir");
			// // emailFolder.open(Folder.READ_ONLY);

			// // Retrieve messages
			// Message[] messages = folder.getMessages();
			// System.out.println("Total messages: " + messages.length);

			// for (int i = 0; i < messages.length; i++) {
			// Message message = messages[i];
			// System.out.println("Email #" + (i + 1));
			// System.out.println("From: " + message.getFrom()[0]);
			// System.out.println("Subject: " + message.getSubject());
			// System.out.println("Sent Date: " + message.getSentDate());
			// System.out.println("Content: " + message.getContent().toString());
			// System.out.println("---------------------------------");
			// }

			// // Close the folder and store
			// folder.close(false);
			// store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
