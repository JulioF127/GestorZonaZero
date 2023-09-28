package com.example.zonazero0;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerSucursalesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerSucursalesAdapter verSucursalesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_sucursales);  // Asegúrate de tener este layout

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarSucursales();
    }

    private void cargarSucursales() {
        ApiService apiService = RetrofitClient.getApiService("http://157.230.0.143:3000/api/");
        apiService.getSucursales1().enqueue(new Callback<List<VerSucursal>>() {
            @Override
            public void onResponse(Call<List<VerSucursal>> call, Response<List<VerSucursal>> response) {
                if (response.isSuccessful()) {
                    List<VerSucursal> verSucursalList = response.body();
                    verSucursalesAdapter = new VerSucursalesAdapter(VerSucursalesActivity.this, verSucursalList);
                    recyclerView.setAdapter(verSucursalesAdapter);
                } else {
                    Toast.makeText(VerSucursalesActivity.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VerSucursal>> call, Throwable t) {
                Toast.makeText(VerSucursalesActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
