package com.example.zonazero0;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerSucursalesAdapter extends RecyclerView.Adapter<VerSucursalesAdapter.VerSucursalesViewHolder> {

    private Context context;
    private List<VerSucursal> verSucursalesList;

    public VerSucursalesAdapter(Context context, List<VerSucursal> verSucursalesList) {
        this.context = context;
        this.verSucursalesList = verSucursalesList;
    }

    @NonNull
    @Override
    public VerSucursalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ver_sucursal, parent, false);
        return new VerSucursalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerSucursalesViewHolder holder, int position) {
        VerSucursal verSucursal = verSucursalesList.get(position);
        holder.tvIdSucursal.setText("ID Sucursal: " + verSucursal.getID_sucursal());
        holder.tvNombreSucursal.setText("Nombre de la Sucursal: " + verSucursal.getNombre_sucursal());
        holder.tvUbicacion.setText("Ubicación: " + verSucursal.getUbicacion());
        holder.tvIdEncargado.setText("ID Encargado: " + verSucursal.getID_encargado());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion(verSucursal.getID_sucursal());
            }
        });
    }

    @Override
    public int getItemCount() {
        return verSucursalesList.size();
    }

    private void mostrarDialogoConfirmacion(final int idSucursal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta sucursal?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSucursal(idSucursal);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void deleteSucursal(int idSucursal) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://157.230.0.143:3000/api/sucursales/" + idSucursal;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejar error de red
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Sucursal eliminada exitosamente
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Sucursal eliminada", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Error al eliminar la sucursal
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Error al eliminar la sucursal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public class VerSucursalesViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdSucursal, tvNombreSucursal, tvUbicacion, tvIdEncargado;
        Button deleteButton;

        public VerSucursalesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdSucursal = itemView.findViewById(R.id.tv_id_sucursal);
            tvNombreSucursal = itemView.findViewById(R.id.tvNombreSucursal);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvIdEncargado = itemView.findViewById(R.id.tv_id_encargado);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
