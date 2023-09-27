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

public class VerProductosAdapter extends RecyclerView.Adapter<VerProductosAdapter.VerProductosViewHolder> {

    private Context context;
    private List<VerProductos> verProductosList;

    public VerProductosAdapter(Context context, List<VerProductos> verProductosList) {
        this.context = context;
        this.verProductosList = verProductosList;
    }

    @NonNull
    @Override
    public VerProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ver_producto, parent, false);
        return new VerProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerProductosViewHolder holder, int position) {
        VerProductos verProductos = verProductosList.get(position);
        holder.tvCodigoProducto.setText("Código Producto: " + verProductos.getCodigoProducto());
        holder.tvNombreProducto.setText("Nombre del producto: " + verProductos.getNombreProducto());
        holder.tvPrecioProducto.setText("Precio: " + verProductos.getPrecio());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion(verProductos.getCodigoProducto());
            }
        });
    }

    @Override
    public int getItemCount() {
        return verProductosList.size();
    }

    private void mostrarDialogoConfirmacion(final String codigoProducto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación de Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(codigoProducto);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar la eliminación, cerrar el diálogo
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteProduct(String codigoProducto) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://157.230.0.143:3000/api/productos/" + codigoProducto;

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
                    // Producto eliminado exitosamente
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Error al eliminar el producto
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public class VerProductosViewHolder extends RecyclerView.ViewHolder {

        TextView tvCodigoProducto, tvNombreProducto, tvPrecioProducto;
        Button deleteButton;

        public VerProductosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoProducto = itemView.findViewById(R.id.tv_codigo_producto);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
