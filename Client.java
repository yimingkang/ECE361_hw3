import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
    public static int lastAck = 0;
    public static int nPack = 2;
    public static int pError = 0;

    public static void setAck(int n){
        System.out.println("Setting ack to " + n);
        Client.lastAck = n;
    }

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
        Socket socket = new Socket("localhost",9876);
        Scanner scr = new Scanner(System.in);
        System.out.print("Please enter the number of packets:");
        // nPack = scr.nextInt();
        System.out.print("Please enter the error rate (between 0 and 100):");
        // pError = scr.nextInt();
        int sent = 1;
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        // start listener
        Thread thread = new Thread(new Listener(socket));
        thread.start();

        // send nPack and pError to server
        writer.write(nPack);
        System.out.println("Client sending number of packets:" + nPack);
        writer.write(pError);
        System.out.println("Client sending error rate:" + pError);
		while(sent <= nPack){
            // send the integer
            System.out.println("Client sending " + sent);
            writer.write(sent);
            sent += 1;
		}
        System.out.println("Sent all " + nPack + " packets");
        while (lastAck < nPack) {
            // System.out.println("Ack is " + lastAck);
            try {
                Thread.sleep(500);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
	}
}

