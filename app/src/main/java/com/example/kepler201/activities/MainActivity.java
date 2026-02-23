package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.Login;
import com.example.kepler201.includes.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("rawtypes")
public class MainActivity extends AppCompatActivity {

    Login loginSave = new Login();
    LinearLayout LinearJacve, LinearTodos;
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
    private String urlactualizar;
    int Resultado = 0;
    String[] opciones1 = {""};

    String ptoOp;
    String ptoCons;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = new SpotsDialog(MainActivity.this);
        mDialog.setCancelable(false);
        usu = findViewById(R.id.txtinUsu);
        clave = findViewById(R.id.txtinCla);
        Button btnSERVIDOR = findViewById(R.id.btnSERVIDOR);
        Button btn1 = findViewById(R.id.btnbuscar);
        imgEmpresa = findViewById(R.id.imagEmpresa);
        LinearTodos = findViewById(R.id.LinearTodos);
        LinearJacve = findViewById(R.id.LinearJacve);
        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        ptoOp = preference.getString("PuertoOperativo", "");
        ptoCons = preference.getString("PuertoConsulta", "");


        btnSERVIDOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Empresa = getString(R.string.Empresa);
                if (Empresa.equals("cedis")) {
                    opciones1 = new String[]{"AUTODIS", "CECRA", "GUVI", "PRESSA", "VIPLA", "SPR", "COLOMBIA", "BPR", "VAZQUIN", "PESBAC"};


                } else {
                    opciones1 = new String[]{"HAZAK"};

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("SELECCIONE UNA EMPRESA").setIcon(R.drawable.icons_servidor);


                builder.setItems(opciones1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String Empresa = getString(R.string.Empresa);
                        if (Empresa.equals("cedis")) {

                            if (which == 0) {
                                StrServer = getString(R.string.strserverautodis);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.autodis)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);
                                ptoOp=getString(R.string.PuertoautodisOperativo);
                                ptoCons=getString(R.string.PuertoautodisConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 1) {
                                StrServer = getString(R.string.strservercecra);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.cecra)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertocecraOperativo);
                                ptoCons=getString(R.string.PuertocecraConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();

                            } else if (which == 2) {
                                StrServer = getString(R.string.strserverguvi);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.guvi)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertoguviOperativo);
                                ptoCons=getString(R.string.PuertoguviConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 3) {

                                StrServer = getString(R.string.strservertabasco);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.pressa)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertotabascoOperativo);
                                ptoCons=getString(R.string.PuertotabascoConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 4) {
                                StrServer = getString(R.string.strservervipla);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.vipla)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertoviplaOperativo);
                                ptoCons=getString(R.string.PuertoviplaConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 5) {
                                StrServer = getString(R.string.strserverspr);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.sprimage)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertoRodatechOperativo);
                                ptoCons=getString(R.string.PuertoRodatechConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 6) {
                                StrServer = getString(R.string.strservervazlocolombia);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.colombia2)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);
                                ptoOp=getString(R.string.PuertovazlocolombiaOperativo);
                                ptoCons=getString(R.string.PuertovazlocolombiaConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();


                            } else if (which == 7) {

                                StrServer = getString(R.string.strserverbpr);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.logobpr)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);


                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertobprOperativo);
                                ptoCons=getString(R.string.PuertobprConsulta);

                                Versiones task1 = new Versiones();
                                task1.execute();
                            } else if (which == 8) {

                                StrServer = getString(R.string.strservervazquin);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.vazquinlogo)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);


                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertovazquinOperativo);
                                ptoCons=getString(R.string.PuertovazquinConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();

                            } else if (which == 9) {

                                StrServer = getString(R.string.strserverpesbac);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.pesbaclight)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);


                                LinearJacve.setVisibility(View.GONE);
                                LinearTodos.setVisibility(View.VISIBLE);

                                ptoOp=getString(R.string.PuertopesbacOperativo);
                                ptoCons=getString(R.string.PuertopesbacConsulta);
                                Versiones task1 = new Versiones();
                                task1.execute();

                            }

                        } else {
                            if (which == 0) {
                                StrServer = getString(R.string.strserverjacve);
                                Picasso.with(getApplicationContext()).
                                        load(R.drawable.jacve)
                                        .error(R.drawable.ic_baseline_error_24)
                                        .fit()
                                        .centerInside()
                                        .into(imgEmpresa);
                                LinearJacve.setVisibility(View.VISIBLE);
                                LinearTodos.setVisibility(View.GONE);

                                ptoOp=getString(R.string.PuertojacveOperativo);
                                ptoCons=getString(R.string.PuertojacveConsulta);

                                Versiones task1 = new Versiones();
                                task1.execute();

                            }
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
                        LoginAsyc task = new LoginAsyc();
                        task.execute();
                    } else {

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
        editor.putString("email", loginSave.getEmail());
        editor.putString("code", loginSave.getCode());
        editor.putString("codBra", loginSave.getCodeBranch());
        editor.putString("branch", loginSave.getBranch());
        editor.putString("codBra2", loginSave.getCodeBranch());
        editor.putString("branch2", loginSave.getBranch());
        editor.putString("Server", StrServer);
        editor.putString("PuertoOperativo", ptoOp);
        editor.putString("PuertoConsulta",ptoCons);
        editor.putString("type2", null);
        //editor.putString("tokenId",token);
        editor.commit();
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class LoginAsyc extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer+ptoOp + "/loginapp";
            String jsonStr = sh.makeServiceCall(url, getUsuario, getPass);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jitems, Numero;
                    int Entro = Integer.parseInt(jsonObject.getString("ok"));
                    if (Entro == 1) {
                        mensaje = "Correcto";

                        jitems = jsonObject.getJSONObject("UserInfo");
                        loginSave = new Login();
                        loginSave.setUsers(jitems.getString("k_usr"));
                        loginSave.setName(jitems.getString("k_name"));
                        loginSave.setLastname(jitems.getString("k_lname"));
                        loginSave.setType(jitems.getString("k_type"));
                        loginSave.setEmail(jitems.getString("k_mail1"));
                        loginSave.setCode(jitems.getString("k_kcode"));
                        loginSave.setBranch(jitems.getString("k_dscr"));
                        loginSave.setCodeBranch(jitems.getString("k_codB"));
                        result1 = 1;
                    } else {
                        result1 = 0;
                        mensaje = "Error en el usuario o contraseña";
                    }
                } catch (final JSONException e) {

                }//catch JSON EXCEPTION
            } else {

            }//else

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (result1 == 0) {
                mDialog.dismiss();
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage(mensaje).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        mDialog.dismiss();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Problemas");
                titulo.show();

            } else if (result1 == 1) {
                String tipo = loginSave.getType();
                if (tipo.equals("VENDEDOR") || tipo.equals("ADMIN")) {
                    mDialog.dismiss();
                    Log task2 = new Log();
                    task2.execute();
                    trasactiv();

                } else {
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


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Versiones extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer+ptoOp + "/versionesapp?Clave=1";
            String jsonStr = sh.makeServiceCall(url, "WEBPETI", "W3B3P3T1");
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    version = jsonObject.getString("Version");
                    urlactualizar = jsonObject.getString("URL");

                    Resultado = 1;
                } catch (final JSONException e) {
                    Resultado = 0;
                }//catch JSON EXCEPTION
            } else {
                Resultado = 0;
            }//else

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Resultado == 1) {
                String versionapp = getString(R.string.version);
                if (version.equals(versionapp)) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("AVISO");
                    builder.setMessage("Hay una nueva actualización\n¿Ir a actualizar app?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openLink();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(false);
                }
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("Hay un problema con el servicio").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Problemas !!");
                titulo.show();
            }
            mDialog.dismiss();
        }

    }


    private void openLink() {
        Uri uri = Uri.parse(urlactualizar);
        startActivityForResult(new Intent(Intent.ACTION_VIEW, uri), 1);
        //startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }//openLink

    private void trasactiv() {
        guardarDatos();
        Intent perfil = new Intent(this, ActivitySplash.class);
        startActivity(perfil);
        finish();


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Log extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "usuario=" + getUsuario + "&identificador=" + id + "&accion=LOG IN&parametro= ";
            String url = "http://" + StrServer+ptoOp + "/logapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, getUsuario, getPass);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String registradolog = jsonObject.getString("Log");
                } catch (final JSONException e) {

                }//catch JSON EXCEPTION
            } else {

            }//else


            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


        }


    }

}
