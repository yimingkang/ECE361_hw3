import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
    public static void main(String[] args) throws IOException{
        int port_num = 9876;
        int basePort = port_num;
        int quit = 0;

        System.out.println("Starting server at port " + port_num);
        ServerSocket serversSocket = new java.net.ServerSocket(port_num);

        Socket socket = serversSocket.accept();
        System.out.println("Server: Accepted connection"); 

        DataOutputStream writer = new DataOutputStream(socket.getOutputStream()); 
        BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        int nPack = Integer.parseInt(socket_reader.readLine());
        int lastAck = 0;
        while (lastAck < nPack){
            String response = socket_reader.readLine();
            if (Integer.parseInt(response) == lastAck + 1){
                lastAck += 1;
                System.out.println("Server got packet " + lastAck);
                writer.writeBytes(lastAck + "" + "\r\n");
            }else{
                System.out.println("Did not get expected packet: " + response);
            }


        }
        socket.close();
    }
}
