package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaAdmin extends JFrame {
    private JPanel panelPrincipal;
    private JTextField campoNombre, campoRut, campoCorreo, campoClave;
    private JComboBox<String> comboTipoUsuario; // JComboBox para seleccionar el tipo de usuario
    private JTextArea areaTextoUsuarios, areaTextoEstadisticas;
    private JButton botonAgregarUsuario, botonEnviarUrgente, botonVerEstadisticas;

    public VistaAdmin() {
        crearVentanaAdmin();
    }

    private void crearVentanaAdmin() {
        setTitle("Administrador de Chat");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(7, 2)); // Aumentamos a 7 filas

        // Campos para agregar usuarios
        panelPrincipal.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panelPrincipal.add(campoNombre);

        panelPrincipal.add(new JLabel("RUT:"));
        campoRut = new JTextField();
        panelPrincipal.add(campoRut);

        panelPrincipal.add(new JLabel("Correo:"));
        campoCorreo = new JTextField();
        panelPrincipal.add(campoCorreo);

        panelPrincipal.add(new JLabel("Clave:"));
        campoClave = new JTextField();
        panelPrincipal.add(campoClave);

        // Añadir JComboBox para seleccionar tipo de usuario
        panelPrincipal.add(new JLabel("Tipo de Usuario:"));
        String[] tipos = {"Admisión", "Pabellón", "Exámenes", "Auxiliar"};
        comboTipoUsuario = new JComboBox<>(tipos);
        panelPrincipal.add(comboTipoUsuario);

        botonAgregarUsuario = new JButton("Agregar Usuario");
        botonEnviarUrgente = new JButton("Enviar Mensaje Urgente");
        botonVerEstadisticas = new JButton("Ver Estadísticas");

        panelPrincipal.add(botonAgregarUsuario);
        panelPrincipal.add(botonEnviarUrgente);
        panelPrincipal.add(botonVerEstadisticas);

        // Text areas for monitoring users and statistics
        areaTextoUsuarios = new JTextArea(10, 40);
        areaTextoEstadisticas = new JTextArea(10, 40);
        JScrollPane scrollUsuarios = new JScrollPane(areaTextoUsuarios);
        JScrollPane scrollEstadisticas = new JScrollPane(areaTextoEstadisticas);

        areaTextoUsuarios.setEditable(false);
        areaTextoEstadisticas.setEditable(false);

        // Add components to the frame
        add(panelPrincipal, BorderLayout.NORTH);
        add(scrollUsuarios, BorderLayout.CENTER);
        add(scrollEstadisticas, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Methods to add ActionListeners
    public void addActionListener(ActionListener listener) {
        botonAgregarUsuario.addActionListener(listener);
        botonEnviarUrgente.addActionListener(listener);
        botonVerEstadisticas.addActionListener(listener);
    }

    // Methods to display user list and statistics
    public void mostrarUsuarios(String usuarios) {
        areaTextoUsuarios.setText(usuarios);
    }

    public void mostrarEstadisticas(String estadisticas) {
        areaTextoEstadisticas.setText(estadisticas);
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
        return (String) comboTipoUsuario.getSelectedItem(); // Obtener el tipo de usuario seleccionado
    }

    public static void main(String[] args) {
        new VistaAdmin();
    }
}
