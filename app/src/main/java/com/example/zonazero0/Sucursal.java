package com.example.zonazero0;

public class Sucursal {
    private int ID_sucursal;
    private String nombre_sucursal;
    private String ubicacion;
    private int ID_encargado;

    public int getID_sucursal() {
        return ID_sucursal;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    @Override
    public String toString() {
        return ID_sucursal + " - " + nombre_sucursal;
    }
}