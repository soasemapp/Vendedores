package com.example.kepler201.activities.Pagos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepler201.Adapter.AdapterDetalladoPagos;
import com.example.kepler201.Adapter.AdapterFacturasnoregistradas;
import com.example.kepler201.Adapter.AdapterPagosnoConfirmados;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.BANKSANDG;
import com.example.kepler201.SetterandGetter.DetalladoPagosSANDG;
import com.example.kepler201.SetterandGetter.FacturasnoregistradasNewSANDG;
import com.example.kepler201.SetterandGetter.FacturasnoregistradasSeleccionadasSANDG;
import com.example.kepler201.SetterandGetter.RegistroPagosNewSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlRegistroPagos2;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class RegitrodepagosActivity extends AppCompatActivity {
    double montototal;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String strclave = "", strnombre = "", strclavebanco = "", strnombrebanco = "", strformadepago = "", mensaje = "";
    AlertDialog mDialog;
    String Fecha, Hora, Cliente;
    Button ButtonCliente, ButtonBanco, ButtonFormadepago, ButtonFacturas, ButtonAgregar;
    TextView Monto;
    EditText Comentario;
    private EditText fechaEn, fechaSa;
    private TextView monto;
    int n = 2000;
    ArrayList<SearachClientSANDG> listaclientes = new ArrayList<>();
    ArrayList<BANKSANDG> listaBancos = new ArrayList<>();
    ArrayList<RegistroPagosNewSANDG> listadepagos = new ArrayList<>();
    ArrayList<DetalladoPagosSANDG> listadetallado = new ArrayList<>();
    ArrayList<FacturasnoregistradasNewSANDG> listanoregistrados = new ArrayList<>();
    ArrayList<FacturasnoregistradasSeleccionadasSANDG> listaseleccionados = new ArrayList<>();
    private AdapterPagosnoConfirmados adapter;
    private AdapterDetalladoPagos adapter1;
    private AdapterFacturasnoregistradas adapter2;
    RecyclerView recyclerPagos, recyclerDialog2, recyclerDialog3;

    AlertDialog.Builder builder2, builder3, builder4;
    AlertDialog dialog2 = null, dialog3 = null, dialog4 = null;
    String FechaIncial;
    String FechaFinal;
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    String fechaainicial;
    String fechafinal;
    String fechapago;
    String currentTime;
    String FACTURASACUM = "", StrComentarios1;
    private EditText fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regitrodepagos);
        MyToolbar.show(this, "Registro de pagos", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        mDialog = new SpotsDialog(RegitrodepagosActivity.this);
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
        TextView vendedor = findViewById(R.id.cVendedor);
        vendedor.setText(strname + " " + strlname);
        ButtonCliente = findViewById(R.id.btncliente);
        ButtonBanco = findViewById(R.id.btnbanco);
        ButtonFormadepago = findViewById(R.id.btnforma);
        recyclerPagos = findViewById(R.id.lis_pagos);
        ButtonFacturas = findViewById(R.id.btnfactura);
        Monto = findViewById(R.id.importestr);
        Comentario = findViewById(R.id.comentariosstr);
        ButtonFacturas.setEnabled(false);
        ButtonFormadepago.setEnabled(false);
        ButtonBanco.setEnabled(false);
        fecha = findViewById(R.id.fecha);
        ButtonAgregar = findViewById(R.id.btnAgregar);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ButtonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agregar();
            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegitrodepagosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        fechaainicial = year + "-" + month + "-" + day;
                        fecha.setText(fechaainicial);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.MONTH, -1);
        fechaainicial = dateformatActually1.format(c.getTime());

        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        fechafinal = dateformatActually.format(c1.getTime());

        Calendar c2 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually2 = new SimpleDateFormat("yyyy-MM-dd");
        fechapago = dateformatActually2.format(c2.getTime());

        fecha.setText(fechapago);
        recyclerPagos.setLayoutManager(new LinearLayoutManager(RegitrodepagosActivity.this));

        Listaclientes();


        ButtonFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfacturas();

            }
        });
        ButtonFormadepago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones1 = {"Efectivo", "Cheque", "Transferencia Bancaria"};
                AlertDialog.Builder builder = new AlertDialog.Builder(RegitrodepagosActivity.this);
                builder.setTitle("SELECCIONE UNA FORMA DE PAGO").setIcon(R.drawable.icons_muro_de_pago);


                builder.setItems(opciones1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strformadepago = opciones1[which];
                        ButtonFormadepago.setText(strformadepago);
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaclientes.size()];

                for (int i = 0; i < listaclientes.size(); i++) {
                    opciones[i] = listaclientes.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(RegitrodepagosActivity.this);
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listanoregistrados.clear();
                        strclave = listaclientes.get(which).getUserCliente();
                        strnombre = listaclientes.get(which).getNombreCliente();
                        ButtonCliente.setText(listaclientes.get(which).getNombreCliente());
                        ButtonFacturas.setEnabled(true);
                        builder2 = new AlertDialog.Builder(getApplicationContext());
                        dialog2 = builder2.create();
                        new Facturasnoregistradas().execute();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ButtonBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaBancos.size()];

                for (int i = 0; i < listaBancos.size(); i++) {
                    opciones[i] = listaBancos.get(i).getNBanco();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(RegitrodepagosActivity.this);
                builder.setTitle("SELECCIONE UN BANCO").setIcon(R.drawable.icons_banco);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strclavebanco = listaBancos.get(which).getCode();
                        strnombrebanco = listaBancos.get(which).getNBanco();
                        ButtonBanco.setText(listaBancos.get(which).getNBanco());
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    public void Listaclientes() {
        new Cliente().execute();
    }

    public void Listabancos() {
        new Banco().execute();
    }

    public void Listadepagosnoconfirmados() {
        new PagosnoConfirmados().execute();
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
                    if (json.length() != 0) {

                        JSONObject jitems, Numero, Clave, Nombre;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Clientes");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Clientes");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaclientes.add(new SearachClientSANDG(
                                    Numero.getString("Clave"),
                                    Numero.getString("Nombre")));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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
            Listabancos();
        }//onPost
    }

    private class Banco extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/bancoapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.length() != 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Banco");
                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Banco");
                            Numero = jitems.getJSONObject("" + i + "");
                            listaBancos.add(new BANKSANDG(
                                    Numero.getString("Clave"),
                                    Numero.getString("Nombre")));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                            alerta1.setMessage("El json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();
                        mDialog.dismiss();
                    }//run
                });//runUniTthread
            }//else
            return null;
        }//doInBackground

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            Listadepagosnoconfirmados();
        }//onPost
    }

    private class PagosnoConfirmados extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strcode;
            String url = "http://" + StrServer + "/pagosnoconfirmadosapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.length() != 0) {

                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Listado");
                        if (jitems.length() != 0) {
                            for (int i = 0; i < jitems.length(); i++) {
                                jitems = jsonObject.getJSONObject("Listado");
                                Numero = jitems.getJSONObject("" + i + "");
                                listadepagos.add(new RegistroPagosNewSANDG(
                                        Numero.getString("fecha"),
                                        Numero.getString("hora"),
                                        Numero.getString("vendedor"),
                                        Numero.getString("clavecliente"),
                                        Numero.getString("nombre"),
                                        Numero.getString("facturas"),
                                        Numero.getString("monto"),
                                        Numero.getString("banco"),
                                        Numero.getString("banconombre"),
                                        Numero.getString("formadepago"),
                                        Numero.getString("com1"),
                                        Numero.getString("com2"),
                                        Numero.getString("com3")));
                            }
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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
            adapter = new AdapterPagosnoConfirmados(listadepagos);


            recyclerPagos.setAdapter(adapter);
            mDialog.dismiss();
        }//onPost
    }


    private class Facturasnoregistradas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "fechainicio=" + fechaainicial + "&fechafinal=" + fechafinal + "&vendedor=" + strcode + "&cliente=" + strclave;
            String url = "http://" + StrServer + "/facturasnoregistradasapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if (json.length() != 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Listado");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Listado");
                            Numero = jitems.getJSONObject("" + i + "");
                            listanoregistrados.add(new FacturasnoregistradasNewSANDG(
                                    Numero.getString("sucursal"),
                                    Numero.getString("folio"),
                                    Numero.getString("fecha"),
                                    Numero.getString("saldo"),
                                    Numero.getString("monto"),
                                    Numero.getString("dpp"),
                                    Numero.getString("pago-dpp"),
                                    Numero.getString("montoapagar"),
                                    Numero.getString("vence"),
                                    Numero.getString("comentario"),
                                    Numero.getString("renglon"),
                                    Numero.getString("facturareg"),
                                    0));
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
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

        }//onPost
    }

    @SuppressLint("MissingInflatedId")
    public void dialogfacturas() {
        builder2 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_facturasnoregistradas, null);
        builder2.setView(dialogView);
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FACTURASACUM = "";
                int contador = 0;

                for (int i = 0; i < listanoregistrados.size(); i++) {


                    if (listanoregistrados.get(i).getSeleccionado() == 1) {
                        contador++;
                        String SUC = listanoregistrados.get(i).getSUC();
                        String FOLIO = listanoregistrados.get(i).getFOLIO();
                        String MONTO = listanoregistrados.get(i).getMONTO();
                        String MONTOAPAGAR = listanoregistrados.get(i).getMONTOAPAGAR();
                        String Comentario = listanoregistrados.get(i).getComentario();
                        if (contador == 1) {
                            FACTURASACUM = FOLIO;
                        } else {
                            FACTURASACUM = FOLIO + "," + FACTURASACUM;
                        }


                        listaseleccionados.add(new FacturasnoregistradasSeleccionadasSANDG(strclave, strcode, SUC, FOLIO, MONTO, MONTOAPAGAR, Comentario));
                    }

                }
                Monto.setText("$" + formatNumberCurrency(String.valueOf(montototal)));
                ButtonFacturas.setText(FACTURASACUM);
                ButtonBanco.setEnabled(true);
                ButtonFormadepago.setEnabled(true);
            }
        });
        builder2.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < listanoregistrados.size(); i++) {

                    if (listanoregistrados.get(i).getSeleccionado() == 1) {
                        listanoregistrados.get(i).setSeleccionado(0);
                    }
                }
                listaseleccionados.clear();
                montototal = 0;
                FACTURASACUM = "";
                Monto.setText("");

                ButtonFacturas.setText("SELECCIONE LAS FACTURAS");
                dialog2.dismiss();
            }
        });
        recyclerDialog2 = (RecyclerView) dialogView.findViewById(R.id.pagosnoregis);
        fechaEn = (EditText) dialogView.findViewById(R.id.FechaInicial);
        fechaSa = (EditText) dialogView.findViewById(R.id.fechafianl);
        monto = (TextView) dialogView.findViewById(R.id.montoapagar);
        monto.setText("Monto a pagar:$0.00");
        GridLayoutManager gl = new GridLayoutManager(this, 1);
        recyclerDialog2.setLayoutManager(gl);
        monto.setText("Monto a pagar: $ " + formatNumberCurrency(String.valueOf(montototal)));
        fechaEn.setText(fechaainicial);
        fechaSa.setText(fechafinal);


        adapter2 = new AdapterFacturasnoregistradas(listanoregistrados);
        recyclerDialog2.setAdapter(null);
        recyclerDialog2.setAdapter(adapter2);
        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegitrodepagosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        fechaainicial = year + "-" + month + "-" + day;
                        fechaEn.setText(fechaainicial);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        fechaSa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegitrodepagosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        fechafinal = year + "-" + month + "-" + day;
                        fechaSa.setText(fechafinal);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        dialog2 = builder2.create();
        dialog2.show();
        mDialog.dismiss();
    }


    private class AgregarFacturas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "agente=" + strcode + "&clave=" + strclave + "&fecha=" + fechapago + "&hora=" + currentTime + "&importe=" + String.valueOf(montototal) + "&facturas=" + FACTURASACUM + "&banco=" + strclavebanco + "&formapago=" + strformadepago + "&comentario1=" + StrComentarios1;
            String url = "http://" + StrServer + "/registropagoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String Registrado;
                    Registrado = jsonObject.getString("Resultado");

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                            alerta1.setMessage("El json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();
                        mDialog.dismiss();
                    }//run
                });//runUniTthread
            }//else
            return null;
        }//doInBackground

        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            RegitrodepagosActivity.Registrodepagos task = new RegitrodepagosActivity.Registrodepagos();
            task.execute();
        }//onPost
    }


    private class Detallado extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "fecha=" + Fecha + "&hora=" + Hora + "&clave=" + Cliente;
            String url = "http://" + StrServer + "/pagodetalladoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);


                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Listado");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("Listado");
                        Numero = jitems.getJSONObject("" + i + "");
                        listadetallado.add(new DetalladoPagosSANDG(
                                Numero.getString("sucursal"),
                                Numero.getString("folio"),
                                Numero.getString("monto"),
                                Numero.getString("montoapagar"),
                                Numero.getString("comentario")));
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                            alerta1.setMessage("El json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();
                        mDialog.dismiss();
                    }//run
                });//runUniTthread
            }//else
            return null;
        }//doInBackground

        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            DetalladoDialog();
        }//onPost
    }


    @SuppressLint("MissingInflatedId")
    public void DetalladoDialog() {
        builder4 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_detalladodepago, null);
        builder4.setView(dialogView);
        recyclerDialog3 = (RecyclerView) dialogView.findViewById(R.id.pagodetallado);
        GridLayoutManager gl = new GridLayoutManager(this, 1);
        recyclerDialog3.setLayoutManager(gl);


        adapter1 = new AdapterDetalladoPagos(listadetallado);


        recyclerDialog3.setAdapter(adapter1);
        dialog3 = builder4.create();
        dialog3.show();
        mDialog.dismiss();
    }

    private class EliminarPago extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "fecha=" + Fecha + "&hora=" + Hora + "&clave=" + Cliente;
            String url = "http://" + StrServer + "/eliminarpagoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String Registrado;
                    Registrado = jsonObject.getString("Resultado");

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                            alerta1.setMessage("El json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();
                        mDialog.dismiss();
                    }//run
                });//runUniTthread
            }//else
            return null;
        }//doInBackground

        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            AlertDialog.Builder alerta1 = new AlertDialog.Builder(RegitrodepagosActivity.this);
            alerta1.setMessage("Pago Eliminado con exito").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listadepagos.clear();
                    recyclerPagos.setAdapter(null);
                    Listadepagosnoconfirmados();
                    dialogInterface.dismiss();
                }
            });
            AlertDialog titulo1 = alerta1.create();
            titulo1.setTitle("Operacion exitosa");
            titulo1.show();
        }//onPost
    }


    public void resaltar(View view) {
        final int position = recyclerPagos.getChildAdapterPosition(Objects.requireNonNull(recyclerPagos.findContainingItemView(view)));
        adapter.index(position);
        adapter.notifyDataSetChanged();
        AlertDialog.Builder alerta = new AlertDialog.Builder(RegitrodepagosActivity.this);
        alerta.setMessage("¿Que deceas hacer?"
        ).setCancelable(false).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alerta2 = new AlertDialog.Builder(RegitrodepagosActivity.this);
                alerta2.setMessage("¿Deseas realizar la siguiente eliminacion?" + "\n" +
                        "Fecha:" + listadepagos.get(position).getFecha() + "\n" +
                        "Hora:" + listadepagos.get(position).getHora() + "\n" +
                        "Cliente:" + listadepagos.get(position).getClaveCliente() + "\n" +
                        "Facturas:" + listadepagos.get(position).getFacturas()
                ).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Fecha = listadepagos.get(position).getFecha();
                        Hora = listadepagos.get(position).getHora();
                        Cliente = listadepagos.get(position).getClaveCliente();

                        new EliminarPago().execute();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog titulo2 = alerta2.create();
                titulo2.setTitle("Eliminar Dato");
                titulo2.show();

                dialogInterface.dismiss();
            }
        }).setNegativeButton("Detallado", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fecha = listadepagos.get(position).getFecha();
                Hora = listadepagos.get(position).getHora();
                Cliente = listadepagos.get(position).getClaveCliente();
                new Detallado().execute();

            }
        }).setCancelable(true);

        AlertDialog titulo = alerta.create();
        titulo.setTitle("Y ahora?");
        titulo.show();


    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    public void resaltar2(View view) {
        final int position = recyclerDialog2.getChildAdapterPosition(Objects.requireNonNull(recyclerDialog2.findContainingItemView(view)));
        int seleccionado = listanoregistrados.get(position).getSeleccionado();

        if (seleccionado == 1) {
            listanoregistrados.get(position).setSeleccionado(0);
            montototal -= Double.parseDouble(listanoregistrados.get(position).getMONTOAPAGAR());
            monto.setText("Monto a pagar: $ " + formatNumberCurrency(String.valueOf(montototal)));
        } else {
            listanoregistrados.get(position).setSeleccionado(1);
            montototal += Double.parseDouble(listanoregistrados.get(position).getMONTOAPAGAR());
            monto.setText("Monto a pagar: $ " + formatNumberCurrency(String.valueOf(montototal)));
        }
        adapter2.notifyDataSetChanged();
    }

    public void Comentario(View view) {
        final int position = recyclerDialog2.getChildAdapterPosition(Objects.requireNonNull(recyclerDialog2.findContainingItemView(view)));

        builder3 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_comentario, null);
        builder3.setView(dialogView);
        EditText COMENTARIO = dialogView.findViewById(R.id.edcomentario);

        builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listanoregistrados.get(position).setComentario(COMENTARIO.getText().toString());
            }
        });
        builder3.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog3 = builder3.create();
        dialog3.show();


    }


    public void BusquedaNoregistardos(View view) {
        listanoregistrados.clear();
        new Facturasnoregistradas().execute();
    }


    public void Agregar() {
        StrComentarios1 = Comentario.getText().toString();
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        fechapago = fecha.getText().toString();

        new AgregarFacturas().execute();


    }


    //WebServise Ingresar

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Registrodepagos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            RegistroPagos();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            ButtonFacturas.setEnabled(false);
            ButtonBanco.setEnabled(false);
            ButtonFormadepago.setEnabled(false);
            Monto.setText("");
            Comentario.setText("");
            Calendar c2 = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually2 = new SimpleDateFormat("yyyy-MM-dd");
            fechapago = dateformatActually2.format(c2.getTime());
            fecha.setText(fechapago);
            listadepagos.clear();
            recyclerPagos.setAdapter(null);
            Listadepagosnoconfirmados();
            Toast.makeText(RegitrodepagosActivity.this, "Movimiento Exitoso", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }


    }


    private void RegistroPagos() {
        String SOAP_ACTION = "SoapAction1";
        String METHOD_NAME = "SoapAction1";
        String NAMESPACE = "http://" + StrServer + "/libreria/";
        String URL = "http://" + StrServer + "/libreria";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlRegistroPagos2 soapEnvelope = new xmlRegistroPagos2(SoapEnvelope.VER11);
            soapEnvelope.xmlRegistroPagos2(strusr, strpass, fechapago, currentTime, listaseleccionados);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;


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
        getMenuInflater().inflate(R.menu.menuflow3, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ConsulPago) {
            Intent ConsultaPagos = new Intent(this, ActivityConsultaPagos.class);
            overridePendingTransition(0, 0);
            startActivity(ConsultaPagos);
            overridePendingTransition(0, 0);

        }

        return super.onOptionsItemSelected(item);

    }


}