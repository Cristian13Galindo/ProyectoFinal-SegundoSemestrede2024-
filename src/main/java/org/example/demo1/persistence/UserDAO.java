package org.example.demo1.persistence;

import org.example.demo1.logic.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO implements InterfaceDAO<User> {

    private final MongoCollection<Document> collection;

    public UserDAO(MongoDatabase database) {
        this.collection = database.getCollection("users");
    }

    @Override
    public void save(User user) {
        Document document = new Document("id", user.getId())
                .append("nombre", user.getNombre())
                .append("email", user.getEmail())
                .append("contraseña", user.getContraseña());
        collection.insertOne(document);
    }

    @Override
    public User findById(String id) {
        Document doc = collection.find(eq("id", id)).first();
        if (doc != null) {
            return new User(doc.getString("id"), doc.getString("nombre"), doc.getString("email"), doc.getString("contraseña"));
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(new User(doc.getString("id"), doc.getString("nombre"), doc.getString("email"), doc.getString("contraseña")));
        }
        return users;
    }

    @Override
    public void update(User user) {
        Document updatedDocument = new Document("id", user.getId())
                .append("nombre", user.getNombre())
                .append("email", user.getEmail())
                .append("contraseña", user.getContraseña());
        collection.replaceOne(eq("id", user.getId()), updatedDocument);
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(eq("id", id));
    }
}
