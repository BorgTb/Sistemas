package Frontend.Controladores;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import Frontend.Vistas.VistaCliente;

public class ControladorCliente implements Runnable, ActionListener{
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Thread hilo;
    private VistaCliente vistaCliente;

    public ControladorCliente(Socket socket) throws IOException{
        vistaCliente = new VistaCliente();
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());
        vistaCliente.addActionListener(this);
        this.hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String mensaje = entrada.readUTF();
                System.out.println(mensaje);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String mensaje = vistaCliente.getTextoIngresado();
        System.out.println(mensaje);
        try {
            salida.writeUTF(mensaje);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
