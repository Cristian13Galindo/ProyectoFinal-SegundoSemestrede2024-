// src/main/java/co/uptc/edu/utils/MongoDBUtil.java

package co.uptc.edu.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Clase de utilidad para manejar la conexión a MongoDB.
 * Utiliza el patrón Singleton para asegurar que solo exista una instancia de MongoClient.
 */
public class MongoDBUtil {
    // Instancia única de MongoClient
    private static MongoClient mongoClient = null;
    // Instancia única de MongoDatabase
    private static MongoDatabase database = null;

    // Constantes de configuración
    private static final String CONNECTION_STRING = "mongodb://127.0.0.1:27017"; // URI de conexión
    private static final String DATABASE_NAME = "ControlGastos"; // Nombre de la base de datos

    // Bloque estático para inicializar la conexión
    static {
        try {
            // Crear una instancia de MongoClient
            mongoClient = MongoClients.create(CONNECTION_STRING);
            // Seleccionar la base de datos
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conexión exitosa a la base de datos: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la instancia de MongoDatabase.
     * @return MongoDatabase correspondiente a DATABASE_NAME.
     */
    public static MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Obtiene la colección 'users' de la base de datos.
     * @return MongoCollection<Document> correspondiente a 'users'.
     */
    public static MongoCollection<Document> getUserCollection() {
        return database.getCollection("users");
    }

    /**
     * Obtiene la colección 'budgets' de la base de datos.
     * @return MongoCollection<Document> correspondiente a 'budgets'.
     */
    public static MongoCollection<Document> getBudgetsCollection() {
        return database.getCollection("budgets");
    }

    /**
     * Obtiene la colección 'categories' de la base de datos.
     * @return MongoCollection<Document> correspondiente a 'categories'.
     */
    public static MongoCollection<Document> getCategoriesCollection() {
        return database.getCollection("categories");
    }

    /**
     * Cierra la conexión con MongoDB.
     * Es recomendable llamar a este método al finalizar la aplicación.
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
}
