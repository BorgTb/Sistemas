package Frontend.Vistas;


import Backend.Administrador;
import Frontend.Controladores.ControladorLogin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class VistaCambiarContra {

    public static class VistaCambiarContraFrame extends JFrame {
        private JPasswordField passwordField;
        private JPasswordField confirmPasswordField;
        private JButton changePasswordButton;
        private String rut;
        private String tipoUsuario;
        private ControladorLogin controladorLogin;

        public VistaCambiarContraFrame() {
            setTitle("Cambiar Contraseña");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(3, 2));

            JLabel passwordLabel = new JLabel("Nueva Contraseña:");
            passwordField = new JPasswordField();
            JLabel confirmPasswordLabel = new JLabel("Confirmar Contraseña:");
            confirmPasswordField = new JPasswordField();
            changePasswordButton = new JButton("Cambiar Contraseña");

            add(passwordLabel);
            add(passwordField);
            add(confirmPasswordLabel);
            add(confirmPasswordField);
            add(new JLabel()); // Empty cell
            add(changePasswordButton);

            changePasswordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cambiarContrasena();
                }
            });
        }

        public void setRut(String rut) {
            this.rut = rut;
        }

        public void setTipoUsuario(String tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
        }

        public void setControladorLogin(ControladorLogin controladorLogin) {
            this.controladorLogin = controladorLogin;
        }

        private void cambiarContrasena() {
            String nuevaContrasena = new String(passwordField.getPassword());
            String confirmarContrasena = new String(confirmPasswordField.getPassword());

            if (nuevaContrasena.equals(confirmarContrasena)) {
                // Aquí puedes agregar la lógica para cambiar la contraseña en tu sistema
                Administrador admin = new Administrador();
                if ("Médico".equals(tipoUsuario)) {
                    admin.cambiarContrasenaMedico(rut, nuevaContrasena);
                } else if ("Administrativo".equals(tipoUsuario)) {
                    admin.cambiarContrasenaAdministrativo(rut, nuevaContrasena);
                }
                JOptionPane.showMessageDialog(this, "Contraseña cambiada con éxito.");
                controladorLogin.volverALogin();
                dispose(); // Cierra la ventana de cambiar contraseña
            } else {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden. Inténtalo de nuevo.");
            }
        }
    }
}