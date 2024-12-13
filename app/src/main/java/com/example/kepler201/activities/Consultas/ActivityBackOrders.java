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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.BackOrdersSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlBackOrders;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.MainActivity;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;

import org.json.JSONArray;
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

public class ActivityBackOrders extends AppCompatActivity {


    String FechaIncial, FechaFinal;
    private Spinner spinerClie;
    ImageView ConsultaFacturas, BackOreders, FacturasVencidas, Cliente0Ventas;
    private EditText fechaEn, fechaSa;
    private TableLayout tableLayout;
    TextView txtClave, txtFecha, txtClaveDelProducto, getTxtDescP, txtBackOrder, txtFolio, Existencia;
    TableRow fila;
    private boolean multicolor = true;


    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String strscliente;
    String mensaje = "";

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<BackOrdersSANDG> listaBackOrders = new ArrayList<>();

    int n = 2000;
    String[] search2 = new String[n];

    String date;
    String date2;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_orders);

        MyToolbar.show(this, "Consulta-BackOrders", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog(ActivityBackOrders.this);
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


        tableLayout = findViewById(R.id.table);
        spinerClie = findViewById(R.id.spinnerClie);
        fechaEn = findViewById(R.id.fechaendtrada);
        fechaSa = findViewById(R.id.fechasalida);
        Button btnsearch = findViewById(R.id.btnSearch);
        ConsultaFacturas = findViewById(R.id.SearchProducto);
        BackOreders = findViewById(R.id.BackOrders);
        FacturasVencidas = findViewById(R.id.FacturasVencidas);
        Cliente0Ventas = findViewById(R.id.Cliente0Ventas);


        ConsultaFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaFacturas.setBackgroundColor(Color.GRAY);
                ConsultaFacturas.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsuFaInten = new Intent(ActivityBackOrders.this, ActivityConsultaFaturas.class);
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
                Intent BackOrdeInten = new Intent(ActivityBackOrders.this, ActivityBackOrders.class);
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
                Intent FactuItent = new Intent(ActivityBackOrders.this, ActivityFacturasVencidas.class);
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
                Intent Cliente0VenIntent = new Intent(ActivityBackOrders.this, ActivityClientes0Ventas.class);
                overridePendingTransition(0, 0);
                startActivity(Cliente0VenIntent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        Listaclientes();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityBackOrders.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityBackOrders.this, new DatePickerDialog.OnDateSetListener() {
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



                    tableLayout.removeAllViews();
                    listaBackOrders.clear();
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

                        BackorderLista();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityBackOrders.this);
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.MONTH,-1);
        String fechatreinta = dateformatActually.format(c.getTime());


        Calendar calendar1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually1.format(calendar1.getTime());

        fechaEn.setText(fechatreinta);
        fechaSa.setText(fechaactual);


    }

    public boolean firtMet() {//firtMet
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {//si hay conexion a internet
            return true;
        } else {
            return false;
        }//else
    }//Fir


    public void Listaclientes() {
        new ActivityBackOrders.Cliente().execute();
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
                    if(json.length()!=0) {

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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityBackOrders.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityBackOrders.this);
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
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "Cliente";
            search2[0] = "Cliente";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search2[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerClie.setAdapter(adapter);
            mDialog.dismiss();
        }//onPost
    }


    public void BackorderLista() {
        new ActivityBackOrders.BackOrders().execute();
    }
        private class BackOrders extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }//onPreExecute

            @Override
            protected Void doInBackground(Void... voids) {
                HttpHandler sh = new HttpHandler();
                String parametros = "cliente="+strscliente+"&fechae="+FechaIncial+"&fechas="+FechaFinal+"&sucursal="+strcodBra;
                String url = "http://" + StrServer + "/backorderscliapp?" + parametros;
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
                                listaBackOrders.add(new BackOrdersSANDG(Numero.getString("clave"),
                                        Numero.getString("fecha"),
                                        Numero.getString("clavepro"),
                                        Numero.getString("descripcion"),
                                        Numero.getString("backorder"),
                                        Numero.getString("folio"),
                                        Numero.getString("existencia")));
                            }
                        }

                    } catch (final JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityBackOrders.this);
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityBackOrders.this);
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
                TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableLayout.setBackgroundColor(Color.parseColor("#043B72"));
                for (int i = -1; i < listaBackOrders.size(); i++) {
                    fila = new TableRow(getApplicationContext());
                    fila.setLayoutParams(layaoutFila);
                    if (i == -1) {
                        txtClave = new TextView(getApplicationContext());
                        txtClave.setText("Clave");
                        txtClave.setGravity(Gravity.START);
                        txtClave.setBackgroundColor(Color.BLUE);
                        txtClave.setTextColor(Color.WHITE);
                        txtClave.setPadding(20, 20, 20, 20);
                        txtClave.setLayoutParams(layaoutDes);
                        fila.addView(txtClave);

                        txtFecha = new TextView(getApplicationContext());
                        txtFecha.setText("Fecha");
                        txtFecha.setGravity(Gravity.START);
                        txtFecha.setBackgroundColor(Color.BLUE);
                        txtFecha.setTextColor(Color.WHITE);
                        txtFecha.setPadding(20, 20, 20, 20);
                        txtFecha.setLayoutParams(layaoutDes);
                        fila.addView(txtFecha);

                        txtClaveDelProducto = new TextView(getApplicationContext());
                        txtClaveDelProducto.setText("Clave del Producto");
                        txtClaveDelProducto.setGravity(Gravity.START);
                        txtClaveDelProducto.setBackgroundColor(Color.BLUE);
                        txtClaveDelProducto.setTextColor(Color.WHITE);
                        txtClaveDelProducto.setPadding(20, 20, 20, 20);
                        txtClaveDelProducto.setLayoutParams(layaoutDes);
                        fila.addView(txtClaveDelProducto);

                        getTxtDescP = new TextView(getApplicationContext());
                        getTxtDescP.setText("Descripcion del Producto");
                        getTxtDescP.setGravity(Gravity.START);
                        getTxtDescP.setBackgroundColor(Color.BLUE);
                        getTxtDescP.setTextColor(Color.WHITE);
                        getTxtDescP.setPadding(20, 20, 20, 20);
                        getTxtDescP.setLayoutParams(layaoutDes);
                        fila.addView(getTxtDescP);

                        txtBackOrder = new TextView(getApplicationContext());
                        txtBackOrder.setText("BackOrder");
                        txtBackOrder.setGravity(Gravity.START);
                        txtBackOrder.setBackgroundColor(Color.BLUE);
                        txtBackOrder.setTextColor(Color.WHITE);
                        txtBackOrder.setPadding(20, 20, 20, 20);
                        txtBackOrder.setLayoutParams(layaoutDes);
                        fila.addView(txtBackOrder);


                        txtFolio = new TextView(getApplicationContext());
                        txtFolio.setText("Folio");
                        txtFolio.setGravity(Gravity.START);
                        txtFolio.setBackgroundColor(Color.BLUE);
                        txtFolio.setTextColor(Color.WHITE);
                        txtFolio.setPadding(20, 20, 20, 20);
                        txtFolio.setLayoutParams(layaoutDes);
                        fila.addView(txtFolio);

                        Existencia = new TextView(getApplicationContext());
                        Existencia.setText("Existencia");
                        Existencia.setGravity(Gravity.START);
                        Existencia.setBackgroundColor(Color.BLUE);
                        Existencia.setTextColor(Color.WHITE);
                        Existencia.setPadding(20, 20, 20, 20);
                        Existencia.setLayoutParams(layaoutDes);
                        fila.addView(Existencia);

                    } else {
                        multicolor = !multicolor;
                        txtClave = new TextView(getApplicationContext());
                        txtClave.setGravity(Gravity.START);
                        txtClave.setBackgroundColor(Color.BLACK);
                        txtClave.setText(listaBackOrders.get(i).getClave());
                        txtClave.setPadding(20, 20, 20, 20);
                        txtClave.setTextColor(Color.WHITE);
                        txtClave.setLayoutParams(layaoutDes);
                        fila.addView(txtClave);

                        txtFecha = new TextView(getApplicationContext());
                        txtFecha.setBackgroundColor(Color.GRAY);
                        txtFecha.setGravity(Gravity.START);
                        txtFecha.setText(listaBackOrders.get(i).getFecha());
                        txtFecha.setPadding(20, 20, 20, 20);
                        txtFecha.setTextColor(Color.WHITE);
                        txtFecha.setLayoutParams(layaoutDes);
                        fila.addView(txtFecha);

                        txtClaveDelProducto = new TextView(getApplicationContext());
                        txtClaveDelProducto.setBackgroundColor(Color.BLACK);
                        txtClaveDelProducto.setGravity(Gravity.START);
                        txtClaveDelProducto.setText(listaBackOrders.get(i).getClaveProducto());
                        txtClaveDelProducto.setPadding(20, 20, 20, 20);
                        txtClaveDelProducto.setTextColor(Color.WHITE);
                        txtClaveDelProducto.setLayoutParams(layaoutDes);
                        fila.addView(txtClaveDelProducto);

                        getTxtDescP = new TextView(getApplicationContext());
                        getTxtDescP.setBackgroundColor(Color.GRAY);
                        getTxtDescP.setGravity(Gravity.START);
                        getTxtDescP.setText(listaBackOrders.get(i).getDescripcion());
                        getTxtDescP.setPadding(20, 20, 20, 20);
                        getTxtDescP.setTextColor(Color.WHITE);
                        getTxtDescP.setLayoutParams(layaoutDes);
                        fila.addView(getTxtDescP);


                        txtBackOrder = new TextView(getApplicationContext());
                        txtBackOrder.setBackgroundColor(Color.BLACK);
                        txtBackOrder.setGravity(Gravity.START);
                        txtBackOrder.setText(listaBackOrders.get(i).getBackOrder());
                        txtBackOrder.setPadding(20, 20, 20, 20);
                        txtBackOrder.setTextColor(Color.WHITE);
                        txtBackOrder.setLayoutParams(layaoutDes);
                        fila.addView(txtBackOrder);

                        txtFolio = new TextView(getApplicationContext());
                        txtFolio.setBackgroundColor(Color.GRAY);
                        txtFolio.setGravity(Gravity.START);
                        txtFolio.setText(listaBackOrders.get(i).getFolio());
                        txtFolio.setPadding(20, 20, 20, 20);
                        txtFolio.setTextColor(Color.WHITE);
                        txtFolio.setLayoutParams(layaoutDes);
                        fila.addView(txtFolio);


                        Existencia = new TextView(getApplicationContext());
                        Existencia.setBackgroundColor(Color.BLACK);
                        Existencia.setGravity(Gravity.END);
                        Existencia.setText(listaBackOrders.get(i).getExistencia());
                        Existencia.setPadding(20, 20, 20, 20);
                        Existencia.setTextColor(Color.WHITE);
                        Existencia.setLayoutParams(layaoutDes);
                        fila.addView(Existencia);

                        fila.setPadding(2, 2, 2, 2);

                    }
                    tableLayout.addView(fila);

                }
                mDialog.dismiss();
            }//onPost
        }




    }

