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
import com.example.kepler201.SetterandGetter.ListPrecSANDG;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdapterListPrecio extends RecyclerView.Adapter<AdapterListPrecio.ViewHolderListPrecio> implements View.OnClickListener {

    ArrayList<ListPrecSANDG> listaLisPrec;
    private View.OnClickListener listener;
    Context context;
    String Empresa="";
    String EmpresaNuevaa="";


    public AdapterListPrecio(ArrayList<ListPrecSANDG> listaLisPrec, Context context,String empresa) {
        this.listaLisPrec = listaLisPrec;
        this.context = context;
        this.Empresa=empresa;
        this.EmpresaNuevaa=empresa;
    }

    @NonNull
    @Override
    public AdapterListPrecio.ViewHolderListPrecio onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_listprecio, null, false);
        view.setOnClickListener(this);
        return new ViewHolderListPrecio(view);
    }

    @Override
    public void onBindViewHolder(AdapterListPrecio.ViewHolderListPrecio holder, int position) {
        holder.Clave.setText(listaLisPrec.get(position).getCodeProdu());
        holder.Nom.setText(Html.fromHtml("Descripcion: <br> <font color ='#000000'>" +listaLisPrec.get(position).getNomProd()+"</font>"));
        holder.CodBarras.setText(Html.fromHtml("Codigo de Barras: <br> <font color ='#000000'>" +listaLisPrec.get(position).getCodBarras()+"</font>"));
        holder.Importe.setText(Html.fromHtml("Importe: <br> <font color ='#000000'>$</font><font color ='#4CAF50'>" +formatNumberCurrency(listaLisPrec.get(position).getImporte())+"</font>"));

        if (!EmpresaNuevaa.equals("https://vazlo.com.mx/assets/img/productos/chica/jpg/")){
            Empresa="";
            Empresa=EmpresaNuevaa+listaLisPrec.get(position).getCodeProdu()+"/4.webp";
        }else{
            Empresa="";
            Empresa=EmpresaNuevaa+listaLisPrec.get(position).getCodeProdu()+".jpg";

        }


        Picasso.with(context).
                load(Empresa)
                .error(R.drawable.ic_baseline_error_24)
                .fit()
                .centerInside()
                .into(holder.prodocuImag);
        holder.Existencia.setText(Html.fromHtml("Existencia:<font color ='#000000'>" +listaLisPrec.get(position).getExistencia1()+"</font>"));




    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    @Override
    public int getItemCount() {
        return listaLisPrec.size();
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

    public static class ViewHolderListPrecio extends RecyclerView.ViewHolder {
        TextView Clave, Nom, CodBarras, Importe,Existencia;
        ImageView prodocuImag;


        public ViewHolderListPrecio(View itemView) {
            super(itemView);

            Clave = itemView.findViewById(R.id.Clave);
            Nom = itemView.findViewById(R.id.NomProdu);
            CodBarras = itemView.findViewById(R.id.CodigoBarras);
            Importe = itemView.findViewById(R.id.Importe);
            Existencia=itemView.findViewById(R.id.Existencia1);
            prodocuImag = itemView.findViewById(R.id.productoImag);

        }
    }


}
