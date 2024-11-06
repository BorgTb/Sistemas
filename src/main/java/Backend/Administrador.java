package Backend;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class Administrador {
    private Database data;

    public Administrador() {
        data = Database.getInstance();
    }

    public void crearCliente(String nombre, String rut, String correo, String clave, String tipoUsuario, String area) {
        MongoCollection<Document> coleccion;

        if ("Médico".equalsIgnoreCase(tipoUsuario)) {
            coleccion = data.getColeccion("Medicos");
            area=null;
        } else if ("Administrativo".equalsIgnoreCase(tipoUsuario)) {
            coleccion = data.getColeccion("Administrativos");
        } else {
            System.out.println("Tipo de usuario no válido.");
            return;
        }

        Document cliente = new Document("nombre", nombre)
                                .append("rut", rut)
                                .append("correo", correo)
                                .append("clave", clave)
                                .append("tipoUsuario", tipoUsuario)
                                .append("area", area);

        try {
            coleccion.insertOne(cliente);
            System.out.println("Cliente agregado exitosamente.");
        } catch (MongoException e) {
            System.out.println("Error al agregar cliente: " + e);
        }
    }
    public boolean autenticarMedico(String rut, String clave) {
        MongoCollection<Document> coleccion = data.getColeccion("Medicos");
        Document medico = coleccion.find(Filters.and(Filters.eq("rut", rut), Filters.eq("clave", clave))).first();
        return medico != null;
    }

    public boolean autenticarAdministrativo(String rut, String clave) {
        MongoCollection<Document> coleccion = data.getColeccion("Administrativos");
        Document administrativo = coleccion.find(Filters.and(Filters.eq("rut", rut), Filters.eq("clave", clave))).first();
        return administrativo != null;
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

    public Document retornarMedico(String rut) {
        MongoCollection<Document> collection = data.getColeccion("Medicos");
        Document cliente = collection.find(Filters.eq("rut", rut)).first();
        if (cliente != null) {
            return cliente;
        } else {
            System.out.println("Cliente con RUT " + rut + " no encontrado.");
            return null;
        }
    }
    
    public Document retornarAdministrativo(String rut) {
        MongoCollection<Document> collection = data.getColeccion("Administrativos");
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
