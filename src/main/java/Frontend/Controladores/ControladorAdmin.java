package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Asumiendo que tienes un administrador para manejar la lógica

import Backend.Administrador;
import Frontend.Vistas.VistaAdmin;

// En ControladorAdmin.java
public class ControladorAdmin {
    private VistaAdmin vista;
    private Administrador administrador;

    public ControladorAdmin() {
        vista = new VistaAdmin();
        administrador = new Administrador();
        vista.addAgregarUsuarioListener(new AgregarUsuarioListener());
        vista.addEnviarUrgenteListener(new EnviarUrgenteListener());
        vista.addVerEstadisticasListener(new VerEstadisticasListener());
    }

    private class AgregarUsuarioListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = vista.getNombre();
            String rut = vista.getRut();
            String correo = vista.getCorreo();
            String clave = vista.getClave();
            String tipoUsuario = vista.getTipoUsuario();

            if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty() || clave.isEmpty() || tipoUsuario.isEmpty()) {
                System.out.println("Todos los campos deben estar llenos.");
                return;
            }

            administrador.crearCliente(nombre, rut, correo, clave, tipoUsuario);
            vista.limpiarCampos();
        }
    }

    private class EnviarUrgenteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para el botón Enviar Urgente
            System.out.println("Enviar Urgente presionado");
        }
    }

    private class VerEstadisticasListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para el botón Ver Estadísticas
            System.out.println("Ver Estadísticas presionado");
        }
    }
}
