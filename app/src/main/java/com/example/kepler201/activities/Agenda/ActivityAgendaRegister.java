package com.example.kepler201.activities.Agenda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlAgendaActivitidad;
import com.example.kepler201.XMLS.xmlAgendaRegister;
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

import dmax.dialog.SpotsDialog;

public class ActivityAgendaRegister extends AppCompatActivity {


    Button ButtonCliente;
    Button ButtonAtividades;

    String strscliente="";
    String strsActividades="";
    AlertDialog mDialog;
    private EditText fecha, edcomentario;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode, Strdate = "", Strdatetoday = "";
    String mensaje, stractividad = "", strcomentario = "";
    RadioButton Todoyes, Todono,Eagleyes, Eagleno, TrackOneyes, TrackOneno, Rodatechyes, Rodatechno, Partechyes, Partechno, TGyes, TGno;
    String Eagle = "N", Trackone = "N", Rodatech = "N", Partech = "N", TG = "N";
    TextView etiqueta;
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<SearachClientSANDG> listaActividades = new ArrayList<>();
    LinearLayout partechlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_register);

        mDialog = new SpotsDialog.Builder().setContext(ActivityAgendaRegister.this).setMessage("Espere un momento...").build();
        MyToolbar.show(this, "Agendar", true);
        ButtonCliente = findViewById(R.id.btnclientes);
        ButtonAtividades=findViewById(R.id.btnActividades);
        Todoyes = findViewById(R.id.Todosyes);
        Todono = findViewById(R.id.Todosno);
        Eagleyes = findViewById(R.id.Eagleyes);
        Eagleno = findViewById(R.id.Eagleno);
        TrackOneyes = findViewById(R.id.trackoneyes);
        TrackOneno = findViewById(R.id.trackoneno);
        Rodatechyes = findViewById(R.id.rodatechyes);
        Rodatechno = findViewById(R.id.rodatechno);
        Partechyes = findViewById(R.id.partechyes);
        Partechno = findViewById(R.id.partechno);
        TGyes = findViewById(R.id.tgyes);
        TGno = findViewById(R.id.tgno);
        fecha = findViewById(R.id.fecha);
        edcomentario = findViewById(R.id.idcomentario);
        etiqueta = findViewById(R.id.txtetiqueta);
        partechlay = findViewById(R.id.partechlay);

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


        if (!StrServer.equals("vazlocolombia.dyndns.org:9085")){
            etiqueta.setText("TG");
            partechlay.setVisibility(View.VISIBLE);
        }else{
            etiqueta.setText("SHOKER");
            Partech = "N";
            partechlay.setVisibility(View.GONE);
        }


        Todoyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eagleno.setChecked(false);
                TrackOneno.setChecked(false);
                Rodatechno.setChecked(false);
                Partechno.setChecked(false);
                TGno.setChecked(false);

                Eagleyes.setChecked(true);
                TrackOneyes.setChecked(true);
                Rodatechyes.setChecked(true);
                Partechyes.setChecked(true);
                TGyes.setChecked(true);
            }
        });
        Todono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eagleyes.setChecked(false);
                TrackOneyes.setChecked(false);
                Rodatechyes.setChecked(false);
                Partechyes.setChecked(false);
                TGyes.setChecked(false);

                Eagleno.setChecked(true);
                TrackOneno.setChecked(true);
                Rodatechno.setChecked(true);
                Partechno.setChecked(true);
                TGno.setChecked(true);
            }
        });

        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaclientG.size()];

                for (int i = 0; i < listaclientG.size(); i++) {
                    opciones[i] = listaclientG.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAgendaRegister.this);
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strscliente = listaclientG.get(which).getUserCliente();
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


        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());
        fecha.setText(fechaactual);
        Strdatetoday = fechaactual;
        Strdate=fechaactual;

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAgendaRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        Strdate = year + "-" + month + "-" + day;
                        fecha.setText(Strdate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });



        ButtonAtividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaActividades.size()];

                for (int i = 0; i < listaActividades.size(); i++) {
                    opciones[i] = listaActividades.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAgendaRegister.this);
                builder.setTitle("SELECCIONE UNA ACTIVIDAD").setIcon(R.drawable.ic_baseline_calendar_today_24);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strsActividades = listaActividades.get(which).getUserCliente();
                        ButtonAtividades.setText(listaActividades.get(which).getNombreCliente());

                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        AsyncClientes task1 = new AsyncClientes();
        task1.execute();

        AsyncAgendaActivity task2 = new AsyncAgendaActivity();
        task2.execute();



    }


    public void AgendaRe(View view) {

        if (Eagleyes.isChecked()) {
            Eagle = "S";
        } else {
            Eagle = "N";

        }

        if (TrackOneyes.isChecked()) {
            Trackone = "S";
        } else {
            Trackone = "N";

        }
        if (Rodatechyes.isChecked()) {
            Rodatech = "S";
        } else {
            Rodatech = "N";
        }

        if (Partechyes.isChecked()) {
            Partech = "S";
        } else {
            Partech = "N";
        }
        if (TGyes.isChecked()) {
            TG = "S";
        } else {
            TG = "N";
        }
        strcomentario = edcomentario.getText().toString();
        if (!strsActividades.equals("")) {
            if (!strscliente.equals("")){
                AsyncAgendaRegister task1 = new AsyncAgendaRegister();
                task1.execute();
            } else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgendaRegister.this);
                alerta.setMessage("Seleccione un cliente").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Clientes no seleccionado");
                titulo.show();
            }

        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgendaRegister.this);
            alerta.setMessage("Ingrese una Clave de Actividad").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Actividad");
            titulo.show();

        }


        /*
         */
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class AsyncClientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Clientes();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
        }


    }

    private void Clientes() {
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
                listaclientG.add(new SearachClientSANDG(response0.getPropertyAsString("k_dscr"), response0.getPropertyAsString("k_line")));


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

    private void agendaActi() {
        String SOAP_ACTION = "agendaActi";
        String METHOD_NAME = "agendaActi";
        String NAMESPACE = "http://" + StrServer + "/WSk75branch/";
        String URL = "http://" + StrServer + "/WSk75branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlAgendaActivitidad soapEnvelope = new xmlAgendaActivitidad(SoapEnvelope.VER11);
            soapEnvelope.xmlAgenda(strusr, strpass);
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
                listaActividades.add(new SearachClientSANDG(response0.getPropertyAsString("k_Clave"), response0.getPropertyAsString("k_Descripcion")));


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class AsyncAgendaActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            agendaActi();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {



            mDialog.dismiss();
        }


    }


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class AsyncAgendaRegister extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            AgendaRe();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgendaRegister.this);
            alerta.setMessage(mensaje).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Aviso");
            titulo.show();

            mDialog.dismiss();
        }


    }

    private void AgendaRe() {
        String SOAP_ACTION = "AgendaRegister";
        String METHOD_NAME = "AgendaRegister";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlAgendaRegister soapEnvelope = new xmlAgendaRegister(SoapEnvelope.VER11);
            soapEnvelope.xmlAgendaRegister(strusr, strpass,Strdatetoday,strcode, strscliente, strsActividades, Partech, Eagle, Rodatech, TG, Trackone,Strdate , strcomentario, strusr);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            response0 = (SoapObject) response0.getProperty("Branch");
            mensaje = response0.getPropertyAsString("k_respuesta");


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