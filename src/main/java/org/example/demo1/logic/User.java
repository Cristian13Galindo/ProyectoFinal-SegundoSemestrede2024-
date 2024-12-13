package org.example.demo1.logic;

public class User {
    private String id;
    private String nameUser;
    private String email;
    private String password;

    public User() {
        super();
    }

    public User(String nameUser, String password) {
        super();
        this.nameUser = nameUser;
        this.password = password;
    }

    // Constructor
    public User(String id, String nameUser, String email, String password) {
        this.id = id;
        this.nameUser = nameUser;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // New methods to match UserDAO requirements
    public String getNombre() {
        return nameUser;
    }

    public String getContraseña() {
        return password;
    }

    @Override
    public String toString() {
        return "UserDTO [nameUser=" + nameUser + ", password=" + password + "]";
    }
}