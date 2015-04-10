import java.io.*;
import java.net.Socket;

/**
 * Created by Hon on 4/7/2015.
 */

public class Client {
    private static final int port_num = 5500;
    private static final String hostname = "localhost";
    private Socket c_sock;               // socket
    private BufferedReader in;           // input stream
    private PrintWriter out;             // output stream
    private BufferedReader userEntry;    // command line input
    private DataInputStream receiveFile; // receive date stream
    private String serverResponse;       // response from server
    private String subCommand;           // detail follows command


    public Client() {
        try {
            c_sock = new Socket(hostname, port_num);
            in = new BufferedReader(new InputStreamReader(c_sock.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(c_sock.getOutputStream()),true);
            userEntry = new BufferedReader(new InputStreamReader(System.in));
            receiveFile = new DataInputStream(c_sock.getInputStream());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }  
    }


    public void run() {
        try {
            while (true) {
                System.out.print("\nUser, enter the command: ");
                String command = userEntry.readLine().trim();
                // judge whether the command is valid
                command = judgeCommand(command);

                // do corresponding things according to the command
                if (command.equals("EXIT")) {
                    System.out.println("EXIT command.");
                    exit(command);
                    break;
                }
                else if (command.equals("GET")) {
                    System.out.println("GET command.");
                    get(command);
                    
                }
                else if (command.equals("BOUNCE")) {
                    System.out.println("BOUNCE command.");
                    bounce(command);
                }
                else {
                    System.out.println("ERROR: Invalid command.");
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private String judgeCommand(String command) {
        // if command is GET <***>    
        if (command.length() > 4 && command.substring(0, 4).equals("GET ")) {
                    System.out.println("judge GET <>");
                    subCommand = command.substring(4, command.length()).trim();
                    System.out.println(subCommand);
                    return "GET";
        }
        // if command is BOUNCE <***>
        else if (command.length() > 7 && command.substring(0, 7).equals("BOUNCE ")) {
            System.out.println("judge BOUNCE <>");
            subCommand = command.substring(7, command.length()).trim();
            System.out.println(subCommand);
            return "BOUNCE";
        }
        else if (command.length() >= 4 && command.substring(0, 4).equals("EXIT")) {
            System.out.println("judge EXIT");
            // if command is EXIT
            if (command.length() == 4) {
                subCommand = "none";
                return command;
            }
            // if command is EXIT <***>
            else if (command.length() > 5 && command.substring(3, 5).equals("T ")) {
                    System.out.println("judge EXIT <>");
                    subCommand = command.substring(5, command.length()).trim();
                    System.out.println(subCommand);
                    return "EXIT";
            }
        }

        // If command invalid, return invalid
        return "invalid";
    }


    private void get(String command) {
        try {
            out.println(command);
            out.println(subCommand);
            serverResponse = in.readLine();

            if (serverResponse.equals("ok")) {
                System.out.println("Start receiving file...");
                // get file name and length
                String fileName = receiveFile.readUTF();
                long fileLength = receiveFile.readLong();

                System.out.println("\nFile name: <" + fileName + ">");
                System.out.println("Size: " + fileLength + " B");
                System.out.println("--- FILE BEGIN ---\n");
                // print out file content
                while (receiveFile.available() != 0)
                {
                    System.out.print((char) receiveFile.readByte());
                }
                System.out.println("\n\n--- FILE END --- ");
            }
            else {
                System.out.println("ERROR: File not exist.");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }     
    }


    private void bounce(String command) {
        try {
            System.out.println("Still developing!");
            out.println(command);
            out.println(subCommand);
            System.out.println("Server says: " + in.readLine());       
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void exit(String command) {
        try {
            out.println(command);
            out.println(subCommand);
            receiveFile.close();
            c_sock.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
