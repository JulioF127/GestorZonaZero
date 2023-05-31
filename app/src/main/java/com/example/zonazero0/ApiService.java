package com.example.zonazero0;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<JsonObject> login(@Body LoginRequest loginRequest);
    /*@GET("ruta/a/tu/api")
    Call<List<Solicitud>> obtenerSolicitudes();*/
}