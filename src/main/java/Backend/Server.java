package Backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Cliente> clientes = new ArrayList<>();

    public Server() {
        try {
            ServerSocket socketServer = new ServerSocket(5000);
            System.out.println("Server is running on port 5000");

            while (true) {
                Socket user = socketServer.accept();
                Cliente cliente = new Cliente(user, this);
                clientes.add(cliente);
                Thread hilo = new Thread(cliente);
                hilo.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void enviarAMensajes(String mensaje) {
        for (Cliente cliente : clientes) {
            try {
                cliente.getSalida().writeUTF(mensaje);
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje: " + e);
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}