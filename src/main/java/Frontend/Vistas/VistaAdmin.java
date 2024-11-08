package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Frontend.Controladores.ControladorAdmin;

public class VistaAdmin extends JFrame {
    private JPanel panelPrincipal;
    private JTextField campoNombre, campoRut, campoCorreo, campoClave;
    private JComboBox<String> comboTipoUsuario;
    private JComboBox<String> area;
    private JTextArea areaTextoUsuarios, areaTextoEstadisticas;
    private JButton botonAgregarUsuario, botonEnviarUrgente, botonVerEstadisticas;
    private JList<String> listaUsuarios;
    private DefaultListModel<String> modeloListaUsuarios;
    private JButton botonReiniciarClave;
    private ControladorAdmin controlador; // Añadir campo ControladorAdmin
    private static VistaAdmin instance;
    public VistaAdmin(ControladorAdmin controlador) {
        this.controlador = controlador; // Inicializar el campo ControladorAdmin

        setTitle("Administrador de Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos para agregar usuarios
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField(20);
        panelPrincipal.add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("RUT:"), gbc);
        gbc.gridx = 1;
        campoRut = new JTextField(20);
        panelPrincipal.add(campoRut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        campoCorreo = new JTextField(20);
        panelPrincipal.add(campoCorreo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Clave:"), gbc);
        gbc.gridx = 1;
        campoClave = new JTextField(20);
        panelPrincipal.add(campoClave, gbc);

        // Añadir JComboBox para seleccionar tipo de usuario
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Tipo de Usuario:"), gbc);
        gbc.gridx = 1;
        String[] tipos = {"Medico", "Administrativo"};
        comboTipoUsuario = new JComboBox<>(tipos);
        panelPrincipal.add(comboTipoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panelPrincipal.add(new JLabel("Área específica:"), gbc);
        gbc.gridx = 1;
        String[] areaEspecifica = {"Admision", "Pabellon", "Examenes", "Auxiliar"};
        area = new JComboBox<>(areaEspecifica);
        area.setEnabled(false); // Inicialmente deshabilitado
        panelPrincipal.add(area, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        botonAgregarUsuario = new JButton("Agregar Usuario");
        panelPrincipal.add(botonAgregarUsuario, gbc);

        gbc.gridy = 7;
        botonEnviarUrgente = new JButton("Enviar Mensaje Urgente");
        panelPrincipal.add(botonEnviarUrgente, gbc);

        gbc.gridy = 8;
        botonVerEstadisticas = new JButton("Ver Estadísticas");
        panelPrincipal.add(botonVerEstadisticas, gbc);

        // Text areas for monitoring users and statistics
        areaTextoUsuarios = new JTextArea(10, 40);
        areaTextoEstadisticas = new JTextArea(10, 40);
        JScrollPane scrollUsuarios = new JScrollPane(areaTextoUsuarios);
        JScrollPane scrollEstadisticas = new JScrollPane(areaTextoEstadisticas);

        areaTextoUsuarios.setEditable(false);
        areaTextoEstadisticas.setEditable(false);

        // Initialize and add the user list
        modeloListaUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloListaUsuarios);
        JScrollPane scrollListaUsuarios = new JScrollPane(listaUsuarios);
        scrollListaUsuarios.setPreferredSize(new Dimension(780, 150));

        // Panel to hold both text areas and user list
        JPanel panelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.insets = new Insets(5, 5, 5, 5);
        gbcCentro.fill = GridBagConstraints.BOTH;
        gbcCentro.gridx = 0;
        gbcCentro.gridy = 0;
        gbcCentro.weightx = 1.0;
        gbcCentro.weighty = 0.5;
        panelCentro.add(scrollUsuarios, gbcCentro);
        botonReiniciarClave = new JButton("Reiniciar Clave");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelCentro.add(botonReiniciarClave, gbc);
        gbcCentro.gridy = 1;
        panelCentro.add(scrollEstadisticas, gbcCentro);

        gbcCentro.gridy = 2;
        gbcCentro.weighty = 0.5;
        panelCentro.add(scrollListaUsuarios, gbcCentro);

        // Add components to the frame
        add(panelPrincipal, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);

        // Add ActionListener to comboTipoUsuario
        comboTipoUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Administrativo".equals(comboTipoUsuario.getSelectedItem())) {
                    area.setEnabled(true);
                } else {
                    area.setEnabled(false);
                }
            }
        });

        botonReiniciarClave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = listaUsuarios.getSelectedValue();
                if (selectedUser != null) {
                    String[] userParts = selectedUser.split(" - ");
                    if (userParts.length >= 2) {
                        String rutUsuario = userParts[1];
                        System.out.println("rut: " + rutUsuario);
                        String nuevaClave = "nuevaClave123"; // Generar una nueva clave aquí
        
                        boolean actualizado = false;
                        if (selectedUser.contains("Medico")) {
                            actualizado = controlador.reiniciarClaveUsuario("Medicos.txt", rutUsuario, nuevaClave);
                        } else if (selectedUser.contains("Administrativo")) {
                            actualizado = controlador.reiniciarClaveUsuario("Administrativos.txt", rutUsuario, nuevaClave);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tipo de usuario desconocido.");
                            return;
                        }
        
                        if (actualizado) {
                            JOptionPane.showMessageDialog(null, "Clave reiniciada correctamente.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al reiniciar la clave.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Formato de usuario seleccionado incorrecto.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un usuario.");
                }
            }
        });
        
        setVisible(true);

        setVisible(true);
    }

    public void setModeloListaUsuarios(DefaultListModel<String> modeloListaUsuarios) {
        this.modeloListaUsuarios = modeloListaUsuarios;
        listaUsuarios.setModel(modeloListaUsuarios);
    }

    public void addAgregarUsuarioListener(ActionListener listener) {
        botonAgregarUsuario.addActionListener(listener);
    }

    public void addEnviarUrgenteListener(ActionListener listener) {
        botonEnviarUrgente.addActionListener(listener);
    }

    public void addVerEstadisticasListener(ActionListener listener) {
        botonVerEstadisticas.addActionListener(listener);
    }

    public String getNombre() {
        return campoNombre.getText();
    }

    public String getRut() {
        return campoRut.getText();
    }

    public String getCorreo() {
        return campoCorreo.getText();
    }

    public String getClave() {
        return campoClave.getText();
    }

    public String getTipoUsuario() {
        return (String) comboTipoUsuario.getSelectedItem();
    }

    public String getArea() {
        return (String) area.getSelectedItem();
    }

    public void limpiarCampos() {
        campoNombre.setText("");
        campoRut.setText("");
        campoCorreo.setText("");
        campoClave.setText("");
        comboTipoUsuario.setSelectedIndex(0);
        area.setSelectedIndex(0);
        area.setEnabled(false);
    }

    public JButton getBotonAgregarUsuario() {
        return botonAgregarUsuario;
    }
    public static VistaAdmin getInstance(ControladorAdmin controlador) {
        if (instance == null) {
            instance = new VistaAdmin(controlador);
        }
        return instance;
    }

    public void iniciarVista() {
        setVisible(true);
    }

    public static void main(String[] args) {
        ControladorAdmin controlador = new ControladorAdmin();
        VistaAdmin vistaAdmin = VistaAdmin.getInstance(controlador);
        vistaAdmin.iniciarVista();
    }
}