package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.DetalladoPagosSANDG;
import com.example.kepler201.SetterandGetter.RegistroPagosNewSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterDetalladoPagos extends RecyclerView.Adapter<AdapterDetalladoPagos.ViewHolderDetallado> implements View.OnClickListener {

    ArrayList<DetalladoPagosSANDG> lista;
    private View.OnClickListener listener;
    private int index=-1;

    public AdapterDetalladoPagos(ArrayList<DetalladoPagosSANDG> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterDetalladoPagos.ViewHolderDetallado onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detalladodepago, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDetallado(view);
    }

    @Override
    public void onBindViewHolder(AdapterDetalladoPagos.ViewHolderDetallado holder, int position) {
        holder.sucursal.setText(lista.get(position).getSucursal());
        holder.folio.setText(lista.get(position).getFolio());
        holder.monto.setText(lista.get(position).getMonto());
        holder.montoapagar.setText(lista.get(position).getMontoapagar());
        holder.comentario.setText(lista.get(position).getComentario());

        
    }
    public int index(int index){
        this.index=index;
        return index;
    }
    @Override
    public int getItemCount() {
        return lista.size();
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

    public static class ViewHolderDetallado extends RecyclerView.ViewHolder {
        TextView sucursal, folio, monto, montoapagar, comentario;
        public ViewHolderDetallado(View itemView) {
            super(itemView);
            sucursal =  itemView.findViewById(R.id.suc);
            folio =  itemView.findViewById(R.id.folio);
            monto =  itemView.findViewById(R.id.monto);
            montoapagar =  itemView.findViewById(R.id.montoapagar);
            comentario =  itemView.findViewById(R.id.comentario);


        }
    }
}