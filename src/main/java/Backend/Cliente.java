package Backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente implements Runnable {
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Server servidor;
    private Socket socket;

    public Cliente(Socket socket, Server servidor) throws IOException {
        this.socket = socket;
        this.servidor = servidor;
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensaje = entrada.readUTF();
                System.out.println("Mensaje recibido: " + mensaje);
                servidor.enviarAMensajes(mensaje); // Enviar mensaje a todos los clientes
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
