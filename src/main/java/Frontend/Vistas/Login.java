package Frontend.Vistas;

import Backend.Administrador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField campoRut;
    private JPasswordField campoClave;
    private JButton botonLogin;
    private JComboBox<String> comboTipoUsuario;

    public Login() {
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

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rut = campoRut.getText();
                String clave = new String(campoClave.getPassword());
                String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();

                Administrador admin = new Administrador();
                boolean autenticado = false;

                if ("Médico".equals(tipoUsuario)) {
                    autenticado = admin.autenticarMedico(rut, clave);
                    if (autenticado) {
                        new VistaMedico().setVisible(true);
                    }
                } else if ("Administrativo".equals(tipoUsuario)) {
                    autenticado = admin.autenticarAdministrativo(rut, clave);
                    if (autenticado) {
                        new Vistadministrativo().setVisible(true);
                    }
                }

                if (autenticado) {
                    dispose(); // Cierra la ventana de login
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Usuario o clave incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}