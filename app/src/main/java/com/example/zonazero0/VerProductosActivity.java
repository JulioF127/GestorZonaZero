package com.example.zonazero0;

// ... (importaciones necesarias)

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


public class VerProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerProductosAdapter verProductosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_productos);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarProductos();
    }

    private void cargarProductos() {
        ApiService apiService = RetrofitClient.getApiService("http://157.230.0.143:3000/api/");
        apiService.getProductos1().enqueue(new Callback<List<VerProductos>>() {
            @Override
            public void onResponse(Call<List<VerProductos>> call, Response<List<VerProductos>> response) {
                if (response.isSuccessful()) {
                    List<VerProductos> verProductosList = response.body();
                    verProductosAdapter = new VerProductosAdapter(VerProductosActivity.this, verProductosList);
                    recyclerView.setAdapter(verProductosAdapter);
                } else {
                    Toast.makeText(VerProductosActivity.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VerProductos>> call, Throwable t) {
                Toast.makeText(VerProductosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
