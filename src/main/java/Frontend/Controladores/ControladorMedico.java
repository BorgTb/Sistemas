package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Frontend.Vistas.VistaMedico;

public class ControladorMedico implements ActionListener, ListSelectionListener {
    private String nombreUsuario;
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private VistaMedico vistaMedico;
    private DefaultListModel<String> modeloListaMedicos = new DefaultListModel<>();
    gestorArchivos gestorArchivos = new gestorArchivos(); // Create an instance of the GestorArchivos class

    public ControladorMedico(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        this.vistaMedico = new VistaMedico();
        this.vistaMedico.addActionListener(this);
        this.vistaMedico.addListSelectionListener(this);
        conectarAlServidor();
        cargarMedicos();
        escucharMensajes();
        this.vistaMedico.setModeloListaMedicos(this.modeloListaMedicos);
    }

    public void iniciarVista() {
        vistaMedico.iniciarVista(this.nombreUsuario, this.rolUsuario);
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

    private void cargarMedicos() {
        try (BufferedReader br = new BufferedReader(new FileReader("Sistemas/src/main/java/Users/Medicos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                String nombreMedico = partes[0].split(": ")[1];
                String rutMedico = partes[1].split(": ")[1];
                if (!nombreMedico.equals(nombreUsuario)) {
                    modeloListaMedicos.addElement(nombreMedico + " - " + rutMedico);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los médicos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escucharMensajes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readUTF()) != null) {
                        System.out.println("Mensaje recibido: " + mensaje);

                        if (mensaje.contains("Privado")) {
                            mensaje = convertirMensajePrivado(mensaje);
                            System.out.println("Mensaje privado: " + mensaje);
                        } else {
                            String[] partes = mensaje.split(":", 2);
                            if (partes.length == 2) {
                                String pestaña = partes[0];
                                String contenidoMensaje = partes[1];
                                switch (pestaña) {
                                    case "Medico-Medico":
                                        vistaMedico.mostrarMensajeMedico(contenidoMensaje);
                                        break;
                                    case "Auxiliar":
                                        vistaMedico.mostrarMensajeAuxiliar(contenidoMensaje);
                                        break;
                                    case "Medico-Admision":
                                        vistaMedico.mostrarMensajeAdmision(contenidoMensaje);
                                        break;
                                    case "Medico-Pabellon":
                                        vistaMedico.mostrarMensajePabellon(contenidoMensaje);
                                        break;
                                    case "Medico-Examenes":
                                        vistaMedico.mostrarMensajeExamenes(contenidoMensaje);
                                        break;
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String convertirMensajePrivado(String mensaje) {
        int index = mensaje.indexOf('[');
        if (index != -1) {
            return mensaje.substring(index);
        }
        return mensaje;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "EnviarMensajeMedico":
                enviarMensajeMedico();
                break;
            case "EnviarMensajeAuxiliar":
                enviarMensajeAuxiliar();
                break;
            case "EnviarMensajeAdmision":
                enviarMensajeAdmision();
                break;
            case "EnviarMensajePabellon":
                enviarMensajePabellon();
                break;
            case "EnviarMensajeExamenes":
                enviarMensajeExamenes();
                break;
            default:
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selectedMedico = vistaMedico.getMedicoSeleccionado();
            if (selectedMedico != null) {
                int response = JOptionPane.showConfirmDialog(null,
                        "¿Desea enviar un mensaje privado a " + selectedMedico + "?", "Mensaje Privado",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    vistaMedico.abrirChatPrivado(selectedMedico);
                }
            }
        }
    }


    private void enviarMensajeMedico() {
        enviarMensaje("Medico-Medico", vistaMedico.getCampoMensajeMedico(), vistaMedico.getAreaChatMedico());
    }

    private void enviarMensajeAuxiliar() {
        enviarMensaje("Auxiliar", vistaMedico.getCampoMensajeAuxiliar(), vistaMedico.getAreaChatAuxiliar());
    }

    private void enviarMensajeAdmision() {
        enviarMensaje("Medico-Admision", vistaMedico.getCampoMensajeAdmision(), vistaMedico.getAreaChatAdmision());
    }

    private void enviarMensajePabellon() {
        enviarMensaje("Medico-Pabellon", vistaMedico.getCampoMensajePabellon(), vistaMedico.getAreaChatPabellon());
    }

    private void enviarMensajeExamenes() {
        enviarMensaje("Medico-Examenes", vistaMedico.getCampoMensajeExamenes(), vistaMedico.getAreaChatExamenes());
    }

    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                System.out.println("Enviando mensaje: " + pestaña + ":" + mensajeFormateado);
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
                gestorArchivos.guardarChat(pestaña, mensajeFormateado); // Call the guardarChat method on the instance
                campoMensaje.setText("");
            } catch (IOException e) {
                System.err.println("Error al enviar el mensaje: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El campo de mensaje está vacío, no se envía nada.");
        }
    }
}
