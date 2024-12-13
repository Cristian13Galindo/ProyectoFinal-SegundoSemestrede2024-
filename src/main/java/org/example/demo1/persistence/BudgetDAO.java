package org.example.demo1.persistence;

import org.example.demo1.logic.Budget;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class BudgetDAO implements InterfaceDAO<Budget> {

    private final MongoCollection<Document> collection;

    public BudgetDAO(MongoDatabase database) {
        this.collection = database.getCollection("budgets");
    }

    @Override
    public void save(Budget budget) {
        Document document = new Document("id", budget.getId())
                .append("montoTotal", budget.getMontoTotal())
                .append("fechaInicio", budget.getFechaInicio().toString())
                .append("fechaFin", budget.getFechaFin().toString())
                .append("tipoPeriodo", budget.getTipoPeriodo());
        collection.insertOne(document);
    }

    @Override
    public Budget findById(String id) {
        Document doc = collection.find(eq("id", id)).first();
        if (doc != null) {
            return new Budget(
                    doc.getString("id"),
                    doc.getDouble("montoTotal"),
                    doc.getString("fechaInicio"),
                    doc.getString("fechaFin"),
                    doc.getString("tipoPeriodo")
            );
        }
        return null;
    }

    @Override
    public List<Budget> findAll() {
        List<Budget> budgets = new ArrayList<>();
        for (Document doc : collection.find()) {
            budgets.add(new Budget(
                    doc.getString("id"),
                    doc.getDouble("montoTotal"),
                    doc.getString("fechaInicio"),
                    doc.getString("fechaFin"),
                    doc.getString("tipoPeriodo")
            ));
        }
        return budgets;
    }

    @Override
    public void update(Budget budget) {
        Document updatedDocument = new Document("id", budget.getId())
                .append("montoTotal", budget.getMontoTotal())
                .append("fechaInicio", budget.getFechaInicio().toString())
                .append("fechaFin", budget.getFechaFin().toString())
                .append("tipoPeriodo", budget.getTipoPeriodo());
        collection.replaceOne(eq("id", budget.getId()), updatedDocument);
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(eq("id", id));
    }
}
