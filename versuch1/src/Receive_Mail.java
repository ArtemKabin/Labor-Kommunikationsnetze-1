import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.InitialContext;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}
	
	public static void fetchMail() {
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "pop3");
			//props.put("mail.debug", "true");
			//props.put("mail.debug.quote", "true");

			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			//session.setDebug(true);
			
			store.connect("localhost", "labrat", "kn1lab");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				System.out.println("Message #" + (i + 1));
				System.out.println(messages[i].getFrom()[0]);
				System.out.println(messages[i].getSubject());
				System.out.println(messages[i].getSentDate());
				System.out.println(messages[i].getContent());
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
