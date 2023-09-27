package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityUsuarios extends AppCompatActivity {
    private Button crear;
    private Button ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        crear = findViewById(R.id.button7);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUsuarios.this, CrearUsuario.class);
                startActivity(intent);
            }
        });

        ver = findViewById(R.id.button13);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUsuarios.this, VerUsuariosActivity.class);
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
