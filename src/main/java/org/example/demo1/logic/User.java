package org.example.demo1.logic;

public class User {
    private String nameUser;
    private String password;

    public User() {
        super();
    }

    public User(String nameUser, String password) {
        super();
        this.nameUser = nameUser;
        this.password = password;
    }

    public String getNameUser() {
        return nameUser;
    }
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO [nameUser=" + nameUser + ", password=" + password + "]";
    }
}
