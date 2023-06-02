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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.Date;
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
    private Button btnActualizar;

    private static final long INTERVALO_COMPROBACION = 1 * 60 * 1000; // 1 minuto
    private Handler tokenHandler;
    private Runnable tokenRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        btnLogout = findViewById(R.id.button);
        btnSolicitud = findViewById(R.id.button2);
        btnActualizar = findViewById(R.id.button5);

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

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarSolicitudes();
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

        // Iniciar la verificación del token cada 5 minutos
        tokenHandler = new Handler();
        tokenRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isTokenValid()) {
                    logoutUser();
                    return;
                }

                tokenHandler.postDelayed(this, INTERVALO_COMPROBACION);
            }
        };
        tokenHandler.postDelayed(tokenRunnable, INTERVALO_COMPROBACION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la verificación del token cuando la actividad se destruye
        tokenHandler.removeCallbacks(tokenRunnable);
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
                    // Manejar respuesta no exitosa aquí
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

    private void actualizarSolicitudes() {
        int branch_id = decodeBranchId(sharedPreferences.getString("token", null));
        getSolicitudes(branch_id);
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
}
