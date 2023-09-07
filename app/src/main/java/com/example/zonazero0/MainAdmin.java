package com.example.zonazero0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zonazero0.RetrofitClient;
import com.example.zonazero0.Solicitud;
import com.example.zonazero0.SolicitudAdapter;
import com.example.zonazero0.ApiService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainAdmin extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private SolicitudAdapter solicitudAdapter1, solicitudAdapter2, solicitudAdapter3;
    private List<Solicitud> listaSolicitudes1, listaSolicitudes2, listaSolicitudes3;
    private Button btnLogout, btnActualizar, btnInventario, btntiendas;
    private SharedPreferences sharedPreferences;

    private static final long INTERVALO_COMPROBACION = 1 * 60 * 1000;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int userType = sharedPreferences.getInt("userType", -1);

        if (token == null || userType != 1) {
            logoutUser();
            return;
        }

        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        btnLogout = findViewById(R.id.btn_logout);
        btnActualizar = findViewById(R.id.button6);
        btnInventario = findViewById(R.id.button10);
        btntiendas= findViewById(R.id.button11);


        listaSolicitudes1 = new ArrayList<>();
        listaSolicitudes2 = new ArrayList<>();
        listaSolicitudes3 = new ArrayList<>();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);

        solicitudAdapter1 = new SolicitudAdapter(listaSolicitudes1);
        solicitudAdapter2 = new SolicitudAdapter(listaSolicitudes2);
        solicitudAdapter3 = new SolicitudAdapter(listaSolicitudes3);

        recyclerView1.setAdapter(solicitudAdapter1);
        recyclerView2.setAdapter(solicitudAdapter2);
        recyclerView3.setAdapter(solicitudAdapter3);

        solicitudAdapter1.setOnItemClickListener(new SolicitudAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Solicitud solicitudClickeada = listaSolicitudes1.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdmin.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de querer actualizar la solicitud con ID: " + solicitudClickeada.getID_solicitud() + "?");

                // Botón de confirmación
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarSolicitudAPI(solicitudClickeada.getID_solicitud());
                        Toast.makeText(MainAdmin.this, "Solicitud Actualizada ID: " + solicitudClickeada.getID_solicitud(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Botón de cancelación
                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
                actualizarSolicitudes();

            }

            @Override
            public void onItemDoubleClicked(int position) {
                Toast.makeText(MainAdmin.this, "No se puede regresar mas la solicitud", Toast.LENGTH_SHORT).show();
                actualizarSolicitudes();
            }
        });

        solicitudAdapter2.setOnItemClickListener(new SolicitudAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Solicitud solicitudClickeada = listaSolicitudes2.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdmin.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de querer actualizar la solicitud con ID: " + solicitudClickeada.getID_solicitud() + "?");

                // Botón de confirmación
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarSolicitudAPI(solicitudClickeada.getID_solicitud());
                        Toast.makeText(MainAdmin.this, "Solicitud Actualizada ID: " + solicitudClickeada.getID_solicitud(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Botón de cancelación
                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
                actualizarSolicitudes();
            }

            @Override
            public void onItemDoubleClicked(int position) {
                Solicitud solicitudClickeada = listaSolicitudes2.get(position);
                Toast.makeText(MainAdmin.this, "Regresando solicitud...", Toast.LENGTH_SHORT).show();
                revertirSolicitudAPI(solicitudClickeada.getID_solicitud());
                actualizarSolicitudes();
            }

        });

        solicitudAdapter3.setOnItemClickListener(new SolicitudAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Solicitud solicitudClickeada = listaSolicitudes3.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdmin.this);
                builder.setTitle("Aviso");
                builder.setMessage("No se puede mover más la solicitud con ID: " + solicitudClickeada.getID_solicitud());

                // Botón de OK
                builder.setPositiveButton("Entendido", null);

                AlertDialog dialog = builder.create();
                dialog.show();
                actualizarSolicitudes();
            }


            @Override
            public void onItemDoubleClicked(int position) {
                Solicitud solicitudClickeada = listaSolicitudes3.get(position);
                Toast.makeText(MainAdmin.this, "Regresando solicitud...", Toast.LENGTH_SHORT).show();
                revertirSolicitudAPI(solicitudClickeada.getID_solicitud());
                actualizarSolicitudes();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUsuarios();
            }
        });

        btnInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdmin.this, InventarioAdmin.class);
                startActivity(intent);
            }
        });

        btntiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdmin.this, ActivityTiendas.class);
                startActivity(intent);
            }
        });

        obtenerSolicitudes1();
        obtenerSolicitudes2();
        obtenerSolicitudes3();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isTokenValid()) {
                    logoutUser();
                    return;
                }
                handler.postDelayed(this, INTERVALO_COMPROBACION);
            }
        };
        handler.postDelayed(runnable, INTERVALO_COMPROBACION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private boolean isTokenValid() {
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            return false;
        }

        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            return false;
        }

        String tokenPayload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));
        JsonObject payloadJson = new JsonParser().parse(tokenPayload).getAsJsonObject();

        long expirationTime = payloadJson.get("exp").getAsLong();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Guatemala"));
        String newYorkTime = sdf.format(new Date(expirationTime * 1000));

        try {
            Date newYorkDate = sdf.parse(newYorkTime);
            long currentTime = System.currentTimeMillis();

            return newYorkDate.getTime() > currentTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void obtenerSolicitudes1() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes1();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes1.clear();
                    listaSolicitudes1.addAll(response.body());
                    solicitudAdapter1.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerSolicitudes2() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes2();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes2.clear();
                    listaSolicitudes2.addAll(response.body());
                    solicitudAdapter2.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerSolicitudes3() {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<List<Solicitud>> call = apiService.getSolicitudes3();
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (response.isSuccessful()) {
                    listaSolicitudes3.clear();
                    listaSolicitudes3.addAll(response.body());
                    solicitudAdapter3.notifyDataSetChanged();
                } else {
                    Log.e("MainAdmin", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Log.e("MainAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void actualizarSolicitudes() {
        obtenerSolicitudes1();
        obtenerSolicitudes2();
        obtenerSolicitudes3();
        Toast.makeText(MainAdmin.this, "Si funciona " , Toast.LENGTH_SHORT).show();

    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userType");
        editor.remove("branchId");
        editor.apply();

        Intent intent = new Intent(MainAdmin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void revertirSolicitudAPI(int idSolicitud) {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<JsonObject> call = apiService.revertSolicitud(idSolicitud);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainAdmin.this, "Solicitud revertida ID: " + idSolicitud, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainAdmin.this, "Error al revertir la solicitud", Toast.LENGTH_SHORT).show();
                }
                refrescarDespuesDeRetraso();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainAdmin.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void actualizarSolicitudAPI(int idSolicitud) {
        String BASE_URL = "http://157.230.0.143:3000/api/";
        ApiService apiService = RetrofitClient.getApiService(BASE_URL);
        Call<JsonObject> call = apiService.updateSolicitud(idSolicitud);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainAdmin.this, "SOLICITUD " + idSolicitud + " actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainAdmin.this, "Error al actualizar la solicitud", Toast.LENGTH_SHORT).show();
                }
                refrescarDespuesDeRetraso();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainAdmin.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void refrescarDespuesDeRetraso() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actualizarSolicitudes();
            }
        }, 500);  // 500 milisegundos de retraso
    }

    private void ActivityUsuarios() {

        Intent intent = new Intent(MainAdmin.this, ActivityUsuarios.class);
        startActivity(intent);
        finish();
    }






}
