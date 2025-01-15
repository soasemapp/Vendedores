package com.example.kepler201.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.activities.models.VentaLinea;

import java.util.List;

public class VentaLineaAdapter extends RecyclerView.Adapter<VentaLineaAdapter.VentaLineaViewHolder> {

    private List<VentaLinea> ventas;

    public VentaLineaAdapter(List<VentaLinea> ventas) {
        this.ventas = ventas;
    }

    @NonNull
    @Override
    public VentaLineaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_venta_linea, parent, false);
        return new VentaLineaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaLineaViewHolder holder, int position) {
        VentaLinea venta = ventas.get(position);


        holder.textLinea.setText(venta.getLinea());
        holder.textPresupuesto.setText(String.valueOf(venta.getPresupuesto()));
        holder.textVendido.setText(String.valueOf(venta.getVendido()));
        holder.textPorcentaje.setText(String.format("%.2f%%", venta.getPorcentaje()));
    }

    @Override
    public int getItemCount() {
        return ventas != null ? ventas.size() : 0;
    }

    public static class VentaLineaViewHolder extends RecyclerView.ViewHolder {


        TextView textLinea, textPresupuesto, textVendido, textPorcentaje;

        public VentaLineaViewHolder(@NonNull View itemView) {
            super(itemView);


            textLinea = itemView.findViewById(R.id.textLinea);
            textPresupuesto = itemView.findViewById(R.id.textPresupuesto);
            textVendido = itemView.findViewById(R.id.textVenta);
            textPorcentaje = itemView.findViewById(R.id.textPorcentaje);
        }
    }


    public void setVentas(List<VentaLinea> nuevaListaVentas) {
        this.ventas = nuevaListaVentas;
        notifyDataSetChanged();
    }
}
