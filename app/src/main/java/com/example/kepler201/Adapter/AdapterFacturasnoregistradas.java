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
import com.example.kepler201.SetterandGetter.FacturasnoregistradasNewSANDG;
import com.example.kepler201.SetterandGetter.FacturasnoregistradasNewSANDG;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterFacturasnoregistradas extends RecyclerView.Adapter<AdapterFacturasnoregistradas.ViewHoldernoResgistrado> implements View.OnClickListener {

    ArrayList<FacturasnoregistradasNewSANDG> lista;
    private View.OnClickListener listener;


    public AdapterFacturasnoregistradas(ArrayList<FacturasnoregistradasNewSANDG> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterFacturasnoregistradas.ViewHoldernoResgistrado onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_facturasnoregistradas, null, false);
        view.setOnClickListener(this);
        return new ViewHoldernoResgistrado(view);
    }

    @Override
    public void onBindViewHolder(AdapterFacturasnoregistradas.ViewHoldernoResgistrado holder, int position) {
        holder.SUC.setText(lista.get(position).getSUC());
        holder.FOLIO.setText(lista.get(position).getFOLIO());
        holder.FECHA.setText(lista.get(position).getFECHA());
        holder.SALDO.setText(lista.get(position).getSALDO());
        holder.MONTO.setText(lista.get(position).getMONTO());
        holder.DDP.setText(lista.get(position).getDDP());
        holder.PAGODDP.setText(lista.get(position).getPAGODDP());
        holder.MONTOAPAGAR.setText(lista.get(position).getMONTOAPAGAR());
        holder.VENCE.setText(lista.get(position).getVENCE());


        if(lista.get(position).getSeleccionado()==1){
            holder.seleccionarcheck.setChecked(true);

        }else {
            holder.seleccionarcheck.setChecked(false);
        }




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

    public static class ViewHoldernoResgistrado extends RecyclerView.ViewHolder {
        TextView   SUC,FOLIO,FECHA,SALDO,MONTO,DDP,PAGODDP,MONTOAPAGAR,VENCE;

        MaterialCheckBox seleccionarcheck;
        public ViewHoldernoResgistrado(View itemView) {
            super(itemView);
            SUC =  itemView.findViewById(R.id.suc);
            FOLIO =  itemView.findViewById(R.id.folio);
            FECHA =  itemView.findViewById(R.id.fecha);
            SALDO =  itemView.findViewById(R.id.saldo);
            MONTO =  itemView.findViewById(R.id.monto);
            DDP =itemView.findViewById(R.id.ddp);
            PAGODDP =itemView.findViewById(R.id.pagoddp);
            MONTOAPAGAR =itemView.findViewById(R.id.montoapagar);
            VENCE =itemView.findViewById(R.id.vence);

            seleccionarcheck=itemView.findViewById(R.id.seleccionarcheck);


        }
    }
}