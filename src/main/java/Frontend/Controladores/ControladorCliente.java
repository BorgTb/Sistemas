package Frontend.Controladores;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

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
    public void actionPerformed(ActionEvent evento){
        try{
            String texto = vistaCliente.getTextoIngresado();
            String nombreGrupo = "Null";
            switch (evento.getActionCommand()) {
                case "Enviar a Todos":
                    System.out.println("Enviando mensaje a todos");
                    salida.writeUTF(texto);
                    break;
                case "Enviar Privado":
                    System.out.println("Enviando mensaje privado");
                    String destinatario = JOptionPane.showInputDialog("Ingrese el nombre del destinatario:");
                    break;
                case "Crear Grupo":
                    System.out.println("Creando grupo");
                    nombreGrupo = JOptionPane.showInputDialog("Ingrese el nombre del grupo:");
                    break;
                case "Unirse a Grupo":  
                    System.out.println("Unirse a grupo");
                    nombreGrupo = JOptionPane.showInputDialog("Ingrese el nombre del grupo:");
                    break;
                case "Enviar a Grupo":
                    System.out.println("Enviar a grupo");
                    nombreGrupo = JOptionPane.showInputDialog("Ingrese el nombre del grupo:");
                    break;
                default:
                    break;
            }
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
}
