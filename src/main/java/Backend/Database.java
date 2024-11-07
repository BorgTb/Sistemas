package Backend;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Database {
    private static Database instance; // Instancia única
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private static final String URI = "mongodb://admin123:123admin123@data-shard-00-00.lohu3.mongodb.net:27017,data-shard-00-01.lohu3.mongodb.net:27017,data-shard-00-02.lohu3.mongodb.net:27017/?ssl=true&replicaSet=atlas-2z76ir-shard-0&authSource=admin&retryWrites=true&w=majority&appName=Data";
    private static final String DATABASE_NAME = "Data";

    // Constructor privado para evitar instanciación directa
    private Database() {
        mongoClient = MongoClients.create(URI);
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

     // Método para obtener la instancia única de la conexión
     public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Método para obtener una colección de la base de datos
    @SuppressWarnings("rawtypes")
    public MongoCollection<Document> getColeccion(String nombreColeccion) {
        return database.getCollection(nombreColeccion);
    }

    public List<Document> getMedicos() {
        MongoCollection<Document> collection = getColeccion("Medicos");
        List<Document> medicos = collection.find().into(new ArrayList<>());
        if (medicos.isEmpty()) {
            System.out.println("No se encontraron médicos.");
        }
        return medicos;
    }

   
}

