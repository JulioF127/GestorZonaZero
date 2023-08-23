package com.example.zonazero0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class InventarioAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_admin);

        // Inicializar el botón
        Button btnNavigate = findViewById(R.id.button8);

        // Configurar el OnClickListener
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar InventarioAdmin2
                Intent intent = new Intent(InventarioAdmin.this, InventarioAdmin2.class);

                // Iniciar el Activity
                startActivity(intent);
            }
        });
    }
}