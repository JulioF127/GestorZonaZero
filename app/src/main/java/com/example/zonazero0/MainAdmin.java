package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zonazero0.RetrofitClient;
import com.example.zonazero0.Solicitud;
import com.example.zonazero0.SolicitudAdapter;
import com.example.zonazero0.ApiService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdmin extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private SolicitudAdapter solicitudAdapter1, solicitudAdapter2, solicitudAdapter3;
    private List<Solicitud> listaSolicitudes1, listaSolicitudes2, listaSolicitudes3;
    private Button btnLogout, btnActualizar;
    private SharedPreferences sharedPreferences;

    private static final long INTERVALO_COMPROBACION = 1 * 60 * 1000; // 5 minutos en milisegundos
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int userType = sharedPreferences.getInt("userType", -1);

        if (token == null || userType != 1) {
            logoutUser();
            return;
        }

        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        btnLogout = findViewById(R.id.btn_logout);
        btnActualizar = findViewById(R.id.button6);

        listaSolicitudes1 = new ArrayList<>();
        listaSolicitudes2 = new ArrayList<>();
        listaSolicitudes3 = new ArrayList<>();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);

        solicitudAdapter1 = new SolicitudAdapter(listaSolicitudes1);
        solicitudAdapter2 = new SolicitudAdapter(listaSolicitudes2);
        solicitudAdapter3 = new SolicitudAdapter(listaSolicitudes3);

        recyclerView1.setAdapter(solicitudAdapter1);
        recyclerView2.setAdapter(solicitudAdapter2);
        recyclerView3.setAdapter(solicitudAdapter3);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarSolicitudes();
            }
        });

        obtenerSolicitudes1();
        obtenerSolicitudes2();
        obtenerSolicitudes3();


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isTokenValid()) {
                    logoutUser();
                    return;
                }

                // Programa la proxima comprobacion despues de 5 minutos
                handler.postDelayed(this, INTERVALO_COMPROBACION);
            }
        };
        handler.postDelayed(runnable, INTERVALO_COMPROBACION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la ejecución del Runnable cuando la actividad se destruye
        handler.removeCallbacks(runnable);
    }

    private boolean isTokenValid() {
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            return false; // No hay token, no es válido
        }

        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            return false; // Token inválido
        }

        String tokenPayload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(tokenPayload).getAsJsonObject();

        long expirationTime = payloadJson.get("exp").getAsLong();

        // Ajustar la hora local del dispositivo a la hora de Nueva York
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Guatemala"));
        String newYorkTime = sdf.format(new Date(expirationTime * 1000));

        try {
            Date newYorkDate = sdf.parse(newYorkTime);
            long currentTime = System.currentTimeMillis();

            return newYorkDate.getTime() > currentTime; // El token es válido si la hora de expiración es posterior a la hora actual ajustada a Nueva York
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Error al parsear la fecha, token inválido
        }
    }

    private void obtenerSolicitudes1() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes1();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes1.clear();
                    listaSolicitudes1.addAll(response.body());
                    solicitudAdapter1.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerSolicitudes2() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes2();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes2.clear();
                    listaSolicitudes2.addAll(response.body());
                    solicitudAdapter2.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerSolicitudes3() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes3();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes3.clear();
                    listaSolicitudes3.addAll(response.body());
                    solicitudAdapter3.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void actualizarSolicitudes() {
        obtenerSolicitudes1();
        obtenerSolicitudes2();
        obtenerSolicitudes3();
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(MainAdmin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
