import java.net.*;
import java.io.*;

public class Listener implements Runnable {
    private Socket socket;

    public Listener (Socket s){
        System.out.println("Listener object is created!");
        socket = s; 
    }

    @Override 
    public void run() {
        System.out.println("Listener object run() method is called!");
        try{ 
            // BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataInputStream readInputStream = new DataInputStream(socket.getInputStream());

            while(true){
                byte[] buffer = new byte[8];
                readInputStream.read(buffer);
                int ackNum = (int)(buffer[0]);
                System.out.println("Client is setting ack to " + ackNum);
                Client.setAck(ackNum);
                if (ackNum == Client.nPack)
                    break;
            }
        }
        catch (Exception e) {e.getStackTrace();}
        
    }
}
