package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ValTipousSANDG;
import com.example.kepler201.includes.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityValdatipo extends AppCompatActivity {
    private SharedPreferences.Editor editor,editor2;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;
    ArrayList<ValTipousSANDG> listasearch = new ArrayList<>();
    ConexionSQLiteHelper conn;
    private SharedPreferences preferenceClie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valdatipo);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor2 = preferenceClie.edit();

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
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strco;
            String url = "http://" + StrServer + "/listasvendedoresapp?" + parametros;
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

                            listasearch.add(new ValTipousSANDG(
                                    (Numero.getString("nombres").equals("anyType{}") ? "" : Numero.getString("nombres")),
                                    (Numero.getString("apellidos").equals("anyType{}") ? "" : Numero.getString("apellidos")),
                                    (Numero.getString("email").equals("anyType{}") ? "" : Numero.getString("email")),
                                    (Numero.getString("sucursal").equals("anyType{}") ? "" : Numero.getString("sucursal")),
                                    (Numero.getString("clave").equals("anyType{}") ? "" : Numero.getString("clave")),
                                    (Numero.getString("type2").equals("anyType{}") ? "" : Numero.getString("type2")),
                                    (Numero.getString("descrabra").equals("anyType{}") ? "" : Numero.getString("descrabra"))));

                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityValdatipo.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityValdatipo.this);
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
                    editor2.clear();
                    editor2.commit();
                    editor2.apply();
                    BorrarCarrito();

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


    private void BorrarCarrito() {
        conn = new ConexionSQLiteHelper(ActivityValdatipo.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        db.delete("carrito", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
        db.close();
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