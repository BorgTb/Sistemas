package Frontend.Vistas;

import java.awt.BorderLayout;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Frontend.Controladores.gestorArchivos;

public class VistaAuxiliar extends JFrame {
    private JTextArea areaChatAuxiliar;
    private JTextField campoMensajeAuxiliar;
    private JButton botonEnviarMensajeAuxiliar;
    private String nombreUsuario;
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private gestorArchivos gestorArchivos = new gestorArchivos();

    private List<String> mensajeCache = new ArrayList<>();
    public VistaAuxiliar(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelAuxiliar = new JPanel(new BorderLayout());
        areaChatAuxiliar = new JTextArea();
        areaChatAuxiliar.setEditable(false);
        campoMensajeAuxiliar = new JTextField();
        botonEnviarMensajeAuxiliar = new JButton("Enviar");
        JButton botonLimpiarChat = new JButton("Limpiar");
        panelAuxiliar.add(botonLimpiarChat, BorderLayout.NORTH);
        panelAuxiliar.add(new JScrollPane(areaChatAuxiliar), BorderLayout.CENTER);
        JPanel panelInputAuxiliar = new JPanel(new BorderLayout());
        panelInputAuxiliar.add(campoMensajeAuxiliar, BorderLayout.CENTER);
        panelInputAuxiliar.add(botonEnviarMensajeAuxiliar, BorderLayout.EAST);
        panelAuxiliar.add(panelInputAuxiliar, BorderLayout.SOUTH);
        gestorArchivos.leerChats("auxiliar").forEach(mensaje -> areaChatAuxiliar.append(mensaje + "\n"));
        add(panelAuxiliar);

        cargarMensajesDesdeArchivo("auxiliar", areaChatAuxiliar);
        conectarAlServidor();
        monitorearConexion();
        botonEnviarMensajeAuxiliar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje("Auxiliar", campoMensajeAuxiliar, areaChatAuxiliar);
            }
        });
        botonLimpiarChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                areaChatAuxiliar.setText("");
            }
        });
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
        escucharMensajes();
    }
    private void limpiarPantallaYRecargarMensajes() {
        areaChatAuxiliar.setText("");
        cargarMensajesDesdeArchivo("auxiliar", areaChatAuxiliar);
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
        new Thread(new Runnable() {
            public void run() {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readUTF()) != null) {
                        System.out.println("Mensaje recibido: " + mensaje);
                        String[] partes = mensaje.split(":", 2);
                        if (partes.length == 2) {
                            String pestaña = partes[0];
                            String contenidoMensaje = partes[1];
                            if (pestaña.equals("Auxiliar")) {
                                areaChatAuxiliar.append(contenidoMensaje + "\n");
                            }
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
}