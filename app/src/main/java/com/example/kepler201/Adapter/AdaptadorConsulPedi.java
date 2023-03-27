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
import com.example.kepler201.SetterandGetter.ConsulPediSANDG;

import java.text.DecimalFormat;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdaptadorConsulPedi extends RecyclerView.Adapter<AdaptadorConsulPedi.ViewHolderConsulPedi> implements View.OnClickListener {

    ArrayList<ConsulPediSANDG> listaConsulPedi;
    private View.OnClickListener listener;

    public AdaptadorConsulPedi(ArrayList<ConsulPediSANDG> listaConsulPedi) {
        this.listaConsulPedi = listaConsulPedi;
    }

    @NonNull
    @Override
    public ViewHolderConsulPedi onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_consulpedi, null, false);
        view.setOnClickListener(this);
        return new ViewHolderConsulPedi(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderConsulPedi holder, int position) {
        holder.Sucursal.setText(Html.fromHtml("Sucursal:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getNombreSucursal()+"</font>"));
        holder.Folio.setText((Html.fromHtml("Folio:<font color ='#F32121'>" +listaConsulPedi.get(position).getFolio()+"</font>")));
        holder.Fecha.setText(Html.fromHtml("Fecha:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getFecha()+"</font>"));
        holder.CCliente.setText(Html.fromHtml("Clave:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getClave()+"</font>"));
        holder.Nombre.setText(Html.fromHtml("Nombre:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getNombre()+"</font>"));
        holder.Importe.setText((Html.fromHtml("Importe:<br>  <font color ='#000000'>$</font><font color ='#4CAF50'>" +formatNumberCurrency(listaConsulPedi.get(position).getImporte())+"</font>")));
        holder.Piezas.setText(Html.fromHtml("Piezas:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getPiezas()+"</font>"));
        holder.Comentario.setText(Html.fromHtml("Comentario:<br> <font color ='#000000'>" +listaConsulPedi.get(position).getComentario()+"</font>"));


    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(Double.parseDouble(number));
    }

    @Override
    public int getItemCount() {
        return listaConsulPedi.size();
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

    public static class ViewHolderConsulPedi extends RecyclerView.ViewHolder {
        TextView Sucursal, Folio, Fecha, CCliente, Nombre, Importe, Piezas,Comentario;

        public ViewHolderConsulPedi(View itemView) {
            super(itemView);
            Sucursal =  itemView.findViewById(R.id.Sucursal);
            Folio =  itemView.findViewById(R.id.Folio);
            Fecha =  itemView.findViewById(R.id.Fecha);
            CCliente =  itemView.findViewById(R.id.CCliente);
            Nombre =  itemView.findViewById(R.id.NCliente);
            Importe =  itemView.findViewById(R.id.Importe);
            Piezas =  itemView.findViewById(R.id.piezas);
            Comentario =  itemView.findViewById(R.id.comentario);
        }
    }
}