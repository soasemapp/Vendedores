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
import com.example.kepler201.SetterandGetter.DisponibilidadSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterDetalleExistencia extends RecyclerView.Adapter<AdapterDetalleExistencia.ViewHolderDetalleExistencia> implements View.OnClickListener {

    ArrayList<DisponibilidadSANDG> Existencias;
    private View.OnClickListener listener;

    public AdapterDetalleExistencia(ArrayList<DisponibilidadSANDG> Existencias) {
        this.Existencias = Existencias;
    }

    @NonNull
    @Override
    public ViewHolderDetalleExistencia onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_productdetall, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDetalleExistencia(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDetalleExistencia holder, int position) {
        holder.SUCURSAL.setText(Existencias.get(position).getNombre());
        int Existencia = Integer.parseInt(Existencias.get(position).getDisponibilidad());
        holder.Existencia.setText(Html.fromHtml("Disponibilidad : "+((Existencia==0)?"<font color = #FF0000>No hay disponibles </font>":"<font color = #4CAF50>"+Existencias.get(position).getDisponibilidad()+" PZA </font>")));


    }


    @Override
    public int getItemCount() {
        return Existencias.size();
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

    public static class ViewHolderDetalleExistencia extends RecyclerView.ViewHolder {
        TextView SUCURSAL;
        TextView Existencia;


        public ViewHolderDetalleExistencia(View itemView) {
            super(itemView);

            SUCURSAL =  itemView.findViewById(R.id.nomSucursal);
            Existencia =  itemView.findViewById(R.id.ExistenciaSucursal);

        }
    }
}