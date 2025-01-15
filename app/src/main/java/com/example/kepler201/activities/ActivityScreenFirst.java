package com.example.kepler201.activities;

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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.PresupuestoLineaSANDG;
import com.example.kepler201.SetterandGetter.ProspectSANDG;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class ActivityScreenFirst extends AppCompatActivity {


    ArrayList<PresupuestoLineaSANDG> listaPresupuesto = new ArrayList<>();
    ArrayList<AgendaSANDG> listaAgenda = new ArrayList<>();
    ArrayList<ProspectSANDG> listaProspectos = new ArrayList<>();
    BarChart barChart;
    private TableLayout tableLayout;
    private TableLayout tableLayoutpros;
    Button plusAgenda;
    Button plusPorspect;
    LinearLayout Agen;
    LinearLayout Prospect;
    TextView txtFecha, txtClientes, txtNombreCliente, txtActividad, txtEstatus;
    TextView txtNombre, txtCotizacion, txtTarea, txtComentario;
    TableRow fila;
    TableRow filapros;
    EditText fecha;

    boolean vermas1 = true;
    boolean vermas2 = true;

    //TextView
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;
    @SuppressWarnings("NonAsciiCharacters")
    String StrAño = "";
    String StrMes = "";
    String StrDia = "";
    String StrFecha = "";
    String StrFecha2 = "";
    String mensaje = "";
    float EaglePor = 0;
    float VazloPor =0;
    float RodatechPor = 0;
    float PartechPor = 0;
    float SharkPor = 0;
    float TrackonePor = 0;
    float Mechanic=0;

    float GSPAMORTIGUADOR=0;
    float GSPHM=0;
    float GSPSUSPENSION=0;
    float GSPRODAMIENTOS=0;
    float GSPTRACCION=0;
    float Zoms=0;
    float Kff=0;
    AlertDialog mDialog;
    String date;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_first);
        MyToolbar.show(this, "Ventas del Mes", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);


        mDialog = new SpotsDialog(ActivityScreenFirst.this);
        mDialog.setCancelable(false);
        barChart = findViewById(R.id.barChart);

        Button btnsearch = findViewById(R.id.btnSearch);
        EditText vendedi = findViewById(R.id.ediVend);
        fecha = findViewById(R.id.fecha);
        tableLayoutpros = (findViewById(R.id.table1));
        tableLayout = findViewById(R.id.table);
        plusAgenda = findViewById(R.id.plusAgenda);
        plusPorspect = findViewById(R.id.plusProspectos);
        Agen = findViewById(R.id.agendatabla);
        Prospect = findViewById(R.id.ProspectTarea);

        strusr = preference.getString("user", "null");
        strpass = preference.getString("pass", "null");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strtype = preference.getString("type", "null");
        strbran = preference.getString("branch", "null");
        strma = preference.getString("email", "null");
        strcodBra = preference.getString("codBra", "null");
        strco = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");


        plusAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vermas1) {
                    Agen.setVisibility(View.VISIBLE);
                    plusAgenda.setText("-");
                    vermas1 = false;
                } else {
                    Agen.setVisibility(View.GONE);
                    plusAgenda.setText("+");
                    vermas1 = true;
                }
            }
        });

        plusPorspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vermas2) {
                    Prospect.setVisibility(View.VISIBLE);
                    plusPorspect.setText("-");
                    vermas2 = false;
                } else {
                    Prospect.setVisibility(View.GONE);
                    plusPorspect.setText("+");
                    vermas2 = true;
                }
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViews();
                listaAgenda.clear();
                tableLayoutpros.removeAllViews();
                listaProspectos.clear();
                StrFecha = fecha.getText().toString();
                StrFecha2 = fecha.getText().toString();
                if (!StrFecha.isEmpty()) {
                    ActivityScreenFirst.AsyncCallWS2 task2 = new ActivityScreenFirst.AsyncCallWS2();
                    task2.execute();
                    ActivityScreenFirst.AsyncCallWS3 task3 = new ActivityScreenFirst.AsyncCallWS3();
                    task3.execute();
                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityScreenFirst.this);
                    alerta.setMessage("Ingrese una Fecha").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("!ERROR!");
                    titulo.show();

                }


            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityScreenFirst.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        fecha.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        vendedi.setText(strname + " " + strlname);


        Calendar c = Calendar.getInstance();
        //noinspection NonAsciiCharacters
        @SuppressLint("SimpleDateFormat") SimpleDateFormat AÑO = new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat MES = new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat DIA = new SimpleDateFormat("DD");
        StrAño = AÑO.format(c.getTime());
        StrMes = MES.format(c.getTime());

        StrFecha = StrAño + "-" + StrMes+"-01";
        StrAño = StrAño.substring(2, 4);


        ActivityScreenFirst.AsyncCallWS task = new ActivityScreenFirst.AsyncCallWS();
        task.execute();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());
        fecha.setText(fechaactual);
        StrFecha2 = fecha.getText().toString();
        ActivityScreenFirst.AsyncCallWS2 task2 = new ActivityScreenFirst.AsyncCallWS2();
        task2.execute();
        ActivityScreenFirst.AsyncCallWS3 task3 = new ActivityScreenFirst.AsyncCallWS3();
        task3.execute();


    }


    public void refre(View view) {
        Intent refres = new Intent(ActivityScreenFirst.this, ActivityScreenFirst.class);
        overridePendingTransition(0, 0);
        startActivity(refres);
        overridePendingTransition(0, 0);
        finish();
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
            String parametros = "fecha=" + StrFecha + "&vendedor=" + strco + "&mes=" +  StrMes + "&ano=" + StrAño;
            String url = "http://" + StrServer + "/graficaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    Log.d("JSON Response", jsonStr);


                    if (json.length() != 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaPresupuesto.add(new PresupuestoLineaSANDG((Numero.getString("k_Vendido").equals("") ? " " : Numero.getString("k_Vendido")),
                                    (Numero.getString("k_Linea").equals("") ? " " : Numero.getString("k_Linea")),
                                    (Numero.getString("k_Presupuesto").equals("") ? " " : Numero.getString("k_Presupuesto")),
                                    (Numero.getString("k_ProximidadRe").equals("") ? " " : Numero.getString("k_ProximidadRe")),
                                    (Numero.getString("k_Lineaname").equals("") ? " " : Numero.getString("k_Lineaname"))));


                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
                            alerta1.setMessage("El Json tiene un problema lol q mal").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {

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
                        mDialog.dismiss();
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
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
            if (listaPresupuesto.size() != 0) {

                switch (StrServer) {
                    case "sprautomotive.servehttp.com:9090":
                    case "sprautomotive.servehttp.com:9075": {

                        float RodatechPresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());

                        float RodatechVendido = Float.parseFloat(listaPresupuesto.get(0).getVendido());

                        RodatechPor = (RodatechVendido * 100) / RodatechPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(RodaGr(), "Rodatech");

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.GREEN);

                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 1000,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.animateY(2000);
                        barChart.invalidate();
                        break;
                    }
                    case "sprautomotive.servehttp.com:9095": {

                        float PartechPresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());


                        float PartechVendido = Float.parseFloat(listaPresupuesto.get(0).getVendido());


                        PartechPor = (PartechVendido * 100) / PartechPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(ParteGr(), "Partech");

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.YELLOW);

                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 1000,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.animateY(2000);
                        barChart.invalidate();
                        break;
                    }
                    case "sprautomotive.servehttp.com:9080": {

                        float SharkPresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());

                        float SharkVendido = Float.parseFloat(listaPresupuesto.get(0).getVendido());


                        SharkPor = (SharkVendido * 100) / SharkPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(SharkGr(), "SHARK");

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.CYAN);

                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 1000,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.animateY(2000);
                        barChart.invalidate();
                        break;
                    }
                    case "vazlocolombia.dyndns.org:9085": {

                        float GeneralPresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());

                        float GenralVendido = Float.parseFloat(listaPresupuesto.get(0).getVendido());

                        RodatechPor = (GenralVendido * 100) / GeneralPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(RodaGr(), "Venta");

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.BLUE);

                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 1000,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);

                        barChart.animateY(2000);
                        barChart.invalidate();
                        break;
                    }
                    case "jacve.dyndns.org:9085": {


                        float GSPAMORTIGUADORPRESU = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());
                        float GSPHMPRESU = Float.parseFloat(listaPresupuesto.get(1).getPresopUesto());
                        float GSPSUSPENSIONPRESU = Float.parseFloat(listaPresupuesto.get(2).getPresopUesto());
                        float GSPRODAMIENTOSPRESU = Float.parseFloat(listaPresupuesto.get(3).getPresopUesto());
                        float GSPTRACCIONPRESU = Float.parseFloat(listaPresupuesto.get(4).getPresopUesto());
                        float MECHANICCHOICEPRESU = Float.parseFloat(listaPresupuesto.get(5).getPresopUesto());
                        float KFFPRESU = Float.parseFloat(listaPresupuesto.get(6).getPresopUesto());
                        float ZOMSPRESU = Float.parseFloat(listaPresupuesto.get(7).getPresopUesto());

                        float GSPAMORTIGUADORVendido = (Float.parseFloat(listaPresupuesto.get(0).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(0).getVendido()) : 0;
                        float GSPHMVendido = (Float.parseFloat(listaPresupuesto.get(1).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(1).getVendido()) : 0;
                        float GSPSUSPENSIONVendido = (Float.parseFloat(listaPresupuesto.get(2).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(2).getVendido()) : 0;
                        float GSPRODAMIENTOSVendido = (Float.parseFloat(listaPresupuesto.get(3).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(3).getVendido()) : 0;
                        float GSPTRACCIONVendido = (Float.parseFloat(listaPresupuesto.get(4).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(4).getVendido()) : 0;
                        float MECHANICCHOICEVendido = (Float.parseFloat(listaPresupuesto.get(5).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(5).getVendido()) : 0;
                        float KFFVendido = (Float.parseFloat(listaPresupuesto.get(6).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(6).getVendido()) : 0;
                        float ZOMSVendido = (Float.parseFloat(listaPresupuesto.get(7).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(7).getVendido()) : 0;

                        GSPAMORTIGUADOR= (GSPAMORTIGUADORVendido * 100) / GSPAMORTIGUADORPRESU;
                        GSPHM= (GSPHMVendido * 100) / GSPHMPRESU;
                        GSPSUSPENSION= (GSPSUSPENSIONVendido * 100) / GSPSUSPENSIONPRESU;
                        GSPRODAMIENTOS= (GSPRODAMIENTOSVendido * 100) / GSPRODAMIENTOSPRESU;
                        GSPTRACCION= (GSPTRACCIONVendido * 100) / GSPTRACCIONPRESU;
                        Mechanic = (MECHANICCHOICEVendido * 100) / MECHANICCHOICEPRESU;
                        Kff = (KFFVendido * 100) / KFFPRESU;
                        Zoms = (ZOMSVendido * 100) / ZOMSPRESU;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(barEntriesJacveGSPAMORTIGUADOR(), listaPresupuesto.get(0).getLineaName());
                        BarDataSet barDataSet2 = new BarDataSet(barEntriesJacveGSPHM(), listaPresupuesto.get(1).getLineaName());
                        BarDataSet barDataSet3 = new BarDataSet(barEntriesJacveGSPSUSPENSION(), listaPresupuesto.get(2).getLineaName());
                        BarDataSet barDataSet4 = new BarDataSet(barEntriesJacveGSPRODAMIENTOS(), listaPresupuesto.get(3).getLineaName());
                        BarDataSet barDataSet5 = new BarDataSet(barEntriesJacveGSPTRACCION(), listaPresupuesto.get(4).getLineaName());
                        BarDataSet barDataSet6 = new BarDataSet(barEntriesJacveMeca(), listaPresupuesto.get(5).getLineaName());
                        BarDataSet barDataSet7 = new BarDataSet(barEntriesJacveKFF(), listaPresupuesto.get(6).getLineaName());
                        BarDataSet barDataSet8 = new BarDataSet(barEntriesJacveZoms(), listaPresupuesto.get(7).getLineaName());


                        barDataSet0.setColor(Color.rgb(255, 0, 0));
                        barDataSet1.setColor(Color.rgb(255, 100, 0));
                        barDataSet2.setColor(Color.rgb(255, 243, 0));
                        barDataSet3.setColor(Color.rgb(205, 255, 0 ));
                        barDataSet4.setColor(Color.rgb(50, 255, 0 ));
                        barDataSet5.setColor(Color.rgb(0, 255, 85  ));
                        barDataSet6.setColor(Color.rgb(0, 255, 162  ));
                        barDataSet7.setColor(Color.rgb(0, 255, 228  ));
                        barDataSet8.setColor(Color.rgb(0, 174, 255  ));

                        barDataSet1.setValueTextSize(1);
                        barDataSet2.setValueTextSize(1);
                        barDataSet3.setValueTextSize(1);
                        barDataSet4.setValueTextSize(1);
                        barDataSet5.setValueTextSize(1);
                        barDataSet6.setValueTextSize(1);
                        barDataSet7.setValueTextSize(1);
                        barDataSet8.setValueTextSize(1);


                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);
                        barData.addDataSet(barDataSet2);
                        barData.addDataSet(barDataSet3);
                        barData.addDataSet(barDataSet4);
                        barData.addDataSet(barDataSet5);
                        barData.addDataSet(barDataSet6);
                        barData.addDataSet(barDataSet7);
                        barData.addDataSet(barDataSet8);


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 2500,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.setDrawGridBackground(false);
                        barChart.setDrawBarShadow(false);
                        barChart.animateY(2000);

                        barChart.invalidate();

                        break;
                    }   case "autodis.ath.cx:9085": {

                        float EaglePresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());
                        float TrackonePresu = Float.parseFloat(listaPresupuesto.get(1).getPresopUesto());
                        float VazloPresu = Float.parseFloat(listaPresupuesto.get(2).getPresopUesto());

                        float EagleVendido = (Float.parseFloat(listaPresupuesto.get(0).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(0).getVendido()) : 0;
                        float TrackoneVendido = (Float.parseFloat(listaPresupuesto.get(1).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(1).getVendido()) : 0;
                        float VazloVendido = (Float.parseFloat(listaPresupuesto.get(2).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(2).getVendido()) : 0;


                        EaglePor = (EagleVendido * 100) / EaglePresu;
                        TrackonePor = (TrackoneVendido * 100) / TrackonePresu;
                        VazloPor = (VazloVendido * 100) / VazloPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(barEntriesAutodisEagle(), listaPresupuesto.get(0).getLineaName());
                        BarDataSet barDataSet2 = new BarDataSet(barEntriesAutodisTrackone(), listaPresupuesto.get(1).getLineaName());
                        BarDataSet barDataSet3 = new BarDataSet(barEntriesAutodisVazlo(), listaPresupuesto.get(2).getLineaName());

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.BLUE);
                        barDataSet2.setColor(Color.MAGENTA);
                        barDataSet3.setColor(Color.GREEN);


                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);
                        barData.addDataSet(barDataSet2);
                        barData.addDataSet(barDataSet3);


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 1000,
                                /*height*/ 1000
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.animateY(2000);
                        barChart.invalidate();

                        break;
                    }
                    default: {


                        float EaglePresu = Float.parseFloat(listaPresupuesto.get(0).getPresopUesto());
                        float RodatechPresu = Float.parseFloat(listaPresupuesto.get(1).getPresopUesto());
                        float PartechPresu = Float.parseFloat(listaPresupuesto.get(2).getPresopUesto());
                        float SharkPresu = Float.parseFloat(listaPresupuesto.get(3).getPresopUesto());
                        float TrackonePresu = Float.parseFloat(listaPresupuesto.get(4).getPresopUesto());
                        float VazloPresu = Float.parseFloat(listaPresupuesto.get(5).getPresopUesto());

                        float EagleVendido = (Float.parseFloat(listaPresupuesto.get(0).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(0).getVendido()) : 0;
                        float RodatechVendido = (Float.parseFloat(listaPresupuesto.get(1).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(1).getVendido()) : 0;
                        float PartechVendido = (Float.parseFloat(listaPresupuesto.get(2).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(2).getVendido()) : 0;
                        float SharkVendido = (Float.parseFloat(listaPresupuesto.get(3).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(3).getVendido()) : 0;
                        float TrackoneVendido = (Float.parseFloat(listaPresupuesto.get(4).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(4).getVendido()) : 0;
                        float VazloVendido = (Float.parseFloat(listaPresupuesto.get(5).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(5).getVendido()) : 0;


                        EaglePor = (EagleVendido * 100) / EaglePresu;
                        RodatechPor = (RodatechVendido * 100) / RodatechPresu;
                        PartechPor = (PartechVendido * 100) / PartechPresu;
                        SharkPor = (SharkVendido * 100) / SharkPresu;
                        TrackonePor = (TrackoneVendido * 100) / TrackonePresu;
                        VazloPor = (VazloVendido * 100) / VazloPresu;

                        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                        BarDataSet barDataSet1 = new BarDataSet(barEntries1(), listaPresupuesto.get(0).getLineaName());
                        BarDataSet barDataSet2 = new BarDataSet(barEntries2(), listaPresupuesto.get(1).getLineaName());
                        BarDataSet barDataSet3 = new BarDataSet(barEntries3(), listaPresupuesto.get(2).getLineaName());
                        BarDataSet barDataSet4 = new BarDataSet(barEntries4(), listaPresupuesto.get(3).getLineaName());
                        BarDataSet barDataSet5 = new BarDataSet(barEntries5(), listaPresupuesto.get(4).getLineaName());
                        BarDataSet barDataSet6 = new BarDataSet(barEntries6(), listaPresupuesto.get(5).getLineaName());

                        barDataSet0.setColor(Color.RED);
                        barDataSet1.setColor(Color.BLUE);
                        barDataSet2.setColor(Color.MAGENTA);
                        barDataSet3.setColor(Color.GREEN);
                        barDataSet4.setColor(Color.YELLOW);
                        barDataSet5.setColor(Color.CYAN);
                        barDataSet6.setColor(Color.BLACK);

                        BarData barData = new BarData();
                        barData.addDataSet(barDataSet0);
                        barData.addDataSet(barDataSet1);
                        barData.addDataSet(barDataSet2);
                        barData.addDataSet(barDataSet3);
                        barData.addDataSet(barDataSet4);
                        barData.addDataSet(barDataSet5);
                        barData.addDataSet(barDataSet6);


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                /*width*/ 17*100,
                                /*height*/ 10*100,
                                /*weight*/ 1.0f
                        );
                        barChart.setLayoutParams(param);
                        barChart.setData(barData);
                        barChart.animateY(2000);
                        barChart.invalidate();

                        break;
                    }
                }



            } else {

switch (StrServer){
    case"jacve.dyndns.org:9085":



        BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
        BarDataSet barDataSet1 = new BarDataSet(barEntriesJacveGSPAMORTIGUADOR(), "GSP AMORTIGUADOR");
        BarDataSet barDataSet2 = new BarDataSet(barEntriesJacveGSPHM(), "GSP HM");
        BarDataSet barDataSet3 = new BarDataSet(barEntriesJacveGSPSUSPENSION(), "GSP SUSPENSION");
        BarDataSet barDataSet4 = new BarDataSet(barEntriesJacveGSPRODAMIENTOS(), "GSP RODAMIENTOS");
        BarDataSet barDataSet5 = new BarDataSet(barEntriesJacveGSPTRACCION(), "GSP TRACCION");
        BarDataSet barDataSet6 = new BarDataSet(barEntriesJacveMeca(), "Mechanic Choice");
        BarDataSet barDataSet7 = new BarDataSet(barEntriesJacveKFF(), "KFF");
        BarDataSet barDataSet8 = new BarDataSet(barEntriesJacveZoms(), "Zoms");


        barDataSet0.setColor(Color.rgb(255, 0, 0));
        barDataSet1.setColor(Color.rgb(255, 100, 0));
        barDataSet2.setColor(Color.rgb(255, 243, 0));
        barDataSet3.setColor(Color.rgb(205, 255, 0 ));
        barDataSet4.setColor(Color.rgb(50, 255, 0 ));
        barDataSet5.setColor(Color.rgb(0, 255, 85  ));
        barDataSet6.setColor(Color.rgb(0, 255, 162  ));
        barDataSet7.setColor(Color.rgb(0, 255, 228  ));
        barDataSet8.setColor(Color.rgb(0, 174, 255  ));

        BarData barData = new BarData();
        barData.addDataSet(barDataSet0);
        barData.addDataSet(barDataSet1);
        barData.addDataSet(barDataSet2);
        barData.addDataSet(barDataSet3);
        barData.addDataSet(barDataSet4);
        barData.addDataSet(barDataSet5);
        barData.addDataSet(barDataSet6);
        barData.addDataSet(barDataSet7);
        barData.addDataSet(barDataSet8);


        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.invalidate();
        break;
    case"autodis.ath.cx:9085":


        barDataSet0 = new BarDataSet(barEntries0(), "Meta");
        barDataSet1 = new BarDataSet(barEntriesAutodisEagle(),"Eagle");
        barDataSet6 = new BarDataSet(barEntriesAutodisTrackone(), "TrackOne");

        barDataSet0.setColor(Color.RED);
        barDataSet1.setColor(Color.BLUE);
        barDataSet6.setColor(Color.BLACK);

        barData = new BarData();
        barData.addDataSet(barDataSet0);
        barData.addDataSet(barDataSet1);
        barData.addDataSet(barDataSet6);

        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.invalidate();
        break;
    case"vazlocolombia.dyndns.org:9085":


      barDataSet0 = new BarDataSet(barEntries0(), "Meta");
      barDataSet1 = new BarDataSet(RodaGr(), "Venta");

        barDataSet0.setColor(Color.RED);
        barDataSet1.setColor(Color.BLUE);

        barData = new BarData();
        barData.addDataSet(barDataSet0);
        barData.addDataSet(barDataSet1);

        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.invalidate();
        break;
        case"sprautomotive.servehttp.com:9080":


             barDataSet0 = new BarDataSet(barEntries0(), "Meta");
             barDataSet1 = new BarDataSet(SharkGr(), "SHARK");

            barDataSet0.setColor(Color.RED);
            barDataSet1.setColor(Color.CYAN);

             barData = new BarData();
            barData.addDataSet(barDataSet0);
            barData.addDataSet(barDataSet1);

            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.invalidate();
            break;
        case"sprautomotive.servehttp.com:9095":


             barDataSet0 = new BarDataSet(barEntries0(), "Meta");
             barDataSet1 = new BarDataSet(ParteGr(), "Partech");

            barDataSet0.setColor(Color.RED);
            barDataSet1.setColor(Color.YELLOW);


             barData = new BarData();
            barData.addDataSet(barDataSet0);
            barData.addDataSet(barDataSet1);

            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.invalidate();
            break;
        case "sprautomotive.servehttp.com:9090":

             barDataSet0 = new BarDataSet(barEntries0(), "Meta");
             barDataSet1 = new BarDataSet(RodaGr(), "Rodatech");

            barDataSet0.setColor(Color.RED);
            barDataSet1.setColor(Color.GREEN);

            barData = new BarData();
            barData.addDataSet(barDataSet0);
            barData.addDataSet(barDataSet1);

            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.invalidate();

            break;
    default:

        EaglePor = 0;
        RodatechPor = 0;
        PartechPor = 0;
        SharkPor = 0;
        TrackonePor = 0;

        barDataSet0 = new BarDataSet(barEntries0(), "Meta");
        barDataSet1 = new BarDataSet(barEntries1(), "Eagle");
        barDataSet2 = new BarDataSet(barEntries2(), "Rodatech");
        barDataSet3 = new BarDataSet(barEntries3(), "Partech");
        barDataSet4 = new BarDataSet(barEntries4(), "Shark");
        barDataSet5 = new BarDataSet(barEntries5(), "TrackOne");

        barDataSet0.setColor(Color.RED);
        barDataSet1.setColor(Color.BLUE);
        barDataSet2.setColor(Color.GREEN);
        barDataSet3.setColor(Color.YELLOW);
        barDataSet4.setColor(Color.CYAN);
        barDataSet5.setColor(Color.BLACK);

        barData = new BarData();
        barData.addDataSet(barDataSet0);
        barData.addDataSet(barDataSet1);
        barData.addDataSet(barDataSet2);
        barData.addDataSet(barDataSet3);
        barData.addDataSet(barDataSet4);
        barData.addDataSet(barDataSet5);

        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.invalidate();
        break;
}


            }
            mDialog.dismiss();
        }


    }


    private ArrayList<BarEntry> barEntries0() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 100));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntries1() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, EaglePor));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntries2() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(3, RodatechPor));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntries3() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4, PartechPor));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntries4() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(5, SharkPor));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntries5() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(6, TrackonePor));
        return barEntries;
    }


    private ArrayList<BarEntry> barEntries6() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(7, VazloPor));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntriesAutodisEagle() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, EaglePor));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesAutodisTrackone() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(3, TrackonePor));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesAutodisVazlo() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4, VazloPor));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveGSPAMORTIGUADOR() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, GSPAMORTIGUADOR));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveGSPHM() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(3, GSPHM));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveGSPSUSPENSION() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4, GSPSUSPENSION));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveGSPRODAMIENTOS() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(5, GSPRODAMIENTOS));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntriesJacveGSPTRACCION() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(6, GSPTRACCION));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveMeca() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(7, Mechanic));
        return barEntries;
    }


    private ArrayList<BarEntry> barEntriesJacveKFF() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(8, Kff));
        return barEntries;
    }
    private ArrayList<BarEntry> barEntriesJacveZoms() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(9, Zoms));
        return barEntries;
    }
    private ArrayList<BarEntry> RodaGr() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, RodatechPor));
        return barEntries;
    }

    private ArrayList<BarEntry> ParteGr() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, PartechPor));
        return barEntries;
    }


    private ArrayList<BarEntry> SharkGr() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2, SharkPor));
        return barEntries;
    }




    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strco + "&fecha=" + StrFecha2;
            String url = "http://" + StrServer + "/agendaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if (json.length() != 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaAgenda.add(new AgendaSANDG(
                                    Numero.getString("fecha"),
                                    Numero.getString("cliente"),
                                    Numero.getString("nombre"),
                                    Numero.getString("actividad"),
                                    Numero.getString("estatus"),
                                    Numero.getString("comentario")));
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
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

                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
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


        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            if (listaAgenda.size() > 0) {
                TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableLayout.setBackgroundColor(Color.parseColor("#043B72"));
                for (int i = -1; i < listaAgenda.size(); i++) {
                    fila = new TableRow(getApplicationContext());
                    fila.setLayoutParams(layaoutFila);
                    txtFecha = new TextView(getApplicationContext());
                    if (i == -1) {
                        txtFecha.setText("Fecha");
                        txtFecha.setGravity(Gravity.START);
                        txtFecha.setBackgroundColor(Color.BLUE);
                        txtFecha.setTextColor(Color.WHITE);
                        txtFecha.setPadding(20, 20, 20, 20);
                        txtFecha.setLayoutParams(layaoutDes);
                        fila.addView(txtFecha);

                        txtClientes = new TextView(getApplicationContext());
                        txtClientes.setText("Clave-Cli");
                        txtClientes.setGravity(Gravity.START);
                        txtClientes.setBackgroundColor(Color.BLUE);
                        txtClientes.setTextColor(Color.WHITE);
                        txtClientes.setPadding(20, 20, 20, 20);
                        txtClientes.setLayoutParams(layaoutDes);
                        fila.addView(txtClientes);


                        txtNombreCliente = new TextView(getApplicationContext());
                        txtNombreCliente.setText("Nombre");
                        txtNombreCliente.setGravity(Gravity.START);
                        txtNombreCliente.setBackgroundColor(Color.BLUE);
                        txtNombreCliente.setTextColor(Color.WHITE);
                        txtNombreCliente.setPadding(20, 20, 20, 20);
                        txtNombreCliente.setLayoutParams(layaoutDes);
                        fila.addView(txtNombreCliente);

                        txtActividad = new TextView(getApplicationContext());
                        txtActividad.setText("Actividad");
                        txtActividad.setGravity(Gravity.START);
                        txtActividad.setBackgroundColor(Color.BLUE);
                        txtActividad.setTextColor(Color.WHITE);
                        txtActividad.setPadding(20, 20, 20, 20);
                        txtActividad.setLayoutParams(layaoutDes);
                        fila.addView(txtActividad);

                        txtEstatus = new TextView(getApplicationContext());
                        txtEstatus.setText("Estatus");
                        txtEstatus.setGravity(Gravity.START);
                        txtEstatus.setBackgroundColor(Color.BLUE);
                        txtEstatus.setTextColor(Color.WHITE);
                        txtEstatus.setPadding(20, 20, 20, 20);
                        txtEstatus.setLayoutParams(layaoutDes);
                        fila.addView(txtEstatus);

                    } else {


                        txtFecha.setBackgroundColor(Color.GRAY);
                        txtFecha.setGravity(Gravity.START);
                        txtFecha.setText(listaAgenda.get(i).getFecha());
                        txtFecha.setPadding(20, 20, 20, 20);
                        txtFecha.setTextColor(Color.WHITE);
                        txtFecha.setLayoutParams(layaoutDes);
                        fila.addView(txtFecha);


                        txtClientes = new TextView(getApplicationContext());
                        txtClientes.setBackgroundColor(Color.BLACK);
                        txtClientes.setGravity(Gravity.START);
                        txtClientes.setText(listaAgenda.get(i).getCliente());
                        txtClientes.setPadding(20, 20, 20, 20);
                        txtClientes.setTextColor(Color.WHITE);
                        txtClientes.setLayoutParams(layaoutDes);
                        fila.addView(txtClientes);


                        txtNombreCliente = new TextView(getApplicationContext());
                        txtNombreCliente.setBackgroundColor(Color.GRAY);
                        txtNombreCliente.setGravity(Gravity.START);
                        txtNombreCliente.setText(listaAgenda.get(i).getClienNom());
                        txtNombreCliente.setPadding(20, 20, 20, 20);
                        txtNombreCliente.setTextColor(Color.WHITE);
                        txtNombreCliente.setLayoutParams(layaoutDes);
                        fila.addView(txtNombreCliente);


                        txtActividad = new TextView(getApplicationContext());
                        txtActividad.setBackgroundColor(Color.BLACK);
                        txtActividad.setGravity(Gravity.START);
                        txtActividad.setText(listaAgenda.get(i).getActividad());
                        txtActividad.setPadding(20, 20, 20, 20);
                        txtActividad.setTextColor(Color.WHITE);
                        txtActividad.setLayoutParams(layaoutDes);
                        fila.addView(txtActividad);


                        txtEstatus = new TextView(getApplicationContext());
                        txtEstatus.setBackgroundColor(Color.GRAY);
                        txtEstatus.setGravity(Gravity.END);
                        txtEstatus.setText(listaAgenda.get(i).getEstatus());
                        txtEstatus.setPadding(20, 20, 20, 20);
                        txtEstatus.setTextColor(Color.WHITE);
                        txtEstatus.setLayoutParams(layaoutDes);
                        fila.addView(txtEstatus);

                        fila.setPadding(2, 2, 2, 2);

                    }
                    tableLayout.addView(fila);

                }
                mDialog.dismiss();
                Agen.setVisibility(View.VISIBLE);
            } else {

            }

            mDialog.dismiss();
        }
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strco + "&fecha=" + StrFecha2;
            String url = "http://" + StrServer + "/prospectoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if (json.length() != 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaProspectos.add(new ProspectSANDG((Numero.getString("k_Nombre").equals("") ? " " : Numero.getString("k_Nombre")),
                                    (Numero.getString("k_Cotizacion").equals("") ? " " : Numero.getString("k_Cotizacion")),
                                    (Numero.getString("k_Tarea").equals("") ? " " : Numero.getString("k_Tarea")),
                                    (Numero.getString("k_Comentario").equals("") ? " " : Numero.getString("k_Comentario"))));

                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
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
                        mDialog.dismiss();
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityScreenFirst.this);
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


        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            if (listaProspectos.size() > 0) {
                TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableLayoutpros.setBackgroundColor(Color.parseColor("#043B72"));
                for (int i = -1; i < listaProspectos.size(); i++) {
                    filapros = new TableRow(getApplicationContext());
                    filapros.setLayoutParams(layaoutFila);
                    txtNombre = new TextView(getApplicationContext());
                    if (i == -1) {
                        txtNombre.setText("Nombre");
                        txtNombre.setGravity(Gravity.START);
                        txtNombre.setBackgroundColor(Color.BLUE);
                        txtNombre.setTextColor(Color.WHITE);
                        txtNombre.setPadding(20, 20, 20, 20);
                        txtNombre.setLayoutParams(layaoutDes);
                        filapros.addView(txtNombre);

                        txtCotizacion = new TextView(getApplicationContext());
                        txtCotizacion.setText("Cotizacion");
                        txtCotizacion.setGravity(Gravity.START);
                        txtCotizacion.setBackgroundColor(Color.BLUE);
                        txtCotizacion.setTextColor(Color.WHITE);
                        txtCotizacion.setPadding(20, 20, 20, 20);
                        txtCotizacion.setLayoutParams(layaoutDes);
                        filapros.addView(txtCotizacion);


                        txtTarea = new TextView(getApplicationContext());
                        txtTarea.setText("Tarea");
                        txtTarea.setGravity(Gravity.START);
                        txtTarea.setBackgroundColor(Color.BLUE);
                        txtTarea.setTextColor(Color.WHITE);
                        txtTarea.setPadding(20, 20, 20, 20);
                        txtTarea.setLayoutParams(layaoutDes);
                        filapros.addView(txtTarea);

                        txtComentario = new TextView(getApplicationContext());
                        txtComentario.setText("Comentario");
                        txtComentario.setGravity(Gravity.START);
                        txtComentario.setBackgroundColor(Color.BLUE);
                        txtComentario.setTextColor(Color.WHITE);
                        txtComentario.setPadding(20, 20, 20, 20);
                        txtComentario.setLayoutParams(layaoutDes);
                        filapros.addView(txtComentario);

                    } else {


                        txtNombre.setBackgroundColor(Color.GRAY);
                        txtNombre.setGravity(Gravity.START);
                        txtNombre.setText(listaProspectos.get(i).getNombre());
                        txtNombre.setPadding(20, 20, 20, 20);
                        txtNombre.setTextColor(Color.WHITE);
                        txtNombre.setLayoutParams(layaoutDes);
                        filapros.addView(txtNombre);


                        txtCotizacion = new TextView(getApplicationContext());
                        txtCotizacion.setBackgroundColor(Color.BLACK);
                        txtCotizacion.setGravity(Gravity.START);
                        txtCotizacion.setText(listaProspectos.get(i).getCotizacion());
                        txtCotizacion.setPadding(20, 20, 20, 20);
                        txtCotizacion.setTextColor(Color.WHITE);
                        txtCotizacion.setLayoutParams(layaoutDes);
                        filapros.addView(txtCotizacion);


                        txtTarea = new TextView(getApplicationContext());
                        txtTarea.setBackgroundColor(Color.GRAY);
                        txtTarea.setGravity(Gravity.START);
                        txtTarea.setText(listaProspectos.get(i).getTarea());
                        txtTarea.setPadding(20, 20, 20, 20);
                        txtTarea.setTextColor(Color.WHITE);
                        txtTarea.setLayoutParams(layaoutDes);
                        filapros.addView(txtTarea);


                        txtComentario = new TextView(getApplicationContext());
                        txtComentario.setBackgroundColor(Color.BLACK);
                        txtComentario.setGravity(Gravity.START);
                        txtComentario.setMaxLines(1);
                        txtComentario.setText(listaProspectos.get(i).getComentario());
                        txtComentario.setPadding(20, 20, 20, 20);
                        txtComentario.setTextColor(Color.WHITE);
                        txtComentario.setLayoutParams(layaoutDes);
                        filapros.addView(txtComentario);
                        filapros.setPadding(2, 2, 2, 2);

                    }
                    tableLayoutpros.addView(filapros);

                }
                mDialog.dismiss();
                Prospect.setVisibility(View.VISIBLE);

            } else {

            }
            mDialog.dismiss();

        }
    }


}