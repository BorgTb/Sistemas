package Frontend.Vistas;

import javax.swing.*;

public class Vistadministrativo extends VentanaBase {
    private JButton botonAgregarUsuario;

    public Vistadministrativo() {
        super();
        botonAgregarUsuario = new JButton("Agregar Usuario");
        panelPrincipal.add(botonAgregarUsuario);

        // Agregar funcionalidades específicas para administrativos
    }
}