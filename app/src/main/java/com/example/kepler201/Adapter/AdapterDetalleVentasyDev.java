package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.CompraasSANDG;
import com.example.kepler201.SetterandGetter.VentasDevSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterDetalleVentasyDev extends RecyclerView.Adapter<AdapterDetalleVentasyDev.ViewHolderDetalleCompras> implements View.OnClickListener {

    ArrayList<VentasDevSANDG> ventas;
    private View.OnClickListener listener;

    public AdapterDetalleVentasyDev(ArrayList<VentasDevSANDG> ventas) {
        this.ventas = ventas;
    }

    @NonNull
    @Override
    public ViewHolderDetalleCompras onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_vendev, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDetalleCompras(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDetalleCompras holder, int position) {
        holder.tipo.setText(ventas.get(position).getTipoVeDe());
        holder.folio.setText(ventas.get(position).getFolioVeDe());
        holder.sucursal.setText(Html.fromHtml("<font color = #000000>"+ventas.get(position).getSucursalVeDe()+"</font>"));
        holder.fecha.setText(Html.fromHtml("<font color = #000000>"+ventas.get(position).getFechaVeDe()+"</font>"));
        holder.cantidad.setText(Html.fromHtml("<font color = #000000>"+ventas.get(position).getCantidadVeDe()+"</font>"));


    }


    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);

        }
    }

    public static class ViewHolderDetalleCompras extends RecyclerView.ViewHolder {
        TextView tipo;
        TextView folio;
        TextView sucursal;
        TextView fecha;
        TextView cantidad;

        public ViewHolderDetalleCompras(View itemView) {
            super(itemView);

            tipo =  itemView.findViewById(R.id.tipo);
            folio =  itemView.findViewById(R.id.folio);
            sucursal = itemView.findViewById(R.id.sucursal);
            fecha = itemView.findViewById(R.id.fecha);
            cantidad = itemView.findViewById(R.id.cantidad);

        }
    }
}