package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!user_name.isEmpty() && !password.isEmpty()) {
                    loginUser(user_name, password);
                } else {
                    // Muestra un mensaje de error si los campos están vacíos
                }
            }
        });
    }

    private void loginUser(String user_name, String password) {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);

        LoginRequest loginRequest = new LoginRequest(user_name, password);
        Call<JsonObject> call = apiService.login(loginRequest);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Procesa la respuesta de la API y realiza acciones pertinentes.
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    // Muestra un mensaje de error en caso de fallo
                    Toast.makeText(MainActivity.this, "Error: Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Muestra un mensaje de error en caso de fallo en la conexión
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}