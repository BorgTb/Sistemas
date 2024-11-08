package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Frontend.Controladores.gestorArchivos;

public class VistaPabellon extends JFrame {
    private JTextArea areaChatMedico;
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
    private gestorArchivos gestorArchivos = new gestorArchivos();

    public VistaPabellon(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelMedico = new JPanel(new BorderLayout());
        areaChatMedico = new JTextArea();
        areaChatMedico.setEditable(false);
        panelMedico.add(new JScrollPane(areaChatMedico), BorderLayout.CENTER);
        JButton botonLimpiarChatMedico = new JButton("Limpiar");
        panelMedico.add(botonLimpiarChatMedico, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Médico", panelMedico);

         // Pestaña de chat con auxiliar
        JPanel panelAuxiliar = new JPanel(new BorderLayout());
        areaChatAuxiliar = new JTextArea();
        areaChatAuxiliar.setEditable(false);
        campoMensajeAuxiliar = new JTextField();
        botonEnviarMensajeAuxiliar = new JButton("Enviar");
        JButton botonLimpiarChatAuxiliar = new JButton("Limpiar");
        panelAuxiliar.add(new JScrollPane(areaChatAuxiliar), BorderLayout.CENTER);
        JPanel panelInputAuxiliar = new JPanel(new BorderLayout());
        panelInputAuxiliar.add(campoMensajeAuxiliar, BorderLayout.CENTER);
        panelInputAuxiliar.add(botonEnviarMensajeAuxiliar, BorderLayout.EAST);
        panelAuxiliar.add(panelInputAuxiliar, BorderLayout.SOUTH);
        panelAuxiliar.add(botonLimpiarChatAuxiliar, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Auxiliar", panelAuxiliar);

        // Pestaña de chat con admisión
        JPanel panelAdmision = new JPanel(new BorderLayout());
        areaChatAdmision = new JTextArea();
        areaChatAdmision.setEditable(false);
        campoMensajeAdmision = new JTextField();
        botonEnviarMensajeAdmision = new JButton("Enviar");
        JButton botonLimpiarChatAdmision = new JButton("Limpiar");
        panelAdmision.add(new JScrollPane(areaChatAdmision), BorderLayout.CENTER);
        JPanel panelInputAdmision = new JPanel(new BorderLayout());
        panelInputAdmision.add(campoMensajeAdmision, BorderLayout.CENTER);
        panelInputAdmision.add(botonEnviarMensajeAdmision, BorderLayout.EAST);
        panelAdmision.add(panelInputAdmision, BorderLayout.SOUTH);
        panelAdmision.add(botonLimpiarChatAdmision, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Admisión", panelAdmision);

        // Pestaña de chat con pabellón
        JPanel panelPabellon = new JPanel(new BorderLayout());
        areaChatPabellon = new JTextArea();
        areaChatPabellon.setEditable(false);
        campoMensajePabellon = new JTextField();
        botonEnviarMensajePabellon = new JButton("Enviar");
        JButton botonLimpiarChatPabellon = new JButton("Limpiar");
        panelPabellon.add(new JScrollPane(areaChatPabellon), BorderLayout.CENTER);
        JPanel panelInputPabellon = new JPanel(new BorderLayout());
        panelInputPabellon.add(campoMensajePabellon, BorderLayout.CENTER);
        panelInputPabellon.add(botonEnviarMensajePabellon, BorderLayout.EAST);
        panelPabellon.add(panelInputPabellon, BorderLayout.SOUTH);
        panelPabellon.add(botonLimpiarChatPabellon, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Pabellón", panelPabellon);

        // Pestaña de chat con exámenes
        JPanel panelExamenes = new JPanel(new BorderLayout());
        areaChatExamenes = new JTextArea();
        areaChatExamenes.setEditable(false);
        campoMensajeExamenes = new JTextField();
        botonEnviarMensajeExamenes = new JButton("Enviar");
        JButton botonLimpiarChatExamenes = new JButton("Limpiar");
        panelExamenes.add(new JScrollPane(areaChatExamenes), BorderLayout.CENTER);
        JPanel panelInputExamenes = new JPanel(new BorderLayout());
        panelInputExamenes.add(campoMensajeExamenes, BorderLayout.CENTER);
        panelInputExamenes.add(botonEnviarMensajeExamenes, BorderLayout.EAST);
        panelExamenes.add(panelInputExamenes, BorderLayout.SOUTH);
        panelExamenes.add(botonLimpiarChatExamenes, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Exámenes", panelExamenes);

         gestorArchivos.leerChats("pabellon-pabellon").forEach(mensaje -> areaChatPabellon.append(mensaje + "\n"));
         gestorArchivos.leerChats("medico-pabellon").forEach(mensaje -> areaChatMedico.append(mensaje + "\n"));
         gestorArchivos.leerChats("examenes-pabellon").forEach(mensaje -> areaChatExamenes.append(mensaje + "\n"));
         gestorArchivos.leerChats("admision-pabellon").forEach(mensaje -> areaChatAdmision.append(mensaje + "\n"));
         gestorArchivos.leerChats("auxiliar").forEach(mensaje -> areaChatAuxiliar.append(mensaje + "\n"));
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
        botonLimpiarChatMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarChatMedico();
            }
        });
        botonLimpiarChatAuxiliar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarChatAuxiliar();
            }
        });
        
        botonLimpiarChatAdmision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarChatAdmision();
            }
        });
        
        botonLimpiarChatPabellon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarChatPabellon();
            }
        });
        
        botonLimpiarChatExamenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarChatExamenes();
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
                                case "Medico-Pabellon":
                                    mostrarMensajeMedico(contenidoMensaje);
                                    break;
                                case "Auxiliar":
                                    mostrarMensajeAuxiliar(contenidoMensaje);
                                    break;
                                case "Admision-Pabellon":
                                    mostrarMensajeAdmision(contenidoMensaje);
                                    break;
                                case "Pabellon-Pabellon":
                                    mostrarMensajePabellon(contenidoMensaje);
                                    break;
                                case "Examenes-Pabellon":
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
        enviarMensaje("Admision-Pabellon", campoMensajeAdmision, areaChatAdmision);
    }

    private void enviarMensajePabellon() {
        enviarMensaje("Pabellon-Pabellon", campoMensajePabellon, areaChatPabellon);
    }

    private void enviarMensajeExamenes() {
        enviarMensaje("Examenes-Pabellon", campoMensajeExamenes, areaChatExamenes);
    }
    
    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                System.out.println("Enviando mensaje: " + pestaña + ":" + mensajeFormateado);
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
                gestorArchivos.guardarChat(pestaña, mensajeFormateado);
                campoMensaje.setText("");
            } catch (IOException e) {
                System.err.println("Error al enviar el mensaje: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El campo de mensaje está vacío, no se envía nada.");
        }
    }

    
    private void mostrarMensajeMedico(String mensaje) {
        areaChatMedico.append(mensaje + "\n");
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
    private void mostrarMensajeAuxiliar(String mensaje) {
        areaChatAuxiliar.append(mensaje + "\n");
    }
    private void limpiarChatMedico() {
        areaChatMedico.setText("");
    }
    
    private void limpiarChatAuxiliar() {
        areaChatAuxiliar.setText("");
    }
    
    private void limpiarChatAdmision() {
        areaChatAdmision.setText("");
    }
    
    private void limpiarChatPabellon() {
        areaChatPabellon.setText("");
    }
    
    private void limpiarChatExamenes() {
        areaChatExamenes.setText("");
    }
}