// src/main/java/co/uptc/edu/model/Budget.java
package co.uptc.edu.model;

import java.util.Date;
import java.util.List;

public class Budget {
    private String budgetId;
    private String userId;
    private String periodType;
    private Date periodStart;
    private Date periodEnd;
    private double totalAmount;
    private List<CategoryAllocation> categories;
    private Date createdAt;
    private Date updatedAt;

    // Constructores, Getters y Setters

    public Budget() {}

    public Budget(String budgetId, String userId, String periodType, Date periodStart, Date periodEnd,
                  double totalAmount, List<CategoryAllocation> categories, Date createdAt, Date updatedAt) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalAmount = totalAmount;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CategoryAllocation> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryAllocation> categories) {
        this.categories = categories;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

class CategoryAllocation {
    private String categoryId;
    private String name;
    private double allocatedAmount;
    private double spentAmount;
    private double threshold;

    // Constructores, Getters y Setters

    public CategoryAllocation() {}

    public CategoryAllocation(String categoryId, String name, double allocatedAmount, double spentAmount, double threshold) {
        this.categoryId = categoryId;
        this.name = name;
        this.allocatedAmount = allocatedAmount;
        this.spentAmount = spentAmount;
        this.threshold = threshold;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
