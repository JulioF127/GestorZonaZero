package com.example.zonazero0;

import static com.google.firebase.messaging.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentValues;
import com.example.zonazero0.DeviceManager;
import com.example.zonazero0.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Log;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;

import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainUser extends AppCompatActivity {
    private RecyclerView recyclerView, recyclerViewInventario; // Agregado recyclerViewInventario para el inventario
    private SolicitudAdapter solicitudAdapter;
    private ProductoAdapter productoAdapterInventario; // Adaptador para el inventario
    private SharedPreferences sharedPreferences;

    private int branchId;


    private Button btnLogout;
    private Button btnSolicitud;
    private Button btnActualizar;

    private static final long INTERVALO_COMPROBACION = 1 * 60 * 1000; // 1 minuto
    private Handler tokenHandler;
    private Runnable tokenRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtener branchId de SharedPreferences
        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        branchId = sharedPreferences.getInt("branchId", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // Inicialización de botones y listeners
        btnLogout = findViewById(R.id.button);
        btnSolicitud = findViewById(R.id.button2);
        btnActualizar = findViewById(R.id.button5);
        addDevice();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSolicitudActivity();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarSolicitudes();
            }
        });

        // Inicialización del RecyclerView de Solicitudes
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Inicializar el RecyclerView para el inventario
        recyclerViewInventario = findViewById(R.id.recyclerViewInventario);
        recyclerViewInventario.setLayoutManager(new LinearLayoutManager(this));
        getInventario();


        // Obtener las solicitudes y el inventario
        int branch_id = decodeBranchId(sharedPreferences.getString("token", null));
        getSolicitudes(branch_id);


        // Iniciar la verificación del token cada 5 minutos
        tokenHandler = new Handler();
        tokenRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isTokenValid()) {
                    logoutUser();
                    return;
                }

                tokenHandler.postDelayed(this, INTERVALO_COMPROBACION);
            }
        };
        tokenHandler.postDelayed(tokenRunnable, INTERVALO_COMPROBACION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la verificación del token cuando la actividad se destruye
        tokenHandler.removeCallbacks(tokenRunnable);
    }

    // Método para obtener las solicitudes
    private void getSolicitudes(int branch_id) {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);

        Call<List<Solicitud>> call = apiService.getSolicitudes4(branch_id);
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    List<Solicitud> solicitudes = response.body();

                    solicitudAdapter = new SolicitudAdapter(solicitudes);
                    recyclerView.setAdapter(solicitudAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                // Manejar error de conexión aquí
            }
        });
    }

    // Método para obtener el inventario
    private void getInventario() {
        if (branchId == -1) {
            Toast.makeText(MainUser.this, "Error en el número de la tienda", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainUser.this, "Branch ID: " + branchId, Toast.LENGTH_SHORT).show();

        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Producto>> call = apiService.getProductosPorSucursal(String.valueOf(branchId));
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    List<Producto> listaProductos = response.body();

                    productoAdapterInventario = new ProductoAdapter(listaProductos);
                    recyclerViewInventario.setAdapter(productoAdapterInventario);
                    productoAdapterInventario.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainUser.this, "Error al obtener el inventario", Toast.LENGTH_SHORT).show();

                    // Imprimir el código de error en la consola
                    Log.d("Inventario", "Error al obtener el inventario. Código de respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(MainUser.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();

                // Imprimir el error en la consola
                Log.d("Inventario", "Error de conexión: " + t.getMessage());
            }
        });
    }





    private int decodeBranchId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }
        String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
        return payloadJson.get("branch_id").getAsInt();
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(MainUser.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    //Para el boton de agregar soli
    private void openSolicitudActivity() {
        Intent intent = new Intent(MainUser.this, SolicitudPregunta.class);
        startActivity(intent);
    }

    private void actualizarSolicitudes() {
        int branch_id = decodeBranchId(sharedPreferences.getString("token", null));
        getSolicitudes(branch_id);
    }

    private boolean isTokenValid() {
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            return false; // No hay token, no es válido
        }

        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            return false; // Token inválido
        }

        String tokenPayload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(tokenPayload).getAsJsonObject();

        long expirationTime = payloadJson.get("exp").getAsLong();

        // Ajustar la hora local del dispositivo a la hora de Nueva York
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Guatemala"));
        String newYorkTime = sdf.format(new Date(expirationTime * 1000));

        try {
            Date newYorkDate = sdf.parse(newYorkTime);
            long currentTime = System.currentTimeMillis();

            return newYorkDate.getTime() > currentTime; // El token es válido si la hora de expiración es posterior a la hora actual ajustada a Nueva York
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Error al parsear la fecha, token inválido
        }
    }


// Metodo para lo de firebase
private void addDevice(){
    FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(ContentValues.TAG,
                                "Fetching FCM registration token failed",
                                task.getException());
                        return;
                    }

                    String token = task.getResult();
                    String tokenGuardado = getSharedPreferences(Constantes.SP_FILE,0)
                            .getString(Constantes.SP_KEY_DEVICEID,null);
                    if (token != null ){
                        if (tokenGuardado == null || !token.equals(tokenGuardado)){
                            //registramos el token en el servidor
                            DeviceManager.postRegistrarDispositivoEnServidor( token, MainUser.this);
                        }
                    }

                    Toast.makeText(MainUser.this, token,
                            Toast.LENGTH_SHORT).show();
                }
            });
}


}
