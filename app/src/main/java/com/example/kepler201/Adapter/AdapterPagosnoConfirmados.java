package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.RegistroPagosNewSANDG;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterPagosnoConfirmados extends RecyclerView.Adapter<AdapterPagosnoConfirmados.ViewHolderPagos> implements View.OnClickListener {

    ArrayList<RegistroPagosNewSANDG> lista;
    private View.OnClickListener listener;
    private int index=-1;

    public AdapterPagosnoConfirmados(ArrayList<RegistroPagosNewSANDG> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterPagosnoConfirmados.ViewHolderPagos onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_facturasreg, null, false);
        view.setOnClickListener(this);
        return new ViewHolderPagos(view);
    }

    @Override
    public void onBindViewHolder(AdapterPagosnoConfirmados.ViewHolderPagos holder, int position) {
        holder.Fecha.setText(lista.get(position).getFecha());
        holder.Hora.setText(lista.get(position).getHora());
        holder.ClaveCLiente.setText(lista.get(position).getClaveCliente());
        holder.NombreCliente.setText(lista.get(position).getNombre());
        holder.Facturas.setText(lista.get(position).getFacturas());
        holder.Importe.setText(lista.get(position).getImporte());
        holder.Banco.setText(lista.get(position).getBancoNombre());
        holder.Formadepago.setText(lista.get(position).getFormadepago());
        holder.Comentario1.setText(lista.get(position).getCom1());
        holder.Comentario2.setText(lista.get(position).getCom3());
        holder.Comentario3.setText(lista.get(position).getCom3());

        if(index==position){
            holder.seleccionar.setBackgroundResource(R.color.blue_200);//seleccion
            holder.Fecha.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Hora.setTextColor(Color.parseColor("#FFFFFF"));
            holder.ClaveCLiente.setTextColor(Color.parseColor("#FFFFFF"));
            holder.NombreCliente.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Facturas.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Importe.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Banco.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Formadepago.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Comentario1.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Comentario2.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Comentario3.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.seleccionar.setBackgroundResource(0);//seleccion
            holder.Fecha.setTextColor(Color.GRAY);
            holder.Hora.setTextColor(Color.GRAY);
            holder.ClaveCLiente.setTextColor(Color.GRAY);
            holder.NombreCliente.setTextColor(Color.GRAY);
            holder.Facturas.setTextColor(Color.GRAY);
            holder.Importe.setTextColor(Color.GRAY);
            holder.Banco.setTextColor(Color.GRAY);
            holder.Formadepago.setTextColor(Color.GRAY);
            holder.Comentario1.setTextColor(Color.GRAY);
            holder.Comentario2.setTextColor(Color.GRAY);
            holder.Comentario3.setTextColor(Color.GRAY);
        }




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

    public static class ViewHolderPagos extends RecyclerView.ViewHolder {
        TextView Fecha, Hora, ClaveCLiente, NombreCliente, Facturas,Importe,Banco,Formadepago,Comentario1,Comentario2,Comentario3;
        LinearLayout seleccionar;
        public ViewHolderPagos(View itemView) {
            super(itemView);
            Fecha =  itemView.findViewById(R.id.fecha);
            Hora =  itemView.findViewById(R.id.hora);
            ClaveCLiente =  itemView.findViewById(R.id.clavecliente);
            NombreCliente =  itemView.findViewById(R.id.nombrecliente);
            Facturas =  itemView.findViewById(R.id.facturas);
            Importe =itemView.findViewById(R.id.importe);
            Banco =itemView.findViewById(R.id.banco);
            Formadepago =itemView.findViewById(R.id.formadepago);
            Comentario1 =itemView.findViewById(R.id.comentario1);
            Comentario2 =itemView.findViewById(R.id.comentario2);
            Comentario3 =itemView.findViewById(R.id.comentario3);
            seleccionar=itemView.findViewById(R.id.seleccionar);

        }
    }
}