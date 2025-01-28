package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.SucursalSANDG;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityPerfil extends AppCompatActivity {


    ImageView imgEmpresa;
    private SharedPreferences.Editor editor;
    private SharedPreferences preference;
    Button sucursal;

    //TextView
    ArrayList<SucursalSANDG> listasucursal = new ArrayList<>();
    String strusr="", strpass="", strname="", strlname="", strtype="", strtype2="", strma="", strco="", strcodBra="",strbran="",strcodBra2="",strbran2="", StrServer="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MyToolbar.show(this, "Perfil", true);

        TextView usr = findViewById(R.id.txtusr);
        TextView password =  findViewById(R.id.txtpass);
        TextView name =  findViewById(R.id.txtname);
        TextView apellido =  findViewById(R.id.txtlname);
        TextView tipo =  findViewById(R.id.txttype);
         sucursal =  findViewById(R.id.btsucursal);
        TextView mail =  findViewById(R.id.txtemail);
        TextView code =  findViewById(R.id.txtk_code);
        imgEmpresa= findViewById(R.id.imgEmpresa);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        strusr = preference.getString("user", "");
        strpass = preference.getString("pass", "");
        strname = preference.getString("name", "");
        strlname = preference.getString("lname", "");
        strtype = preference.getString("type", null);
        strma = preference.getString("email", "");
        strbran = preference.getString("branch", "");
        strcodBra = preference.getString("codBra", "");
        strbran2 = preference.getString("branch2", "");
        strcodBra2 = preference.getString("codBra2", "");
        strco = preference.getString("code", "");
        StrServer = preference.getString("Server", "");
        strtype2 = preference.getString("type2", null);

        usr.setText("                                          " + strusr);
        password.setText("                                          " + strpass);
        name.setText(strname);
        apellido.setText(strlname);
        tipo.setText((strtype2 != null )?strtype2:strtype);
        sucursal.setText(strbran);
        mail.setText(strma);
        code.setText(strco);
        sucursal.setEnabled(false);
        sucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] opciones = new String[listasucursal.size()];

                for (int i = 0; i < listasucursal.size(); i++) {
                    opciones[i] = listasucursal.get(i).getNombre();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPerfil.this);
                builder.setTitle("Seleccione una Sucursal").setIcon(R.drawable.icons_banco);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strbran = listasucursal.get(which).getNombre();
                        strcodBra= listasucursal.get(which).getClave();
                        editor.putString("branch", strbran);
                        editor.putString("codBra",strcodBra);
                        editor.commit();
                        editor.apply();
                        sucursal.setText(strbran);
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        switch (StrServer) {
            case "jacve.dyndns.org: ":
                Picasso.with(getApplicationContext()).load(R.drawable.jacve)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
            case "autodis.ath.cx:9085":

                Picasso.with(getApplicationContext()).load(R.drawable.autodis)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
            case "cecra.ath.cx:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.cecra)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "guvi.ath.cx:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.guvi)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.vipla)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "cedistabasco.ddns.net:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.pressa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9090":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9095":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9080":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "vazlocolombia.dyndns.org:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.colombia2)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
        }
if(strbran2.equals("")) {
    sucursal.setEnabled(true);
    new ActivityPerfil.SucursalesLista().execute();
}


    }

    private class SucursalesLista extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/listasucursalapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {
                        if (json.length() != 0) {
                            JSONObject jitems, Numero;
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            jitems = jsonObject.getJSONObject("Listado");

                            for (int i = 0; i < jitems.length(); i++) {
                                jitems = jsonObject.getJSONObject("Listado");
                                Numero = jitems.getJSONObject("" + i + "");
                                listasucursal.add(new SucursalSANDG(
                                        Numero.getString("clave"),
                                        Numero.getString("nombre")));
                            }
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityPerfil.this);
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

                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityPerfil.this);
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
}