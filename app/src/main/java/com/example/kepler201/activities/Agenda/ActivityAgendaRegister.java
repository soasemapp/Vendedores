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
import com.example.kepler201.activities.Consultas.ActivityConsultaFaturas;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
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
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_register);

        mDialog = new SpotsDialog.Builder().setContext(ActivityAgendaRegister.this).setMessage("Espere un momento...").build();
        mDialog.setCancelable(false);
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








        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR,1);
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
                        if (String.valueOf(month).length() < 2) {

                            if (String.valueOf(day).length() < 2) {
                                Strdate = year + "-" + (month) + "-0" + day;
                            }else{
                                Strdate = year + "-0" + (month) + "-" + day;
                            }
                        }else{
                            if (String.valueOf(day).length() < 2) {
                                Strdate = year + "-" + (month) + "-0" + day;
                            }else{
                                Strdate = year + "-" + (month) + "-" + day;
                            }
                        }


                        Calendar c = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                        String fechadate=dateformatActually.format(c.getTime());
                        if (Strdate.equals(fechadate)){
                            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgendaRegister.this);
                            alerta.setMessage("No puedes agendar del dia de hoy").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fecha.setText(Strdate);
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Agenda");
                            titulo.show();
                        }else{
                            fecha.setText(Strdate);
                        }

                    }
                }, year, month, day+1);
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


        Listaclientes();
        Listaactividades();
       }

    public void Listaclientes() {
        new ActivityAgendaRegister.Cliente().execute();
    }
    public void Listaactividades() {
        new ActivityAgendaRegister.AgendaActividad().execute();
    }


    private class Cliente extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strcode;
            String url = "http://" + StrServer + "/listaclientesapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if(json.length()!=0){
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Clientes");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("Clientes");
                        Numero = jitems.getJSONObject("" + i + "");
                        listaclientG.add(new SearachClientSANDG(
                                Numero.getString("Clave"),
                                Numero.getString("Nombre")));
                    }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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
            mDialog.dismiss();
            return null;

        }//doInBackground
    }



    private class AgendaActividad extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String url = "http://" + StrServer + "/agendaactividadapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {

                        JSONObject jitems, Numero, Clave, Nombre;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaActividades.add(new SearachClientSANDG(
                                    Numero.getString("clave"),
                                    Numero.getString("descripcion")));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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

        }//doInBackground
    }



        public void AgendaRe(View view) {

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());
        String fechadate = fecha.getText().toString();
        if (fechadate.equals(fechaactual)) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityAgendaRegister.this);
            alerta.setMessage("No puedes agendar del dia de hoy").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Agenda");
            titulo.show();
        } else {


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
                if (!strscliente.equals("")) {
                    AgendaRegistertodos();
                } else {
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
        }
    }
    public void AgendaRegistertodos() {
        new ActivityAgendaRegister.AgendaRegister().execute();
    }


    private class AgendaRegister extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "fecha="+Strdatetoday+"&vendedor="+strcode+"&cliente="+strscliente+"&actividad="+strsActividades+"&parteh="+Partech+"&eagle="+Eagle+"&rodatech="+Rodatech+"&tg="+TG+"&trackone="+Trackone+"&fechavis="+Strdate+"&comentario="+strcomentario+"&agendo="+strusr;
            String url = "http://" + StrServer + "/agendaregisterapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("Branch");
                        mensaje = jitems.getString("k_respuesta");
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgendaRegister.this);
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

        }//doInBackground
        // @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
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
        }//onPost
    }





}