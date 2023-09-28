package com.example.zonazero0;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    // Rutas para sucursales
    @POST("sucursales")
    Call<JsonObject> crearSucursal(@Body JsonObject data);

    @GET("sucursales")
    Call<List<Sucursal>> getSucursales();

    @GET("sucursales")
    Call<List<VerSucursal>> getSucursales1();

    @PUT("sucursales")
    Call<JsonObject> actualizarSucursal(@Body JsonObject data);

    @DELETE("sucursales/:id")
    Call<Void> eliminarSucursal(@Path("id") int idSucursal);

    // Rutas para productos
    @POST("productos")
    Call<JsonObject> crearProducto(@Body CrearProductoRequest request);

    @GET("productos")
    Call<List<JsonObject>> getProductos();

    @GET("productos")
    Call<List<VerProductos>> getProductos1();

    @PUT("productos")
    Call<JsonObject> actualizarProducto(@Body JsonObject data);

    @DELETE("productos/{id}")
    Call<Void> eliminarProducto(@Path("id") int idProducto);

    // Rutas para usuarios
    @POST("users")
    Call<JsonObject> crearUsuario(@Body CrearUsuarioRequest request);

    @GET("users")
    Call<List<Usuario>> getUsuarios();

    @GET("users")
    Call<List<VerUsuarios>> getUsuarios1();
    @PUT("users")
    Call<JsonObject> actualizarUsuario(@Body JsonObject data);

    @DELETE("users")
    Call<Void> eliminarUsuario();

    @DELETE("users/{id}")
    Call<Void> eliminarUsuario(@Path("id") String id);

}
