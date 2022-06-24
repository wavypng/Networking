/*
 * SMTP upon TCP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * 
 * Worked on by: Hung Pham, Rigoberto Hinojos
 * 2/21/2022
 */ 

import java.net.*;
import java.io.*;

public class TCPMultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean listening = true;

        try {
            serverTCPSocket = new ServerSocket(5260);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5260.");
            System.exit(-1);
        }

        while (listening){
               new TCPMultiServerThread(serverTCPSocket.accept()).start();
        }

        serverTCPSocket.close();
    }
}
