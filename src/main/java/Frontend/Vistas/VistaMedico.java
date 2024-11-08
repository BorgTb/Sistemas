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

import javax.swing.Action;
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
    private JTextArea areaChatMedicoPrivado;
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
    private JButton botonLimpiarChatMedico;
    private JButton botonLimpiarChatAuxiliar;
    private JButton botonLimpiarChatAdmision;
    private JButton botonLimpiarChatPabellon;
    private JButton botonLimpiarChatExamenes;
    private JList listaMedicos;
    private DefaultListModel modeloListaMedicos;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Map<String, JPanel> chatsAbiertos;
    private gestorArchivos gestorArchivos = new gestorArchivos();
    private Map<String, JTextArea> areasDeChatPrivado = new HashMap<>();
    private JLabel avisoNuevoMensajeMedico;

    public VistaMedico() {
        chatsAbiertos = new HashMap<>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelMedico = new JPanel(new BorderLayout());
        avisoNuevoMensajeMedico = new JLabel("Nuevo mensaje");
        avisoNuevoMensajeMedico.setVisible(false);
        areaChatMedico = new JTextArea();
        areaChatMedico.setEditable(false);
        campoMensajeMedico = new JTextField();
        botonEnviarMensajeMedico = new JButton("Enviar");
        botonEnviarMensajeMedico.setActionCommand("EnviarMensajeMedico");
        botonLimpiarChatMedico = new JButton("Limpiar");
        panelMedico.add(new JScrollPane(areaChatMedico), BorderLayout.CENTER);
        JPanel panelInputMedico = new JPanel(new BorderLayout());
        panelInputMedico.add(campoMensajeMedico, BorderLayout.CENTER);
        panelInputMedico.add(botonEnviarMensajeMedico, BorderLayout.EAST);
        panelMedico.add(panelInputMedico, BorderLayout.SOUTH);
        panelMedico.add(botonLimpiarChatMedico, BorderLayout.NORTH);
        tabbedPane.addTab("Chat Médico", panelMedico);

        // Pestaña de chat con auxiliar
        JPanel panelAuxiliar = new JPanel(new BorderLayout());
        areaChatAuxiliar = new JTextArea();
        areaChatAuxiliar.setEditable(false);
        campoMensajeAuxiliar = new JTextField();
        botonEnviarMensajeAuxiliar = new JButton("Enviar");
        botonEnviarMensajeAuxiliar.setActionCommand("EnviarMensajeAuxiliar");
        botonLimpiarChatAuxiliar = new JButton("Limpiar");
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
        botonEnviarMensajeAdmision.setActionCommand("EnviarMensajeAdmision");
        botonLimpiarChatAdmision = new JButton("Limpiar");
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
        botonEnviarMensajePabellon.setActionCommand("EnviarMensajePabellon");
        botonLimpiarChatPabellon = new JButton("Limpiar");
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
        botonEnviarMensajeExamenes.setActionCommand("EnviarMensajeExamenes");
        botonLimpiarChatExamenes = new JButton("Limpiar");
        panelExamenes.add(new JScrollPane(areaChatExamenes), BorderLayout.CENTER);
        JPanel panelInputExamenes = new JPanel(new BorderLayout());
        panelInputExamenes.add(campoMensajeExamenes, BorderLayout.CENTER);
        panelInputExamenes.add(botonEnviarMensajeExamenes, BorderLayout.EAST);
        panelExamenes.add(panelInputExamenes, BorderLayout.SOUTH);
        panelExamenes.add(botonLimpiarChatExamenes, BorderLayout.NORTH);
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

    public void abrirChatPrivado(String medico, DataOutputStream salida, DataInputStream entrada, String nombreUsuario, String rolUsuario) {
        if (chatsAbiertos.containsKey(medico)) {
            tabbedPane.setSelectedComponent(chatsAbiertos.get(medico));
            return;
        }
        
        //medico -> recibe el mensaje y esta por su rut 
        // nombreUsuario -> envia el mensaje y esta por su rut
        
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
                    areasDeChatPrivado.remove(medico);
                }
            }
        });

        tabPanel.add(tabLabel, BorderLayout.WEST);
        tabPanel.add(closeButton, BorderLayout.EAST);

        tabbedPane.addTab("Chat con " + medico, panelChatPrivado);
        int index = tabbedPane.indexOfComponent(panelChatPrivado);
        tabbedPane.setTabComponentAt(index, tabPanel);
        tabbedPane.setSelectedComponent(panelChatPrivado);

        gestorArchivos.leerChatsPrivados(nombreUsuario, medico.split(" ")[2]).forEach(mensaje -> areaChatPrivado.append(mensaje + "\n"));
        

        chatsAbiertos.put(medico, panelChatPrivado);
        areasDeChatPrivado.put(medico.split(" ")[2], areaChatPrivado);
        

        botonEnviarMensajePrivado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensaje = campoMensajePrivado.getText();
                if (!mensaje.isEmpty()) {
                    String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
                    try {
                        System.out.println("Enviando mensaje privado a " + medico + ": " + mensajeFormateado);
                        areaChatPrivado.append(mensajeFormateado + "\n");
                        gestorArchivos.guardarChat(nombreUsuario+"-"+medico.split(" ")[2], mensajeFormateado);
                        gestorArchivos.guardarChat(medico.split(" ")[2]+"-"+nombreUsuario, mensajeFormateado);
                        salida.writeUTF("Privado:" + medico + ":" + mensajeFormateado);
                        campoMensajePrivado.setText("");
                    } catch (IOException ex) {
                        System.err.println("Error al enviar el mensaje privado: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("El campo de mensaje está vacío, no se envía nada.");
                }
            }
        });
    }

    


    public void mostrarMensajePrivado(String remitente, String mensaje) {
        
        JTextArea areaChatPrivado = areasDeChatPrivado.get(remitente);
        if (areaChatPrivado != null) {
            areaChatPrivado.append(mensaje + "\n");
        } else {
            System.out.println("No se ha creado el JTextArea para el chat privado con " + remitente);
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