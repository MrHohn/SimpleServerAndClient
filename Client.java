import java.io.*;
import java.net.Socket;

/**
 * Created by Hon on 4/7/2015.
 */
public class Client {
    private static final int port_num = 5500;
    private static final String hostname = "localhost";
    private Socket c_sock;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userEntry;
    private DataInputStream receiveFile;
    private String serverResponse;


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
                System.out.print("User, enter the command: ");
                String command = userEntry.readLine();
                // judge whether the command is valid
                command = judgeCommand(command);

                // do corrosponding thinds according to the command
                if (command.equals("EXIT")) {
                    out.println(command);
                    break;
                }
                else if (command.equals("GET")) {
                    get(command);
                    
                }
                else if (command.equals("BOUNCE")) {
                    out.println(command);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void get(String command) {
        try {
            out.println(command);
            serverResponse = in.readLine();

            if (serverResponse.equals("ok")) {
                System.out.println("start receiving file...");
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
                System.out.println("File not exist.");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }     
    }


    private void bounce() {

    }


    private void exit() {
        try {
            receiveFile.close();
            c_sock.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String judgeCommand(String command) {

        return command;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
