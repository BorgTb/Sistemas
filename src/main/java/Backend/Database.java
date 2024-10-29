package Backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Database {
    private static Database instance; // Instancia única
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private static final String URI = "mongodb+srv://admin123:123admin123@data.lohu3.mongodb.net/?retryWrites=true&w=majority&appName=Data";
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
    public MongoCollection getColeccion(String nombreColeccion) {
        return database.getCollection(nombreColeccion);
    }


   
}

