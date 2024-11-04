package Backend;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class Administrador {
    private Database data = Database.getInstance();

    public Administrador() {
        System.out.println("Administrador creado");
    }

    // Método para crear un cliente validando si el RUT ya existe
    @SuppressWarnings("unchecked")
    public void crearCliente(String nombre, String rut, String correo, String clave, String tipoUsuario) {
    try {
        MongoCollection<Document> collection = data.getColeccion("Clientes");
        Document document = new Document("_id", rut) // Usando RUT como ID único
                .append("nombre", nombre)
                .append("correo", correo)
                .append("clave", clave)
                .append("tipo", tipoUsuario); // Almacenar el tipo de usuario
        collection.insertOne(document); // Insertar en la colección
        System.out.println("Cliente creado: " + nombre + " Tipo: " + tipoUsuario);
    } catch (MongoException e) {
        System.out.println("Error al insertar el cliente: " + e.getMessage());
    }
}


    public void leerCliente() {
        MongoCollection<Document> collection = data.getColeccion("Clientes");
        Document document = collection.find().first();
        System.out.println(document != null ? document.toJson() : "No se encontró ningún cliente.");
    }

    public void eliminarCliente(String rut) {
        MongoCollection<Document> collection = data.getColeccion("Clientes");
        collection.deleteOne(Filters.eq("rut", rut));
        System.out.println("Cliente con RUT " + rut + " eliminado.");
    }

    public Document retornarCliente(String rut) {
        MongoCollection<Document> collection = data.getColeccion("Clientes");
        Document cliente = collection.find(Filters.eq("rut", rut)).first();
        if (cliente != null) {
            return cliente;
        } else {
            System.out.println("Cliente con RUT " + rut + " no encontrado.");
            return null;
        }
    }

    public static void main(String[] args) {
        Administrador admin = new Administrador();
        admin.leerCliente();
    }
}
