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
import com.example.kepler201.SetterandGetter.ConsulFacfturasSANDG;

import java.text.DecimalFormat;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class AdaptadorConsulFacturas extends RecyclerView.Adapter<AdaptadorConsulFacturas.ViewHolderConsulFacturas> implements View.OnClickListener {


    ArrayList<ConsulFacfturasSANDG> listasearch;
    private View.OnClickListener listener;

    public AdaptadorConsulFacturas(ArrayList<ConsulFacfturasSANDG> listasearch) {
        this.listasearch = listasearch;
    }

    @NonNull
    @Override
    public AdaptadorConsulFacturas.ViewHolderConsulFacturas onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_consultafacturas, null, false);
        view.setOnClickListener(this);
        return new ViewHolderConsulFacturas(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorConsulFacturas.ViewHolderConsulFacturas holder, int position) {
        holder.Clave.setText(listasearch.get(position).getFoliodelDocumento());
        holder.Nombre.setText(Html.fromHtml("Clave:<br> <font color ='#000000'>" + listasearch.get(position).getCliente() + "</font>"));
        holder.Folio.setText(Html.fromHtml("Nombre:<br> <font color ='#000000'>" + listasearch.get(position).getNombreCliente() + "</font>"));
       // holder.FechaF.setText(Html.fromHtml("Folio:<br> <font color ='#000000'>" + listasearch.get(position).getFoliodelDocumento() + "</font>"));
        holder.Plazo.setText(Html.fromHtml("Fecha:<br> <font color ='#000000'>" + listasearch.get(position).getFechadeDocumento() + "</font>"));
        holder.FechaVen.setText(Html.fromHtml("Plazo:<br> <font color ='#000000'>" + listasearch.get(position).getPlazo() + "</font>"));
        holder.Monto.setText(Html.fromHtml("Monto:<br> <font color=#000000>$</font> <font color ='#4CAF50'>" + formatNumberCurrency(listasearch.get(position).getMonto()) + "</font>"));
        holder.Saldo.setText(Html.fromHtml("Saldo:<br>" + (listasearch.get(position).getSaldo().equals("0") ? "<font color='#F32121'> PAGADO </font>" : "$<font color='#4CAF50'>" + formatNumberCurrency(listasearch.get(position).getSaldo())) + "</font>"));
        holder.Sucursal.setText(Html.fromHtml("Sucursal:<br> <font color ='#000000'>" + listasearch.get(position).getNomSuc() + "</font>"));
//(listasearch.get(position).getSaldo().equals("0")) ? "SALDO:PAGADO" : "Saldo: $" + formatNumberCurrency(listasearch.get(position).getSaldo())
    }

    //
    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }


    @Override
    public int getItemCount() {
        return listasearch.size();
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

    public static class ViewHolderConsulFacturas extends RecyclerView.ViewHolder {
        TextView Clave, Nombre, Folio, FechaF, Plazo, FechaVen, Saldo, Monto, Sucursal;

        public ViewHolderConsulFacturas(View itemView) {
            super(itemView);
            Clave =  itemView.findViewById(R.id.Clave);
            Nombre =  itemView.findViewById(R.id.NomCliente);
            Folio =  itemView.findViewById(R.id.Folio);
            //FechaF =  itemView.findViewById(R.id.Fecha);
            Plazo =  itemView.findViewById(R.id.Plazo);
            FechaVen =  itemView.findViewById(R.id.FechaVenci);
            Saldo =  itemView.findViewById(R.id.Saldo);
            Monto =  itemView.findViewById(R.id.Monto);
            Sucursal =  itemView.findViewById(R.id.Sucursal);

        }
    }
}
