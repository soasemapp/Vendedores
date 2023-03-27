package com.example.kepler201.activities.Agenda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kepler201.Adapter.AdapterAgenda;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.XMLS.xmlAgenda;
import com.example.kepler201.XMLS.xmlStatusAgen;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.activities.Productos.ActivityConsultaProductos;
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

public class ActivityAgenda extends AppCompatActivity {

    ArrayList<AgendaSANDG> listaAgenda = new ArrayList<>();
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String mensaje = "";
    String StrFecha;
    RecyclerView recyclerAgenda;
    String ClaveCliente = "";
    String Nombre = "";
    String Actividad = "";
    String Status= "";
    String Status2="";
    String Fecha2="";
    String Mensaje="";
    int cont ;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        MyToolbar.show(this, "Agenda", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        mDialog = new SpotsDialog.Builder().setContext(ActivityAgenda.this).setMessage("Espere un momento...").build();
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
        EditText fecha = findViewById(R.id.fecha);
        recyclerAgenda = findViewById(R.id.lisAgenda);

        listaAgenda = new ArrayList<>();
        recyclerAgenda.setLayoutManager(new LinearLayoutManager(ActivityAgenda.this));

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());
        fecha.setText(fechaactual);
        StrFecha = fecha.getText().toString();
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

    }



    public void statusAgenda(View view) {
        final int position = recyclerAgenda.getChildAdapterPosition(Objects.requireNonNull(recyclerAgenda.findContainingItemView(view)));
        final String[] status = getResources().getStringArray(R.array.status);
        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgenda.this);
        alerta.setTitle("Seleccione un Status").setItems(R.array.status, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cont=i;
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgenda.this);
                alerta.setMessage("¿Deseas Cambiar este status de  "+listaAgenda.get(position).getEstatus()+ " a " + status[i] + "?"
                ).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Fecha2 = listaAgenda.get(position).getFecha();
                        ClaveCliente = listaAgenda.get(position).getCliente();
                        Nombre = listaAgenda.get(position).getClienNom();
                        Actividad = listaAgenda.get(position).getActividad();
                        Status= listaAgenda.get(position).getEstatus();
                        Status2=status[cont];
                        AsyncCallWS2 task = new AsyncCallWS2();
                        task.execute();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Aviso");
                titulo.show();


            }
        });

        AlertDialog titulo = alerta.create();
        titulo.show();


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

            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgenda.this);
            alerta.setMessage(Mensaje).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    Intent Modificar = new Intent(ActivityAgenda.this,ActivityAgenda.class);
                    overridePendingTransition(0, 0);
                    startActivity(Modificar);
                    overridePendingTransition(0, 0);
                    finish();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("AVISO");
            titulo.show();
            mDialog.dismiss();
        }
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

            AdapterAgenda adapter = new AdapterAgenda(listaAgenda);
            recyclerAgenda.setAdapter(adapter);
            mDialog.dismiss();
        }
    }


    private void conecta() {
        String SOAP_ACTION = "Agen";
        String METHOD_NAME = "Agen";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlAgenda soapEnvelope = new xmlAgenda(SoapEnvelope.VER11);
            soapEnvelope.xmlAgenda(strusr, strpass, strcode, StrFecha);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;

            int json=response.getPropertyCount()-1;
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

    private void conecta2() {
        String SOAP_ACTION = "statusAgenda";
        String METHOD_NAME = "statusAgenda";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlStatusAgen soapEnvelope = new xmlStatusAgen(SoapEnvelope.VER11);
            soapEnvelope.xmlStatusAgen(strusr, strpass,Fecha2,strcode,ClaveCliente,Actividad,Status,Status2);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            response = (SoapObject) response.getProperty("Branch");
            Mensaje= response.getPropertyAsString("k_respuesta");



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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuflow7, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addAgenda) {
            Intent AgendaAdd = new Intent(this, ActivityAgendaRegister.class);
            overridePendingTransition(0, 0);
            startActivity(AgendaAdd);
            overridePendingTransition(0, 0);

        }

        return super.onOptionsItemSelected(item);

    }



}