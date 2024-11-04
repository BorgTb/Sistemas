package Frontend.Vistas;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class VentanaBase extends JFrame {
    protected JPanel panelPrincipal;
    protected JTextArea areaTextoUsuarios;
    protected JTextArea areaTextoEstadisticas;

    public VentanaBase() {
        panelPrincipal = new JPanel();
        areaTextoUsuarios = new JTextArea(10, 40);
        areaTextoEstadisticas = new JTextArea(10, 40);
        JScrollPane scrollUsuarios = new JScrollPane(areaTextoUsuarios);
        JScrollPane scrollEstadisticas = new JScrollPane(areaTextoEstadisticas);

        areaTextoUsuarios.setEditable(false);
        areaTextoEstadisticas.setEditable(false);

        add(panelPrincipal, BorderLayout.NORTH);
        add(scrollUsuarios, BorderLayout.CENTER);
        add(scrollEstadisticas, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Métodos comunes
}