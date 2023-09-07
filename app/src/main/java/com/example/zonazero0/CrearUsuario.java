package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;

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

public class CrearUsuario extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, userNameEditText;
    private Spinner userTypeSpinner, sucursalSpinner;
    private Button crearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        nameEditText = findViewById(R.id.editText);
        emailEditText = findViewById(R.id.editText2);
        passwordEditText = findViewById(R.id.editText3);
        userNameEditText = findViewById(R.id.editText4);
        userTypeSpinner = findViewById(R.id.userRoleSpinner);
        sucursalSpinner = findViewById(R.id.userRoleSpinner2);
        crearButton = findViewById(R.id.button12);

        // Configuración del spinner para roles de usuario
        String[] roles = new String[]{"Admin", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        userTypeSpinner.setAdapter(adapter);

        // Obtener las sucursales del servidor
        obtenerSucursales();

        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevoUsuario();
            }
        });
    }

    private void obtenerSucursales() {
        ApiService apiService = RetrofitClient.getApiService("http://157.230.0.143:3000/api/");

        apiService.getSucursales().enqueue(new Callback<List<Sucursal>>() {
            @Override
            public void onResponse(Call<List<Sucursal>> call, Response<List<Sucursal>> response) {
                if (response.isSuccessful()) {
                    List<Sucursal> sucursales = response.body();
                    ArrayAdapter<Sucursal> adapter = new ArrayAdapter<>(CrearUsuario.this, android.R.layout.simple_spinner_dropdown_item, sucursales);
                    sucursalSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Sucursal>> call, Throwable t) {
                Toast.makeText(CrearUsuario.this, "Error al obtener sucursales", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearNuevoUsuario() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String userName = userNameEditText.getText().toString();
        int userType = userTypeSpinner.getSelectedItemPosition() + 1;  // Admin es 1, User es 2
        Sucursal selectedSucursal = (Sucursal) sucursalSpinner.getSelectedItem();
        int sucursalId = selectedSucursal.getID_sucursal();

        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setUser_name(userName);
        request.setUser_type(userType);
        request.setID_sucursal(sucursalId);

        ApiService apiService = RetrofitClient.getApiService("http://157.230.0.143:3000/api/");
        apiService.crearUsuario(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearUsuario.this, "Usuario creado exitosamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CrearUsuario.this, "Error al crear el usuario.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearUsuario.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });


    }



}
