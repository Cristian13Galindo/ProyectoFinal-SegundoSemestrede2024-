package org.example.demo1.persistence;

import org.example.demo1.logic.Transaction;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class TransactionDAO implements InterfaceDAO<Transaction> {

    private final MongoCollection<Document> collection;

    public TransactionDAO(MongoDatabase database) {
        this.collection = database.getCollection("transactions");
    }

    @Override
    public void save(Transaction transaction) {
        Document document = new Document("id", transaction.getId())
                .append("monto", transaction.getMonto())
                .append("fecha", transaction.getFecha().toString())
                .append("descripcion", transaction.getDescripcion())
                .append("ubicacion", transaction.getUbicacion())
                .append("metodoPago", transaction.getMetodoPago());
        collection.insertOne(document);
    }

    @Override
    public Transaction findById(String id) {
        Document doc = collection.find(eq("id", id)).first();
        if (doc != null) {
            return new Transaction(
                    doc.getString("id"),
                    doc.getDouble("monto"),
                    doc.getString("fecha"),
                    doc.getString("descripcion"),
                    doc.getString("ubicacion"),
                    doc.getString("metodoPago")
            );
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        for (Document doc : collection.find()) {
            transactions.add(new Transaction(
                    doc.getString("id"),
                    doc.getDouble("monto"),
                    doc.getString("fecha"),
                    doc.getString("descripcion"),
                    doc.getString("ubicacion"),
                    doc.getString("metodoPago")
            ));
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        Document updatedDocument = new Document("id", transaction.getId())
                .append("monto", transaction.getMonto())
                .append("fecha", transaction.getFecha().toString())
                .append("descripcion", transaction.getDescripcion())
                .append("ubicacion", transaction.getUbicacion())
                .append("metodoPago", transaction.getMetodoPago());
        collection.replaceOne(eq("id", transaction.getId()), updatedDocument);
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(eq("id", id));
    }
}
