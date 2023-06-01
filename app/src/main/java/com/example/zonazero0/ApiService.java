package com.example.zonazero0;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

}