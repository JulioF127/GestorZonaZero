package com.example.zonazero0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudPregunta extends AppCompatActivity {
    private Button btnLogout, btnIngresar;
    private EditText productoEditText, cantidadEditText, prioridadEditText;
    private SharedPreferences sharedPreferences;
    private static final String BASE_URL = "http://157.230.0.143:3000/api/";  // Reemplazar con tu URL base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_pregunta);

        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);


        btnLogout = findViewById(R.id.button3);
        btnIngresar = findViewById(R.id.button4);
        productoEditText = findViewById(R.id.productoEditText);
        cantidadEditText = findViewById(R.id.cantidadEditText);
        prioridadEditText = findViewById(R.id.prioridadEditText);
        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresarSolicitud();
            }
        });
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(SolicitudPregunta.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void ingresarSolicitud() {
        String producto = productoEditText.getText().toString();
        int id = sharedPreferences.getInt("id", 1);
        int cantidad = Integer.parseInt(cantidadEditText.getText().toString());
        int prioridad = Integer.parseInt(prioridadEditText.getText().toString());
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());

        Log.e("SolicitudPregunta", "Valor de Id: " + id);

        CrearSolicitud solicitud = new CrearSolicitud(producto, id, cantidad, prioridad, fecha);

        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<CrearSolicitud> call = apiService.crearSolicitud(solicitud);
        call.enqueue(new Callback<CrearSolicitud>() {

            @Override
            public void onResponse(Call<CrearSolicitud> call, Response<CrearSolicitud> response) {
                if (response.isSuccessful()) {
                    String successMessage = "Solicitud ingresada con éxito";
                    Toast.makeText(SolicitudPregunta.this, successMessage, Toast.LENGTH_LONG).show();
                    Log.d("SolicitudPregunta", successMessage);
                } else {
                    // Manejar la respuesta fallida
                    String errorMessage = "Error en la respuesta de la API";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("SolicitudPregunta", "Error al obtener el cuerpo de error", e);
                    }
                    Toast.makeText(SolicitudPregunta.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("SolicitudPregunta", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<CrearSolicitud> call, Throwable t) {
                Toast.makeText(SolicitudPregunta.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}