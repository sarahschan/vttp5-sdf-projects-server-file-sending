import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    /*
     *  args[0] = port, args[1] = csv file
     *  > Send csv file to client
     */

    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.out.println("Usage: [port] [filePath]");
            System.exit(-1);
        }
        
        
        int port = 0;
        try {
            port = Integer.parseInt(args[0].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
            System.exit(-1);
        }
        

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);


        // prepare file
        File file = new File(args[1]);
        String fileName = file.getName();


        while (true) {
            
            try {

                Socket serverConn = serverSocket.accept();
                System.out.println("Client connected");
            
                // Input stream to read client's request
                InputStream is = serverConn.getInputStream();
                DataInputStream dis = new DataInputStream(is);
            
                // Keep reading requests from the same client connection
                String request;
                while ((request = dis.readUTF()) != null) {

                    if (request.equals("send_file")) {
                        // FileInputStream to read the file
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
            
                        // Output stream to send the file
                        OutputStream os = serverConn.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(os);
            
                        // Send the client the file name
                        dos.writeUTF(fileName);
            
                        // Send the client the file
                        byte[] buff = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = bis.read(buff)) > 0) {
                            dos.write(buff, 0, bytesRead);
                        }
                        dos.flush();
            
                        System.out.println("File sent successfully");
            
                        // Close streams after sending the file
                        dos.close();
                        bis.close();
                        fis.close(); // Close the FileInputStream as well

                    } else {
                        // Optionally, handle invalid requests, if needed
                        System.out.println("Received invalid request: " + request);
                        // Simply do nothing and re-read the request from the same client
                    }
                }

            } catch (IOException e) {
                System.out.println("Client disconnected");
            }



            
        }
    }
}
