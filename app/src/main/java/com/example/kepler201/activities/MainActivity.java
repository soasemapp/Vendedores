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
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.Login;
import com.example.kepler201.XMLS.xmlLog;
import com.example.kepler201.XMLS.xmlLogin;
import com.example.kepler201.XMLS.xmlVerificacionPrecios;
import com.example.kepler201.XMLS.xmlVersiones;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Vector;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("rawtypes")
public class MainActivity extends AppCompatActivity {

    Login loginSave = new Login();
    private EditText usu;
    private EditText clave;
    private int result1 = 0;
    private ImageView imgEmpresa;
    private String getUsuario = "", getPass = "", mensaje = "";
    private AlertDialog mDialog;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private String StrServer = "";
    private String id;
    private String version;
    int  Resultado=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = new SpotsDialog.Builder().setContext(MainActivity.this).setMessage("Espere un momento...").build();
        usu = findViewById(R.id.txtinUsu);
        clave = findViewById(R.id.txtinCla);
        Button btnSERVIDOR = findViewById(R.id.btnSERVIDOR);
        Button btn1 = findViewById(R.id.btnbuscar);
        imgEmpresa = findViewById(R.id.imagEmpresa);
        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();


        btnSERVIDOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] opciones1 = {"JACVE", "AUTODIS", "CECRA", "GUVI", "PRESSA", "Vipla", "SPR", "COLOMBIA"};


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("SELECCIONE UNA EMPRESA").setIcon(R.drawable.icons_servidor);


                builder.setItems(opciones1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (which == 0) {
                            StrServer = "jacve.dyndns.org:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.jacve)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        } else if (which == 1) {
                            StrServer = "autodis.ath.cx:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.autodis)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        } else if (which == 2) {
                            StrServer = "cecra.ath.cx:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.cecra)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        } else if (which == 3) {
                            StrServer = "guvi.ath.cx:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.guvi)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        } else if (which == 4) {

                            StrServer = "cedistabasco.ddns.net:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.pressa)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);

                        } else if (which == 5) {
                            StrServer = "sprautomotive.servehttp.com:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.vipla)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);

                        } else if (which == 6) {
                            StrServer = "sprautomotive.servehttp.com:9090";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.sprimage)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        } else if (which == 7) {
                            StrServer = "vazlocolombia.dyndns.org:9085";
                            Picasso.with(getApplicationContext()).
                                    load(R.drawable.colombia2)
                                    .error(R.drawable.ic_baseline_error_24)
                                    .fit()
                                    .centerInside()
                                    .into(imgEmpresa);
                        }

                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View view) {

                    getUsuario = usu.getText().toString();
                    getPass = clave.getText().toString();
                    if (usu.length() != 0 && clave.length() != 0) {
                        if (!StrServer.equals("")) {
                            id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            AsyncCallWS task = new AsyncCallWS();
                            task.execute();
                        }else{

                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("La empresa no ah sido seleccionada").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Seleccione una empresa");
                            titulo.show();
                        }
                    } else {

                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setMessage("Ingrese los datos Faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Faltan Datos");
                        titulo.show();
                    }

            }
        });
        if (!preference.contains("user") && !preference.contains("pass")) {
        Versiones task1 = new Versiones();
        task1.execute();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (preference.contains("user") && preference.contains("pass")) {
            Intent perfil = new Intent(this, ActivitySplash.class);
            startActivity(perfil);
            finish();
        }

    }

    private void guardarDatos() {
        editor.putString("user", getUsuario);
        editor.putString("pass", getPass);
        editor.putString("name", loginSave.getName());
        editor.putString("lname", loginSave.getLastname());
        editor.putString("type", loginSave.getType());
        editor.putString("branch", loginSave.getBranch());
        editor.putString("email", loginSave.getEmail());
        editor.putString("code", loginSave.getCode());
        editor.putString("codBra", loginSave.getCodeBranch());
        editor.putString("Server", StrServer);
        editor.putString("type2", null);
        //editor.putString("tokenId",token);
        editor.commit();
    }



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Conectar();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (result1 == 0) {
                mDialog.dismiss();
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();

            } else if (result1 == 1) {
                String tipo=loginSave.getType();
                if (tipo.equals("VENDEDOR") || tipo.equals("ADMIN")){
                    mDialog.dismiss();
                    AsyncCallWS2 task2 = new AsyncCallWS2();
                    task2.execute();
                    trasactiv();

                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                    alerta.setMessage("Este usuario no está dado de alta comuníquese con su proveedor").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            mDialog.dismiss();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Problemas");
                    titulo.show();
                }


            }

        }

    }


    private void Conectar() {

        String SOAP_ACTION = "login";
        String METHOD_NAME = "login";
        String NAMESPACE = "http://" + StrServer + "/WSlogin/";
        String URL = "http://" + StrServer + "/WSlogin";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlLogin soapEnvelope = new xmlLogin(SoapEnvelope.VER11);
            soapEnvelope.valoresLogin(getUsuario, getPass);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.call(SOAP_ACTION, soapEnvelope);
            Vector response0 = (Vector) soapEnvelope.getResponse();
            result1 = Integer.parseInt(response0.get(0).toString());

            if (result1 == 0) {
                mensaje = "Contraseña y/o Usuario Inconrrecto";
            } else if (result1 == 1) {
                mensaje = "Correcto";
                SoapObject response = (SoapObject) soapEnvelope.bodyIn;
                response = (SoapObject) response.getProperty("UserInfo");
                loginSave = new Login();
                loginSave.setUsers(response.getPropertyAsString("k_usr"));
                loginSave.setName(response.getPropertyAsString("k_name"));
                loginSave.setLastname(response.getPropertyAsString("k_lname"));
                loginSave.setType(response.getPropertyAsString("k_type"));
                loginSave.setBranch(response.getPropertyAsString("k_dscr"));
                loginSave.setEmail(response.getPropertyAsString("k_mail1"));
                loginSave.setCode(response.getPropertyAsString("k_kcode"));
                loginSave.setCodeBranch(response.getPropertyAsString("k_codB"));

            }


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


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Versiones extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Versiones();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Resultado==1){
                if (version.equals("2.4")) {

                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                    alerta.setMessage("La versión instalada no está actualizada por favor comuníquese con su proveedor para actualizar.").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            editor.clear().commit();
                            System.exit(0);
                            finish();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Version desactualizada");
                    titulo.show();
                }
            }else{

            }
          mDialog.dismiss();
        }

    }


    private void Versiones() {

        String SOAP_ACTION = "Versiones";
        String METHOD_NAME = "Versiones";
        String NAMESPACE = "http://guvi.ath.cx:9085/WSk75ClientesSOAP/";
        String URL = "http://guvi.ath.cx:9085/WSk75ClientesSOAP";


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlVersiones soapEnvelope = new xmlVersiones(SoapEnvelope.VER11);
            soapEnvelope.xmlVersiones("WEBPETI", "W3B3P3T1", "1");
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;

            version =response0.getPropertyAsString("Version");


            Resultado=1;


        } catch (SoapFault | XmlPullParserException soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();
            soapFault.printStackTrace();
            Resultado=0;
        } catch (IOException e) {
            mDialog.dismiss();
            mensaje = "No se encontro servidor";
            Resultado=0;
            e.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
            mensaje = "Error:" + ex.getMessage();
            Resultado=0;
        }

    }



    private void trasactiv() {
        guardarDatos();
        Intent perfil = new Intent(this, ActivitySplash.class);
        startActivity(perfil);
        finish();


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar2();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


        }


    }

    private void conectar2() {
        String SOAP_ACTION = "LogAppUs";
        String METHOD_NAME = "LogAppUs";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlLog soapEnvelope = new xmlLog(SoapEnvelope.VER11);
            soapEnvelope.xmlLog(getUsuario, getPass, id, "LOG IN", "");
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);


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

}
