package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SolicitudAdapter solicitudAdapter;
    private SharedPreferences sharedPreferences;

    private Button btnLogout;
    private Button btnSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        btnLogout = findViewById(R.id.button); // Inicializa el botón btnLogout
        btnSolicitud = findViewById(R.id.button2); // Inicializa el botón btnSolicitud

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSolicitudActivity();
            }
        });

        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int userType = sharedPreferences.getInt("userType", -1);
        int branch_id = decodeBranchId(token);

        // Si no hay token guardado o el tipo de usuario no es 2, redirige a MainActivity
        if (token == null || userType != 2) {
            logoutUser();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getSolicitudes(branch_id);
    }

    private void getSolicitudes(int branch_id) {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);

        Call<List<Solicitud>> call = apiService.getSolicitudes4(branch_id);
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    List<Solicitud> solicitudes = response.body();
                    solicitudAdapter = new SolicitudAdapter(solicitudes);
                    recyclerView.setAdapter(solicitudAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                // Manejar error de conexión aquí
            }
        });
    }

    private int decodeBranchId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }
        String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
        return payloadJson.get("branch_id").getAsInt();
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(MainUser.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSolicitudActivity() {
        Intent intent = new Intent(MainUser.this, SolicitudPregunta.class);
        startActivity(intent);
    }
}
