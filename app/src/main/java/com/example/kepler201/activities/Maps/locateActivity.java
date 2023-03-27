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
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlDirEnvio;
import com.example.kepler201.XMLS.xmlDirEnvioMapas;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.includes.MyToolbar;
import com.google.android.material.textfield.TextInputLayout;

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
    TextInputLayout viewDireccion,viewOpciones;

    String mensaje = "";
    String strClaveCli;

    int positionClient,positionDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        MyToolbar.show(this, "", true);

        mDialog = new SpotsDialog.Builder().setContext(locateActivity.this).setMessage("Espere un momento...").build();

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
        String[] btn_Opcion = {"Mi Localizacion","Cambiar Localizacion","Localizacion Cliente","Trazar Ruta"};


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
                if(i==0){
                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente  = listaclientG.get(positionClient).getUserCliente();
                    String Direccion =listasearch5.get(positionDireccion).getCalle()+"\n"+listasearch5.get(positionDireccion).getColonia()+"\n"+listasearch5.get(positionDireccion).getNumero()+"\n"+listasearch5.get(positionDireccion).getPoblacion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud =listasearch5.get(positionDireccion).getLongitud();
                    Intent  vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                    vendedoresMaps.putExtra("val", 0);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                    vendedoresMaps.putExtra("idDireccion", idDireccion);
                    vendedoresMaps.putExtra("Direccion", Direccion);
                    vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                    vendedoresMaps.putExtra("DirLongitud", DirLongitud);
                    startActivity(vendedoresMaps);





                }else if (i==1){

                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente  = listaclientG.get(positionClient).getUserCliente();
                    String Direccion =listasearch5.get(positionDireccion).getCalle()+"\n"+listasearch5.get(positionDireccion).getColonia()+"\n"+listasearch5.get(positionDireccion).getNumero()+"\n"+listasearch5.get(positionDireccion).getPoblacion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud =listasearch5.get(positionDireccion).getLongitud();


                    Intent  vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
                    vendedoresMaps.putExtra("val", 1);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("NomCliente", NombreCliente);
                    vendedoresMaps.putExtra("ClaveCliente", claveCliente);
                    vendedoresMaps.putExtra("idDireccion", idDireccion);
                    vendedoresMaps.putExtra("Direccion", Direccion);
                    vendedoresMaps.putExtra("DirLatitud", DirLatitud);
                    vendedoresMaps.putExtra("DirLongitud", DirLongitud);

                    startActivity(vendedoresMaps);
                }else if (i==2){

                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente  = listaclientG.get(positionClient).getUserCliente();
                    String Direccion =listasearch5.get(positionDireccion).getCalle()+"\n"+listasearch5.get(positionDireccion).getColonia()+"\n"+listasearch5.get(positionDireccion).getNumero()+"\n"+listasearch5.get(positionDireccion).getPoblacion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud =listasearch5.get(positionDireccion).getLongitud();


                    if(Double.valueOf(DirLatitud)==0 && Double.valueOf(DirLongitud)==0){
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



                    }else{
                        Intent  vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
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



                }else if (i==3){


                    String NombreCliente = listaclientG.get(positionClient).getNombreCliente();
                    String claveCliente  = listaclientG.get(positionClient).getUserCliente();
                    String Direccion =listasearch5.get(positionDireccion).getCalle()+"\n"+listasearch5.get(positionDireccion).getColonia()+"\n"+listasearch5.get(positionDireccion).getNumero()+"\n"+listasearch5.get(positionDireccion).getPoblacion();
                    String idDireccion = listasearch5.get(positionDireccion).getId();
                    String DirLatitud = listasearch5.get(positionDireccion).getLatitud();
                    String DirLongitud =listasearch5.get(positionDireccion).getLongitud();


                    if(Double.valueOf(DirLatitud)==0 && Double.valueOf(DirLongitud)==0){
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



                    }else{
                        Intent  vendedoresMaps = new Intent(locateActivity.this, MapsActivity.class);
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
                        positionClient =which;
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
                    opciones[i] = listasearch5.get(i).getCalle() + "," + listasearch5.get(i).getColonia()+","+listasearch5.get(i).getNumero()+","+listasearch5.get(i).getPoblacion();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(locateActivity.this);
                builder.setTitle("Seleccione una Direccion").setIcon(R.drawable.icons8_marcador_de_mapa_100);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        positionDireccion =which;
                        ButtonDireccion.setText(listasearch5.get(which).getCalle()+","+listasearch5.get(which).getColonia()+","+listasearch5.get(which).getPoblacion());
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
            Clientes();
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


        } catch (SoapFault soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            mDialog.dismiss();
            mensaje = "Error:" + e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            mDialog.dismiss();
            mensaje = "No se encontro servidor";
            e.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
            mensaje = "Error:" + ex.getMessage();
        }
    }

    private class AsyncCallWS6 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar6();

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listasearch5.size()];

            for (int i = 0; i < listasearch5.size(); i++) {
                opciones[i] = "Calle:"+listasearch5.get(i).getCalle() + ",Poblacion:" + listasearch5.get(i).getPoblacion();
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


    private void conectar6() {
        String SOAP_ACTION = "EnvioMapa";
        String METHOD_NAME = "EnvioMapa";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlDirEnvioMapas soapEnvelope = new xmlDirEnvioMapas(SoapEnvelope.VER11);
            soapEnvelope.xmlDirEnvioMapas(strusr, strpass, strClaveCli);
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

                listasearch5.add(new Envio2SANDG((response0.getPropertyAsString("k_id").equals("anyType{}") ? "" : response0.getPropertyAsString("k_id")),
                        (response0.getPropertyAsString("k_CalleyNumero").equals("anyType{}") ? "" : response0.getPropertyAsString("k_CalleyNumero")),
                        (response0.getPropertyAsString("k_ColoClinte").equals("anyType{}") ? "" : response0.getPropertyAsString("k_ColoClinte")),
                        (response0.getPropertyAsString("k_Poblacion").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Poblacion")),
                        (response0.getPropertyAsString("k_Numero").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Numero")),
                        (response0.getPropertyAsString("k_latitud").equals("anyType{}") ? "" : response0.getPropertyAsString("k_latitud")),
                        (response0.getPropertyAsString("k_longitud").equals("anyType{}") ? "" : response0.getPropertyAsString("k_longitud"))));



            }


        } catch (SoapFault soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            mDialog.dismiss();
            mensaje = "Error:" + e.getMessage();
            e.printStackTrace();
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


