package com.example.kepler201.activities.Consultas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorConsulFacturas;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ConsulFacfturasSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlConsulFacturas;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.includes.MyToolbar;

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

public class ActivityConsultaFaturas extends AppCompatActivity {

    String FechaIncial, FechaFinal;
    private Spinner spinerClie;
    ImageView ConsultaFacturas, BackOreders, FacturasVencidas, Cliente0Ventas;
    private EditText fechaEn, fechaSa;


    ArrayList<ConsulFacfturasSANDG> listasearch = new ArrayList<>();
    RecyclerView recyclerConsulta;
    String ClaveFolDialog = "";
    String ClaveNumDialog = "";


    String Clave, FFactura, FechaFact, Plazo, FechaVen, Saldo;


    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String strscliente = "";
    String mensaje = "";

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();

    int n = 2000;
    String[] search2 = new String[n];

    String date;
    String date2;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_faturas);

        MyToolbar.show(this, "Consulta-Facturas", true);

        recyclerConsulta =  findViewById(R.id.lisFacturas);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog.Builder().setContext(ActivityConsultaFaturas.this).setMessage("Espere un momento...").build();
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


        spinerClie =  findViewById(R.id.spinnerClie);
        fechaEn =  findViewById(R.id.fechaendtrada);
        fechaSa =  findViewById(R.id.fechasalida);
        Button btnsearch =  findViewById(R.id.btnSearch);
        ConsultaFacturas =  findViewById(R.id.SearchProducto);
        BackOreders =  findViewById(R.id.BackOrders);
        FacturasVencidas = findViewById(R.id.FacturasVencidas);
        Cliente0Ventas =  findViewById(R.id.Cliente0Ventas);


        ConsultaFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaFacturas.setBackgroundColor(Color.GRAY);
                ConsultaFacturas.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsuFaInten = new Intent(ActivityConsultaFaturas.this, ActivityConsultaFaturas.class);
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
                Intent BackOrdeInten = new Intent(ActivityConsultaFaturas.this, ActivityBackOrders.class);
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
                Intent FactuItent = new Intent(ActivityConsultaFaturas.this, ActivityFacturasVencidas.class);
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
                Intent Cliente0VenIntent = new Intent(ActivityConsultaFaturas.this, ActivityClientes0Ventas.class);
                overridePendingTransition(0, 0);
                startActivity(Cliente0VenIntent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ActivityConsultaFaturas.AsyncCallWS task = new ActivityConsultaFaturas.AsyncCallWS();
        task.execute();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaFaturas.this, new DatePickerDialog.OnDateSetListener() {
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

        fechaSa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaFaturas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date2 = year + "-" + month + "-" + day;
                        fechaSa.setText(date2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    listasearch = new ArrayList<>();
                    recyclerConsulta.setLayoutManager(new LinearLayoutManager(ActivityConsultaFaturas.this));
                    FechaIncial = fechaEn.getText().toString();
                    FechaFinal = fechaSa.getText().toString();
                    for (int i = 0; i < search2.length; i++) {
                        int posi = spinerClie.getSelectedItemPosition();
                        if (posi == i) {
                            strscliente = search2[i];
                            break;
                        }
                    }

                    if (!FechaIncial.isEmpty() && !FechaFinal.isEmpty() && spinerClie.getSelectedItemPosition() != 0) {

                        ActivityConsultaFaturas.AsyncCallWS2 task2 = new ActivityConsultaFaturas.AsyncCallWS2();
                        task2.execute();
                    } else if (!FechaIncial.isEmpty() && !FechaFinal.isEmpty() && spinerClie.getSelectedItemPosition() == 0) {
                        strscliente = "";
                        ActivityConsultaFaturas.AsyncCallWS2 task2 = new ActivityConsultaFaturas.AsyncCallWS2();
                        task2.execute();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaFaturas.this);
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
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.MONTH, -1);
        String fechaasalll = dateformatActually1.format(c.getTime());

        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c1.getTime());

        fechaEn.setText(fechaasalll);
        fechaSa.setText(fechaactual);


        FechaIncial = fechaEn.getText().toString();
        FechaFinal = fechaSa.getText().toString();

        listasearch = new ArrayList<>();
        recyclerConsulta.setLayoutManager(new LinearLayoutManager(this));


    }




    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar();
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


    private void conectar() {
        String SOAP_ACTION = "SearchClient";
        String METHOD_NAME = "SearchClient";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlSearchClientesG soapEnvelope = new xmlSearchClientesG(SoapEnvelope.VER11);
            soapEnvelope.xmlSearchG(strusr, strpass, strcode);
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
                listaclientG.add(new SearachClientSANDG((response0.getPropertyAsString("k_dscr").equals("anyType{}") ? "" : response0.getPropertyAsString("k_dscr")), (response0.getPropertyAsString("k_line").equals("anyType{}") ? "" : response0.getPropertyAsString("k_line"))));


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

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


            if (listasearch.size() > 0) {
                AdaptadorConsulFacturas adapter = new AdaptadorConsulFacturas(listasearch);
                recyclerConsulta.setAdapter(adapter);
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaFaturas.this);
                alerta.setMessage("El cliente no tiene facturas").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("No existen Facturas");
                titulo.show();

            }

        }
    }


    public void detalleFactura(View view) {
        Clave = "";
        FFactura = "";
        FechaFact = "";
        Plazo = "";
        FechaVen = "";
        Saldo = "";

        int position = recyclerConsulta.getChildAdapterPosition(Objects.requireNonNull(recyclerConsulta.findContainingItemView(view)));


        ClaveFolDialog = listasearch.get(position).getFoliodelDocumento();
        ClaveNumDialog = listasearch.get(position).getNumSuc();

        Intent FactutaDetall = new Intent(ActivityConsultaFaturas.this, ActivityFactuDetall.class);
        FactutaDetall.putExtra("Folio", ClaveFolDialog);
        FactutaDetall.putExtra("NumSucu", ClaveNumDialog);
        startActivity(FactutaDetall);
    }


    private void conecta2() {
        String SOAP_ACTION = "ConsulFact";
        String METHOD_NAME = "ConsulFact";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConsulFacturas soapEnvelope = new xmlConsulFacturas(SoapEnvelope.VER11);
            soapEnvelope.xmlConsulFactur(strusr, strpass, strcode, strscliente, FechaIncial, FechaFinal);
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

                listasearch.add(new ConsulFacfturasSANDG((response0.getPropertyAsString("k_Clave").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Clave")),
                        (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                        (response0.getPropertyAsString("k_Folio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Folio")),
                        (response0.getPropertyAsString("k_FechaFa").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FechaFa")),
                        (response0.getPropertyAsString("k_Plazo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Plazo")),
                        (response0.getPropertyAsString("k_FechaVenc").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FechaVenc")),
                        (response0.getPropertyAsString("k_Saldo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Saldo")),
                        (response0.getPropertyAsString("k_Monto").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Monto")),
                        (response0.getPropertyAsString("k_NumSu").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NumSu")),
                        (response0.getPropertyAsString("k_NomSu").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NomSu"))));


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