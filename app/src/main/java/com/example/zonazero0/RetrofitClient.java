package com.example.zonazero0;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase necesaria para las solicitudes http
public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService(String BASE_URL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
