package Frontend.Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class gestorArchivos {
    private String fileDirectory = "Sistemas/src/main/java/Chats";

    public void guardarChat(String grupo, String mensaje) {
        String fileName = grupo + ".txt";
        String filePath = Paths.get("Sistemas/src/main/java/Chats", fileName).toString();
        File directory = new File("Sistemas/src/main/java/Chats");

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(mensaje);
            writer.newLine();
            System.out.println("Message written to file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> leerChats(String grupo) {
        List<String> chats = new ArrayList<>();
        String fileName = grupo + ".txt";
        String filePath = Paths.get("Sistemas/src/main/java/Chats", fileName).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chats.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chats;
    }

    public List<String> leerChatsPrivados(String emisor, String receptor) {
        List<String> chats = new ArrayList<>();

        String fileName = fileDirectory + "/" + emisor + "-" + receptor + ".txt";

        if (!Paths.get(fileName).toFile().exists()) {
            fileName = fileDirectory + "/" + receptor + "-" + emisor + ".txt";
        }

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