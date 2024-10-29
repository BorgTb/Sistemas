package Backend;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class Administrador {
    Database data = Database.getInstance();
    
    public Administrador() {
        System.out.println("Administrador creado");
    }


    /*
    FALTA VALIDAR:
    - SI UN CLIENTE YA EXISTE, O SI SE INGRESA UN CLIENTE CON EL MISMO RUT, ETC..
    - 
    */
    @SuppressWarnings("unchecked")
    public void crearCliente() {
        @SuppressWarnings("rawtypes")
        MongoCollection collection = data.getColeccion("Clientes");
        Document document = new Document("_id","1")
        .append("nombre","Juan").
        append("rut","211992204").
        append("correo","elpepe@gmail.com").
        append("clave", "1234");

        collection.insertOne(document);
    }

    public void leerCliente() {
        @SuppressWarnings("rawtypes")
        MongoCollection collection = data.getColeccion("Clientes");
        Document document = (Document) collection.find().first();
        System.out.println(document.toJson());
    } 

    public void eliminarCliente(String rut) {
        @SuppressWarnings("rawtypes")
        MongoCollection collection = data.getColeccion("Clientes");
        Document query = new Document("rut", rut);
        collection.deleteOne(query);
        System.out.println("Cliente con RUT " + rut + " eliminado.");
    }

    public Document retornarCliente(String rut) {
        @SuppressWarnings("rawtypes")
        MongoCollection collection = data.getColeccion("Clientes");
        Document query = new Document("rut", rut);
        Document cliente = (Document) collection.find(query).first();
        if (cliente != null) {
            return cliente;
        } else {
            System.out.println("Cliente con RUT " + rut + " no encontrado.");
            return null;
        }
    }



    //opcional un metodo para actualizar al cliente



    public static void main(String[] args) {
        Administrador admin = new Administrador();
        //admin.crearCliente();
        //admin.leerCliente();
        admin.leerCliente();
    }

}
