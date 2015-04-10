import java.io.*;
import java.net.Socket;

/**
 * Created by Hon on 4/7/2015.
 */
public class Client {
    private static final int port_num = 5500;
    private static final String hostname = "localhost";

    public Client() {
        try {
            Socket c_sock = new Socket(hostname, port_num);
            BufferedReader in = new BufferedReader(new InputStreamReader(c_sock.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(c_sock.getOutputStream()),true);
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream receiveFile = new DataInputStream(c_sock.getInputStream());
            String serverResponse;

            while (true) {
                System.out.print("User, enter your message: ");
                String command = userEntry.readLine();
                if (command.equals("EXIT")) {
                    out.println(command);
                    break;
                }
                else if (command.equals("GET")) {
                    out.println(command);
                    // System.out.println("Server says: " + in.readLine());
                    serverResponse = in.readLine();

                    if (serverResponse.equals("ok")) {
                        System.out.println("start receiving file");
                        // get file name and length
                        String fileName = receiveFile.readUTF();
                        long fileLength = receiveFile.readLong();

                        System.out.println("\nFile name: <" + fileName + ">");
                        System.out.println("Size: " + fileLength + " B");
                        System.out.println("--- FILE BEGIN ---\n");
                        while (receiveFile.available() != 0)
                        {
                            System.out.print((char) receiveFile.readByte());
                        }
                        System.out.println("\n\n--- FILE END ---\n");

                    }
                    else {
                        System.out.println("file not exist");
                    }
                }
                else if (command.equals("BOUNCE")) {
                    out.println(command);
                }
            }

            receiveFile.close();
            c_sock.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
    }
}
