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
import com.example.kepler201.SetterandGetter.BusquedaCliente0VentasSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolderClientes> implements View.OnClickListener {

    ArrayList<BusquedaCliente0VentasSANDG> listaClientes;
    private View.OnClickListener listener;

    public AdaptadorClientes(ArrayList<BusquedaCliente0VentasSANDG> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ViewHolderClientes onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_clientes, null, false);
        view.setOnClickListener(this);
        return new ViewHolderClientes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderClientes holder, int position) {
        holder.Clave.setText(listaClientes.get(position).getClave());
        holder.Nombre.setText(Html.fromHtml("Nombre: \n <br> <font color ='#000000'>" +listaClientes.get(position).getNombre()+"</font>"));
        holder.Direccion.setText(Html.fromHtml("Direccion: \n <br> <font color ='#000000'>" +listaClientes.get(position).getDireccion()+"</font>"));
        holder.Ciudad.setText(Html.fromHtml("Ciudad: \n <br> <font color ='#000000'>" +listaClientes.get(position).getCiudad()+"</font>"));

    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
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

    public static class ViewHolderClientes extends RecyclerView.ViewHolder {
        TextView Clave, Nombre, Direccion, Ciudad;

        public ViewHolderClientes(View itemView) {
            super(itemView);
            Clave =  itemView.findViewById(R.id.Clave);
            Nombre =  itemView.findViewById(R.id.NomCliente);
            Direccion =  itemView.findViewById(R.id.Direccion);
            Ciudad =  itemView.findViewById(R.id.Ciudad);
        }
    }
}
