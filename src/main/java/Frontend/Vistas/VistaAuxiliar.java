package Frontend.Vistas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaAuxiliar extends JFrame {
    private JTextArea areaChatAuxiliar;
    private JTextField campoMensajeAuxiliar;
    private JButton botonEnviarMensajeAuxiliar;
    private String nombreUsuario;
    private String rolUsuario;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public VistaAuxiliar(String nombreUsuario, String rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelAuxiliar = new JPanel(new BorderLayout());
        areaChatAuxiliar = new JTextArea();
        areaChatAuxiliar.setEditable(false);
        campoMensajeAuxiliar = new JTextField();
        botonEnviarMensajeAuxiliar = new JButton("Enviar");

        panelAuxiliar.add(new JScrollPane(areaChatAuxiliar), BorderLayout.CENTER);
        JPanel panelInputAuxiliar = new JPanel(new BorderLayout());
        panelInputAuxiliar.add(campoMensajeAuxiliar, BorderLayout.CENTER);
        panelInputAuxiliar.add(botonEnviarMensajeAuxiliar, BorderLayout.EAST);
        panelAuxiliar.add(panelInputAuxiliar, BorderLayout.SOUTH);

        add(panelAuxiliar);

        conectarAlServidor();
        escucharMensajes();

        botonEnviarMensajeAuxiliar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje("Auxiliar", campoMensajeAuxiliar, areaChatAuxiliar);
            }
        });
    }

    private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 12345);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Conectado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharMensajes() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readUTF()) != null) {
                        System.out.println("Mensaje recibido: " + mensaje);
                        String[] partes = mensaje.split(":", 2);
                        if (partes.length == 2) {
                            String pestaña = partes[0];
                            String contenidoMensaje = partes[1];
                            if (pestaña.equals("Auxiliar")) {
                                mostrarMensajeAuxiliar(contenidoMensaje);
                            }
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void enviarMensaje(String pestaña, JTextField campoMensaje, JTextArea areaChat) {
        String mensaje = campoMensaje.getText();
        if (!mensaje.isEmpty()) {
            String horaActual = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String mensajeFormateado = "[" + horaActual + "] " + nombreUsuario + " (" + rolUsuario + "): " + mensaje;
            try {
                salida.writeUTF(pestaña + ":" + mensajeFormateado);
                System.out.println(entrada.readUTF());
                campoMensaje.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarMensajeAuxiliar(String mensaje) {
        areaChatAuxiliar.append(mensaje + "\n");
    }
}