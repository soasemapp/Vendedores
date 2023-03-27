package com.example.kepler201.activities.Pedidos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorConsulCoti;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ConsulCotiSANDG;
import com.example.kepler201.XMLS.xmlConsulCoti;
import com.example.kepler201.includes.MyToolbar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityConsulCoti extends AppCompatActivity {
    ImageView SeguimiPed, ConsulPed, ConsulCot;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String mensaje = "";
    AlertDialog mDialog;
    ArrayList<ConsulCotiSANDG> listaConsulCoti = new ArrayList<>();
    RecyclerView recyclerConsulCoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consul_coti);

        MyToolbar.show(this, "Cotizaciones", true);
        mDialog = new SpotsDialog.Builder().setContext(ActivityConsulCoti.this).setMessage("Espere un momento...").build();

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        strusr = preference.getString("user", "null");
        strpass = preference.getString("pass", "null");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strtype = preference.getString("type", "null");
        strbran = preference.getString("branch", "null");
        strma = preference.getString("email", "null");
        strcodBra = preference.getString("codBra", "null");
        strcode = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");


        SeguimiPed =  findViewById(R.id.seguPed);
        ConsulCot =  findViewById(R.id.ConsulCoti);
        ConsulPed =  findViewById(R.id.ConsulPed);
        recyclerConsulCoti =  findViewById(R.id.listCoti);


        SeguimiPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeguimiPed.setBackgroundColor(Color.GRAY);
                SeguimiPed.setBackgroundColor(Color.TRANSPARENT);
                Intent SeguPedi = new Intent(ActivityConsulCoti.this, ActivitySegumientoPedidos.class);
                overridePendingTransition(0, 0);
                startActivity(SeguPedi);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ConsulPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsulPed.setBackgroundColor(Color.GRAY);
                ConsulPed.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsulPed = new Intent(ActivityConsulCoti.this, ActivityConsulPedi.class);
                overridePendingTransition(0, 0);
                startActivity(ConsulPed);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ConsulCot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsulCot.setBackgroundColor(Color.GRAY);
                ConsulCot.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsulCoti = new Intent(ActivityConsulCoti.this, ActivityConsulCoti.class);
                overridePendingTransition(0, 0);
                startActivity(ConsulCoti);
                overridePendingTransition(0, 0);
                finish();

            }
        });


        listaConsulCoti = new ArrayList<>();
        recyclerConsulCoti.setLayoutManager(new LinearLayoutManager(this));

        ActivityConsulCoti.AsyncCallWS task = new ActivityConsulCoti.AsyncCallWS();
        task.execute();




    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conecta();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            if (listaConsulCoti.size() == 0) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsulCoti.this);
                alerta.setMessage("No se encontraron cotizaciones recientes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("No hay Cotizaciones");
                titulo.show();
            } else {
                AdaptadorConsulCoti adapter = new AdaptadorConsulCoti(listaConsulCoti);
                recyclerConsulCoti.setAdapter(adapter);
                mDialog.dismiss();
            }

        }
    }


    public void detalladoCoti(View view) {
        String ClaveFolDialog, ClaveNumDialog;

        int position = recyclerConsulCoti.getChildAdapterPosition(Objects.requireNonNull(recyclerConsulCoti.findContainingItemView(view)));
        ClaveFolDialog = listaConsulCoti.get(position).getFolio();
        ClaveNumDialog = listaConsulCoti.get(position).getSucursal();
        Intent Coti = new Intent(ActivityConsulCoti.this, ActivityDetallCoti.class);
        Coti.putExtra("Folio", ClaveFolDialog);
        Coti.putExtra("NumSucu", ClaveNumDialog);
        startActivity(Coti);
    }

    private void conecta() {
        String SOAP_ACTION = "ConsulCot";
        String METHOD_NAME = "ConsulCot";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConsulCoti soapEnvelope = new xmlConsulCoti(SoapEnvelope.VER11);
            soapEnvelope.xmlConsulCoti(strusr, strpass, strcodBra, strcode);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                listaConsulCoti.add(new ConsulCotiSANDG((response0.getPropertyAsString("k_Sucursal").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Sucursal")),
                        (response0.getPropertyAsString("k_Folio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Folio")),
                        (response0.getPropertyAsString("k_Fecha").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Fecha")),
                        (response0.getPropertyAsString("k_cCliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_cCliente")),
                        (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                        (response0.getPropertyAsString("k_Importe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Importe")),
                        (response0.getPropertyAsString("k_Piezas").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Piezas")),
                        (response0.getPropertyAsString("k_nomsucursal").equals("anyType{}") ? " " : response0.getPropertyAsString("k_nomsucursal")),
                        (response0.getPropertyAsString("k_comentario").equals("anyType{}") ? " " : response0.getPropertyAsString("k_comentario"))));



            }


        } catch (SoapFault | XmlPullParserException soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();
            soapFault.printStackTrace();
        } catch (IOException e) {
            mDialog.dismiss();
            mensaje = "No se encontro servidor";
            e.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
            mensaje = "Error:" + ex.getMessage();
        }
    }




}