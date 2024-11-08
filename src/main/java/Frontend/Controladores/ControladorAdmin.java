package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Backend.Administrador;
import Frontend.Vistas.VistaAdmin;

// En ControladorAdmin.java
public class ControladorAdmin {
    private VistaAdmin vista;
    private Administrador administrador;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public ControladorAdmin() {
        vista = new VistaAdmin();
        administrador = new Administrador();
        conectarAlServidor();
        vista.addAgregarUsuarioListener(new AgregarUsuarioListener());
        vista.addEnviarUrgenteListener(new EnviarUrgenteListener());
        vista.addVerEstadisticasListener(new VerEstadisticasListener());
    }

    private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 12345);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Conectado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AgregarUsuarioListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = vista.getNombre();
            String rut = vista.getRut();
            String correo = vista.getCorreo();
            String clave = vista.getClave();
            String tipoUsuario = vista.getTipoUsuario();
            String area =  vista.getArea();

            if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty() || clave.isEmpty() || tipoUsuario.isEmpty()) {
                System.out.println("Todos los campos deben estar llenos.");
                return;
            }

            administrador.guardarClienteEnArchivo(nombre, rut,  correo,  clave,  tipoUsuario, area);
            vista.limpiarCampos();
        }
    }

    private class EnviarUrgenteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Obtener el mensaje urgente desde el campo de texto en la vista
            String mensajeUrgente = vista.getMensajeUrgente();  // Este método deberá ser creado en VistaAdmin.

            if (mensajeUrgente.isEmpty()) {
                System.out.println("El mensaje urgente no puede estar vacío.");
                return;
            }

            try {
                administrador.enviarMensajeUrgenteAChats(mensajeUrgente);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Mensaje de urgencia enviado a todos los chats.");
        }
    }

    private class VerEstadisticasListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para el botón Ver Estadísticas
            System.out.println("Ver Estadísticas presionado");
        }
    }
}
