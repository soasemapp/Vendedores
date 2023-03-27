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
import com.example.kepler201.SetterandGetter.AgendaSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterAgenda extends RecyclerView.Adapter<AdapterAgenda.ViewHolderAgenda> implements View.OnClickListener {

    ArrayList<AgendaSANDG> listaAgenda;
    private View.OnClickListener listener;

    public AdapterAgenda(ArrayList<AgendaSANDG> listaAgenda) {
        this.listaAgenda = listaAgenda;
    }

    @NonNull
    @Override
    public AdapterAgenda.ViewHolderAgenda onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_agenda, null, false);
        view.setOnClickListener(this);
        return new ViewHolderAgenda(view);
    }

    @Override
    public void onBindViewHolder(AdapterAgenda.ViewHolderAgenda holder, int position) {
        holder.Fecha.setText(listaAgenda.get(position).getFecha());
        holder.ClaveCl.setText(Html.fromHtml("Clave-Cliente: <br> <font color ='#000000'>" +listaAgenda.get(position).getCliente()+"</font>"));
        holder.Nombre.setText(Html.fromHtml("Nombre: <br> <font color ='#000000'>" +listaAgenda.get(position).getClienNom()+"</font>"));
        holder.Actividad.setText(Html.fromHtml("Actividad: <br> <font color ='#000000'>" +listaAgenda.get(position).getActividad()+"</font>"));
        holder.Estatus.setText(Html.fromHtml("Estatus: <br> <font color ='#000000'>" +listaAgenda.get(position).getEstatus()+"</font>"));
        holder.Comentario.setText(Html.fromHtml("Comentario: <br> <font color ='#000000'>" +listaAgenda.get(position).getComentario()+"</font>"));


    }

    @Override
    public int getItemCount() {
        return listaAgenda.size();
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

    public static class ViewHolderAgenda extends RecyclerView.ViewHolder {
        TextView Fecha, Nombre, ClaveCl, Actividad, Estatus,Comentario;

        public ViewHolderAgenda(View itemView) {
            super(itemView);
            Fecha =  itemView.findViewById(R.id.Fecha);
            ClaveCl =  itemView.findViewById(R.id.cCliente);
            Nombre =  itemView.findViewById(R.id.Nombre);
            Actividad =  itemView.findViewById(R.id.Actividad);
            Estatus =  itemView.findViewById(R.id.Estatus);
            Comentario =itemView.findViewById(R.id.Comentario);
        }
    }
}