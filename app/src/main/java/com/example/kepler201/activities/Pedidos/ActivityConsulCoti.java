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
import com.example.kepler201.SetterandGetter.SetGetListModelo2;
import com.example.kepler201.XMLS.xmlConsulCoti;
import com.example.kepler201.activities.BusquedaActivity;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;
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
        mDialog.setCancelable(false);
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
            HttpHandler sh = new HttpHandler();
            String parametros = "sucursal="+strcodBra+"&vendedor="+strcode;
            String url = "http://" + StrServer + "/cotizacionesapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                    jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");

                            listaConsulCoti.add(new ConsulCotiSANDG((Numero.getString("k_Sucursal").equals("") ? "" : Numero.getString("k_Sucursal")),
                                    (Numero.getString("k_Folio").equals("") ? "" : Numero.getString("k_Folio")),
                                    (Numero.getString("k_Fecha").equals("") ? "" : Numero.getString("k_Fecha")),
                                    (Numero.getString("k_cCliente").equals("") ? "" : Numero.getString("k_cCliente")),
                                    (Numero.getString("k_Nombre").equals("") ? "" : Numero.getString("k_Nombre")),
                                    (Numero.getString("k_Importe").equals("") ? "" : Numero.getString("k_Importe")),
                                    (Numero.getString("k_Piezas").equals("") ? "" : Numero.getString("k_Piezas")),
                                    (Numero.getString("k_nomsucursal").equals("") ? "" : Numero.getString("k_nomsucursal")),
                                    (Numero.getString("k_comentario").equals("") ? "" : Numero.getString("k_comentario"))));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsulCoti.this);
                            alerta1.setMessage("El Json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                }
                            });
                            AlertDialog titulo1 = alerta1.create();
                            titulo1.setTitle("Hubo un problema");
                            titulo1.show();

                        }//run
                    });
                }//catch JSON EXCEPTION
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsulCoti.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();

                    }//run
                });//runUniTthread
            }//else
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
                mDialog.dismiss();
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






}