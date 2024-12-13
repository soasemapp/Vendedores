package com.example.kepler201;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorBackOrderlist;
import com.example.kepler201.SetterandGetter.BackordersADDSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlBackOrders;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;

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

public class ActivityBackOrdersAdd extends AppCompatActivity {

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    RecyclerView recyclerDialog2;
    AlertDialog mDialog;
    String mensaje, Cliente;
    Button ButtonCliente;
    private EditText fechaEn, fechaSa;
    String date,date2,FechaIncial,FechaFinal;

    ArrayList<BackordersADDSANDG> listaBackOrders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_orders_add);

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
        mDialog = new SpotsDialog(ActivityBackOrdersAdd.this);
        recyclerDialog2 = findViewById(R.id.BackOrderAdd);
        ButtonCliente = findViewById(R.id.Clientesbtn);
        fechaEn = findViewById(R.id.fechaendtrada);
        fechaSa = findViewById(R.id.fechasalida);
        Button btnsearch = findViewById(R.id.btnSearch);

        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaclientG.size()];

                for (int i = 0; i < listaclientG.size(); i++) {
                    opciones[i] = listaclientG.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBackOrdersAdd.this);
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Cliente = listaclientG.get(which).getUserCliente();

                        ButtonCliente.setText(listaclientG.get(which).getNombreCliente());
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityBackOrdersAdd.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityBackOrdersAdd.this, new DatePickerDialog.OnDateSetListener() {
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
                ActivityBackOrdersAdd.AsyncCallWS2 task = new ActivityBackOrdersAdd.AsyncCallWS2();
                task.execute();
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.MONTH,-1);
        String fechatreinta = dateformatActually.format(c.getTime());


        Calendar calendar1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually1.format(calendar1.getTime());

        fechaEn.setText(fechatreinta);
        fechaSa.setText(fechaactual);

        FechaIncial = fechaEn.getText().toString();
        FechaFinal = fechaSa.getText().toString();


        GridLayoutManager gl = new GridLayoutManager(this, 1);
        recyclerDialog2.setLayoutManager(gl);


        ActivityBackOrdersAdd.AsyncCallWS task = new ActivityBackOrdersAdd.AsyncCallWS();
        task.execute();
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
            for (int i = 0; i < response.getPropertyCount(); i++) {
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

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            AdaptadorBackOrderlist adapter = new AdaptadorBackOrderlist(listaBackOrders);
            recyclerDialog2.setAdapter(null);
            recyclerDialog2.setAdapter(adapter);

        }
    }


    private void conecta2() {
        String SOAP_ACTION = "BackOrders";
        String METHOD_NAME = "BackOrders";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlBackOrders soapEnvelope = new xmlBackOrders(SoapEnvelope.VER11);
            soapEnvelope.xmlBackOrdersdatos(strusr, strpass, Cliente, FechaIncial, FechaFinal, strcodBra);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                //(response0.getPropertyAsString("k_code").equals("anyType{}")?" ":response0.getPropertyAsString("k_Hora"))
                listaBackOrders.add(new BackordersADDSANDG((response0.getPropertyAsString("k_Folio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Folio")),
                        (response0.getPropertyAsString("k_ClavePro").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ClavePro")),
                        (response0.getPropertyAsString("k_Descr").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Descr")),
                        (response0.getPropertyAsString("k_BackOrder").equals("anyType{}") ? " " : response0.getPropertyAsString("k_BackOrder")),
                        (response0.getPropertyAsString("k_Existencia").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Existencia"))));


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


