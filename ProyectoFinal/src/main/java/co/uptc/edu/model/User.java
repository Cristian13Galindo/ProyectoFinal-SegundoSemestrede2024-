// src/main/java/co/uptc/edu/models/User.java

package co.uptc.edu.model;

public class User {
    private String username;
    private String email;
    private String passwordHash;

    // Constructor
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() { // Método correcto
        return passwordHash;
    }

    // Setters (si es necesario)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
