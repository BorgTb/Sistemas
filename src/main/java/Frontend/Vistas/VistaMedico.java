package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.w3c.dom.events.MouseEvent;

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
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Backend.Database;

import javax.swing.JList;

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

    public VistaMedico(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        chatsAbiertos = new HashMap<>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

         // Panel de lista de médicos
        modeloListaMedicos = new DefaultListModel<>();
        listaMedicos = new JList<>(modeloListaMedicos);
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
         conectarAlServidor();
         escucharMensajes();
         cargarMedicos();
         botonEnviarMensajeMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensajeMedico();
            }
        });
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
        listaMedicos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMedico = (String) listaMedicos.getSelectedValue();
                    if (selectedMedico != null) {
                        int response = JOptionPane.showConfirmDialog(null, "¿Desea enviar un mensaje privado a " + selectedMedico + "?", "Mensaje Privado", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            abrirChatPrivado(selectedMedico);
                        }
                    }
                }
            }
        });
    }
    private void abrirChatPrivado(String medico) {
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
        closeButton.setMargin(new Insets(0, 10, 0, 0));
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
    private void cargarMedicos() {
        Database db = Database.getInstance();
        List<Document> medicos = db.getMedicos();
        for (Document medico : medicos) {
            String nombreMedico = medico.getString("nombre");
            String rutMedico = medico.getString("rut");
            if (!nombreMedico.equals(nombreUsuario)) {
                modeloListaMedicos.addElement(nombreMedico + " - " + rutMedico);
            }
        }
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
                                case "Medico-Medico":
                                    mostrarMensajeMedico(contenidoMensaje);
                                    break;
                                case "Auxiliar":
                                    mostrarMensajeAuxiliar(contenidoMensaje);
                                    break;
                                case "Medico-Admision":
                                    mostrarMensajeAdmision(contenidoMensaje);
                                    break;
                                case "Medico-Pabellon":
                                    mostrarMensajePabellon(contenidoMensaje);
                                    break;
                                case "Medico-Examenes":
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
    
    private void enviarMensajeMedico() {
        enviarMensaje("Medico-Medico", campoMensajeMedico, areaChatMedico);
    }

    private void enviarMensajeAuxiliar() {
        enviarMensaje("Auxiliar", campoMensajeAuxiliar, areaChatAuxiliar);
    }

    private void enviarMensajeAdmision() {
        enviarMensaje("Medico-Admision", campoMensajeAdmision, areaChatAdmision);
    }

    private void enviarMensajePabellon() {
        enviarMensaje("Medico-Pabellon", campoMensajePabellon, areaChatPabellon);
    }

    private void enviarMensajeExamenes() {
        enviarMensaje("Medico-Examenes", campoMensajeExamenes, areaChatExamenes);
    }
    
    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                System.out.println("Enviando mensaje: " + pestaña + ":" + mensajeFormateado);
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
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
}