package com.example.zonazero0;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<JsonObject> login(@Body LoginRequest loginRequest);

    @GET("solicitudes1")
    Call<List<Solicitud>> getSolicitudes1();

    @GET("solicitudes2")
    Call<List<Solicitud>> getSolicitudes2();

    @GET("solicitudes3")
    Call<List<Solicitud>> getSolicitudes3();

    @GET("solicitudes4/{branch_id}")
    Call<List<Solicitud>> getSolicitudes4(@Path("branch_id") int branch_id);

    @POST("solicitudes")
    Call<CrearSolicitud> crearSolicitud(@Body CrearSolicitud crearSolicitud);


    @POST("actualizarInventario")
    Call<Producto> actualizarInventario(@Body Producto producto);

    @GET("productosPorSucursal/{id}")
    Call<List<Producto>> getProductosPorSucursal(@Path("id") String idSucursal);

    @PUT("solicitudes/{id}")
    Call<JsonObject> updateSolicitud(@Path("id") int idSolicitud);

    @PUT("solicitudes/negative/{id}")
    Call<JsonObject> revertSolicitud(@Path("id") int idSolicitud);


}