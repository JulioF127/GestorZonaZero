package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdmin extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private SolicitudAdapter solicitudAdapter1, solicitudAdapter2, solicitudAdapter3;
    private List<Solicitud> listaSolicitudes1, listaSolicitudes2, listaSolicitudes3;
    private Button btnLogout;
    private SharedPreferences sharedPreferences;


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



        obtenerSolicitudes1();
        obtenerSolicitudes2();
        obtenerSolicitudes3();
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


    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.apply();

        Intent intent = new Intent(MainAdmin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
