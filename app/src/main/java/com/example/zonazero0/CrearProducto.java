package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearProducto extends AppCompatActivity {

    private EditText codigoProductoEditText, nombreProductoEditText, precioEditText;
    private Button crearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        codigoProductoEditText = findViewById(R.id.editText);
        nombreProductoEditText = findViewById(R.id.editText2);
        precioEditText = findViewById(R.id.editText4);
        crearButton = findViewById(R.id.button12);

        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevoProducto();
            }
        });
    }

    private void crearNuevoProducto() {
        String codigoProducto = codigoProductoEditText.getText().toString();
        String nombreProducto = nombreProductoEditText.getText().toString();
        double precio = Double.parseDouble(precioEditText.getText().toString());

        CrearProductoRequest request = new CrearProductoRequest();
        request.setCodigo_producto(codigoProducto);
        request.setNombre_producto(nombreProducto);
        request.setPrecio(precio);

        request.setCantidad_total(100);

        ApiService apiService = RetrofitClient.getApiService("http://tu_base_url.com/api/");
        apiService.crearProducto(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearProducto.this, "Producto creado exitosamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CrearProducto.this, "Error al crear el producto.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearProducto.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
