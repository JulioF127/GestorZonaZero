package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SolicitudPregunta extends AppCompatActivity {

    private Button btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_pregunta);

        btnLogout = findViewById(R.id.button3);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(SolicitudPregunta.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
