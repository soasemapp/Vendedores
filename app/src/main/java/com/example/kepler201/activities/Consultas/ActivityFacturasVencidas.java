package com.example.kepler201.activities.Consultas;

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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.FacturasVencidasSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlFacturasVencidas;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.includes.MyToolbar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static com.example.kepler201.R.id;
import static com.example.kepler201.R.layout;

public class ActivityFacturasVencidas extends AppCompatActivity {

    private TableLayout tableLayout;
    ImageView ConsultaFacturas, BackOreders, FacturasVencidas, Cliente0Ventas;
    TextView txtCliente, txtNombreCliente, txtFDocumento, txtFecDocumento, txtPlazo, txtFechPago, txtSaldo;
    ArrayList<FacturasVencidasSANDG> listasearch = new ArrayList<>();
    TableRow fila;
    String mensaje = "";
    private boolean multicolor = true;
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    int n = 2000;
    String[] search2 = new String[n];

    private Spinner spinerClie;
    String strusr = "", strpass = "", strname = "", strlname = "", strtype = "", strbran = "", strma = "", strco = "", strclien = "", strcodBra, StrServer = "";
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_facturas_vencidas);

        MyToolbar.show(this, "Consulta-Facturas Vencidas", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog.Builder().setContext(ActivityFacturasVencidas.this).setMessage("Espere un momento...").build();
        spinerClie = findViewById(R.id.spinnerClie);
        tableLayout = findViewById(R.id.table);
        Button btnbuscar =  findViewById(id.btnSearch);
        ConsultaFacturas = findViewById(R.id.SearchProducto);
        BackOreders =  findViewById(R.id.BackOrders);
        FacturasVencidas =  findViewById(R.id.FacturasVencidas);
        Cliente0Ventas =  findViewById(R.id.Cliente0Ventas);



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


        ConsultaFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaFacturas.setBackgroundColor(Color.GRAY);
                ConsultaFacturas.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsuFaInten = new Intent(ActivityFacturasVencidas.this, ActivityConsultaFaturas.class);
                overridePendingTransition(0, 0);
                startActivity(ConsuFaInten);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        BackOreders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackOreders.setBackgroundColor(Color.GRAY);
                BackOreders.setBackgroundColor(Color.TRANSPARENT);
                Intent BackOrdeInten = new Intent(ActivityFacturasVencidas.this, ActivityBackOrders.class);
                overridePendingTransition(0, 0);
                startActivity(BackOrdeInten);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        FacturasVencidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacturasVencidas.setBackgroundColor(Color.GRAY);
                FacturasVencidas.setBackgroundColor(Color.TRANSPARENT);
                Intent FactuItent = new Intent(ActivityFacturasVencidas.this, ActivityFacturasVencidas.class);
                overridePendingTransition(0, 0);
                startActivity(FactuItent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        Cliente0Ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente0Ventas.setBackgroundColor(Color.GRAY);
                Cliente0Ventas.setBackgroundColor(Color.TRANSPARENT);
                Intent Cliente0VenIntent = new Intent(ActivityFacturasVencidas.this, ActivityClientes0Ventas.class);
                overridePendingTransition(0, 0);
                startActivity(Cliente0VenIntent);
                overridePendingTransition(0, 0);
                finish();

            }
        });



        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    tableLayout.removeAllViews();
                    listasearch.clear();
                    listasearch = new ArrayList<>();


                    for (int i = 0; i < search2.length; i++) {
                        int posi = spinerClie.getSelectedItemPosition();
                        if (posi == i) {
                            strclien = search2[i];
                            break;
                        }
                    }

                    if (spinerClie.getSelectedItemPosition() != 0) {
                        ActivityFacturasVencidas.AsyncCallWS task = new ActivityFacturasVencidas.AsyncCallWS();
                        task.execute();
                    } else if (spinerClie.getSelectedItemPosition() == 0) {
                        strclien = "";
                        ActivityFacturasVencidas.AsyncCallWS task = new ActivityFacturasVencidas.AsyncCallWS();
                        task.execute();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityFacturasVencidas.this);
                        alerta.setMessage("Ingrese datos faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("¡ERROR!");
                        titulo.show();
                    }


            }
        });



        ActivityFacturasVencidas.AsyncCallWS2 task2 = new ActivityFacturasVencidas.AsyncCallWS2();
        task2.execute();


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
            conectar();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listasearch.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                if (i == -1) {
                    txtCliente = new TextView(getApplicationContext());
                    txtCliente.setText("Cliente");
                    txtCliente.setGravity(Gravity.START);
                    txtCliente.setBackgroundColor(Color.BLUE);
                    txtCliente.setTextColor(Color.WHITE);
                    txtCliente.setPadding(20, 20, 20, 20);
                    txtCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCliente);

                    txtNombreCliente = new TextView(getApplicationContext());
                    txtNombreCliente.setText("Nombre del Cliente ");
                    txtNombreCliente.setGravity(Gravity.START);
                    txtNombreCliente.setBackgroundColor(Color.BLUE);
                    txtNombreCliente.setTextColor(Color.WHITE);
                    txtNombreCliente.setPadding(20, 20, 20, 20);
                    txtNombreCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtNombreCliente);

                    txtFDocumento = new TextView(getApplicationContext());
                    txtFDocumento.setText("Folio de Factura");
                    txtFDocumento.setGravity(Gravity.START);
                    txtFDocumento.setBackgroundColor(Color.BLUE);
                    txtFDocumento.setTextColor(Color.WHITE);
                    txtFDocumento.setPadding(20, 20, 20, 20);
                    txtFDocumento.setLayoutParams(layaoutDes);
                    fila.addView(txtFDocumento);

                    txtFecDocumento = new TextView(getApplicationContext());
                    txtFecDocumento.setText("Fecha de Factura");
                    txtFecDocumento.setGravity(Gravity.START);
                    txtFecDocumento.setBackgroundColor(Color.BLUE);
                    txtFecDocumento.setTextColor(Color.WHITE);
                    txtFecDocumento.setPadding(20, 20, 20, 20);
                    txtFecDocumento.setLayoutParams(layaoutDes);
                    fila.addView(txtFecDocumento);

                    txtPlazo = new TextView(getApplicationContext());
                    txtPlazo.setText("Plazo");
                    txtPlazo.setGravity(Gravity.START);
                    txtPlazo.setBackgroundColor(Color.BLUE);
                    txtPlazo.setTextColor(Color.WHITE);
                    txtPlazo.setPadding(20, 20, 20, 20);
                    txtPlazo.setLayoutParams(layaoutDes);
                    fila.addView(txtPlazo);


                    txtFechPago = new TextView(getApplicationContext());
                    txtFechPago.setText("Fecha de Vencimiento");
                    txtFechPago.setGravity(Gravity.START);
                    txtFechPago.setBackgroundColor(Color.BLUE);
                    txtFechPago.setTextColor(Color.WHITE);
                    txtFechPago.setPadding(20, 20, 20, 20);
                    txtFechPago.setLayoutParams(layaoutDes);
                    fila.addView(txtFechPago);

                    txtSaldo = new TextView(getApplicationContext());
                    txtSaldo.setText("Saldo");
                    txtSaldo.setGravity(Gravity.END);
                    txtSaldo.setBackgroundColor(Color.BLUE);
                    txtSaldo.setTextColor(Color.WHITE);
                    txtSaldo.setPadding(20, 20, 20, 20);
                    txtSaldo.setLayoutParams(layaoutDes);
                    fila.addView(txtSaldo);

                } else {

                    multicolor = !multicolor;

                    txtCliente = new TextView(getApplicationContext());

                    txtCliente.setGravity(Gravity.START);
                    txtCliente.setBackgroundColor(Color.BLACK);
                    txtCliente.setText(listasearch.get(i).getCliente());
                    txtCliente.setPadding(20, 20, 20, 20);
                    txtCliente.setTextColor(Color.WHITE);
                    txtCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCliente);

                    txtNombreCliente = new TextView(getApplicationContext());
                    txtNombreCliente.setBackgroundColor(Color.GRAY);
                    txtNombreCliente.setGravity(Gravity.START);
                    txtNombreCliente.setMaxLines(1);
                    txtNombreCliente.setText(listasearch.get(i).getNombreCliente());
                    txtNombreCliente.setPadding(20, 20, 20, 20);
                    txtNombreCliente.setTextColor(Color.WHITE);
                    txtNombreCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtNombreCliente);

                    txtFDocumento = new TextView(getApplicationContext());
                    txtFDocumento.setBackgroundColor(Color.BLACK);
                    txtFDocumento.setGravity(Gravity.START);
                    txtFDocumento.setText(listasearch.get(i).getFoliodelDocumento());
                    txtFDocumento.setPadding(20, 20, 20, 20);
                    txtFDocumento.setTextColor(Color.WHITE);
                    txtFDocumento.setLayoutParams(layaoutDes);
                    fila.addView(txtFDocumento);

                    txtFecDocumento = new TextView(getApplicationContext());
                    txtFecDocumento.setBackgroundColor(Color.GRAY);
                    txtFecDocumento.setGravity(Gravity.START);
                    txtFecDocumento.setText(listasearch.get(i).getFechadeDocumento());
                    txtFecDocumento.setPadding(20, 20, 20, 20);
                    txtFecDocumento.setTextColor(Color.WHITE);
                    txtFecDocumento.setLayoutParams(layaoutDes);
                    fila.addView(txtFecDocumento);

                    txtPlazo = new TextView(getApplicationContext());
                    txtPlazo.setBackgroundColor(Color.BLACK);
                    txtPlazo.setGravity(Gravity.START);
                    txtPlazo.setText(listasearch.get(i).getPlazo());
                    txtPlazo.setPadding(20, 20, 20, 20);
                    txtPlazo.setTextColor(Color.WHITE);
                    txtPlazo.setLayoutParams(layaoutDes);
                    fila.addView(txtPlazo);

                    txtFechPago = new TextView(getApplicationContext());
                    txtFechPago.setBackgroundColor(Color.GRAY);
                    txtFechPago.setGravity(Gravity.START);
                    txtFechPago.setText(listasearch.get(i).getFechadepago());
                    txtFechPago.setPadding(20, 20, 20, 20);
                    txtFechPago.setTextColor(Color.WHITE);
                    txtFechPago.setLayoutParams(layaoutDes);
                    fila.addView(txtFechPago);


                    txtSaldo = new TextView(getApplicationContext());
                    txtSaldo.setBackgroundColor(Color.BLACK);
                    txtSaldo.setGravity(Gravity.START);
                    txtSaldo.setText("$" + formatNumberCurrency(listasearch.get(i).getSaldo()));
                    txtSaldo.setPadding(20, 20, 20, 20);
                    txtSaldo.setTextColor(Color.WHITE);
                    txtSaldo.setLayoutParams(layaoutDes);
                    fila.addView(txtSaldo);

                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout.addView(fila);
            }
            mDialog.dismiss();
        }
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar2();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "Todos los clientes";
            search2[0] = "Todos los clientes";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search2[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerClie.setAdapter(adapter);
        }


    }


    private void conectar2() {
        String SOAP_ACTION = "SearchClient";
        String METHOD_NAME = "SearchClient";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlSearchClientesG soapEnvelope = new xmlSearchClientesG(SoapEnvelope.VER11);
            soapEnvelope.xmlSearchG(strusr, strpass, strco);
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
                listaclientG.add(new SearachClientSANDG((response0.getPropertyAsString("k_dscr").equals("anyType{}") ?"" : response0.getPropertyAsString("k_dscr")), (response0.getPropertyAsString("k_line").equals("anyType{}") ?"" : response0.getPropertyAsString("k_line"))));


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

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    private void conectar() {


        String SOAP_ACTION = "FactVencidas";
        String METHOD_NAME = "FactVencidas";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlFacturasVencidas soapEnvelope = new xmlFacturasVencidas(SoapEnvelope.VER11);
            soapEnvelope.xmlFacturasV(strusr, strpass, strco, strclien);
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
                listasearch.add(new FacturasVencidasSANDG((response0.getPropertyAsString("k_Cliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cliente")),
                        (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                        (response0.getPropertyAsString("k_Folio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Folio")),
                        (response0.getPropertyAsString("k_FechFactura").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FechFactura")),
                        (response0.getPropertyAsString("k_Plazo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Plazo")),
                        (response0.getPropertyAsString("k_FechaVen").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FechaVen")),
                        (response0.getPropertyAsString("k_Saldo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Saldo"))));

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