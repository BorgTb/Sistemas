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
                while ((mensaje = entrada.readUTF()) != null) {
                    
                    //System.err.println("Nuevo cliente conectado: " + nombre);
                    if (mensaje.startsWith("Privado:")) {
                        // Mensaje privado
                        nombre = obtenerNombre(mensaje);
                        int guion = mensaje.indexOf(" - ");
                        if (guion != -1) {
                            String nombreDestinatario = mensaje.substring(8, guion).trim();
                            System.out.println("Nombre destinatario: " + nombreDestinatario);
                            String mensajePrivado = mensaje.substring(guion + 3).trim();
                            System.out.println("Privado de " + nombre + " a " + nombreDestinatario + ": " + mensajePrivado);
                            enviarMensajePrivado("Privado de " + nombre + ": " + mensajePrivado, nombreDestinatario);
                        } else {
                            salida.writeUTF("Formato incorrecto para mensaje privado. Usa Privado:(nombre) - (rut):mensaje");
                        }
                    } else {
                        enviarMensajeATodos(mensaje);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static String obtenerNombre(String mensaje) {
            int start = mensaje.indexOf("]") + 2;
            int end = mensaje.indexOf(":", start);
            if (start != -1 && end != -1) {
            String nombreConRol = mensaje.substring(start, end).trim();
            int rolIndex = nombreConRol.indexOf(" (");
            if (rolIndex != -1) {
                return nombreConRol.substring(0, rolIndex).trim();
            }
            return nombreConRol;
            }
            return null;
        }
    }
    public static void main(String[] args) {
        new Server();
    }
}