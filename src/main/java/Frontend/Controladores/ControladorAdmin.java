package Frontend.Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Asumiendo que tienes un administrador para manejar la lógica

import Backend.Administrador;
import Frontend.Vistas.VistaAdmin;

public class ControladorAdmin implements ActionListener {
    private VistaAdmin vista;
    private Administrador administrador; // Clase que maneja la lógica de usuario

    public ControladorAdmin() {
        vista = new VistaAdmin();
        administrador = new Administrador();
        vista.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        // Recopila los datos de la vista
        String nombre = vista.getNombre();
        String rut = vista.getRut();
        String correo = vista.getCorreo();
        String clave = vista.getClave();
        String tipoUsuario = vista.getTipoUsuario();
    
        // Validar campos
        if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty() || clave.isEmpty() || tipoUsuario.isEmpty()) {
            System.out.println("Todos los campos deben estar llenos.");
            return;
        }else{
            System.out.println("hola");
        }
    
        // Intentar crear el cliente
        administrador.crearCliente(nombre, rut, correo, clave, tipoUsuario);
    }
    
}
