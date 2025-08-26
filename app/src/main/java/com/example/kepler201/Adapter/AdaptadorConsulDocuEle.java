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
public class AdaptadorConsulDocuEle extends RecyclerView.Adapter<AdaptadorConsulDocuEle.ViewHolderConsulFacturas> implements View.OnClickListener {


    ArrayList<ConsulFacfturasSANDG> listasearch;
    private View.OnClickListener listener;

    public AdaptadorConsulDocuEle(ArrayList<ConsulFacfturasSANDG> listasearch) {
        this.listasearch = listasearch;
    }

    @NonNull
    @Override
    public AdaptadorConsulDocuEle.ViewHolderConsulFacturas onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_docelectro, null, false);
        view.setOnClickListener(this);
        return new ViewHolderConsulFacturas(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorConsulDocuEle.ViewHolderConsulFacturas holder, int position) {
        holder.Folio.setText(listasearch.get(position).getFoliodelDocumento());
        holder.ClaveCli.setText(Html.fromHtml("Clave:<br> <font color ='#000000'>" + listasearch.get(position).getCliente() + "</font>"));
        holder.NombreCli.setText(Html.fromHtml("Nombre:<br> <font color ='#000000'>" + listasearch.get(position).getNombreCliente() + "</font>"));
        holder.Plazo.setText(Html.fromHtml("Plazo:<br> <font color ='#000000'>" + listasearch.get(position).getPlazo() + "</font>"));
        holder.FechaFac.setText(Html.fromHtml("Fecha Realizada:<br> <font color ='#000000'>" + listasearch.get(position).getFechadeDocumento() + "</font>"));
        holder.FechaVen.setText(Html.fromHtml("Fecha Vencimiento:<br> <font color ='#000000'>" + listasearch.get(position).getFechadepago() + "</font>"));
        holder.Monto.setText(Html.fromHtml("Monto:<br> <font color=#000000>$</font> <font color ='#4CAF50'>" + formatNumberCurrency(listasearch.get(position).getMonto()) + "</font>"));
        holder.Saldo.setText(Html.fromHtml("Saldo:<br>" + (listasearch.get(position).getSaldo().equals("0") ? "<font color='#F32121'> PAGADO </font>" : "$<font color='#4CAF50'>" + formatNumberCurrency(listasearch.get(position).getSaldo())) + "</font>"));
        holder.UUID.setText(Html.fromHtml("UUID:<br> <font color ='#000000'>" + listasearch.get(position).getUUID() + "</font>"));
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
        TextView Folio,ClaveCli,NombreCli,Plazo,FechaFac,FechaVen,Monto,Saldo,UUID,Sucursal;

        public ViewHolderConsulFacturas(View itemView) {
            super(itemView);
            Folio =  itemView.findViewById(R.id.FOLIO);
            ClaveCli =  itemView.findViewById(R.id.CLAVECLIEN);
            NombreCli =  itemView.findViewById(R.id.NomCliente);
            Plazo =  itemView.findViewById(R.id.Plazo);
            FechaFac =  itemView.findViewById(R.id.FechaFactura);
            FechaVen =  itemView.findViewById(R.id.FechaVenci);
            Monto =  itemView.findViewById(R.id.Monto);
            Saldo =  itemView.findViewById(R.id.Saldo);
            UUID =  itemView.findViewById(R.id.UUID);
            Sucursal =  itemView.findViewById(R.id.Sucursal);

        }
    }
}
