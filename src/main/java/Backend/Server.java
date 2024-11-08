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
                    cliente.salida.writeUTF(mensaje);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void enviarListaConectados() {
        StringBuilder listaConectados = new StringBuilder("Conectados:");
        for (ClienteHandler cliente : clientes) {
            if (!cliente.nombre.equals("Administrador")) {
                listaConectados.append(cliente.nombre).append(",");
            }
            
        }
        String mensaje = listaConectados.toString();
        for (ClienteHandler cliente : clientes) {
            try {
                cliente.salida.writeUTF(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
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
                this.nombre = entrada.readUTF();
                enviarListaConectados(); // Enviar lista de conectados cuando un nuevo cliente se conecta
                String mensaje;
                while ((mensaje = entrada.readUTF()) != null) {
                    if (mensaje.startsWith("Privado:")) {
                        String nombreDestinatario = obtenerNombreDestinatario(mensaje);
                        mensaje = parsearMensajePrivado(mensaje, this.nombre);
                        enviarMensajePrivado(mensaje, nombreDestinatario);
                    }else {
                        enviarMensajeATodos(mensaje);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientes.remove(this);
                enviarListaConectados(); // Enviar lista de conectados cuando un cliente se desconecta
            }
        }

        private String obtenerNombreDestinatario(String mensaje) {
            String[] partes = mensaje.split(" ");
            return partes[0].split(":")[1];
        }

        private String parsearMensajePrivado(String mensaje, String emisor) {
            int index = mensaje.indexOf('[');
            if (index != -1) {
                mensaje = "PrivateMessage:" + " " + emisor + mensaje.substring(index);
            }
            return mensaje;
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}