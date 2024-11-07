package Frontend.Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gestorArchivos {
        public void guardarChat(String grupo, String mensaje) {
        String fileName = grupo+".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> leerChats(String grupo) {
        List<String> chats = new ArrayList<>();
        String fileName = grupo + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chats.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chats;
    }
}
