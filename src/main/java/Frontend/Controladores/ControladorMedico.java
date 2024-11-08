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
    private String nombreUsuario; //este es el rut del medico
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private VistaMedico vistaMedico;
    private DefaultListModel<String> modeloListaMedicos = new DefaultListModel<>();
    gestorArchivos gestorArchivos = new gestorArchivos(); // Create an instance of the GestorArchivos class

    public ControladorMedico(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario; // Asegúrate de que este es el rut del médico
        this.rolUsuario = rolUsuario;
        this.vistaMedico = new VistaMedico();
        this.vistaMedico.addActionListener(this);
        this.vistaMedico.addListSelectionListener(this);
        conectarAlServidor();
        //cargarMedicos();
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
            salida.writeUTF(nombreUsuario);
            System.out.println("Conectado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharMensajes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readUTF()) != null) {
                        if (mensaje.startsWith("Conectados:")) {
                            actualizarListaConectados(mensaje);
                        } else if (mensaje.contains("PrivateMessage")) {
                            String emisor = mensaje.split(" ")[1].split("")[0];
                            String remitente = mensaje.split(" ")[1].split("\\[")[0];
                            String contenido = convertirMensajePrivado(mensaje);
                            vistaMedico.mostrarMensajePrivado(remitente, contenido);
                        } else if (mensaje.contains("URGENTE")) {
                            String[] partes = mensaje.split(";");
                            String mensajeUrgente = "Mensaje URGENTE DE ADMINISTRACION : "+partes[1];
                            vistaMedico.mostrarMensajeUrgente(mensajeUrgente);
                        }else {
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
    private void actualizarListaConectados(String mensaje) {
        String[] partes = mensaje.split(":")[1].split(",");
        modeloListaMedicos.clear();
        for (String medico : partes) {
            if (!medico.isEmpty() && !medico.equals(nombreUsuario)) {
                modeloListaMedicos.addElement(medico);
            }
        }
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
                    vistaMedico.abrirChatPrivado(selectedMedico, this.salida, this.entrada, this.nombreUsuario, this.rolUsuario);
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
