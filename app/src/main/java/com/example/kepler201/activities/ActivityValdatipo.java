package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ValTipousSANDG;
import com.example.kepler201.XMLS.xmlValTipo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityValdatipo extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;
    ArrayList<ValTipousSANDG> listasearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valdatipo);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();


        strusr = preference.getString("user", "null");
        strpass = preference.getString("pass", "null");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strtype = preference.getString("type", "null");
        strbran = preference.getString("branch", "null");
        strma = preference.getString("email", "null");
        strcodBra = preference.getString("codBra", "null");
        strco = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");




        valdidatipo();


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


            String[] opciones = new String[listasearch.size()];

            for (int i = 0; i < listasearch.size(); i++) {
                opciones[i] = listasearch.get(i).getNombre() + " " + listasearch.get(i).getApellido();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityValdatipo.this);
            builder.setTitle("SELECCIONE UN VENDEDOR");


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String tipoad =listasearch.get(which).getTipo2();

                    editor.putString("name", listasearch.get(which).getNombre());
                    editor.putString("lname", listasearch.get(which).getApellido());
                    editor.putString("email", listasearch.get(which).getEmail());
                    editor.putString("code", listasearch.get(which).getClave());
                    editor.putString("codBra", listasearch.get(which).getSucursal());
                    editor.putString("type2",tipoad);
                    editor.putString("branch", listasearch.get(which).getDescr());
                    editor.commit();



                    Intent ScreenFir = new Intent(ActivityValdatipo.this, inicioActivity.class);
                    startActivity(ScreenFir);
                    finish();

                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }


    private void conectar() {
        String SOAP_ACTION = "ValTipoUser";
        String METHOD_NAME = "ValTipoUser";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlValTipo soapEnvelope = new xmlValTipo(SoapEnvelope.VER11);
            soapEnvelope.xmlValTipo(strusr, strpass);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                listasearch.add(new ValTipousSANDG(
                        (response0.getPropertyAsString("k_Nombres").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Nombres")),
                        (response0.getPropertyAsString("k_Apellidos").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Apellidos")),
                        (response0.getPropertyAsString("k_Email").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Email")),
                        (response0.getPropertyAsString("k_Sucursal").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Sucursal")),
                        (response0.getPropertyAsString("k_Clave").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Clave")),
                        (response0.getPropertyAsString("k_type2").equals("anyType{}") ? "" : response0.getPropertyAsString("k_type2")),
                        (response0.getPropertyAsString("k_descBra").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descBra"))));


            }

        } catch (XmlPullParserException | IOException soapFault) {
            soapFault.printStackTrace();
        } catch (Exception ignored) {
        }
    }


    public void valdidatipo() {

        if (strtype.equals("VENDEDOR")) {
            Intent ScreenFir = new Intent(ActivityValdatipo.this, inicioActivity.class);
            startActivity(ScreenFir);
            finish();
        } else if (strtype.equals("ADMIN")) {

            ActivityValdatipo.AsyncCallWS task = new ActivityValdatipo.AsyncCallWS();
            task.execute();


        }else {
            Intent ScreenFir = new Intent(ActivityValdatipo.this, inicioActivity.class);
            startActivity(ScreenFir);
            finish();
        }


    }
}