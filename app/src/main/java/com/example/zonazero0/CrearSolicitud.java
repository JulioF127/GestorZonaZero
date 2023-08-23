package com.example.zonazero0;

//Clase para el esquema de crear solicitudes
public class CrearSolicitud {
    private String ID_producto;
    private int id;
    private int cantidad_solicitada;
    private int prioridad;
    private String fecha_solicitud;

    public CrearSolicitud(String ID_producto, int id, int cantidad_solicitada, int prioridad, String fecha_solicitud) {
        this.ID_producto = ID_producto;
        this.id = id;
        this.cantidad_solicitada = cantidad_solicitada;
        this.prioridad = prioridad;
        this.fecha_solicitud = fecha_solicitud;
    }
}