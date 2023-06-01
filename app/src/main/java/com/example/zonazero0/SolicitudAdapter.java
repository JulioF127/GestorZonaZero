package com.example.zonazero0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new SolicitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        Solicitud solicitudActual = listaSolicitudes.get(position);

        holder.nombreProducto.setText(solicitudActual.getNombre_producto());
        holder.cantidadSolicitada.setText(String.valueOf(solicitudActual.getCantidad_solicitada()));
        holder.prioridad.setText(String.valueOf(solicitudActual.getPrioridad()));
        DateFormat df = DateFormat.getDateInstance();
        String fechaComoCadena = df.format(solicitudActual.getFecha_solicitud());
        holder.fecha.setText(fechaComoCadena);
        holder.estado.setText(solicitudActual.getEstado());
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {

        TextView nombreProducto;
        TextView cantidadSolicitada;
        TextView prioridad;
        TextView fecha;
        TextView estado;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            cantidadSolicitada = itemView.findViewById(R.id.tvCantidadSolicitada);
            prioridad = itemView.findViewById(R.id.tvPrioridad);
            fecha = itemView.findViewById(R.id.tvFecha);
            estado = itemView.findViewById(R.id.tvEstado);
        }
    }
}