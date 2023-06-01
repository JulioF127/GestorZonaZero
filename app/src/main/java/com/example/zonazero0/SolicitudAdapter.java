package com.example.zonazero0;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
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

        holder.idSolicitud.setText("Orden No. " + solicitudActual.getID_solicitud());
        holder.nombreProducto.setText("Nombre del producto: " + solicitudActual.getNombre_producto());
        holder.cantidadSolicitada.setText("Cantidad Solicitada: " + String.valueOf(solicitudActual.getCantidad_solicitada()));
        holder.prioridad.setText("Prioridad: " + String.valueOf(solicitudActual.getPrioridad()));
        DateFormat df = DateFormat.getDateInstance();
        String fechaComoCadena = df.format(solicitudActual.getFecha_solicitud());
        holder.fecha.setText("Fecha de Solicitud: " + fechaComoCadena);
        holder.estado.setText("Estado: " + solicitudActual.getEstado());

        //pARA CAMBIA la tarjeta de color
        int prioridad = solicitudActual.getPrioridad();
        switch (prioridad) {
            case 1:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E9C5FF"));
                break;
            case 3:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#C08BFF"));
                break;
            default:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {
        TextView idSolicitud;
        TextView nombreProducto;
        TextView cantidadSolicitada;
        TextView prioridad;
        TextView fecha;
        TextView estado;
        CardView cardView;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            idSolicitud = itemView.findViewById(R.id.tv_id_solicitud);
            nombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            cantidadSolicitada = itemView.findViewById(R.id.tvCantidadSolicitada);
            prioridad = itemView.findViewById(R.id.tvPrioridad);
            fecha = itemView.findViewById(R.id.tvFecha);
            estado = itemView.findViewById(R.id.tvEstado);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}