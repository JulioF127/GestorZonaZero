package com.example.zonazero0;

public class CrearUsuarioRequest {
    private String name;
    private String email;
    private String password;
    private String user_name;
    private int user_type;
    private int ID_sucursal;

    // Getters y setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getID_sucursal() {
        return ID_sucursal;
    }

    public void setID_sucursal(int ID_sucursal) {
        this.ID_sucursal = ID_sucursal;
    }
}
