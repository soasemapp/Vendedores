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
import com.example.kepler201.SetterandGetter.FacturasnoregistradasNewSANDG;
import com.example.kepler201.XMLS.xmlAgenda;
import com.example.kepler201.XMLS.xmlStatusAgen;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
import com.example.kepler201.activities.Productos.ActivityConsultaProductos;
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
        mDialog.setCancelable(false);
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
        ListaAgenda();

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
                        StatusAgenda();
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







    public void ListaAgenda() {
        new ActivityAgenda.agenda().execute();
    }


    private class agenda extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor="+strcode+"&fecha="+StrFecha;
            String url = "http://" + StrServer + "/agendaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if (json.length()!=0) {
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
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgenda.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgenda.this);
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

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            AdapterAgenda adapter = new AdapterAgenda(listaAgenda);
            recyclerAgenda.setAdapter(adapter);
            mDialog.dismiss();
        }//onPost
    }


    public void StatusAgenda() {
        new ActivityAgenda.agendastatus().execute();
    }


    private class agendastatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }//onPreExecute
        @Override
        protected Void doInBackground(Void... voids) {


            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor="+strcode+"&fecha="+Fecha2+"&cliente="+ClaveCliente+"&actividad="+Actividad+"&status="+Status+"&statuscambio="+Status2;
            String url = "http://" + StrServer + "/statusagendaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if (json.length()!=0) {
                        JSONObject jitems, Numero = null;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Mensaje");



                        Mensaje = jitems.getString("k_respuesta");

                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgenda.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityAgenda.this);
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

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
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
        }//onPost
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