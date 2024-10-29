package Frontend.Vistas;


import Frontend.Controladores.ControladorCliente;
import javax.swing.*;

import Backend.Cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;


public class VistaCliente extends JFrame{
    private JScrollPane scroll;
    private JTextArea textArea;
    private JTextField textField;
    private JButton boton;
    private JPanel panelPrincipal;
    



    public VistaCliente() throws IOException{
        crearVentanaCliente();

    }

    private void crearVentanaCliente() {
        setTitle("Cliente");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelPrincipal = new JPanel();

        textField = new JTextField(20);
        panelPrincipal.add(textField);

        boton = new JButton("Enviar");
        panelPrincipal.add(boton);


        add(panelPrincipal);
        setVisible(true);
    }

    public String getTextoIngresado(){
        return textField.getText();
    }


    public void addActionListener(ActionListener accion){
        textField.addActionListener(accion);
        boton.addActionListener(accion);
    }

}
