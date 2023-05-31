package com.example.zonazero0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder> {

    private List<Solicitud> listaSolicitudes;

    public SolicitudAdapter(List<Solicitud> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new SolicitudViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);
        // Aquí debes vincular los datos de tu solicitud con los elementos de tu diseño de tarjeta.
        // Por ejemplo:
        holder.tvIdSolicitud.setText(String.valueOf(solicitud.getId()));
        holder.tvNombreProducto.setText(solicitud.getNombreProducto());
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {

        public TextView tvIdSolicitud;
        public TextView tvNombreProducto;

        public SolicitudViewHolder(View v) {
            super(v);
            tvIdSolicitud = v.findViewById(R.id.tv_id_solicitud);
            tvNombreProducto = v.findViewById(R.id.tv_nombre_producto);
        }
    }
}
