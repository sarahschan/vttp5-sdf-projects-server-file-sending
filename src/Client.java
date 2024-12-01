import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    
    /*
     *  localhost:8000
     */

    public static void main(String[] args) throws UnknownHostException, IOException {
        
        String host = "";
        int port = 0;

        if (args.length != 1) {
            System.out.println("Usage: localhost:portNum");
            System.exit(-1);
        }

        String[] terms = args[0].split(":");
        host = terms[0].trim();
        try {
            port = Integer.parseInt(terms[1].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid portNum");
            System.exit(-1);
        }


        // connect to the server
        Socket clientConn = new Socket(host, port);
        System.out.println("Connection to server success");


        // open output stream to send request
        OutputStream os = clientConn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        // Send request to server
        dos.writeUTF("send_file");
        dos.flush();
        System.out.println("Sent request to server");


        // open input stream to recieve request
        InputStream is = clientConn.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        
        // Read file name and create file object
        String fileName = dis.readUTF();
        File csvFile = new File("clientFiles" + File.separator + fileName);

        // Receive file
        FileOutputStream fos = new FileOutputStream(csvFile);
        byte[] buff = new byte[1024];
        int bytesRead;
        while ((bytesRead = dis.read(buff)) > 0) {
            fos.write(buff, 0, bytesRead);
        }
        fos.flush();
        fos.close();
        is.close();
        System.out.println("File recieved");
    }
}
