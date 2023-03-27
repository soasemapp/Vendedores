package com.example.kepler201.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ConsulPediSANDG;
import com.example.kepler201.SetterandGetter.ExistClasifiSANDG;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdaptadorExisClaPro extends RecyclerView.Adapter<AdaptadorExisClaPro.ViewHolderExistClas> implements View.OnClickListener {

    ArrayList<ExistClasifiSANDG> listaClaExi;
    Context context;
    private View.OnClickListener listener;

    public AdaptadorExisClaPro(ArrayList<ExistClasifiSANDG> listaClientes, Context context) {
        this.listaClaExi = listaClientes;
        this.context = context ;
    }

    @NonNull
    @Override
    public AdaptadorExisClaPro.ViewHolderExistClas onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_exiscla, null, false);
        view.setOnClickListener(this);
        return new ViewHolderExistClas(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorExisClaPro.ViewHolderExistClas holder, int position) {

        holder.Clave.setText(listaClaExi.get(position).getProducto());
        holder.Precio.setText(Html.fromHtml("Precio:<br> <font color ='#000000'>$</font><font color ='#4CAF50'>" +listaClaExi.get(position).getPrecio()+"</font>"));
        holder.Descri.setText(Html.fromHtml("Descripcion:<br> <font color ='#000000'>" +listaClaExi.get(position).getDescripcion()+"</font>"));
        holder.CodBar.setText(Html.fromHtml("Codigo de Barras:<br> <font color ='#000000'>" +listaClaExi.get(position).getCodigoBar()+"</font>"));
        holder.Existencia.setText(Html.fromHtml("Existencia: <font color ='#000000'>" +listaClaExi.get(position).getExistencia1()+"</font>"));
        Picasso.with(context).
                load("https://www.pressa.mx/es-mx/img/products/xl/"+listaClaExi.get(position).getProducto()+"/4.webp")
                .error(R.drawable.ic_baseline_error_24)
                .fit()
                .centerInside()
                .into(holder.prodocuImag);

    }


    @Override
    public int getItemCount() {
        return listaClaExi.size();
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

    public static class ViewHolderExistClas extends RecyclerView.ViewHolder {
        TextView Clave, Precio, Descri, CodBar,Existencia;
        ImageView prodocuImag;

        public ViewHolderExistClas(View itemView) {
            super(itemView);
            Clave =  itemView.findViewById(R.id.Clave);
            Precio =  itemView.findViewById(R.id.Precio);
            Descri =  itemView.findViewById(R.id.Descr);
            CodBar =  itemView.findViewById(R.id.CodBar);
            Existencia=itemView.findViewById(R.id.Existencia1);
            prodocuImag = itemView.findViewById(R.id.productoImag);
        }
    }


}
