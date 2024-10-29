package Frontend.Vistas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaAdmin extends JFrame {

    private JPanel panelPrincipal;
    private JList<String> listaUsuarios;
    private DefaultListModel<String> modeloListaUsuarios;
    private JTextField campoBusqueda;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonAgregar;

    

    public VistaAdmin() {
        crearVentaAdmin();
    }

    public void crearVentaAdmin() {
        setTitle("Administrador de usuarios");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelPrincipal = new JPanel();


        // crea la lista de usuarios
        modeloListaUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloListaUsuarios);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setLayoutOrientation(JList.VERTICAL);
        listaUsuarios.setVisibleRowCount(-1);
        JScrollPane listaUsuariosScroll = new JScrollPane(listaUsuarios);
        listaUsuariosScroll.setPreferredSize(new Dimension(250, 80));
        panelPrincipal.add(listaUsuariosScroll);

        // crea el campo de busqueda
        campoBusqueda = new JTextField(20);
        panelPrincipal.add(campoBusqueda);

        // crea el boton de busqueda
        botonBuscar = new JButton("Buscar");

        // crea el boton de eliminar
        botonEliminar = new JButton("Eliminar");
        
        // crea el boton de agregar
        botonAgregar = new JButton("Agregar");


        panelPrincipal.add(botonAgregar);
        panelPrincipal.add(botonEliminar);
        panelPrincipal.add(botonBuscar);

        add(panelPrincipal);
        setVisible(true);
    }

    public void addActionListener(ActionListener accion) {
        botonEliminar.addActionListener(accion);
        botonBuscar.addActionListener(accion);
    }


    public static void main(String[] args) {
        new VistaAdmin();
    }
}
