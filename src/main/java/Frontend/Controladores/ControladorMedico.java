package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String nombreUsuario; // Este es el rut del médico
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private VistaMedico vistaMedico;
    private DefaultListModel<String> modeloListaMedicos = new DefaultListModel<>();
    private gestorArchivos gestorArchivos = new gestorArchivos(); // Instancia del gestor de archivos
    private List<String> mensajeCache = new ArrayList<>();
    public ControladorMedico(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        this.vistaMedico = new VistaMedico();
        this.vistaMedico.addActionListener(this);
        this.vistaMedico.addListSelectionListener(this);
        conectarAlServidor();
        escucharMensajes();
        monitorearConexion(); // Inicia la monitorización de la conexión
        this.vistaMedico.setModeloListaMedicos(this.modeloListaMedicos);
    }

    public void iniciarVista() {
        vistaMedico.iniciarVista(this.nombreUsuario, this.rolUsuario);
    }

    private void conectarAlServidor() {
        while (true) {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close(); // Cierra el socket antiguo si existe
                }
                System.out.println("Intentando conectar al servidor...");
                socket = new Socket("34.176.62.179", 8080);
                salida = new DataOutputStream(socket.getOutputStream());
                entrada = new DataInputStream(socket.getInputStream());
                salida.writeUTF(nombreUsuario);
                System.out.println("Conexión restablecida con el servidor.");
                limpiarPantallaYRecargarMensajes();
                break; // Sale del bucle tras conectar correctamente
            } catch (IOException e) {
                System.err.println("Servidor no disponible. Intentando reconectar en 5 segundos...");
                try {
                    Thread.sleep(5000); // Espera 5 segundos antes de intentar de nuevo
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    System.err.println("Reconexión interrumpida.");
                    break;
                }
            }
        }
    }
    private void limpiarPantallaYRecargarMensajes() {
        vistaMedico.limpiarChats();
        cargarMensajesDesdeArchivos();
    }
    
    private void cargarMensajesDesdeArchivos() {
        cargarMensajesDesdeArchivo("Medico-Medico", vistaMedico.getAreaChatMedico());
        cargarMensajesDesdeArchivo("auxiliar", vistaMedico.getAreaChatAuxiliar());
        cargarMensajesDesdeArchivo("medico-Admision", vistaMedico.getAreaChatAdmision());
        cargarMensajesDesdeArchivo("medico-Pabellon", vistaMedico.getAreaChatPabellon());
        cargarMensajesDesdeArchivo("medico-Examenes", vistaMedico.getAreaChatExamenes());
    }
    
    private void cargarMensajesDesdeArchivo(String pestaña, JTextArea areaChat) {
        List<String> mensajes = gestorArchivos.leerChats(pestaña);
        for (String mensaje : mensajes) {
            areaChat.append(mensaje + "\n");
        }
    }
    private void monitorearConexion() {
        new Thread(() -> {
            while (true) {
                try {
                    if (socket == null || socket.isClosed()) {
                        throw new IOException("Socket cerrado.");
                    }
                    salida.writeUTF("PING"); // Envía un mensaje ligero para comprobar la conexión
                    Thread.sleep(10000); // Monitorea cada 10 segundos
                } catch (IOException | InterruptedException e) {
                    System.err.println("Conexión perdida. Intentando reconectar...");
                    conectarAlServidor();
                }
            }
        }).start();
    }
    private void escucharMensajes() {
        new Thread(() -> {
            while (true) {
                try {
                    if (entrada == null) {
                        conectarAlServidor(); // Reconecta si `entrada` es null
                    }
                    String mensaje = entrada.readUTF();
                    System.out.println("Mensaje recibido: " + mensaje);
                    procesarMensaje(mensaje); // Lógica para manejar mensajes
                } catch (IOException e) {
                    System.err.println("Conexión perdida durante la escucha. Intentando reconectar...");
                    conectarAlServidor();
                }
            }
        }).start();
    }

    private void procesarMensaje(String mensaje) {
        if (mensaje.startsWith("Conectados:")) {
            actualizarListaConectados(mensaje);
        } else if (mensaje.contains("PrivateMessage")) {
            String remitente = mensaje.split(" ")[1].split("\\[")[0];
            String contenido = convertirMensajePrivado(mensaje);
            vistaMedico.mostrarMensajePrivado(remitente, contenido);
        } else if (mensaje.contains("URGENTE")) {
            String[] partes = mensaje.split(";");
            String mensajeUrgente = "Mensaje URGENTE DE ADMINISTRACION : " + partes[1];
            vistaMedico.mostrarMensajeUrgente(mensajeUrgente);
        } else {
            String[] partes = mensaje.split(":", 2);
            if (partes.length == 2) {
                String pestaña = partes[0];
                String contenidoMensaje = partes[1];
                switch (pestaña) {
                    case "Medico-Medico" -> vistaMedico.mostrarMensajeMedico(contenidoMensaje);
                    case "Auxiliar" -> vistaMedico.mostrarMensajeAuxiliar(contenidoMensaje);
                    case "Medico-Admision" -> vistaMedico.mostrarMensajeAdmision(contenidoMensaje);
                    case "Medico-Pabellon" -> vistaMedico.mostrarMensajePabellon(contenidoMensaje);
                    case "Medico-Examenes" -> vistaMedico.mostrarMensajeExamenes(contenidoMensaje);
                }
            }
        }
    }

    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                if (socket == null || socket.isClosed()) {
                    areaChat.append("Conexión caída. Reconectando...\n");
                    System.out.println("Socket cerrado. Guardando mensaje en caché...");
                    mensajeCache.add(pestaña + ":" + mensajeFormateado);
                    gestorArchivos.guardarChat(pestaña, mensajeFormateado); // Guardar en el archivo
                    campoMensaje.setText("");
                    return;
                }
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
                gestorArchivos.guardarChat(pestaña, mensajeFormateado);
                campoMensaje.setText("");
            } catch (SocketException e) {
                System.err.println("Error al enviar el mensaje: " + e.getMessage());
                System.out.println("Guardando mensaje en caché...");
                mensajeCache.add(pestaña + ":" + mensajeFormateado);
                gestorArchivos.guardarChat(pestaña, mensajeFormateado); // Guardar en el archivo
                conectarAlServidor();
            } catch (IOException e) {
                System.err.println("Error general al enviar el mensaje: " + e.getMessage());
            }
        } else {
            System.out.println("El campo de mensaje está vacío, no se envía nada.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "EnviarMensajeMedico" -> enviarMensaje("Medico-Medico", vistaMedico.getCampoMensajeMedico(), vistaMedico.getAreaChatMedico());
            case "EnviarMensajeAuxiliar" -> enviarMensaje("Auxiliar", vistaMedico.getCampoMensajeAuxiliar(), vistaMedico.getAreaChatAuxiliar());
            case "EnviarMensajeAdmision" -> enviarMensaje("Medico-Admision", vistaMedico.getCampoMensajeAdmision(), vistaMedico.getAreaChatAdmision());
            case "EnviarMensajePabellon" -> enviarMensaje("Medico-Pabellon", vistaMedico.getCampoMensajePabellon(), vistaMedico.getAreaChatPabellon());
            case "EnviarMensajeExamenes" -> enviarMensaje("Medico-Examenes", vistaMedico.getCampoMensajeExamenes(), vistaMedico.getAreaChatExamenes());
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
}
