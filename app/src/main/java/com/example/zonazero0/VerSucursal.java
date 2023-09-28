package com.example.zonazero0;

public class VerSucursal {
    private int ID_sucursal;
    private String nombre_sucursal;
    private String ubicacion;
    private int ID_encargado;

    // Constructor
    public VerSucursal(int ID_sucursal, String nombre_sucursal, String ubicacion, int ID_encargado) {
        this.ID_sucursal = ID_sucursal;
        this.nombre_sucursal = nombre_sucursal;
        this.ubicacion = ubicacion;
        this.ID_encargado = ID_encargado;
    }

    // Getters
    public int getID_sucursal() {
        return ID_sucursal;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getID_encargado() {
        return ID_encargado;
    }

    // Setters
    public void setID_sucursal(int ID_sucursal) {
        this.ID_sucursal = ID_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setID_encargado(int ID_encargado) {
        this.ID_encargado = ID_encargado;
    }
}
