package Backend;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Server {
    
    public Server() {
        try {
            ServerSocket socketServer = new ServerSocket(5000);
            Database data = Database.getInstance();
            
            while (true) {
                Socket user = socketServer.accept();
                System.out.println("Server is running on port 5000");
                Cliente cliente = new Cliente(user, this);
                Thread hilo = new Thread(cliente);
                hilo.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }


    public static void main(String[] args) {
        new Server();
        Database.getInstance();
    }

}
