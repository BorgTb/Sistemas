package Backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ClienteHandler> clientes = new ArrayList<>();

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado en el puerto 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                ClienteHandler clienteHandler = new ClienteHandler(socket);
                clientes.add(clienteHandler);
                new Thread(clienteHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void enviarMensajeATodos(String mensaje) {
            for (ClienteHandler cliente : clientes) {
                try {
                    if (mensaje.contains("Medico")){
                        System.out.println("Mensaje recibido de un medico y debe enviarse solo a los medios: " + mensaje);
                    }
                    cliente.salida.writeUTF(mensaje);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
        private static class ClienteHandler implements Runnable {
            private Socket socket;
            private DataOutputStream salida;
    
            public ClienteHandler(Socket socket) {
                this.socket = socket;
            }
    
            @Override
            public void run() {
                try (DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                    salida = new DataOutputStream(socket.getOutputStream());
                    String mensaje;
                    while ((mensaje = entrada.readUTF()) != null) {
                        enviarMensajeATodos(mensaje);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}