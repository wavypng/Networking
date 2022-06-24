/*
 * SMTP upon TCP
 * 
 * Worked on by: Hung Pham, Rigoberto Hinojos
 * 2/21/2022
 */ 

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        //      System.out.println("Usage: java TCPClient <hostname>");
        //      return;
        // }
        
        // Get user input

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Enter IP address or hostname to connect (suggessted 147.153.10.87): ");
        
        Socket tcpSocket = null;
        String addressInput = sysIn.readLine();

        if(!"".equals(addressInput.trim())) {
            try {
                tcpSocket = new Socket(addressInput, 5260);
            } 
            catch (java.net.UnknownHostException e) {
                System.out.println("Don't know about host: " + addressInput);
                System.exit(1);
            }
            catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + addressInput);
                System.exit(1);
            }
        }

        PrintWriter socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

        System.out.println("Connecting to " + tcpSocket);

        String serverResponse = socketIn.readLine();
        System.out.println(serverResponse);

        String senderEmail, receiverEmail, subject;
        String helo, mail, rcpt, data, messageServerResponse = "";
        long timeSend, rtt;

        while (true) {
            System.out.print("Enter your email address: ");
            senderEmail = sysIn.readLine();
            System.out.print("Enter the reciever email address: ");
            receiverEmail = sysIn.readLine();
            System.out.print("Enter email subject: ");
            subject = sysIn.readLine();
            System.out.print("Enter the email content (Enter \".\" on a single line to end the message): \n");
            String emailInput, emailMessage = "";
            while (true) {
                System.out.print("> ");
                emailInput = sysIn.readLine();
                if (emailInput.equals(".")) {
                    emailMessage += emailInput + "\r\n";
                    break;
                } else {
                    emailMessage += emailInput + "\r\n";
                }
            }

            //send HELO
            timeSend = System.currentTimeMillis();
			socketOut.println("HELO msudenver.edu");
			helo = socketIn.readLine();
			rtt = System.currentTimeMillis() - timeSend;
            System.out.println(helo);
			System.out.println("RTT = " + rtt + "ms");

            //send MAIL FROM
            timeSend = System.currentTimeMillis();
			socketOut.println("MAIL FROM: " + senderEmail);
			mail = socketIn.readLine();
			rtt = System.currentTimeMillis() - timeSend;
            System.out.println(mail);
			System.out.println("RTT = " + rtt + "ms");

            //send RCPT TO:
            timeSend = System.currentTimeMillis();
			socketOut.println("RCPT TO: " + receiverEmail);
			rcpt = socketIn.readLine();
			rtt = System.currentTimeMillis() - timeSend;
            System.out.println(rcpt);
			System.out.println("RTT = " + rtt + "ms");

            //send DATA
            timeSend = System.currentTimeMillis();
			socketOut.println("DATA");
			data = socketIn.readLine();
			rtt = System.currentTimeMillis() - timeSend;
            System.out.println(data);
			System.out.println("RTT = " + rtt + "ms");

            //send email message
            timeSend = System.currentTimeMillis();
            String message = "To: " + receiverEmail + "\r\n" + "From: " + senderEmail + "\r\n" + "Subject: " + subject + "\r\n" + emailMessage;
			socketOut.println(message);
			messageServerResponse = socketIn.readLine();
			rtt = System.currentTimeMillis() - timeSend;
            System.out.println(messageServerResponse);
			System.out.println("RTT = " + rtt + "ms");

            //check for quit
            System.out.print("\nPress enter to continue or type quit to exit.\n> ");
            String option = sysIn.readLine();
            socketOut.println(option);
            if (option.equalsIgnoreCase("quit")) {
                String quitMessage = socketIn.readLine();
                System.out.println(quitMessage);
                socketOut.close();
                socketIn.close();
                sysIn.close();
                tcpSocket.close();
                break;
            }
        }
        // socketOut.close();
        // socketIn.close();
        // tcpSocket.close();
        // sysIn.close();
    }

}
