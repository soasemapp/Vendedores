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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorClientes;
import com.example.kepler201.Adapter.AdapterAgenda;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.BusquedaCliente0VentasSANDG;
import com.example.kepler201.XMLS.xmlClient0VeEspe;
import com.example.kepler201.XMLS.xmlClient0Ventas;
import com.example.kepler201.activities.Agenda.ActivityAgenda;
import com.example.kepler201.activities.MainActivity;
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
import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityClientes0Ventas extends AppCompatActivity {

    ImageView ConsultaFacturas, BackOreders, FacturasVencidas, Cliente0Ventas;
    private EditText eddias;

    ArrayList<BusquedaCliente0VentasSANDG> listasearch = new ArrayList<>();
    RecyclerView recyclerClientes;
    String ClaveDialog = "";
    String strcode, strdias;
    String Clave, Folio, Fecha, Monto;
    String mensaje = "";
    String strusr, strpass, strname, strlname, strtype, strbran, strcodBra, strma, StrServer;
    AlertDialog mDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes0_ventas);
        mDialog = new SpotsDialog(ActivityClientes0Ventas.this);
        mDialog.setCancelable(false);
        recyclerClientes =  findViewById(R.id.lisClient);
        eddias =  findViewById(R.id.edidias);
        Button btnbuscar =  findViewById(R.id.btnSearch);
        ConsultaFacturas =  findViewById(R.id.SearchProducto);
        BackOreders =  findViewById(R.id.BackOrders);
        FacturasVencidas = findViewById(R.id.FacturasVencidas);
        Cliente0Ventas =  findViewById(R.id.Cliente0Ventas);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        MyToolbar.show(this, "Consulta-Cliente 0 Ventas", true);

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




        eddias.setText("45");


        ConsultaFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaFacturas.setBackgroundColor(Color.GRAY);
                ConsultaFacturas.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsuFaInten = new Intent(ActivityClientes0Ventas.this, ActivityConsultaFaturas.class);
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
                Intent BackOrdeInten = new Intent(ActivityClientes0Ventas.this, ActivityBackOrders.class);
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
                Intent FactuItent = new Intent(ActivityClientes0Ventas.this, ActivityFacturasVencidas.class);
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
                Intent Cliente0VenIntent = new Intent(ActivityClientes0Ventas.this, ActivityClientes0Ventas.class);
                overridePendingTransition(0, 0);
                startActivity(Cliente0VenIntent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listasearch = new ArrayList<>();
                    recyclerClientes.setLayoutManager(new LinearLayoutManager(ActivityClientes0Ventas.this));
                    strdias = eddias.getText().toString();

                Cliente0Ventas();
            }
        });
        strdias = eddias.getText().toString();
        listasearch = new ArrayList<>();
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));


    }

    public void Cliente0Ventas() {
        new ActivityClientes0Ventas.Cliente0Ventas().execute();
    }


    private class Cliente0Ventas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor="+strcode+"&dias="+strdias;
            String url = "http://" + StrServer + "/clientes0ventasapp?" + parametros;
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
                            listasearch.add(new BusquedaCliente0VentasSANDG(
                                    Numero.getString("clave"),
                                    Numero.getString("nombre"),
                                    Numero.getString("ciudad"),
                                    Numero.getString("direccion"),
                                    Numero.getString("activos")));
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityClientes0Ventas.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityClientes0Ventas.this);
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

            AdaptadorClientes adapter = new AdaptadorClientes(listasearch);


            recyclerClientes.setAdapter(adapter);
            mDialog.dismiss();
        }//onPost
    }


    public void Cliente0VentasDeta() {
        new ActivityClientes0Ventas.Cliente0VentasDetallado().execute();
    }


    private class Cliente0VentasDetallado extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente="+ClaveDialog;
            String url = "http://" + StrServer + "/clientes0ventasdetallapp?" + parametros;
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
                          Clave=Numero.getString("clave");
                          Folio=Numero.getString("folio");
                          Fecha=Numero.getString("fecha");
                          Monto=Numero.getString("monto");;
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityClientes0Ventas.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityClientes0Ventas.this);
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
            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityClientes0Ventas.this);
            if (!Clave.isEmpty() && !Fecha.isEmpty() && !Folio.isEmpty() && !Monto.isEmpty()) {

                alerta.setMessage("Folio= " + Folio + "\n" +
                        "Fecha= " + Fecha + "\n" +
                        "Monto= " + "$" + formatNumberCurrency(Monto) + "\n").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Cliente:" + Clave);
                titulo.show();

            } else {
                alerta.setMessage("Cliente sin venta").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Ventas");
                titulo.show();
            }
        }//onPost
    }






    public void detalladoCliente(View view) {
        Clave = "";
        Folio = "";
        Fecha = "";
        Monto = "";

        int position = recyclerClientes.getChildAdapterPosition(Objects.requireNonNull(recyclerClientes.findContainingItemView(view)));
        ClaveDialog = listasearch.get(position).getClave();
        Cliente0VentasDeta();

    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuflow4, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.GraphicInput) {

            if (listasearch.size() != 0) {

                String Inactivos = String.valueOf(listasearch.size());
                String Activos = listasearch.get(0).getActivos();
                Intent Grafica = new Intent(this, ActivityGraphic.class);
                Grafica.putExtra("ClInativos", Inactivos);
                Grafica.putExtra("ClIActivos", Activos);
                startActivity(Grafica);
            }else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityClientes0Ventas.this);
                alerta.setMessage("No hay datos para llenar la graficas").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Error de grafica");
                titulo.show();
            }


        }

        return super.onOptionsItemSelected(item);

    }


}