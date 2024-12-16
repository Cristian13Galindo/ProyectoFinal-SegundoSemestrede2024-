package co.uptc.edu.model;


import org.bson.types.ObjectId;
import java.util.Date;

public class Transaction {
    private ObjectId id;
    private ObjectId budgetId;
    private ObjectId categoryId;
    private Date date;
    private double amount;
    private String description;
    private Date createdAt;

    public Transaction() {}

    public Transaction(ObjectId budgetId, ObjectId categoryId, Date date, double amount, String description, Date createdAt) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(ObjectId budgetId) {
        this.budgetId = budgetId;
    }

    public ObjectId getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(ObjectId categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
