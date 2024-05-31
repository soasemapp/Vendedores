package com.example.kepler201.activities.Maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.Envio2SANDG;
import com.example.kepler201.SetterandGetter.RegistroPagosSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlDirEnvio;
import com.example.kepler201.XMLS.xmlDirEnvioMapas;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.activities.Pagos.ActivityConsultaPagos;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class locateActivity extends AppCompatActivity {

    AlertDialog mDialog;

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<Envio2SANDG> listasearch5 = new ArrayList<>();

    Button ButtonCliente;
    Button ButtonDireccion;


    int n = 2000;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    String[] search2 = new String[n];
    String[] search3 = new String[n];
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    AutoCompleteTextView /*spinerClie, spinnerDireccion,*/spinnerOpciones;
    TextInputLayout viewDireccion, viewOpciones;

    String mensaje = "";
    String strClaveCli;

    int positionClient, positionDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        MyToolbar.show(this, "", true);

        mDialog = new SpotsDialog.Builder().setContext(locateActivity.this).setMessage("Espere un momento...").build();
        mDialog.setCancelable(false);
        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

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

        ButtonCliente = findViewById(R.id.btnClientes);
        ButtonDireccion = findViewById(R.id.btnDirecciones);
        //spinerClie = findViewById(R.id.clienteSpinner);
        //spinnerDireccion = findViewById(R.id.Direccion);
        spinnerOpciones = findViewById(R.id.autoComplete_txt);

        //viewDireccion = findViewById(R.id.textInDireccion);
        viewOpciones = findViewById(R.id.textInOpciones);

        //spinerClie.setInputType(InputType.TYPE_NULL);
        spinnerOpciones.setInputType(InputType.TYPE_NULL);

        //viewDireccion.setEnabled(false);
        viewOpciones.setEnabled(false);
        spinnerOpciones.setEnabled(false);
        ButtonDireccion.setEnabled(false);
        AsyncClientes task = new AsyncClientes();
        task.execute();
        String[] btn_Opcion = {"Mi Localizacion", "Cambiar Localizacion", "Localizacion Cliente", "Trazar Ruta"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, btn_Opcion);
        spinnerOpciones.setAdapter(adapter);
/*
        spinerClie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                positionClient =i;
                strClaveCli = listaclientG.get(i).getUserCliente();
                listasearch5.clear();
                AsyncCallWS6 task = new AsyncCallWS6();
                task.execute();
                spinnerDireccion.setText(""); spinnerDireccion.setText("");
                spinnerOpciones.setText("");

            }
        });*/

      /*  spinnerDireccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                positionDireccion =i;
            }
        });*/

        spinnerOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente = listaclientG.get(positionClient).getUserCliente();
                    String Direccion = listasearch5.get(positionDireccion).getDireccion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud = listasearch5.get(positionDireccion).getLongitud();
                    Intent vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                    vendedoresMaps.putExtra("val", 0);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                    vendedoresMaps.putExtra("idDireccion", idDireccion);
                    vendedoresMaps.putExtra("Direccion", Direccion);
                    vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                    vendedoresMaps.putExtra("DirLongitud", DirLongitud);
                    startActivity(vendedoresMaps);


                } else if (i == 1) {

                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente = listaclientG.get(positionClient).getUserCliente();
                    String Direccion = listasearch5.get(positionDireccion).getDireccion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud = listasearch5.get(positionDireccion).getLongitud();


                    Intent vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                    vendedoresMaps.putExtra("val", 1);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                    vendedoresMaps.putExtra("idDireccion", idDireccion);
                    vendedoresMaps.putExtra("Direccion", Direccion);
                    vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                    vendedoresMaps.putExtra("DirLongitud", DirLongitud);

                    startActivity(vendedoresMaps);
                } else if (i == 2) {

                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente = listaclientG.get(positionClient).getUserCliente();
                    String Direccion = listasearch5.get(positionDireccion).getDireccion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud = listasearch5.get(positionDireccion).getLongitud();


                    if (Double.valueOf(DirLatitud) == 0 && Double.valueOf(DirLongitud) == 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(locateActivity.this);
                        alerta.setMessage("La direccion de este cliente no a sido dada de alta").setIcon(R.drawable.icons8_error_100).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Localizacion no ah sido encontrada");
                        titulo.show();


                    } else {
                        Intent vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                        vendedoresMaps.putExtra("val", 2);
                        vendedoresMaps.putExtra("NomCliente", NombreCliente);
                        vendedoresMaps.putExtra("NomCliente", NombreCliente);
                        vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                        vendedoresMaps.putExtra("idDireccion", idDireccion);
                        vendedoresMaps.putExtra("Direccion", Direccion);
                        vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                        vendedoresMaps.putExtra("DirLongitud", DirLongitud);
                        startActivity(vendedoresMaps);
                    }


                } else if (i == 3) {


                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente = listaclientG.get(positionClient).getUserCliente();
                    String Direccion = listasearch5.get(positionDireccion).getDireccion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud = listasearch5.get(positionDireccion).getLongitud();


                    if (Double.valueOf(DirLatitud) == 0 && Double.valueOf(DirLongitud) == 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(locateActivity.this);
                        alerta.setMessage("La direccion de este cliente no a sido dada de alta").setIcon(R.drawable.icons8_error_100).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Localizacion no ah sido encontrada");
                        titulo.show();


                    } else {
                        Intent vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                        vendedoresMaps.putExtra("val", 3);
                        vendedoresMaps.putExtra("NomCliente", NombreCliente);
                        vendedoresMaps.putExtra("NomCliente", NombreCliente);
                        vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                        vendedoresMaps.putExtra("idDireccion", idDireccion);
                        vendedoresMaps.putExtra("Direccion", Direccion);
                        vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                        vendedoresMaps.putExtra("DirLongitud", DirLongitud);
                        startActivity(vendedoresMaps);
                    }

                }
            }
        });


        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaclientG.size()];

                for (int i = 0; i < listaclientG.size(); i++) {
                    opciones[i] = listaclientG.get(i).getUserCliente() + ":" + listaclientG.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(locateActivity.this);
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ButtonDireccion.setText("SELECCIONA UNA DIRECCION");
                        listasearch5.clear();
                        positionClient = which;
                        strClaveCli = listaclientG.get(which).getUserCliente();
                        ButtonCliente.setText(listaclientG.get(which).getNombreCliente());
                        AsyncCallWS6 task = new AsyncCallWS6();
                        task.execute();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ButtonDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listasearch5.size()];

                for (int i = 0; i < listasearch5.size(); i++) {
                    opciones[i] = listasearch5.get(i).getDireccion();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(locateActivity.this);
                builder.setTitle("Seleccione una Direccion").setIcon(R.drawable.icons8_marcador_de_mapa_100);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        positionDireccion = which;
                        ButtonDireccion.setText(listasearch5.get(which).getDireccion());
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }


    private class AsyncClientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(locateActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(locateActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listaclientG.size()];

            for (int i = 0; i < listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i).getUserCliente() + ":" + listaclientG.get(i).getNombreCliente();
                search2[i] = listaclientG.get(i).getUserCliente();
            }
            mDialog.dismiss();

        }


    }


    private class AsyncCallWS6 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strClaveCli;
            String url = "http://" + StrServer + "/enviomapaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if(json.length()!=0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Dir");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Dir");
                            Numero = jitems.getJSONObject("" + i + "");
                            listasearch5.add(new Envio2SANDG((Numero.getString("k_id").equals("") ? "" : Numero.getString("k_id")),
                                    (Numero.getString("k_direcciones").equals("") ? "" : Numero.getString("k_direcciones")),
                                    (Numero.getString("k_latitud").equals("") ? "0" : Numero.getString("k_latitud")),
                                    (Numero.getString("k_longitud").equals("") ? "0" : Numero.getString("k_longitud"))));


                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(locateActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(locateActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listasearch5.size()];

            for (int i = 0; i < listasearch5.size(); i++) {
                opciones[i] = listasearch5.get(i).getDireccion();
                search3[i] = listasearch5.get(i).getId();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);


            if (listasearch5.size() > 0) {
                //     viewDireccion.setEnabled(true);
                viewOpciones.setEnabled(true);
                ButtonDireccion.setEnabled(true);


            } else {

                AlertDialog.Builder alerta = new AlertDialog.Builder(locateActivity.this);
                alerta.setMessage("No se encontro una direccion del cliente").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("!Error de Direccion!");
                titulo.show();
                viewOpciones.setEnabled(false);
                spinnerOpciones.setEnabled(false);
                ButtonDireccion.setEnabled(false);
                /*spinnerDireccion.setText("");
                spinnerOpciones.setText("");*/

            }
            mDialog.dismiss();
        }

    }
}



