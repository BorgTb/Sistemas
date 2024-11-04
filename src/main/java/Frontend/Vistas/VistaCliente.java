package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException; // Asegúrate de importar ActionListener

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaCliente extends JFrame {
    private JTextArea areaTexto;
    private JTextField campoTexto;
    private JButton botonEnviar;
    private JButton botonEnviarPrivado;
    private JButton botonGrupo;
    private JButton botonGrupoUnirse;
    private JButton botonGrupoEnviar;
    private JPanel panel;

    public VistaCliente() throws IOException {
        crearVentanaCliente();
    }

    private void crearVentanaCliente() {
        setTitle("Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel();
        areaTexto = new JTextArea(20, 50);
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        campoTexto = new JTextField(40);
        botonEnviar = new JButton("Enviar a Todos");
        botonEnviarPrivado = new JButton("Enviar Privado");
        botonGrupo = new JButton("Crear Grupo");
        botonGrupoUnirse = new JButton("Unirse a Grupo");
        botonGrupoEnviar = new JButton("Enviar a Grupo");

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(campoTexto, BorderLayout.SOUTH);
        panel.add(botonEnviar, BorderLayout.SOUTH);
        panel.add(botonEnviarPrivado, BorderLayout.SOUTH);
        panel.add(botonGrupo, BorderLayout.SOUTH);
        panel.add(botonGrupoUnirse, BorderLayout.SOUTH);
        panel.add(botonGrupoEnviar, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    }

    public void limpiarCampo() {
        campoTexto.setText("");
    }

    public String getTextoIngresado() {
        return campoTexto.getText();
    }

    public void addActionListener(ActionListener listener) {
        botonEnviar.addActionListener(listener);
        botonEnviarPrivado.addActionListener(listener);
        botonGrupo.addActionListener(listener);
        botonGrupoUnirse.addActionListener(listener);
        botonGrupoEnviar.addActionListener(listener);
    }

    public static void main(String[] args) {
        try {
            VistaCliente vista = new VistaCliente();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
