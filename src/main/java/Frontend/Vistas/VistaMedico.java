package Frontend.Vistas;

import javax.swing.JButton;

public class VistaMedico extends VentanaBase {
    private JButton botonEnviarUrgente;

    public VistaMedico() {
        super();
        botonEnviarUrgente = new JButton("Enviar Mensaje Urgente");
        panelPrincipal.add(botonEnviarUrgente);

        // Agregar funcionalidades específicas para médicos
    }
}