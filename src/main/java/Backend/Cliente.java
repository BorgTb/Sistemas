package Backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Cliente implements Runnable{
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Server servidor;
    private Socket socket;


    public Cliente(Socket socket, Server servidor) throws IOException{
        this.socket = socket;
        this.servidor = servidor;
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());
    }


    @Override
    public void run() {
        try{
            String mensaje = entrada.readUTF();
            System.out.println(mensaje);

        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }
    
}
