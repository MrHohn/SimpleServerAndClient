import java.io.*;
import java.net.*;

/**
 * Created by Hon on 4/7/2015.
 */
public class Server {
    private static final int port_num = 5500;
    private ServerSocket rv_sock = null; // server socket
    private Socket s_sock;               //  socket
    private BufferedReader in;           // file input stream
    private PrintWriter out;             // message output stream
    private DataOutputStream sendFile;   // file output stream
    private String clientResponse;       // client message
    private int count = 0;               // count for client


    public Server() {
        try {
            rv_sock = new ServerSocket(port_num);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void initNew(Socket sock) {
        try {
            in = new BufferedReader(new InputStreamReader(s_sock.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s_sock.getOutputStream()), true);
            sendFile = new DataOutputStream(s_sock.getOutputStream());
            ++count; // count for client serviced
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void run() {

        String command;
        // run forever, waiting for clients to connect
        while (true) { 
            System.out.println("\nWaiting for client to connect...");
            try {
                s_sock = rv_sock.accept();
                initNew(s_sock); // initialize a new socket for a new client
                out.println(1); // send the very first response to client
                System.out.println("\n----------- Client <" + count + "> is connected ------------");     

                while (true) {
                    System.out.println("\nWaiting for command...");
                    command = in.readLine();
                    System.out.println("Client's command: " + command);
                    if (command.equals("EXIT")) {
                        exit();
                        break;
                    }
                    else if (command.equals("GET")) {
                        get();
                    }
                    else if (command.equals("BOUNCE")) {
                        bounce();
                    }
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void get() {

        try {
            clientResponse = in.readLine();
            System.out.println("File the user retrieving: <" + clientResponse + ">");
            File file = new File("files/" + clientResponse);
            FileInputStream readFile = new FileInputStream(file); // if file not exist, the program would throw out exception here

            // inform the client if file exist
            out.println("ok");

            // print out the content for test purpose
            System.out.println("\n--- Displaying file content ---\n");
            InputStreamReader readerTest = new InputStreamReader(new FileInputStream(file));
            BufferedReader buffer = new BufferedReader(readerTest);
            String line;
            while ((line = buffer.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("\n--- End ---\n");

            // start sending file
            System.out.println("Start sending file...");
            // file name and file length
            sendFile.writeUTF(file.getName());
            sendFile.flush();
            sendFile.writeLong(file.length());
            sendFile.flush();

            // begin transfer file
            byte[] sendBytes = new byte[1024];
            int length;
            while ((length = readFile.read(sendBytes, 0, sendBytes.length)) > 0) {
                sendFile.write(sendBytes, 0, length);
                sendFile.flush();
            }
            System.out.println("Finish sending file...\n");
        }
        catch (IOException ex) {
            System.out.println("ERROR: no such file");
            // if file not exist, inform the client
            out.println("none");
        }
    }


    private void bounce() {
        try {
            // bounce the command
            out.println(in.readLine());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void exit() {
        try {
            clientResponse = in.readLine();
            if (clientResponse.equals("none")) {
                // default exit
                System.out.println("Exit code: Normal_Exit");
            }
            else {
                // specific exit
                System.out.println("Exit code: " + clientResponse);
            }
            System.out.println("\n------------- Close the connection -------------");
            sendFile.close();
            s_sock.close();
        }
        catch (IOException ex) {
                ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}
