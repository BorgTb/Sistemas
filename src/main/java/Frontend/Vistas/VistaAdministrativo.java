package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaAdministrativo extends JFrame {
    private JTextArea areaChatAuxiliar;
    private JTextArea areaChatAdmision;
    private JTextArea areaChatPabellon;
    private JTextArea areaChatExamenes;
    private JTextField campoMensajeAuxiliar;
    private JTextField campoMensajeAdmision;
    private JTextField campoMensajePabellon;
    private JTextField campoMensajeExamenes;
    private JButton botonEnviarMensajeAuxiliar;
    private JButton botonEnviarMensajeAdmision;
    private JButton botonEnviarMensajePabellon;
    private JButton botonEnviarMensajeExamenes;
    private String nombreUsuario;
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public VistaAdministrativo(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Chat Administrativo");

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña de chat con auxiliar
        JPanel panelAuxiliar = new JPanel(new BorderLayout());
        areaChatAuxiliar = new JTextArea();
        areaChatAuxiliar.setEditable(false);
        campoMensajeAuxiliar = new JTextField();
        botonEnviarMensajeAuxiliar = new JButton("Enviar");
        panelAuxiliar.add(new JScrollPane(areaChatAuxiliar), BorderLayout.CENTER);
        JPanel panelInputAuxiliar = new JPanel(new BorderLayout());
        panelInputAuxiliar.add(campoMensajeAuxiliar, BorderLayout.CENTER);
        panelInputAuxiliar.add(botonEnviarMensajeAuxiliar, BorderLayout.EAST);
        panelAuxiliar.add(panelInputAuxiliar, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Auxiliar", panelAuxiliar);

        // Pestaña de chat con admisión
        JPanel panelAdmision = new JPanel(new BorderLayout());
        areaChatAdmision = new JTextArea();
        areaChatAdmision.setEditable(false);
        campoMensajeAdmision = new JTextField();
        botonEnviarMensajeAdmision = new JButton("Enviar");
        panelAdmision.add(new JScrollPane(areaChatAdmision), BorderLayout.CENTER);
        JPanel panelInputAdmision = new JPanel(new BorderLayout());
        panelInputAdmision.add(campoMensajeAdmision, BorderLayout.CENTER);
        panelInputAdmision.add(botonEnviarMensajeAdmision, BorderLayout.EAST);
        panelAdmision.add(panelInputAdmision, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Admisión", panelAdmision);

        // Pestaña de chat con pabellón
        JPanel panelPabellon = new JPanel(new BorderLayout());
        areaChatPabellon = new JTextArea();
        areaChatPabellon.setEditable(false);
        campoMensajePabellon = new JTextField();
        botonEnviarMensajePabellon = new JButton("Enviar");
        panelPabellon.add(new JScrollPane(areaChatPabellon), BorderLayout.CENTER);
        JPanel panelInputPabellon = new JPanel(new BorderLayout());
        panelInputPabellon.add(campoMensajePabellon, BorderLayout.CENTER);
        panelInputPabellon.add(botonEnviarMensajePabellon, BorderLayout.EAST);
        panelPabellon.add(panelInputPabellon, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Pabellón", panelPabellon);

        // Pestaña de chat con exámenes
        JPanel panelExamenes = new JPanel(new BorderLayout());
        areaChatExamenes = new JTextArea();
        areaChatExamenes.setEditable(false);
        campoMensajeExamenes = new JTextField();
        botonEnviarMensajeExamenes = new JButton("Enviar");
        panelExamenes.add(new JScrollPane(areaChatExamenes), BorderLayout.CENTER);
        JPanel panelInputExamenes = new JPanel(new BorderLayout());
        panelInputExamenes.add(campoMensajeExamenes, BorderLayout.CENTER);
        panelInputExamenes.add(botonEnviarMensajeExamenes, BorderLayout.EAST);
        panelExamenes.add(panelInputExamenes, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Exámenes", panelExamenes);

        add(tabbedPane);
        conectarAlServidor();
        escucharMensajes();
        botonEnviarMensajeAuxiliar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajeAuxiliar();
            }
        });
        botonEnviarMensajeAdmision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajeAdmision();
            }
        });
        botonEnviarMensajePabellon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajePabellon();
            }
        });
        botonEnviarMensajeExamenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajeExamenes();
            }
        });
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
                        System.out.println("Mensaje recibido: " + mensaje);
                        String[] partes = mensaje.split(":", 2);
                        if (partes.length == 2) {
                            String pestaña = partes[0];
                            String contenidoMensaje = partes[1];
                            switch (pestaña) {
                                case "Auxiliar":
                                    mostrarMensajeAuxiliar(contenidoMensaje);
                                    break;
                                case "Admision":
                                    mostrarMensajeAdmision(contenidoMensaje);
                                    break;
                                case "Pabellon":
                                    mostrarMensajePabellon(contenidoMensaje);
                                    break;
                                case "Examenes":
                                    mostrarMensajeExamenes(contenidoMensaje);
                                    break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void enviarMensajeAuxiliar() {
        enviarMensaje("Auxiliar", campoMensajeAuxiliar, areaChatAuxiliar);
    }

    private void enviarMensajeAdmision() {
        enviarMensaje("Admision", campoMensajeAdmision, areaChatAdmision);
    }

    private void enviarMensajePabellon() {
        enviarMensaje("Pabellon", campoMensajePabellon, areaChatPabellon);
    }

    private void enviarMensajeExamenes() {
        enviarMensaje("Examenes", campoMensajeExamenes, areaChatExamenes);
    }

    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
                campoMensaje.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarMensajeAuxiliar(String mensaje) {
        areaChatAuxiliar.append(mensaje + "\n");
    }

    private void mostrarMensajeAdmision(String mensaje) {
        areaChatAdmision.append(mensaje + "\n");
    }

    private void mostrarMensajePabellon(String mensaje) {
        areaChatPabellon.append(mensaje + "\n");
    }

    private void mostrarMensajeExamenes(String mensaje) {
        areaChatExamenes.append(mensaje + "\n");
    }

}