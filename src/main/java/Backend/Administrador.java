package Backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.bson.Document;

public class Administrador {
    public Administrador() {
    }

    public void guardarClienteEnArchivo(String nombre, String rut, String correo, String clave, String tipoUsuario, String area) {
        String fileName = tipoUsuario.equalsIgnoreCase("Medico") ? "Medicos.txt" : "Administrativos.txt";
        String filePath = Paths.get("Sistemas/src/main/java/Users", fileName).toString();
        File directory = new File("Sistemas/src/main/java/Users");
        if (tipoUsuario=="Medico"){
            area=null;
        }
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.format("Nombre: %s, RUT: %s, Correo: %s, Clave: %s, TipoUsuario: %s, Area: %s", nombre, rut, correo, clave, tipoUsuario, area));
            writer.newLine();
            System.out.println("Cliente guardado en archivo: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean autenticarMedico(String rut, String clave) {
        System.out.println("medico");
        return autenticarDesdeArchivo("Medicos.txt", rut, clave);
    }

    public boolean autenticarAdministrativo(String rut, String clave) {
        System.out.println("Administrativos");
        return autenticarDesdeArchivo("Administrativos.txt", rut, clave);
    }

    private boolean autenticarDesdeArchivo(String fileName, String rut, String clave) {
        String filePath = Paths.get("Sistemas/src/main/java/Users", fileName).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String fileRut = parts[1].split(": ")[1];
                    String fileClave = parts[3].split(": ")[1];
                    if (fileRut.equals(rut) && fileClave.equals(clave)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return false;
    }

    public Document retornarMedico(String rut) {
        return retornarDesdeArchivo("Medicos.txt", rut);
    }

    public Document retornarAdministrativo(String rut) {
        return retornarDesdeArchivo("Administrativos.txt", rut);
    }

    private Document retornarDesdeArchivo(String fileName, String rut) {
        String filePath = Paths.get("Sistemas/src/main/java/Users", fileName).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String fileRut = parts[1].split(": ")[1];
                    if (fileRut.equals(rut)) {
                        Document doc = new Document();
                        doc.append("nombre", parts[0].split(": ")[1]);
                        doc.append("rut", fileRut);
                        doc.append("correo", parts[2].split(": ")[1]);
                        doc.append("clave", parts[3].split(": ")[1]);
                        doc.append("tipoUsuario", parts[4].split(": ")[1]);
                        doc.append("area", parts[5].split(": ")[1].equals("null") ? null : parts[5].split(": ")[1]);
                        System.out.println(doc);
                        return doc;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
