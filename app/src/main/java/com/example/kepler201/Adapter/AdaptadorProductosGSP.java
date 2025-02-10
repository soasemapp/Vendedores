package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ProductosNuevosSANDG;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorProductosGSP extends RecyclerView.Adapter<AdaptadorProductosGSP.ViewHolderProductosNuevos> implements View.OnClickListener {

    ArrayList<ProductosNuevosSANDG> listaproductos;
    Context context;
    private View.OnClickListener listener;
    String Empresa;
    String EmpresaNuevaa="";
    String EmpresaAd="";


    public AdaptadorProductosGSP(ArrayList<ProductosNuevosSANDG> listaConsulCoti, Context context, String empresa) {
        this.listaproductos = listaConsulCoti;
        this.context = context;
        this.Empresa=empresa;
        this.EmpresaNuevaa=empresa;
    }

    @NonNull
    @Override
    public ViewHolderProductosNuevos onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_productosnuevosshark, null, false);
        view.setOnClickListener(this);
        return new ViewHolderProductosNuevos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderProductosNuevos holder, int position) {

        if(Empresa.equals("https://www.jacve.mx/tools/pictures-urlProductos?ids=") || Empresa.equals("https://www.guvi.mx/tools/pictures-urlProductos?ids=")||    Empresa .equals("https://www.cecra.mx/tools/pictures-urlProductos?ids=") || Empresa .equals("https://www.vipla.mx/tools/pictures-urlProductos?ids=")){
            EmpresaAd = "";
            EmpresaAd=listaproductos.get(position).getUrl();
        }else  if (!Empresa.equals("https://vazlo.com.mx/assets/img/productos/chica/jpg/")){
            EmpresaAd="";
            EmpresaAd=Empresa+listaproductos.get(position).getClave()+"/4.webp";

        }else {
            EmpresaAd = "";
            EmpresaAd = Empresa + listaproductos.get(position).getClave() + ".jpg";
        }



        holder.Parte.setText(listaproductos.get(position).getClave());
        if (!EmpresaAd.equals("")) {

            Picasso.with(context).
                    load(EmpresaAd)
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.loadingpro)
                    .fit()
                    .centerInside()
                    .into(holder.imgPro);
        }else{
            Picasso.with(context).
                    load(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.loadingpro)
                    .fit()
                    .centerInside()
                    .into(holder.imgPro);
        }

    }



    @Override
    public int getItemCount() {
        return listaproductos.size();
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

    public static class ViewHolderProductosNuevos extends RecyclerView.ViewHolder {
        TextView Parte;
        ImageView imgPro;


        public ViewHolderProductosNuevos(View itemView) {
            super(itemView);
            imgPro =  itemView.findViewById(R.id.productoImag);
            Parte =  itemView.findViewById(R.id.Parte);
        }
    }
}
