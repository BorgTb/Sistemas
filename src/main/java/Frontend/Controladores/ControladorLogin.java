package Frontend.Controladores;

import Frontend.Vistas.VistaAdmision;
import Frontend.Vistas.VistaAuxiliar;
import Frontend.Vistas.VistaExamenes;
import Frontend.Vistas.VistaLogin;
import Frontend.Vistas.VistaMedico;
import Frontend.Vistas.VistaPabellon;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.bson.Document;

import Backend.Administrador;

import java.awt.event.ActionEvent;


public class ControladorLogin implements ActionListener{
    VistaLogin  vistaLogin;


    public ControladorLogin() {
        this.vistaLogin =new VistaLogin();
        this.vistaLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Login":
                iniciarSesion();
                break;
            default:
                break;
        }
        
    }

    public void iniciarSesion() {
        String rut = vistaLogin.getRut();
        String clave = new String(vistaLogin.getClave());
        String tipoUsuario = vistaLogin.getTipoUsuario();
        Administrador admin = new Administrador();
        Document medico = admin.retornarMedico(rut);
        Document administrativo = admin.retornarAdministrativo(rut);
        boolean autenticado = false;
        if ("Médico".equals(tipoUsuario)) {
            if (medico != null) {
                autenticado = admin.autenticarMedico(rut, clave);
                if (autenticado) {
                    ControladorMedico controladorMedico = new ControladorMedico(rut, "Medico");
                    controladorMedico.iniciarVista();
                    vistaLogin.dispose(); // Cierra la ventana de login
                } else {
                    vistaLogin.mostrarMensaje("Usuario o clave incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                vistaLogin.mostrarMensaje("Usuario no encontrado", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } else if ("Administrativo".equals(tipoUsuario)) {
            if (administrativo != null) {
                autenticado = admin.autenticarAdministrativo(rut, clave);
                if (autenticado) {
                    switch (administrativo.getString("area")) {
                        case "Auxiliar" -> {
                            VistaAuxiliar vistaAuxiliar = new VistaAuxiliar(rut, administrativo.getString("area"));
                            vistaAuxiliar.setTitle("Vista Auxiliar: " + administrativo.getString("nombre"));
                            vistaAuxiliar.setVisible(true);
                            vistaLogin.dispose(); // Cierra la ventana de login
                        }
                        case "Admision" -> {
                            VistaAdmision vistaAdmision = new VistaAdmision(rut, administrativo.getString("area"));
                            vistaAdmision.setTitle("Vista Admision: " + administrativo.getString("nombre"));
                            vistaAdmision.setVisible(true);
                            vistaLogin.dispose(); // Cierra la ventana de login
                        }
                        case "Pabellon" -> {
                            VistaPabellon vistaPabellon = new VistaPabellon(rut, administrativo.getString("area"));
                            vistaPabellon.setTitle("Vista Pabellon: " + administrativo.getString("nombre"));
                            vistaPabellon.setVisible(true);
                            vistaLogin.dispose(); // Cierra la ventana de login
                        }
                        case "Examenes" -> {
                            VistaExamenes vistaExamenes = new VistaExamenes(rut, administrativo.getString("area"));
                            vistaExamenes.setTitle("Vista Examenes: " + administrativo.getString("nombre"));
                            vistaExamenes.setVisible(true);
                            vistaLogin.dispose();// Cierra la ventana de login
                        }
                        default -> {
                        }
                    }
                } else {
                    vistaLogin.mostrarMensaje("Usuario o clave incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                vistaLogin.mostrarMensaje("Usuario no encontrado", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    public void iniciarVista(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                vistaLogin.setVisible(true);
            }
        });
    }



}
