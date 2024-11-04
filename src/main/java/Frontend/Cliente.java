package Frontend;

import java.net.Socket;

import Frontend.Controladores.ControladorCliente;


public class Cliente {
        private Socket socket;

        public static void main(String[] args) {
            new Cliente();
        }

        public Cliente() {
            try {
                socket = new Socket("localhost", 5000);
                System.out.println("Connected to server on port " + 5000);
                ControladorCliente controlador = new ControladorCliente(socket);
                
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
}