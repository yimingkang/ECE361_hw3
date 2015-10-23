import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
    public static int lastAck = 0;
    public static int nPack = 5;
    public static int pError = 0;
    public static int wSize = 2;
    public static long timeOut = 5000L;
    public static long startTime = 0L;

    public static void setAck(int n){
        System.out.println("Setting ack to " + n);
        Client.lastAck = n;
    }

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
        Socket socket = new Socket("localhost",9876);
        Scanner scr = new Scanner(System.in);
        System.out.print("Please enter the number of packets:");
        nPack = scr.nextInt();
        System.out.print("Please enter the error rate (between 0 and 100):");
        pError = scr.nextInt();
        System.out.print("Please enter the window size:");
        wSize = scr.nextInt();
        System.out.print("Please enter timeout threshold (in milliseconds):");
        timeOut = scr.nextLong();
        long timer[] = new long[wSize];
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
        startTime = System.currentTimeMillis();

        // Send wSize packets to server
		while(sent <= wSize){
            System.out.println("Client sending packet: " + sent);
            writer.write(sent);
            sent += 1;
            timer[(sent-1) % wSize] = System.currentTimeMillis();
		}

        // Send nPack packets to server
		while(lastAck != nPack){
            if (sent - lastAck <= wSize && sent <= nPack){
                System.out.println("Client sending packet: " + sent);
                writer.write(sent);
                sent +=1;
                timer[(sent-1) % wSize] = System.currentTimeMillis();
            }
            if ((System.currentTimeMillis() - timer[lastAck % wSize]) > timeOut){
                System.out.println("Time out occurs! Resend packet: "+ (lastAck+1));
                sent = lastAck+1;
                timer[lastAck % wSize] = System.currentTimeMillis();
            }
		}

        System.out.println("Sent all " + nPack + " packets");

        // Wait for the other thread to receive all acknowledges.
        while (lastAck < nPack) {
            // System.out.println("Ack is " + lastAck);
            try {
                Thread.sleep(1);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Transferring took " + (endTime - startTime) + " milliseconds");
        socket.close();
	}
}

