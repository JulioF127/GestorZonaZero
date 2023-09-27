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

public class VerUsuariosAdapter extends RecyclerView.Adapter<VerUsuariosAdapter.VerUsuariosViewHolder> {

    private Context context;
    private List<VerUsuarios> verUsuariosList;

    public VerUsuariosAdapter(Context context, List<VerUsuarios> verUsuariosList) {
        this.context = context;
        this.verUsuariosList = verUsuariosList;
    }

    @NonNull
    @Override
    public VerUsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ver_usuario, parent, false);
        return new VerUsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerUsuariosViewHolder holder, int position) {
        VerUsuarios verUsuario = verUsuariosList.get(position);
        holder.tvIdUsuario.setText("ID Usuario: " + verUsuario.getId());
        holder.tvNombreUsuario.setText("Nombre: " + verUsuario.getName());
        holder.tvEmailUsuario.setText("Email: " + verUsuario.getEmail());

        holder.deleteButton.setOnClickListener(v -> {
            int userId = verUsuario.getId();
            mostrarDialogoConfirmacion(userId);
        });
    }

    @Override
    public int getItemCount() {
        return verUsuariosList.size();
    }

    class VerUsuariosViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdUsuario, tvNombreUsuario, tvEmailUsuario;
        Button deleteButton;

        public VerUsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdUsuario = itemView.findViewById(R.id.tv_id_usuario);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvEmailUsuario = itemView.findViewById(R.id.tvEmailUsuario);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    private void mostrarDialogoConfirmacion(final int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este usuario?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(userId);  // Llama a la función que elimina el usuario
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser(int userId) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://157.230.0.143:3000/api/users/" + userId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Manejar error
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            // Usuario eliminado exitosamente
                            Toast.makeText(context, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                            // Puedes notificar al usuario o actualizar la lista
                        } else {
                            // Error en la solicitud
                            Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
