import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

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
        // Text einlesen und in Worte zerlegen
        System.out.println("Please type in Message: ");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        scanner.close();
        String[] splitMessage = message.split(" ");

        // Socket erzeugen auf Port 9998 und Timeout auf eine Sekunde setzen
        DatagramSocket clientSocket = new DatagramSocket(9998);
        clientSocket.setSoTimeout(1000);

        InetAddress address = InetAddress.getByName("localhost");
        
        int seqNum = 0;
        int i = 0;
        int ackNum = 0;
        while (i < splitMessage.length) {
            byte[] payload = splitMessage[i].getBytes();
            ackNum = seqNum + payload.length;
            boolean ackFlag = false;
            while (ackFlag == false) {
                // create new packet 
                Packet packetOut = new Packet(seqNum, ackNum, ackFlag, payload);

                // serialize Packet for sending
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ObjectOutputStream o = new ObjectOutputStream(b);
                o.writeObject(packetOut);
                byte[] buf = b.toByteArray();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9997);
                clientSocket.send(packet);
                System.out.println("Sent packet: \"" + splitMessage[i] + "\" (seq=" + seqNum + ")");

                try {
                    // Auf ACK warten und erst dann Schleifenzähler inkrementieren
                    byte[] ackBuf = new byte[256];
                    DatagramPacket rcvPacketRaw = new DatagramPacket(ackBuf, ackBuf.length);
                    clientSocket.receive(rcvPacketRaw);

                    ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(rcvPacketRaw.getData()));
                    Packet packetIn = (Packet) is.readObject();

                    if (packetIn.isAckFlag() && packetIn.getAckNum() == ackNum) {
                        System.out.println("Received ACK for seq " + seqNum);
                        ackFlag = true;
                        i++;
                        seqNum = ackNum + 1;
                    } else {
                        System.out.println("Wrong ACK received, retrying...");
                    }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.out.println("Receive timed out, retrying...");
                    }
                }
        }
        // EOT senden
        Packet eotPacket = new Packet(seqNum, 0, false, "EOT".getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(eotPacket);
        byte[] buf = bos.toByteArray();

        DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, 9997);
        clientSocket.send(udpPacket);
        System.out.println("Sent EOT packet.");
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        clientSocket.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            clientSocket.disconnect();
        }

        System.exit(0);
    }
}