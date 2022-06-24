/*
 * SMTP upon TCP Thread
 * A thread is started to handle every client TCP connection to this server
 * 
 * Worked on by: Hung Pham, Rigoberto Hinojos
 * 2/21/2022
 * 
 */ 

import java.net.*;
import java.util.*;
import java.io.*;

public class TCPMultiServerThread extends Thread {

    //creates Socket
    private Socket clientTCPSocket = null;
    //Creates date
    static Date date = new Date();
    static String empty = null;

    public TCPMultiServerThread(Socket socket) {
        super("TCPMultiServerThread");
        clientTCPSocket = socket;
        
    }

    public void run() {
        try {
            BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            String localIP = clientTCPSocket.getLocalAddress().getHostAddress();
            String clientIP = clientTCPSocket.getInetAddress().getHostAddress();
            cSocketOut.println("220 " + localIP);

            String helo, mail, rcpt, data;
            // StringTokenizer sTokenizer;
            String fromClient;

            while (true) {
                
                //get HELO command
                while (true) {
                    helo = cSocketIn.readLine();
                    if (helo.contains("HELO")) {
                        System.out.println(helo);
                        cSocketOut.println("250 " + localIP + " Hello " + clientIP);
                        break;
                    } else {
                        cSocketOut.println("503 5.5.2 Send hello first");
                    }
                }

                //get MAIL FROM command
                while (true) {
                    mail = cSocketIn.readLine();
                    if(mail.contains("MAIL FROM:")) {
						System.out.println(mail);
						cSocketOut.println("250 2.1.0 Sender OK");
						break;
					} else {
						cSocketOut.println("503 5.5.2 Need mail command");
					}
                }

                //get RCPT TO command
                while (true) {
                    rcpt = cSocketIn.readLine();
                    if(rcpt.contains("RCPT TO:")) {
						System.out.println(rcpt);
						cSocketOut.println("250 2.1.5 Recipient OK");
						break;
					} else {
						cSocketOut.println("503 5.5.2 Need rcpt command");
					}
                }

                //get DATA command
                while (true) {
                    data = cSocketIn.readLine();
                    if(data.contains("DATA")) {
						System.out.println(data);
						cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
						break;
					} else {
						cSocketOut.println("503 5.5.2 Need data command");
					}
                }

                //get & print message
                while ((fromClient = cSocketIn.readLine()) != null) {
                    if (fromClient.equals(".")) {
                        System.out.println(fromClient);
						cSocketOut.println("250 Message received and to be delivered");
						break;
					} else {
						System.out.println(fromClient);
					}
                }

                //check quit
                String option = cSocketIn.readLine();
                if (option.equalsIgnoreCase("quit")) {
                    System.out.println("221 " + localIP + " closing connection to " + clientIP);
                    cSocketOut.println("221 " + localIP + " closing connection");
                    cSocketOut.close();
                    cSocketIn.close();
                    clientTCPSocket.close();
                    break;
                }
            }
            // cSocketOut.close();
            // cSocketIn.close();
            // clientTCPSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
