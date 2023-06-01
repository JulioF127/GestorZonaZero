package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int userType = sharedPreferences.getInt("userType", -1);

        if (token != null && userType != -1) {
            redirectUser(userType);
            return;
        }

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
                    Toast.makeText(MainActivity.this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
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
                    String token = response.body().get("token").getAsString();
                    int userType = decodeUserType(token);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.putInt("userType", userType);
                    editor.apply();

                    redirectUser(userType);
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

    private void redirectUser(int userType) {
        if (userType == 1) {
            // redirige al usuario a MainAdmin
            Intent intent = new Intent(MainActivity.this, MainAdmin.class);
            startActivity(intent);
            finish();
        } else if (userType == 2) {
            // redirige al usuario a MainUser
            Intent intent = new Intent(MainActivity.this, MainUser.class);
            startActivity(intent);
            finish();
        } else {
            // Muestra un mensaje de error si el tipo de usuario es desconocido
            Toast.makeText(MainActivity.this, "Error: tipo de usuario desconocido", Toast.LENGTH_SHORT).show();
        }
    }

    private int decodeUserType(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }
        String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
        return payloadJson.get("role").getAsInt();
    }
}