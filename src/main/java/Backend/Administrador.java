package Backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.bson.Document;

import Frontend.Controladores.gestorArchivos;

public class Administrador {
    private gestorArchivos gestorArchivos = new gestorArchivos();
    private DataOutputStream salida;

    public Administrador() {
    }

    public void guardarClienteEnArchivo(String nombre, String rut, String correo, String clave, String tipoUsuario, String area, String primerInicio) {
        String fileName = tipoUsuario.equalsIgnoreCase("Medico") ? "Medicos.txt" : "Administrativos.txt";
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        File directory = new File("./src/main/java/Users");
        if (tipoUsuario=="Medico"){
            area=null;
        }

        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Error al crear el directorio: " + directory.getAbsolutePath());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.format("Nombre: %s, RUT: %s, Correo: %s, Clave: %s, TipoUsuario: %s, Area: %s, PrimerInicio: %s", nombre, rut, correo, clave, tipoUsuario, area, primerInicio));
            writer.newLine();
            System.out.println("Cliente guardado en archivo: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean autenticarMedico(String rut, String clave) {
        return autenticarDesdeArchivo("Medicos.txt", rut, clave);
    }

    public boolean autenticarAdministrativo(String rut, String clave) {
        return autenticarDesdeArchivo("Administrativos.txt", rut, clave);
    }

    private boolean autenticarDesdeArchivo(String fileName, String rut, String clave) {
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 7) {
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
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 7) {
                    String fileRut = parts[1].split(": ")[1];
                    if (fileRut.equals(rut)) {
                        Document doc = new Document();
                        doc.append("nombre", parts[0].split(": ")[1]);
                        doc.append("rut", fileRut);
                        doc.append("correo", parts[2].split(": ")[1]);
                        doc.append("clave", parts[3].split(": ")[1]);
                        doc.append("tipoUsuario", parts[4].split(": ")[1]);
                        doc.append("area", parts[5].split(": ")[1].equals("null") ? null : parts[5].split(": ")[1]);
                        doc.append("primerInicio", Boolean.parseBoolean(parts[6].split(": ")[1])); // Agrega el campo PrimerInicio
                        return doc;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enviarMensajeUrgenteAChats(String mensajeUrgente, DataOutputStream salida) throws IOException {

        salida.writeUTF("URGENTE;"+mensajeUrgente);

        File directorioChats = new File("./src/main/java/Chats");
        if (!directorioChats.exists()) {
            System.out.println("El directorio de chats no existe.");
            return;
        }

        File[] archivosChats = directorioChats.listFiles((dir, name) -> name.endsWith(".txt"));
        if (archivosChats == null || archivosChats.length == 0) {
            System.out.println("No se encontraron archivos de chat.");
            return;
        }

        for (File archivoChat : archivosChats) {
            try (FileWriter escritor = new FileWriter(archivoChat, true)) {
                
                escritor.write("\n[MENSAJE URGENTE]: " + mensajeUrgente + "\n");
            } catch (IOException e) {
                System.out.println("Error al escribir en el archivo: " + archivoChat.getName());
                e.printStackTrace();
            }
        }
    }public void cambiarContrasenaMedico(String rut, String nuevaContrasena) {
        cambiarContrasenaEnArchivo("Medicos.txt", rut, nuevaContrasena);
    }
    
    public void cambiarContrasenaAdministrativo(String rut, String nuevaContrasena) {
        cambiarContrasenaEnArchivo("Administrativos.txt", rut, nuevaContrasena);
    }
    
    private void cambiarContrasenaEnArchivo(String fileName, String rut, String nuevaContrasena) {
        String filePath = Paths.get("./src/main/java/Users", fileName).toString();
        String tempFilePath = Paths.get("./src/main/java/Users", "temp_" + fileName).toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts[1].split(": ")[1].equals(rut)) {
                    parts[3] = "Clave: " + nuevaContrasena; // Actualiza la contraseña
                    parts[6] = "PrimerInicio: false"; // Actualiza el estado de primer inicio
                    writer.write(String.join(", ", parts) + "\n");
                } else {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Renombra el archivo temporal al nombre original
        new File(filePath).delete();
        new File(tempFilePath).renameTo(new File(filePath));
    }



}
