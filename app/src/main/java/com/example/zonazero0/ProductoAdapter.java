package com.example.zonazero0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto productoActual = listaProductos.get(position);

        holder.idProducto.setText("ID Producto: " + productoActual.getID_producto());
        holder.nombreProducto.setText("Nombre del producto: " + productoActual.getNombre_producto());
        holder.cantidadProducto.setText("Cantidad: " + String.valueOf(productoActual.getCantidad_producto()));
        // Puedes agregar más campos si es necesario
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView idProducto;
        TextView nombreProducto;
        TextView cantidadProducto;
        // Agrega más campos si es necesario

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            idProducto = itemView.findViewById(R.id.tv_id_producto);
            nombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            cantidadProducto = itemView.findViewById(R.id.tvCantidadProducto);
            // Inicializa más campos si es necesario
        }
    }
}
