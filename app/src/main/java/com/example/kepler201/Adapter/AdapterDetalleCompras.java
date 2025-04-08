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
import com.example.kepler201.SetterandGetter.DisponibilidadSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterDetalleCompras extends RecyclerView.Adapter<AdapterDetalleCompras.ViewHolderDetalleCompras> implements View.OnClickListener {

    ArrayList<CompraasSANDG> Compras;
    private View.OnClickListener listener;

    public AdapterDetalleCompras(ArrayList<CompraasSANDG> Compras) {
        this.Compras = Compras;
    }

    @NonNull
    @Override
    public ViewHolderDetalleCompras onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_compras, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDetalleCompras(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDetalleCompras holder, int position) {
        holder.SUCURSAL.setText(Compras.get(position).getNombre());
        int Existencia = Integer.parseInt(Compras.get(position).getCantidad());
        holder.Existencia.setText(Html.fromHtml("Llegaran : "+((Existencia==0)?"<font color = #FF0000>No hay disponibles </font>":"<font color = #4CAF50>"+Compras.get(position).getCantidad()+" PZA </font>")));
        holder.Fecha.setText("Fecha de llegada: "+Compras.get(position).getFecha()+"");


    }


    @Override
    public int getItemCount() {
        return Compras.size();
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
        TextView SUCURSAL;
        TextView Existencia;
        TextView Fecha;


        public ViewHolderDetalleCompras(View itemView) {
            super(itemView);

            SUCURSAL =  itemView.findViewById(R.id.SucursalCompras);
            Existencia =  itemView.findViewById(R.id.cantidadcompras);
            Fecha = itemView.findViewById(R.id.Fechallegadacompras);

        }
    }
}