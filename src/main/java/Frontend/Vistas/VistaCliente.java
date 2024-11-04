package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

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

    public VistaCliente() {
        // Inicializar componentes y configurar la interfaz gráfica
        areaTexto = new JTextArea();
        campoTexto = new JTextField();
        botonEnviar = new JButton("Enviar a Todos");

        // Configurar el layout y agregar componentes
        setLayout(new BorderLayout());
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(campoTexto, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    public void addActionListener(ActionListener listener) {
        botonEnviar.addActionListener(listener);
    }

    public String getTextoIngresado() {
        return campoTexto.getText();
    }

    public void limpiarCampo() {
        campoTexto.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    }
}