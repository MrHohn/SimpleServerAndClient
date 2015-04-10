import java.io.*;
import java.net.*;

/**
 * Created by Hon on 4/7/2015.
 */
public class Server {
    // private String clientResponse;

    public static void main(String[] args) {
        // if (args.length != 1) { // Test for correct num. of args
        //     System.out.println("ERROR server port number not given");
        //     System.exit(1);
        // }
        // int port_num = Integer.parseInt(args[0]);
        int port_num = 5500;
        ServerSocket rv_sock = null;
        try {
            rv_sock = new ServerSocket(port_num);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        while (true) { // run forever, waiting for clients to connect
            System.out.println("\nWaiting for client to connect...");
            try {
                Socket s_sock = rv_sock.accept();
                System.out.println("\nOne client is connected.");
//                BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(s_sock.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(s_sock.getOutputStream()), true);
                DataOutputStream sendFile = new DataOutputStream(s_sock.getOutputStream());
                String command;
                String clientResponse;

                while (true) {
                    System.out.println("\nWaiting for command...");
                    command = in.readLine();
                    System.out.println("Client's command: " + command);
                    if (command.equals("EXIT")) {
                        clientResponse = in.readLine();
                        if (clientResponse.equals("none")) {
                            System.out.println("Normal_Exit");
                        }
                        else {
                            System.out.println(clientResponse);
                        }
                            break;
                    }
                    else if (command.equals("GET")) {
//                        System.out.print("Server, enter your message: ");
//                        out.println(userEntry.readLine());
                        clientResponse = in.readLine();
                        File file = new File("files/ClientFile.txt");
                        try {
                            FileInputStream readFile = new FileInputStream(file);
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
                            System.out.println("ERROR: file not exist");
                            out.println("none");
                        }

//                        readFile.close();
                    }
                    else if (command.equals("BOUNCE")) {

                    }
                }
                sendFile.close();
                s_sock.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
