package Frontend.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private void enviarMensajeAuxiliar() {
        String mensaje = campoMensajeAuxiliar.getText();
        if (!mensaje.isEmpty()) {
            mostrarMensajeAuxiliar(nombreUsuario + " (" + rolUsuario + "): " + mensaje);
            campoMensajeAuxiliar.setText("");
            // Aquí puedes agregar la lógica para enviar el mensaje a otros auxiliares
        }
    }

    private void mostrarMensajeAuxiliar(String mensaje) {
        areaChatAuxiliar.append(mensaje + "\n");
    }

    private void enviarMensajeAdmision() {
        String mensaje = campoMensajeAdmision.getText();
        if (!mensaje.isEmpty()) {
            mostrarMensajeAdmision(nombreUsuario + " (" + rolUsuario + "): " + mensaje);
            campoMensajeAdmision.setText("");
            // Aquí puedes agregar la lógica para enviar el mensaje a otros de admisión
        }
    }

    private void mostrarMensajeAdmision(String mensaje) {
        areaChatAdmision.append(mensaje + "\n");
    }

    private void enviarMensajePabellon() {
        String mensaje = campoMensajePabellon.getText();
        if (!mensaje.isEmpty()) {
            mostrarMensajePabellon(nombreUsuario + " (" + rolUsuario + "): " + mensaje);
            campoMensajePabellon.setText("");
            // Aquí puedes agregar la lógica para enviar el mensaje a otros de pabellón
        }
    }

    private void mostrarMensajePabellon(String mensaje) {
        areaChatPabellon.append(mensaje + "\n");
    }

    private void enviarMensajeExamenes() {
        String mensaje = campoMensajeExamenes.getText();
        if (!mensaje.isEmpty()) {
            mostrarMensajeExamenes(nombreUsuario + " (" + rolUsuario + "): " + mensaje);
            campoMensajeExamenes.setText("");
            // Aquí puedes agregar la lógica para enviar el mensaje a otros de exámenes
        }
    }

    private void mostrarMensajeExamenes(String mensaje) {
        areaChatExamenes.append(mensaje + "\n");
    }

}