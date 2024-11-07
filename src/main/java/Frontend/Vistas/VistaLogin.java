package Frontend.Vistas;

import Backend.Administrador;

import javax.swing.*;

import org.bson.Document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaLogin extends JFrame {
    private JTextField campoRut;
    private JPasswordField campoClave;
    private JButton botonLogin;
    private JComboBox<String> comboTipoUsuario;

    public VistaLogin() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        campoRut = new JTextField(15);
        campoClave = new JPasswordField(15);
        botonLogin = new JButton("Login");
        comboTipoUsuario = new JComboBox<>(new String[]{"Médico", "Administrativo"});

        panel.add(new JLabel("RUT:"));
        panel.add(campoRut);
        panel.add(new JLabel("Clave:"));
        panel.add(campoClave);
        panel.add(new JLabel("Tipo de Usuario:"));
        panel.add(comboTipoUsuario);
        panel.add(botonLogin);

        add(panel);

    }

    public void addActionListener(ActionListener listener) {
        botonLogin.addActionListener(listener);
    }

   

    public String getRut() {
        return campoRut.getText();
    }

    public String getClave() {
        return new String(campoClave.getPassword());
    }

    public String getTipoUsuario() {
        return (String) comboTipoUsuario.getSelectedItem();
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje);
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VistaLogin().setVisible(true);
            }
        });
    }
}