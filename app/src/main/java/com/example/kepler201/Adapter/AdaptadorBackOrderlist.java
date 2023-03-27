package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.BackordersADDSANDG;

import java.util.ArrayList;

public class AdaptadorBackOrderlist extends RecyclerView.Adapter<AdaptadorBackOrderlist.ViewHolderListFoli> implements View.OnClickListener {

    ArrayList<BackordersADDSANDG> listaFolios;
    private View.OnClickListener listener;

    public AdaptadorBackOrderlist(ArrayList<BackordersADDSANDG> listaFolios) {
        this.listaFolios = listaFolios;
    }

    @NonNull
    @Override
    public AdaptadorBackOrderlist.ViewHolderListFoli onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_backorderlist, null, false);
        view.setOnClickListener(this);
        return new ViewHolderListFoli(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorBackOrderlist.ViewHolderListFoli holder, int position) {
        holder.Folio.setText(listaFolios.get(position).getFolio());
        holder.Producto.setText(listaFolios.get(position).getProducto());
        holder.Descripcion.setText(listaFolios.get(position).getDescripcion());
        holder.BackOrder.setText(listaFolios.get(position).getBackOrder());
        holder.Existencia.setText(listaFolios.get(position).getExistencia());
    }



    @Override
    public int getItemCount() {
        return listaFolios.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);

        }
    }


    public static class ViewHolderListFoli extends RecyclerView.ViewHolder {
        TextView Folio, Producto, Descripcion, BackOrder,Existencia;

        public ViewHolderListFoli(View itemView) {
            super(itemView);
            Folio =  itemView.findViewById(R.id.txtFolio);
            Producto =  itemView.findViewById(R.id.txtClaveP);
            Descripcion =  itemView.findViewById(R.id.txtDescripcion);
            BackOrder =  itemView.findViewById(R.id.txtBackOrder);
            Existencia =  itemView.findViewById(R.id.txtExistencia);

        }
    }
}
