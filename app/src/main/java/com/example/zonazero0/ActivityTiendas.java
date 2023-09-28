package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTiendas extends AppCompatActivity {
    private Button crear;
    private Button ver;  // Referencia al botón Ver

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        crear = findViewById(R.id.button7);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTiendas.this, CrearTiendas.class);
                startActivity(intent);
            }
        });

        ver = findViewById(R.id.button13);  // Encuentra el botón Ver por ID
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTiendas.this, VerSucursalesActivity.class);
                startActivity(intent);  // Inicia la actividad VerSucursalesActivity
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainAdmin.class);
        startActivity(intent);
    }
}
