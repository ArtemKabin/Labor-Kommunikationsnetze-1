import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.Scanner;

/**
 * Die "Klasse" Sender liest einen String von der Konsole und zerlegt ihn in einzelne Worte. Jedes Wort wird in ein
 * einzelnes {@link Packet} verpackt und an das Medium verschickt. Erst nach dem Erhalt eines entsprechenden
 * ACKs wird das nächste {@link Packet} verschickt. Erhält der Sender nach einem Timeout von einer Sekunde kein ACK,
 * überträgt er das {@link Packet} erneut.
 */
public class Sender {
    /**
     * Hauptmethode, erzeugt Instanz des {@link Sender} und führt {@link #send()} aus.
     * @param args Argumente, werden nicht verwendet.
     */
    public static void main(String[] args) {
        Sender sender = new Sender();
        try {
            sender.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt neuen Socket. Liest Text von Konsole ein und zerlegt diesen. Packt einzelne Worte in {@link Packet}
     * und schickt diese an Medium. Nutzt {@link SocketTimeoutException}, um eine Sekunde auf ACK zu
     * warten und das {@link Packet} ggf. nochmals zu versenden.
     * @throws IOException Wird geworfen falls Sockets nicht erzeugt werden können.
     */
    private void send() throws IOException {
   	//Text einlesen und in Worte zerlegen
    Scanner scanner = new Scanner(System.in);
    System.out.println("Pls enter your Message ");
    String message = scanner.nextLine() + " EOT";
    scanner.close();
    String[] words = message.split(" ");
   
    
    // Socket erzeugen auf Port 9998 und Timeout auf eine Sekunde setzen
    DatagramSocket clientSocket = new DatagramSocket(9998);
    clientSocket.setSoTimeout(1000);

    InetAddress address = InetAddress.getByName("localhost");
    int mediumPort = 9997;

    int seq = 0;
    int i = 0;
    int ackNum = 0;      
        while(i < words.length){
            boolean send = false;
            byte[] payload = words[i].getBytes();
            ackNum = seq + payload.length;
         while (!send) {
        	// Paket an Port 9997 senden
            Packet packetOut = new Packet(seq, ackNum, true, payload);

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(packetOut);
            byte[] buf = b.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, mediumPort);
            clientSocket.send(sendPacket);
        	
            try {
                // Auf ACK warten und erst dann Schleifenzähler inkrementieren
                byte[] ackbuff = new byte[256];
                DatagramPacket ackPacket = new DatagramPacket(ackbuff, ackbuff.length);
                clientSocket.receive(ackPacket);

                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(ackPacket.getData()));
                Packet ack = (Packet) is.readObject();

                if((ack.isAckFlag() && ack.getAckNum() == ackNum)){
                    send = true;
                    i++;
                    seq++;
                }
                else {
                    
                }



            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
            	System.out.println("Receive timed out, retrying...");
            }
        }
    }
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        clientSocket.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            clientSocket.disconnect();
        }
    
        System.exit(0);
    }
}