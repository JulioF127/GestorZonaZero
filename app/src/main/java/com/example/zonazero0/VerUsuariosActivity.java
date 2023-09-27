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

public class VerUsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerUsuariosAdapter verUsuariosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        ApiService apiService = RetrofitClient.getApiService("http://157.230.0.143:3000/api/");
        apiService.getUsuarios1().enqueue(new Callback<List<VerUsuarios>>() {
            @Override
            public void onResponse(Call<List<VerUsuarios>> call, Response<List<VerUsuarios>> response) {
                if (response.isSuccessful()) {
                    List<VerUsuarios> verUsuariosList = response.body();
                    verUsuariosAdapter = new VerUsuariosAdapter(VerUsuariosActivity.this, verUsuariosList);
                    recyclerView.setAdapter(verUsuariosAdapter);
                } else {
                    Toast.makeText(VerUsuariosActivity.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VerUsuarios>> call, Throwable t) {
                Toast.makeText(VerUsuariosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
