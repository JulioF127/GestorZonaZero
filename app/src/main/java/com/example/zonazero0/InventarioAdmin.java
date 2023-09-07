package com.example.zonazero0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InventarioAdmin extends AppCompatActivity {
    private Button btnProducts;
    private static final String PREFERENCES_NAME = "myPreferences";
    private static final String KEY_TIENDA = "tiendaSeleccionada";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_admin);



        // Inicializar el botón
        Button btnNavigate = findViewById(R.id.button8);
        Button btontienda1 = findViewById(R.id.button7);
        Button btontienda3 = findViewById(R.id.button9);
        Button btnProducts = findViewById(R.id.button10);

        // Configurar el OnClickListener
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTiendaSeleccionada("2");
                mostrarToast("2");
                Intent intent = new Intent(InventarioAdmin.this, InventarioAdmin2.class);
                startActivity(intent);
            }
        });

        btontienda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTiendaSeleccionada("1");
                mostrarToast("1");
                Intent intent = new Intent(InventarioAdmin.this, InventarioAdmin2.class);
                startActivity(intent);
            }
        });

        btontienda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTiendaSeleccionada("3");
                mostrarToast("3");
                Intent intent = new Intent(InventarioAdmin.this, InventarioAdmin2.class);
                startActivity(intent);
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventarioAdmin.this, ActivityProductos.class);
                startActivity(intent);
            }
        });
    }

    private void guardarTiendaSeleccionada(String tienda) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TIENDA, tienda);
        editor.apply();
    }

    private void mostrarToast(String tienda) {
        Toast.makeText(this, "Tienda " + tienda + " seleccionada", Toast.LENGTH_SHORT).show();
    }
}
