package com.example.zonazero0;

public class Producto {

    private String ID_producto;
    private String nombre_producto;
    private int cantidad_producto;

    public Producto(String ID_producto, String nombre_producto, int cantidad_producto) {
        this.ID_producto = ID_producto;
        this.nombre_producto = nombre_producto;
        this.cantidad_producto = cantidad_producto;
    }

    public String getID_producto() {
        return ID_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public int getCantidad_producto() {
        return cantidad_producto;
    }

}
