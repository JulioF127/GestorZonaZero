package com.example.zonazero0;

import java.util.Date;

public class Solicitud {

    private int ID_solicitud;
    private String nombre_producto;
    private int cantidad_solicitada;
    private int prioridad;
    private Date fecha_solicitud;
    private String estado;

    public Solicitud(int ID_solicitud, String nombre_producto, int cantidad_solicitada,
                     int prioridad, Date fecha_solicitud, String estado) {
        this.ID_solicitud = ID_solicitud;
        this.nombre_producto = nombre_producto;
        this.cantidad_solicitada = cantidad_solicitada;
        this.prioridad = prioridad;
        this.fecha_solicitud = fecha_solicitud;
        this.estado = estado;
    }

    public int getID_solicitud() {
        return ID_solicitud;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public int getCantidad_solicitada() {
        return cantidad_solicitada;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public String getEstado() {
        return estado;
    }
}