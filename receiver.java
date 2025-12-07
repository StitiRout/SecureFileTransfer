package SecureFileTransfer;


    import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class receiver {
        public static void main(String[] args) throws Exception {
        int port = 4433;
        System.out.println(" Receiver waiting on port " + port);
        ServerSocket server = new ServerSocket(port);

        while (true) {
            Socket socket = server.accept();
            System.out.println(" Connection received from " + socket.getInetAddress());

            FileOutputStream fos = new FileOutputStream("received_encrypted.enc");
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[4096];
            int count;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            socket.close();
            System.out.println(" File received and saved as received_encrypted.enc");

            // Optional: decrypt using OpenSSL command manually
            System.out.println(" You can now decrypt using OpenSSL.");
        }
    }
}


