package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearTiendas extends AppCompatActivity {

    private EditText nombreTiendaEditText, ubicacionEditText;
    private Spinner usuariosSpinner;
    private Button crearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tiendas);

        nombreTiendaEditText = findViewById(R.id.editText);
        ubicacionEditText = findViewById(R.id.editText4);
        usuariosSpinner = findViewById(R.id.userRoleSpinner2);
        crearButton = findViewById(R.id.button12);

        obtenerUsuarios();

        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaTienda();
            }
        });
    }

    private void obtenerUsuarios() {
        ApiService apiService = RetrofitClient.getApiService("http://tu_base_url.com/api/");

        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(CrearTiendas.this, android.R.layout.simple_spinner_dropdown_item, usuarios);
                    usuariosSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(CrearTiendas.this, "Error al cargar los usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(CrearTiendas.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearNuevaTienda() {
        String nombreTienda = nombreTiendaEditText.getText().toString();
        String ubicacion = ubicacionEditText.getText().toString();
        Usuario selectedUsuario = (Usuario) usuariosSpinner.getSelectedItem();
        int usuarioId = selectedUsuario.getId();

        JsonObject tiendaData = new JsonObject();
        tiendaData.addProperty("nombre_sucursal", nombreTienda);
        tiendaData.addProperty("ubicacion", ubicacion);
        tiendaData.addProperty("ID_encargado", usuarioId);

        ApiService apiService = RetrofitClient.getApiService("http://tu_base_url.com/api/");
        apiService.crearSucursal(tiendaData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearTiendas.this, "Tienda creada exitosamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CrearTiendas.this, "Error al crear la tienda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearTiendas.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainAdmin.class);
        startActivity(intent);
    }
}
