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
import android.view.Gravity;
import android.view.View;
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
import com.example.kepler201.XMLS.xmlAgenda;
import com.example.kepler201.XMLS.xmlPresopuestoLinea;
import com.example.kepler201.XMLS.xmlPros;
import com.example.kepler201.includes.MyToolbar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
     String StrFecha = "";
     String StrFecha2= "";
     String mensaje = "";
     float EaglePor = 0;
     float RodatechPor = 0;
     float PartechPor = 0;
     float SharkPor = 0;
     float TrackonePor = 0;
     AlertDialog mDialog;
     String date;

     @SuppressLint("SetTextI18n")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_screen_first);
         MyToolbar.show(this, "Ventas del Mes", true);
         SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);


         mDialog = new SpotsDialog.Builder().setContext(ActivityScreenFirst.this).setMessage("Espere un momento...").build();

         barChart = findViewById(R.id.barChart);
         Button btnsearch = findViewById(R.id.btnSearch);
         EditText vendedi = findViewById(R.id.ediVend);
         fecha =  findViewById(R.id.fecha);
         tableLayoutpros = (findViewById(R.id.table1));
         tableLayout =  findViewById(R.id.table);
         plusAgenda =  findViewById(R.id.plusAgenda);
         plusPorspect =  findViewById(R.id.plusProspectos);
         Agen =  findViewById(R.id.agendatabla);
         Prospect =  findViewById(R.id.ProspectTarea);

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
                     StrFecha2= fecha.getText().toString();
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
         StrAño = AÑO.format(c.getTime());
         StrMes = MES.format(c.getTime());
         StrFecha = StrAño + "-" + StrMes;
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



     public void refre(View view){
         Intent refres = new Intent (ActivityScreenFirst.this ,ActivityScreenFirst.class);
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
             conecta();
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
                         float TrackonePresu = Float.parseFloat(listaPresupuesto.get(5).getPresopUesto());


                         float EagleVendido = (Float.parseFloat(listaPresupuesto.get(0).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(0).getVendido()) : 0;
                         float RodatechVendido = (Float.parseFloat(listaPresupuesto.get(1).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(1).getVendido()) : 0;
                         float PartechVendido = (Float.parseFloat(listaPresupuesto.get(2).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(2).getVendido()) : 0;
                         float SharkVendido = (Float.parseFloat(listaPresupuesto.get(3).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(3).getVendido()) : 0;
                         float TrackoneVendido = (Float.parseFloat(listaPresupuesto.get(5).getVendido()) > 0) ? Float.parseFloat(listaPresupuesto.get(5).getVendido()) : 0;


                         EaglePor = (EagleVendido * 100) / EaglePresu;
                         RodatechPor = (RodatechVendido * 100) / RodatechPresu;
                         PartechPor = (PartechVendido * 100) / PartechPresu;
                         SharkPor = (SharkVendido * 100) / SharkPresu;
                         TrackonePor = (TrackoneVendido * 100) / TrackonePresu;

                         BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                         BarDataSet barDataSet1 = new BarDataSet(barEntries1(), "Eagle");
                         BarDataSet barDataSet2 = new BarDataSet(barEntries2(), "Rodatech");
                         BarDataSet barDataSet3 = new BarDataSet(barEntries3(), "Partech");
                         BarDataSet barDataSet4 = new BarDataSet(barEntries4(), "Shark");
                         BarDataSet barDataSet5 = new BarDataSet(barEntries5(), "TrackOne");

                         barDataSet0.setColor(Color.RED);
                         barDataSet1.setColor(Color.BLUE);
                         barDataSet2.setColor(Color.GREEN);
                         barDataSet3.setColor(Color.YELLOW);
                         barDataSet4.setColor(Color.CYAN);
                         barDataSet5.setColor(Color.BLACK);

                         BarData barData = new BarData();
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



             } else {


                 EaglePor = 0;
                 RodatechPor = 0;
                 PartechPor = 0;
                 SharkPor = 0;
                 TrackonePor = 0;

                 BarDataSet barDataSet0 = new BarDataSet(barEntries0(), "Meta");
                 BarDataSet barDataSet1 = new BarDataSet(barEntries1(), "Eagle");
                 BarDataSet barDataSet2 = new BarDataSet(barEntries2(), "Rodatech");
                 BarDataSet barDataSet3 = new BarDataSet(barEntries3(), "Partech");
                 BarDataSet barDataSet4 = new BarDataSet(barEntries4(), "Shark");
                 BarDataSet barDataSet5 = new BarDataSet(barEntries5(), "TrackOne");

                 barDataSet0.setColor(Color.RED);
                 barDataSet1.setColor(Color.BLUE);
                 barDataSet2.setColor(Color.GREEN);
                 barDataSet3.setColor(Color.YELLOW);
                 barDataSet4.setColor(Color.CYAN);
                 barDataSet5.setColor(Color.BLACK);

                 BarData barData = new BarData();
                 barData.addDataSet(barDataSet0);
                 barData.addDataSet(barDataSet1);
                 barData.addDataSet(barDataSet2);
                 barData.addDataSet(barDataSet3);
                 barData.addDataSet(barDataSet4);
                 barData.addDataSet(barDataSet5);

                 barChart.setData(barData);
                 barChart.animateY(2000);
                 barChart.invalidate();

             }

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


     private void conecta() {
         String SOAP_ACTION = "RVPLYV";
         String METHOD_NAME = "RVPLYV";
         String NAMESPACE = "http://" + StrServer + "/WS75Branch/";
         String URL = "http://" + StrServer + "/WSk75Branch";


         try {

             SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
             xmlPresopuestoLinea soapEnvelope = new xmlPresopuestoLinea(SoapEnvelope.VER11);
             soapEnvelope.xmlPresopuesto(strusr, strpass, StrFecha, strco, StrMes, StrAño);
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
                 listaPresupuesto.add(new PresupuestoLineaSANDG((response0.getPropertyAsString("k_Vendido").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Vendido")),
                         (response0.getPropertyAsString("k_Linea").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Linea")),
                         (response0.getPropertyAsString("k_Presupuesto").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Presupuesto")),
                         (response0.getPropertyAsString("k_ProximidadRe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ProximidadRe"))));


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


     @SuppressWarnings("deprecation")
     @SuppressLint("StaticFieldLeak")
     private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

         @Override
         protected void onPreExecute() {
             mDialog.show();
         }

         @Override
         protected Void doInBackground(Void... params) {
             conecta2();
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


         }
     }

     private void conecta2() {
         String SOAP_ACTION = "Agen";
         String METHOD_NAME = "Agen";
         String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
         String URL = "http://" + StrServer + "/WSk75Branch";


         try {

             SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
             xmlAgenda soapEnvelope = new xmlAgenda(SoapEnvelope.VER11);
             soapEnvelope.xmlAgenda(strusr, strpass, strco, StrFecha2);
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
                 //(response0.getPropertyAsString("k_code").equals("anyType{}")?" ":response0.getPropertyAsString("k_Hora"))
                 listaAgenda.add(new AgendaSANDG((response0.getPropertyAsString("k_Fecha").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Fecha")),
                         (response0.getPropertyAsString("k_Cliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cliente")),
                         (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                         (response0.getPropertyAsString("k_Actividad").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Actividad")),
                         (response0.getPropertyAsString("k_Estatus").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Estatus")),
                         (response0.getPropertyAsString("k_Comentario").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Comentario"))));


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


     @SuppressWarnings("deprecation")
     @SuppressLint("StaticFieldLeak")
     private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

         @Override
         protected void onPreExecute() {
             mDialog.show();
         }

         @Override
         protected Void doInBackground(Void... params) {
             conecta3();
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


         }
     }

     private void conecta3() {
         String SOAP_ACTION = "Pros";
         String METHOD_NAME = "Pros";
         String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
         String URL = "http://" + StrServer + "/WSk75Branch";


         try {

             SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
             xmlPros soapEnvelope = new xmlPros(SoapEnvelope.VER11);
             soapEnvelope.xmlProspect(strusr, strpass, strco, StrFecha2);
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
                 //(response0.getPropertyAsString("k_code").equals("anyType{}")?" ":response0.getPropertyAsString("k_Hora"))
                 listaProspectos.add(new ProspectSANDG((response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                         (response0.getPropertyAsString("k_Cotizacion").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cotizacion")),
                         (response0.getPropertyAsString("k_Tarea").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Tarea")),
                         (response0.getPropertyAsString("k_Comentario").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Comentario"))));


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