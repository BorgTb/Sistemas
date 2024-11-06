package Frontend.Vistas;

import Backend.Administrador;
import Frontend.Vistas.VistaCliente;

import javax.swing.*;

import org.bson.Document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField campoRut;
    private JPasswordField campoClave;
    private JButton botonLogin;
    private JComboBox<String> comboTipoUsuario;
    private VistaCliente vistaCliente;

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
                Document medico = admin.retornarMedico(rut);
                Document administrativo = admin.retornarAdministrativo(rut);
                boolean autenticado = false;
                if ("Médico".equals(tipoUsuario)) {
                    autenticado = admin.autenticarMedico(medico.getString("nombre"), clave);
                    if (autenticado) {
                        VistaMedico vistaMedico = new VistaMedico(rut, tipoUsuario);
                        vistaMedico.setTitle("Vista Médico: "+ medico.getString("nombre"));
                        vistaMedico.setVisible(true);
                        dispose(); // Cierra la ventana de login
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Usuario o clave incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                    }
                } else if ("Administrativo".equals(tipoUsuario)) {
                    autenticado = admin.autenticarAdministrativo(administrativo.getString("nombre"), clave);
                    if (autenticado) {
                        if (administrativo.getString("area").equals("Auxiliar")) {
                            VistaAuxiliar vistaAuxiliar = new VistaAuxiliar(rut, administrativo.getString("area"));
                            vistaAuxiliar.setTitle("Vista Auxiliar: " + administrativo.getString("nombre"));
                            vistaAuxiliar.setVisible(true);
                            dispose(); // Cierra la ventana de login
                        } else if (administrativo.getString("area").equals("Admision")) {
                            VistaAdmision vistaAdmision = new VistaAdmision(rut, administrativo.getString("area"));
                            vistaAdmision.setTitle("Vista Admision: " + administrativo.getString("nombre"));
                            vistaAdmision.setVisible(true);
                            dispose(); // Cierra la ventana de login
                        } else if (administrativo.getString("area").equals("Pabellon")) {
                            VistaPabellon vistaPabellon = new VistaPabellon(rut, administrativo.getString("area"));
                            vistaPabellon.setTitle("Vista Pabellon: " + administrativo.getString("nombre"));
                            vistaPabellon.setVisible(true);
                            dispose(); // Cierra la ventana de login
                        } else if (administrativo.getString("area").equals("Exámenes")) {
                            VistaExamenes vistaExamenes = new VistaExamenes(rut, administrativo.getString("area"));
                            vistaExamenes.setTitle("Vista Exaemenes: " + administrativo.getString("nombre"));
                            vistaExamenes.setVisible(true);
                            dispose(); // Cierra la ventana de login
                        }
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Usuario o clave incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                    }
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