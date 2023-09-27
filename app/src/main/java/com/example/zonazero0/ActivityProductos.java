package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityProductos extends AppCompatActivity {
    private Button crear;
    private Button ver;  // referencia al botón "Ver"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        crear = findViewById(R.id.button7);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductos.this, CrearProducto.class);
                startActivity(intent);
            }
        });

        ver = findViewById(R.id.button13);  // inicializa la referencia al botón "Ver"
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductos.this, VerProductosActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainAdmin.class);
        startActivity(intent);
    }
}
