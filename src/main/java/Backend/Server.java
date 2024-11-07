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
                cliente.salida.writeUTF(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void enviarMensajePrivado(String mensaje, String nombre) {
        for (ClienteHandler cliente : clientes) {
            if (cliente.nombre.equals(nombre)) {
                try {
                    System.out.println("Enviando mensaje privado a " + nombre);
                    cliente.salida.writeUTF(mensaje);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket socket;
        private DataOutputStream salida;
        public String nombre;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                salida = new DataOutputStream(socket.getOutputStream());
                String mensaje;

                this.nombre = entrada.readUTF();
                
                while ((mensaje = entrada.readUTF()) != null) {
                    // nombre es quien emiute el mensaje
                    // System.err.println("Nuevo cliente conectado: " + nombre);
                    if (mensaje.startsWith("Privado:")) {
                        // Mensaje privado
                        
                        String nombreDestinatario = obtenerNombreDestinatario(mensaje);
                        mensaje = parsearMensajePrivado(mensaje,this.nombre);;
                        System.out.println("Mensaje privado: " + mensaje);
                        System.out.println("Destinatario: " + nombreDestinatario);

                        enviarMensajePrivado(mensaje, nombreDestinatario);

                    } else {
                        enviarMensajeATodos(mensaje);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String obtenerNombreDestinatario(String mensaje) {
            String[] partes = mensaje.split(" ");
            return partes[2].split(":")[0];
        }

        private String parsearMensajePrivado(String mensaje, String emisor) {
            int index = mensaje.indexOf('[');
            if (index != -1) {
                mensaje = "PrivateMessage:"+" "+ emisor + mensaje.substring(index);
            }
            return mensaje;
        }
    }

    public static void main(String[] args) {
        // Enviando mensaje privado a asd - 1: [11:20:13] 2 (Médico): saludame asd
        new Server();
    }
}