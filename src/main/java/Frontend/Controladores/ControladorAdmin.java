package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;

import Backend.Administrador;
import Frontend.Vistas.VistaAdmin;

public class ControladorAdmin {
    private VistaAdmin vista;
    private Administrador administrador;
    private DefaultListModel<String> modeloListaUsuarios;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public ControladorAdmin() {
        this.vista = VistaAdmin.getInstance(this); // Pass 'this' to the VistaAdmin constructor
        administrador = new Administrador();
        modeloListaUsuarios = new DefaultListModel<>(); // Inicializar modeloListaUsuarios
        vista.setModeloListaUsuarios(modeloListaUsuarios); // Establecer el modelo en la vista
        conectarAlServidor();
        vista.addAgregarUsuarioListener(new AgregarUsuarioListener());
        vista.addEnviarUrgenteListener(new EnviarUrgenteListener());
        vista.addVerEstadisticasListener(new VerEstadisticasListener());
        cargarUsuarios(); // Llamar al método cargarUsuarios
    }

    private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 12345);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            salida.writeUTF("Administrador");
            System.out.println("Conectado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarUsuarios() {
        cargarUsuariosDesdeArchivo("Medicos.txt");
        cargarUsuariosDesdeArchivo("Administrativos.txt");
    }

    private void cargarUsuariosDesdeArchivo(String fileName) {
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                String nombreUsuario = partes[0].split(": ")[1];
                String rutUsuario = partes[1].split(": ")[1];
                String correoUsuario = partes[2].split(": ")[1];
                String claveUsuario = partes[3].split(": ")[1];
                String tipoUsuario = partes[4].split(": ")[1];
                String areaUsuario = partes.length > 5 ? partes[5].split(": ")[1] : "N/A";
                modeloListaUsuarios.addElement(nombreUsuario + " - " + rutUsuario + " - " + correoUsuario + " - " + claveUsuario + " - " + tipoUsuario + " - " + areaUsuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean reiniciarClaveUsuario(String fileName, String rutUsuario, String nuevaClave) {
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        File inputFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String fileRut = parts[1].split(": ")[1];
                    if (fileRut.equals(rutUsuario)) {
                        parts[3] = "Clave: " + nuevaClave;
                        line = String.join(", ", parts);
                    }
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    
        if (!inputFile.delete()) {
            return false;
        }
        return tempFile.renameTo(inputFile);
    }

    private class AgregarUsuarioListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = vista.getNombre();
            String rut = vista.getRut();
            String correo = vista.getCorreo();
            String clave = vista.getClave();
            String tipoUsuario = vista.getTipoUsuario();
            String area = vista.getArea();
            String PrimerInicio = "true";

            if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty() || clave.isEmpty() || tipoUsuario.isEmpty()) {
                System.out.println("Todos los campos deben estar llenos.");
                return;
            }

            administrador.guardarClienteEnArchivo(nombre, rut, correo, clave, tipoUsuario, area, PrimerInicio);
            vista.limpiarCampos();
        }
    }

    public void iniciarVista() {
        vista.iniciarVista();
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
                administrador.enviarMensajeUrgenteAChats(mensajeUrgente,salida);
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