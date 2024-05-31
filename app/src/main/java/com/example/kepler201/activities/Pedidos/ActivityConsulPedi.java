package com.example.kepler201.activities.Pedidos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorConsulPedi;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ConsulCotiSANDG;
import com.example.kepler201.SetterandGetter.ConsulPediSANDG;
import com.example.kepler201.XMLS.xmlConsulPedi;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityConsulPedi extends AppCompatActivity {



    ImageView SeguimiPed, ConsulPed, ConsulCot;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String FechaIncial;
    private EditText fechaEn;
    String date;
    String mensaje = "";
    AlertDialog mDialog;
    ArrayList<ConsulPediSANDG> listaConsulPedi = new ArrayList<>();
    RecyclerView recyclerConsulPedi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consul_pedi);

        MyToolbar.show(this, "Pedidos", true);
        mDialog = new SpotsDialog.Builder().setContext(ActivityConsulPedi.this).setMessage("Espere un momento...").build();
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

        Button btnsearch =  findViewById(R.id.btnSearch);
        SeguimiPed =  findViewById(R.id.seguPed);
        ConsulCot =  findViewById(R.id.ConsulCoti);
        ConsulPed =  findViewById(R.id.ConsulPed);
        recyclerConsulPedi =  findViewById(R.id.lisPedi);
        fechaEn =  findViewById(R.id.fecha);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);



        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsulPedi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        fechaEn.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        SeguimiPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeguimiPed.setBackgroundColor(Color.GRAY);
                SeguimiPed.setBackgroundColor(Color.TRANSPARENT);
                Intent SeguPedi = new Intent(ActivityConsulPedi.this, ActivitySegumientoPedidos.class);
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
                Intent ConsulPed = new Intent(ActivityConsulPedi.this, ActivityConsulPedi.class);
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
                Intent ConsulCoti = new Intent(ActivityConsulPedi.this, ActivityConsulCoti.class);
                overridePendingTransition(0, 0);
                startActivity(ConsulCoti);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    listaConsulPedi = new ArrayList<>();
                    recyclerConsulPedi.setLayoutManager(new LinearLayoutManager(ActivityConsulPedi.this));
                    FechaIncial = fechaEn.getText().toString();
                    ActivityConsulPedi.AsyncCallWS task = new ActivityConsulPedi.AsyncCallWS();
                    task.execute();

            }
        });

        Calendar c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());

        fechaEn.setText(fechaactual);
        FechaIncial = fechaEn.getText().toString();
        listaConsulPedi = new ArrayList<>();
        recyclerConsulPedi.setLayoutManager(new LinearLayoutManager(this));


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
            String parametros = "sucursal="+strcodBra+"&vendedor="+strcode+"&fecha="+FechaIncial;

            String url = "http://" + StrServer + "/pedidosapp?" + parametros;
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


                            listaConsulPedi.add(new ConsulPediSANDG((Numero.getString("k_Sucursal").equals("") ? " " : Numero.getString("k_Sucursal")),
                                    (Numero.getString("k_Folio").equals("") ? " " : Numero.getString("k_Folio")),
                                    (Numero.getString("k_Fecha").equals("") ? " " : Numero.getString("k_Fecha")),
                                    (Numero.getString("k_cCliente").equals("") ? " " : Numero.getString("k_cCliente")),
                                    (Numero.getString("k_Nombre").equals("") ? " " : Numero.getString("k_Nombre")),
                                    (Numero.getString("k_Importe").equals("") ? " " : Numero.getString("k_Importe")),
                                    (Numero.getString("k_Piezas").equals("") ? " " : Numero.getString("k_Piezas")),
                                    (Numero.getString("k_nomsucursal").equals("") ? " " : Numero.getString("k_nomsucursal")),
                                    (Numero.getString("k_comentario").equals("") ? " " : Numero.getString("k_comentario"))));


                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsulPedi.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsulPedi.this);
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

            if (listaConsulPedi.size() == 0) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsulPedi.this);
                alerta.setMessage("No se encontraron datos dentro de la fecha establecida").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        mDialog.dismiss();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Pedidos sin existencia");
                titulo.show();
            } else {
                AdaptadorConsulPedi adapter = new AdaptadorConsulPedi(listaConsulPedi);
                recyclerConsulPedi.setAdapter(adapter);
                mDialog.dismiss();
            }

        }
    }

    public void detalladoPedi(View view) {
        String ClaveFolDialog, ClaveNumDialog;

        int position = recyclerConsulPedi.getChildAdapterPosition(Objects.requireNonNull(recyclerConsulPedi.findContainingItemView(view)));

        ClaveFolDialog = listaConsulPedi.get(position).getFolio();
        ClaveNumDialog = listaConsulPedi.get(position).getSucursal();
        Intent Coti = new Intent(ActivityConsulPedi.this, ActivityDetallPedi.class);
        Coti.putExtra("Folio", ClaveFolDialog);
        Coti.putExtra("NumSucu", ClaveNumDialog);
        startActivity(Coti);
    }


}