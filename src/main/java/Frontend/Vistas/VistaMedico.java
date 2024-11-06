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

public class VistaMedico extends JFrame {
    private JTextArea areaChatMedico;
    private JTextField campoMensajeMedico;
    private JButton botonEnviarMensajeMedico;
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

    public VistaMedico(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Chat Médicos");
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelMedico = new JPanel(new BorderLayout());
        areaChatMedico = new JTextArea();
        areaChatMedico.setEditable(false);
        campoMensajeMedico = new JTextField();
        botonEnviarMensajeMedico = new JButton("Enviar");
        panelMedico.add(new JScrollPane(areaChatMedico), BorderLayout.CENTER);
        JPanel panelInputMedico = new JPanel(new BorderLayout());
        panelInputMedico.add(campoMensajeMedico, BorderLayout.CENTER);
        panelInputMedico.add(botonEnviarMensajeMedico, BorderLayout.EAST);
        panelMedico.add(panelInputMedico, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Médico", panelMedico);


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

        botonEnviarMensajeMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajeMedico();
            }
        });
    }

        private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 12345);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
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
                        mostrarMensajeMedico(mensaje);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void enviarMensajeMedico() {
        String mensaje = campoMensajeMedico.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                salida.writeUTF(mensajeFormateado);
                campoMensajeMedico.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarMensajeMedico(String mensaje) {
        areaChatMedico.append(mensaje + "\n");
    }
}