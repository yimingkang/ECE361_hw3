import java.net.*;
import java.io.*;

class Listener implements Runnable {
    private Thread thisThread;
    private Socket socket;

    Listener (Socket s){
        socket = s; 
    }

    @Override 
    public void run() {
        try{ 
            BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                int ackNum = Integer.parseInt(socket_reader.readLine());
                System.out.println("Client is setting ack to " + ackNum);
                Client.setAck(ackNum);
            }
        }
        catch (Exception e) {e.getStackTrace();}
        
    }

    public void start(){
        if (thisThread == null){
            thisThread = new Thread(this, "ReaderThread");
            thisThread.start();
        }
    }

}
