import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
        Socket socket = new Socket("localhost",9876);
        Scanner scr = new Scanner(System.in);
        int nPack = scr.nextInt();
        int sent = 1;
        BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        writer.writeBytes(nPack + "" + "\r\n");
		while(sent <= nPack){
            // send the integer
            System.out.println("Client sending " + sent);
            writer.writeBytes(sent + "" + "\r\n");

            // wait for response
            String response = socket_reader.readLine();
            if (Integer.parseInt(response) == sent) {
                sent += 1;
            }else{
                System.out.println("Client didnt get expected package number, expecting: " + sent);
            }

		}
        System.out.println("Sent all " + nPack + " packets");
        socket.close();
	}
}

