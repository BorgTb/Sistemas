package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

import Frontend.Controladores.gestorArchivos;

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
    private JList listaMedicos;
    private DefaultListModel modeloListaMedicos;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Map<String, JPanel> chatsAbiertos;
    private gestorArchivos gestorArchivos = new gestorArchivos();

    public VistaMedico() {
        chatsAbiertos = new HashMap<>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelMedico = new JPanel(new BorderLayout());
        areaChatMedico = new JTextArea();
        areaChatMedico.setEditable(false);
        campoMensajeMedico = new JTextField();
        botonEnviarMensajeMedico = new JButton("Enviar");
        botonEnviarMensajeMedico.setActionCommand("EnviarMensajeMedico");
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
        botonEnviarMensajeAuxiliar.setActionCommand("EnviarMensajeAuxiliar");
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
        botonEnviarMensajeAdmision.setActionCommand("EnviarMensajeAdmision");
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
        botonEnviarMensajePabellon.setActionCommand("EnviarMensajePabellon");
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
        botonEnviarMensajeExamenes.setActionCommand("EnviarMensajeExamenes");
        panelExamenes.add(new JScrollPane(areaChatExamenes), BorderLayout.CENTER);
        JPanel panelInputExamenes = new JPanel(new BorderLayout());
        panelInputExamenes.add(campoMensajeExamenes, BorderLayout.CENTER);
        panelInputExamenes.add(botonEnviarMensajeExamenes, BorderLayout.EAST);
        panelExamenes.add(panelInputExamenes, BorderLayout.SOUTH);
        tabbedPane.addTab("Chat Exámenes", panelExamenes);

        gestorArchivos.leerChats("medico-medico").forEach(mensaje -> areaChatMedico.append(mensaje + "\n"));
        gestorArchivos.leerChats("medico-admision").forEach(mensaje -> areaChatAdmision.append(mensaje + "\n"));
        gestorArchivos.leerChats("medico-pabellon").forEach(mensaje -> areaChatPabellon.append(mensaje + "\n"));
        gestorArchivos.leerChats("medico-examenes").forEach(mensaje -> areaChatExamenes.append(mensaje + "\n"));
        gestorArchivos.leerChats("auxiliar").forEach(mensaje -> areaChatAuxiliar.append(mensaje + "\n"));

        // Panel de lista de médicos
        listaMedicos = new JList<>();
        JScrollPane scrollListaMedicos = new JScrollPane(listaMedicos);
        JPanel panelListaMedicos = new JPanel(new BorderLayout());

        // Add title to the panel
        JLabel tituloListaMedicos = new JLabel("Listado Médicos");
        tituloListaMedicos.setHorizontalAlignment(JLabel.CENTER);
        tituloListaMedicos.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelListaMedicos.add(tituloListaMedicos, BorderLayout.NORTH);

        panelListaMedicos.add(scrollListaMedicos, BorderLayout.CENTER);

        // Set preferred width for the panel
        panelListaMedicos.setPreferredSize(new java.awt.Dimension(200, 0));

        add(tabbedPane, BorderLayout.CENTER);
        add(panelListaMedicos, BorderLayout.EAST);
        
    }

    @SuppressWarnings("unchecked")
    public void setModeloListaMedicos(DefaultListModel modeloListaMedicos) {
        this.modeloListaMedicos = modeloListaMedicos;
        listaMedicos.setModel(modeloListaMedicos);
    }
    
    public void addListSelectionListener(ListSelectionListener listener){
        listaMedicos.addListSelectionListener(listener);
    }

    public void addActionListener(ActionListener listener){
        botonEnviarMensajeMedico.addActionListener(listener);
        botonEnviarMensajeAuxiliar.addActionListener(listener);
        botonEnviarMensajeAdmision.addActionListener(listener);
        botonEnviarMensajePabellon.addActionListener(listener);
        botonEnviarMensajeExamenes.addActionListener(listener);
    }

    public void iniciarVista(String nombreUsuario, String rolUsuario) {
        setTitle("Chat Médico - " + nombreUsuario + " (" + rolUsuario + ")");
        setVisible(true);
    }

    public void abrirChatPrivado(String medico) {
        if (chatsAbiertos.containsKey(medico)) {
            // Si el chat ya está abierto, seleccionarlo
            tabbedPane.setSelectedComponent(chatsAbiertos.get(medico));
            return;
        }

        JPanel panelChatPrivado = new JPanel(new BorderLayout());
        JTextArea areaChatPrivado = new JTextArea();
        areaChatPrivado.setEditable(false);
        JTextField campoMensajePrivado = new JTextField();
        JButton botonEnviarMensajePrivado = new JButton("Enviar");
        panelChatPrivado.add(new JScrollPane(areaChatPrivado), BorderLayout.CENTER);
        JPanel panelInputPrivado = new JPanel(new BorderLayout());
        panelInputPrivado.add(campoMensajePrivado, BorderLayout.CENTER);
        panelInputPrivado.add(botonEnviarMensajePrivado, BorderLayout.EAST);
        panelChatPrivado.add(panelInputPrivado, BorderLayout.SOUTH);

        // Crear un panel para la pestaña con un botón de cerrar
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        JLabel tabLabel = new JLabel("Chat con " + medico);
        JButton closeButton = new JButton("X");
        closeButton.setMargin(new Insets(0, 20, 0, 0));
        closeButton.setBorder(null);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.indexOfComponent(panelChatPrivado);
                if (index != -1) {
                    tabbedPane.remove(index);
                    chatsAbiertos.remove(medico);
                }
            }
        });

        tabPanel.add(tabLabel, BorderLayout.CENTER);
        tabPanel.add(closeButton, BorderLayout.EAST);

        tabbedPane.addTab(null, panelChatPrivado);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panelChatPrivado), tabPanel);
        chatsAbiertos.put(medico, panelChatPrivado);

        botonEnviarMensajePrivado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajePrivado(medico, campoMensajePrivado, areaChatPrivado);
            }
        });
    }

    private void enviarMensajePrivado(String medico, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                System.out.println("Enviando mensaje privado a " + medico + ": " + mensajeFormateado);
                salida.writeUTF("Privado:" + medico + ":" + mensajeFormateado);
                campoMensaje.setText("");
            } catch (IOException e) {
                System.err.println("Error al enviar el mensaje privado: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El campo de mensaje está vacío, no se envía nada.");
        }
    }

    public void mostrarMensajeMedico(String mensaje) {
        areaChatMedico.append(mensaje + "\n");
    }

    public void mostrarMensajeAdmision(String mensaje) {
        areaChatAdmision.append(mensaje + "\n");
    }

    public void mostrarMensajePabellon(String mensaje) {
        areaChatPabellon.append(mensaje + "\n");
    }

    public void mostrarMensajeExamenes(String mensaje) {
        areaChatExamenes.append(mensaje + "\n");
    }

    public void mostrarMensajeAuxiliar(String mensaje) {
        areaChatAuxiliar.append(mensaje + "\n");
    }


    public JTextField getCampoMensajeMedico() {
        return campoMensajeMedico;
    }

    public JTextField getCampoMensajeAuxiliar() {
        return campoMensajeAuxiliar;
    }

    public JTextField getCampoMensajeAdmision() {
        return campoMensajeAdmision;
    }

    public JTextField getCampoMensajePabellon() {
        return campoMensajePabellon;
    }

    public JTextField getCampoMensajeExamenes() {
        return campoMensajeExamenes;
    }

    public JTextArea getAreaChatMedico() {
        return areaChatMedico;
    }

    public JTextArea getAreaChatAuxiliar() {
        return areaChatAuxiliar;
    }

    public JTextArea getAreaChatAdmision() {
        return areaChatAdmision;
    }

    public JTextArea getAreaChatPabellon() {
        return areaChatPabellon;
    }

    public JTextArea getAreaChatExamenes() {
        return areaChatExamenes;
    }

    public String getMedicoSeleccionado() {
        return (String) listaMedicos.getSelectedValue();
    }

}