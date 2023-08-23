package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioAdmin2 extends AppCompatActivity {
    private static final String PREFERENCES_NAME = "myPreferences";
    private static final String KEY_TIENDA = "tiendaSeleccionada";
    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private static final String BASE_URL = "http://157.230.0.143:3000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_admin2);

        // 1. Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Hacer la llamada a la API
        obtenerProductos();
    }

    private void obtenerProductos() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String idSucursal = sharedPreferences.getString(KEY_TIENDA, "");

        if (idSucursal.isEmpty()) {
            Toast.makeText(InventarioAdmin2.this, "Error en el número de la tienda", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Producto>> call = apiService.getProductosPorSucursal(idSucursal);
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> listaProductos = response.body();

                    // 2. Configurar el Adaptador
                    productoAdapter = new ProductoAdapter(listaProductos);
                    recyclerView.setAdapter(productoAdapter);
                } else {
                    Toast.makeText(InventarioAdmin2.this, "Error al obtener los productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(InventarioAdmin2.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //Estos metodos de aqui borran el cache por decirlo asi, que guarda el numero de tienda
    //Que se selecciono
    @Override
    protected void onDestroy() {
        super.onDestroy();
        eliminarTiendaSeleccionada();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eliminarTiendaSeleccionada();
    }

    private void eliminarTiendaSeleccionada() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TIENDA);
        editor.apply();
    }
}
