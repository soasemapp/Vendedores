package com.example.kepler201.Historial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.HistorialSANDG;
import com.example.kepler201.SetterandGetter.ListLineaSANDG;
import com.example.kepler201.SetterandGetter.ListTypeSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlHistorial;
import com.example.kepler201.XMLS.xmlListLine;
import com.example.kepler201.XMLS.xmlListType;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.Agenda.ActivityAgenda;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.activities.Pagos.RegitrodepagosActivity;
import com.example.kepler201.activities.Productos.ListaPreciosActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("NonAsciiCharacters")
public class ActivityHistorial extends AppCompatActivity {


    private Spinner spinerClie;
    private Spinner spinerLinea;
    private Spinner spinerTipo;
    RadioButton radTipo, radLinea, radTodo;
    CheckBox clienteCheck;
    EditText vendedor;
    EditText AñoTXT;
    private TableLayout tableLayout;
    TextView txtClienVende, txtProducto, txtEnero, txtFebrero, txtMarzo, txtAbril, txtMayo, txtJunio, txtJulio, txtAgosto, txtSeptiembre, txtOctubre, txtNoviembre, txtDiciembre, txtCantidad;
    TableRow fila;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String mensaje = "";
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<ListLineaSANDG> listaLinea = new ArrayList<>();
    ArrayList<ListTypeSANDG> listaTipo = new ArrayList<>();
    ArrayList<HistorialSANDG> listaHistorial = new ArrayList<>();


    int n = 2000;
    String[] search1 = new String[n];

    int n1 = 2000;
    String[] search2 = new String[n1];

    String[] search3 = new String[n];

    AlertDialog mDialog;
    String strVendedor = "", strCliente = "", strLinea = "", strTipo = "", strFecha = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        MyToolbar.show(this, "Historial", true);
        mDialog = new SpotsDialog(ActivityHistorial.this);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        tableLayout =  findViewById(R.id.table);
        spinerClie =  findViewById(R.id.spinnerClie);
        spinerLinea =  findViewById(R.id.spinnerLinea);
        spinerTipo =  findViewById(R.id.spinnerType);
        Button btnsearch = findViewById(R.id.btnSearch);
        vendedor =  findViewById(R.id.ediVend);
        radLinea =  findViewById(R.id.radLinea);
        radTipo =  findViewById(R.id.radTipo);
        radTodo =  findViewById(R.id.radTodo);
        clienteCheck =  findViewById(R.id.checkCliente);
        AñoTXT =  findViewById(R.id.Año);


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

        vendedor.setText(strname + " " + strlname);

        radLinea.setChecked(true);
        clienteCheck.setChecked(false);
        spinerClie.setEnabled(false);
        spinerTipo.setEnabled(false);

        clienteCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clienteCheck.isChecked()) {
                    spinerClie.setEnabled(true);
                } else {
                    spinerClie.setEnabled(false);
                    spinerClie.setSelection(0);
                }

            }
        });

        radLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radLinea.isChecked()) {
                    spinerLinea.setEnabled(true);
                    spinerTipo.setEnabled(false);
                    spinerTipo.setSelection(0);
                }

            }
        });
        radTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radTipo.isChecked()) {
                    spinerTipo.setEnabled(true);
                    spinerLinea.setEnabled(false);
                    spinerLinea.setSelection(0);
                }

            }
        });
        radTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radTodo.isChecked()) {
                    spinerTipo.setEnabled(false);
                    spinerLinea.setEnabled(false);
                    spinerLinea.setSelection(0);
                    spinerTipo.setSelection(0);
                } else {
                }

            }
        });


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    listaHistorial.clear();
                    tableLayout.removeAllViews();

                    strFecha = AñoTXT.getText().toString();
                    strVendedor = strcode;

                    for (int i = 0; i < search1.length; i++) {
                        int posi = spinerClie.getSelectedItemPosition();
                        if (posi == i) {
                            strCliente = search1[i];
                            break;
                        }
                    }


                    for (int i = 0; i < search2.length; i++) {
                        int posi = spinerLinea.getSelectedItemPosition();
                        if (posi == i) {
                            strLinea = search2[i];
                            break;
                        }
                    }

                    for (int i = 0; i < search3.length; i++) {
                        int posi = spinerTipo.getSelectedItemPosition();
                        if (posi == i) {
                            strTipo = search3[i];
                            break;
                        }
                    }
                    if (clienteCheck.isChecked() && spinerClie.getSelectedItemPosition() != 0 && radLinea.isChecked() && spinerLinea.getSelectedItemPosition() != 0 && !strFecha.isEmpty()) {
                        strVendedor = "";
                        strTipo = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else if (clienteCheck.isChecked() && spinerClie.getSelectedItemPosition() != 0 && radTipo.isChecked() && spinerTipo.getSelectedItemPosition() != 0 && !strFecha.isEmpty()) {
                        strVendedor = "";
                        strLinea = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else if (clienteCheck.isChecked() && spinerClie.getSelectedItemPosition() != 0 && radTodo.isChecked() && !strFecha.isEmpty()) {
                        strVendedor = "";
                        strLinea = "";
                        strTipo = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else if (!clienteCheck.isChecked() && radLinea.isChecked() && spinerLinea.getSelectedItemPosition() != 0 && !strFecha.isEmpty()) {
                        strCliente = "";
                        strTipo = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else if (!clienteCheck.isChecked() && radTipo.isChecked() && spinerTipo.getSelectedItemPosition() != 0 && !strFecha.isEmpty()) {
                        strCliente = "";
                        strLinea = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else if (!clienteCheck.isChecked() && radTodo.isChecked() && !strFecha.isEmpty()) {
                        strCliente = "";
                        strTipo = "";
                        strLinea = "";
                        ActivityHistorial.AsyncCallWS4 task4 = new ActivityHistorial.AsyncCallWS4();
                        task4.execute();
                    } else {

                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityHistorial.this);
                        alerta.setMessage("Porfavor verifique su informacion sea correcta").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("¡ERROR!");
                        titulo.show();
                    }

                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityHistorial.this);
                    alerta.setMessage("No hay Conexion a Interet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("!ERROR! CONEXION");
                    titulo.show();
                }
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat AÑO = new SimpleDateFormat("yyyy");
        String StrAño = AÑO.format(c.getTime());

        AñoTXT.setText(StrAño);

        ActivityHistorial.AsyncCallWS task = new ActivityHistorial.AsyncCallWS();
        task.execute();
        ActivityHistorial.AsyncCallWS2 task2 = new ActivityHistorial.AsyncCallWS2();
        task2.execute();
        ActivityHistorial.AsyncCallWS3 task3 = new ActivityHistorial.AsyncCallWS3();
        task3.execute();

        strVendedor = strcode;
        strCliente = "";
        strTipo = "";
        strLinea = "";
        strFecha = AñoTXT.getText().toString();



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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "--Seleccione--";
            search1[0] = "--Seleccione--";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search1[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerClie.setAdapter(adapter);
        }


    }




    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/ListLiPreapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("item");
                            Numero = jitems.getJSONObject("" + i + "");

                            if (!Numero.getString("k_Linea").equals("")) {
                                listaLinea.add(new ListLineaSANDG((Numero.getString("k_CodeLinea").equals("") ? "" : Numero.getString("k_CodeLinea")),
                                        (Numero.getString("k_Linea").equals("") ? "" : Numero.getString("k_Linea"))));
                            }


                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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

            String[] opciones = new String[listaLinea.size() + 1];
            opciones[0] = "--Todas las Lineas--";
            search2[0] = "--Todas las Lineas--";

            for (int i = 1; i <= listaLinea.size(); i++) {
                opciones[i] = listaLinea.get(i - 1).getLinea();
                search2[i] = listaLinea.get(i - 1).getCodeLinea();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerLinea.setAdapter(adapter);
        }


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/ListLiTyapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("item");
                            Numero = jitems.getJSONObject("" + i + "");

                            if (!Numero.getString("k_Type").equals("")) {

                                listaTipo.add(new ListTypeSANDG((Numero.getString("k_CodeType").equals("") ? "" : Numero.getString("k_CodeType")),
                                        (Numero.getString("k_Type").equals("") ? "" : Numero.getString("k_Type"))));

                            }


                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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

            String[] opciones = new String[listaTipo.size() + 1];
            opciones[0] = "--Todos los Tipos--";
            search3[0] = "--Todas las Lineas--";


            for (int i = 1; i <= listaTipo.size(); i++) {
                opciones[i] = listaTipo.get(i - 1).getType();
                search3[i] = listaTipo.get(i - 1).getCodeType();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerTipo.setAdapter(adapter);
            mDialog.dismiss();
        }


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS4 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor="+strVendedor+"&clinte="+strCliente+"&linea="+strLinea+"&tipo="+strTipo+"&fecha="+strFecha;
            String url = "http://" + StrServer + "/historialapp?" + parametros;
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
                            listaHistorial.add(new HistorialSANDG((Numero.getString("k_ClaVenClien").equals("") ? " " : Numero.getString("k_ClaVenClien")),
                                    (Numero.getString("k_ClavPro").equals("") ? " " : Numero.getString("k_ClavPro")),
                                    (Numero.getString("k_Enero").equals("") ? "0" : Numero.getString("k_Enero")),
                                    (Numero.getString("k_Febrero").equals("") ? "0" : Numero.getString("k_Febrero")),
                                    (Numero.getString("k_Marzo").equals("") ? "0" : Numero.getString("k_Marzo")),
                                    (Numero.getString("k_Abril").equals("") ? "0" : Numero.getString("k_Abril")),
                                    (Numero.getString("k_Mayo").equals("") ? "0" : Numero.getString("k_Mayo")),
                                    (Numero.getString("k_Junio").equals("") ? "0" : Numero.getString("k_Junio")),
                                    (Numero.getString("k_Julio").equals("") ? "0" : Numero.getString("k_Julio")),
                                    (Numero.getString("k_Agosto").equals("") ? "0" : Numero.getString("k_Agosto")),
                                    (Numero.getString("k_Septiembre").equals("") ? "0" : Numero.getString("k_Septiembre")),
                                    (Numero.getString("k_Octubre").equals("") ? "0" : Numero.getString("k_Octubre")),
                                    (Numero.getString("k_Noviembre").equals("") ? "0" : Numero.getString("k_Noviembre")),
                                    (Numero.getString("k_Diciembre").equals("") ? "0" : Numero.getString("k_Diciembre")),
                                    (Numero.getString("k_Cant").equals("") ? "0" : Numero.getString("k_Cant"))));
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityHistorial.this);
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

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listaHistorial.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                txtClienVende = new TextView(getApplicationContext());
                if (i == -1) {

                    txtClienVende.setText((clienteCheck.isChecked()) ? "Cliente" : "Vendedor");
                    txtClienVende.setGravity(Gravity.START);
                    txtClienVende.setBackgroundColor(Color.BLUE);
                    txtClienVende.setTextColor(Color.WHITE);
                    txtClienVende.setPadding(20, 20, 20, 20);
                    txtClienVende.setLayoutParams(layaoutDes);
                    fila.addView(txtClienVende);


                    txtProducto = new TextView(getApplicationContext());
                    txtProducto.setText("Producto");
                    txtProducto.setGravity(Gravity.START);
                    txtProducto.setBackgroundColor(Color.BLUE);
                    txtProducto.setTextColor(Color.WHITE);
                    txtProducto.setPadding(20, 20, 20, 20);
                    txtProducto.setLayoutParams(layaoutDes);
                    fila.addView(txtProducto);

                    txtEnero = new TextView(getApplicationContext());
                    txtEnero.setText("Ene");
                    txtEnero.setGravity(Gravity.START);
                    txtEnero.setBackgroundColor(Color.BLUE);
                    txtEnero.setTextColor(Color.WHITE);
                    txtEnero.setPadding(20, 20, 20, 20);
                    txtEnero.setLayoutParams(layaoutDes);
                    fila.addView(txtEnero);

                    txtFebrero = new TextView(getApplicationContext());
                    txtFebrero.setText("Feb");
                    txtFebrero.setGravity(Gravity.START);
                    txtFebrero.setBackgroundColor(Color.BLUE);
                    txtFebrero.setTextColor(Color.WHITE);
                    txtFebrero.setPadding(20, 20, 20, 20);
                    txtFebrero.setLayoutParams(layaoutDes);
                    fila.addView(txtFebrero);

                    txtMarzo = new TextView(getApplicationContext());
                    txtMarzo.setText("Mar");
                    txtMarzo.setGravity(Gravity.START);
                    txtMarzo.setBackgroundColor(Color.BLUE);
                    txtMarzo.setTextColor(Color.WHITE);
                    txtMarzo.setPadding(20, 20, 20, 20);
                    txtMarzo.setLayoutParams(layaoutDes);
                    fila.addView(txtMarzo);

                    txtAbril = new TextView(getApplicationContext());
                    txtAbril.setText("Abr");
                    txtAbril.setGravity(Gravity.START);
                    txtAbril.setBackgroundColor(Color.BLUE);
                    txtAbril.setTextColor(Color.WHITE);
                    txtAbril.setPadding(20, 20, 20, 20);
                    txtAbril.setLayoutParams(layaoutDes);
                    fila.addView(txtAbril);

                    txtMayo = new TextView(getApplicationContext());
                    txtMayo.setText("May");
                    txtMayo.setGravity(Gravity.START);
                    txtMayo.setBackgroundColor(Color.BLUE);
                    txtMayo.setTextColor(Color.WHITE);
                    txtMayo.setPadding(20, 20, 20, 20);
                    txtMayo.setLayoutParams(layaoutDes);
                    fila.addView(txtMayo);

                    txtJunio = new TextView(getApplicationContext());
                    txtJunio.setText("Jun");
                    txtJunio.setGravity(Gravity.START);
                    txtJunio.setBackgroundColor(Color.BLUE);
                    txtJunio.setTextColor(Color.WHITE);
                    txtJunio.setPadding(20, 20, 20, 20);
                    txtJunio.setLayoutParams(layaoutDes);
                    fila.addView(txtJunio);


                    txtJulio = new TextView(getApplicationContext());
                    txtJulio.setText("Jul");
                    txtJulio.setGravity(Gravity.START);
                    txtJulio.setBackgroundColor(Color.BLUE);
                    txtJulio.setTextColor(Color.WHITE);
                    txtJulio.setPadding(20, 20, 20, 20);
                    txtJulio.setLayoutParams(layaoutDes);
                    fila.addView(txtJulio);


                    txtAgosto = new TextView(getApplicationContext());
                    txtAgosto.setText("Ago");
                    txtAgosto.setGravity(Gravity.START);
                    txtAgosto.setBackgroundColor(Color.BLUE);
                    txtAgosto.setTextColor(Color.WHITE);
                    txtAgosto.setPadding(20, 20, 20, 20);
                    txtAgosto.setLayoutParams(layaoutDes);
                    fila.addView(txtAgosto);


                    txtSeptiembre = new TextView(getApplicationContext());
                    txtSeptiembre.setText("Sept");
                    txtSeptiembre.setGravity(Gravity.START);
                    txtSeptiembre.setBackgroundColor(Color.BLUE);
                    txtSeptiembre.setTextColor(Color.WHITE);
                    txtSeptiembre.setPadding(20, 20, 20, 20);
                    txtSeptiembre.setLayoutParams(layaoutDes);
                    fila.addView(txtSeptiembre);

                    txtOctubre = new TextView(getApplicationContext());
                    txtOctubre.setText("Oct");
                    txtOctubre.setGravity(Gravity.START);
                    txtOctubre.setBackgroundColor(Color.BLUE);
                    txtOctubre.setTextColor(Color.WHITE);
                    txtOctubre.setPadding(20, 20, 20, 20);
                    txtOctubre.setLayoutParams(layaoutDes);
                    fila.addView(txtOctubre);


                    txtNoviembre = new TextView(getApplicationContext());
                    txtNoviembre.setText("Nov");
                    txtNoviembre.setGravity(Gravity.START);
                    txtNoviembre.setBackgroundColor(Color.BLUE);
                    txtNoviembre.setTextColor(Color.WHITE);
                    txtNoviembre.setPadding(20, 20, 20, 20);
                    txtNoviembre.setLayoutParams(layaoutDes);
                    fila.addView(txtNoviembre);


                    txtDiciembre = new TextView(getApplicationContext());
                    txtDiciembre.setText("Dic");
                    txtDiciembre.setGravity(Gravity.START);
                    txtDiciembre.setBackgroundColor(Color.BLUE);
                    txtDiciembre.setTextColor(Color.WHITE);
                    txtDiciembre.setPadding(20, 20, 20, 20);
                    txtDiciembre.setLayoutParams(layaoutDes);
                    fila.addView(txtDiciembre);


                    txtCantidad = new TextView(getApplicationContext());
                    txtCantidad.setText("Cant");
                    txtCantidad.setGravity(Gravity.START);
                    txtCantidad.setBackgroundColor(Color.BLUE);
                    txtCantidad.setTextColor(Color.WHITE);
                    txtCantidad.setPadding(20, 20, 20, 20);
                    txtCantidad.setLayoutParams(layaoutDes);
                    fila.addView(txtCantidad);


                } else {


                    txtClienVende.setBackgroundColor(Color.BLACK);
                    txtClienVende.setGravity(Gravity.START);
                    txtClienVende.setText(listaHistorial.get(i).getVenClien());
                    txtClienVende.setPadding(20, 20, 20, 20);
                    txtClienVende.setTextColor(Color.WHITE);
                    txtClienVende.setLayoutParams(layaoutDes);
                    fila.addView(txtClienVende);

                    txtProducto = new TextView(getApplicationContext());
                    txtProducto.setBackgroundColor(Color.GRAY);
                    txtProducto.setGravity(Gravity.START);
                    txtProducto.setText(listaHistorial.get(i).getProducto());
                    txtProducto.setPadding(20, 20, 20, 20);
                    txtProducto.setTextColor(Color.WHITE);
                    txtProducto.setLayoutParams(layaoutDes);
                    fila.addView(txtProducto);


                    txtEnero = new TextView(getApplicationContext());
                    txtEnero.setBackgroundColor(Color.BLACK);
                    txtEnero.setGravity(Gravity.START);
                    txtEnero.setText(listaHistorial.get(i).getEnero());
                    txtEnero.setPadding(20, 20, 20, 20);
                    txtEnero.setTextColor(Color.WHITE);
                    txtEnero.setLayoutParams(layaoutDes);
                    fila.addView(txtEnero);


                    txtFebrero = new TextView(getApplicationContext());
                    txtFebrero.setBackgroundColor(Color.GRAY);
                    txtFebrero.setGravity(Gravity.START);
                    txtFebrero.setText(listaHistorial.get(i).getFebrero());
                    txtFebrero.setPadding(20, 20, 20, 20);
                    txtFebrero.setTextColor(Color.WHITE);
                    txtFebrero.setLayoutParams(layaoutDes);
                    fila.addView(txtFebrero);


                    txtMarzo = new TextView(getApplicationContext());
                    txtMarzo.setBackgroundColor(Color.BLACK);
                    txtMarzo.setGravity(Gravity.START);
                    txtMarzo.setText(listaHistorial.get(i).getMarzo());
                    txtMarzo.setPadding(20, 20, 20, 20);
                    txtMarzo.setTextColor(Color.WHITE);
                    txtMarzo.setLayoutParams(layaoutDes);
                    fila.addView(txtMarzo);


                    txtAbril = new TextView(getApplicationContext());
                    txtAbril.setBackgroundColor(Color.GRAY);
                    txtAbril.setGravity(Gravity.START);
                    txtAbril.setText(listaHistorial.get(i).getAbril());
                    txtAbril.setPadding(20, 20, 20, 20);
                    txtAbril.setTextColor(Color.WHITE);
                    txtAbril.setLayoutParams(layaoutDes);
                    fila.addView(txtAbril);


                    txtMayo = new TextView(getApplicationContext());
                    txtMayo.setBackgroundColor(Color.BLACK);
                    txtMayo.setGravity(Gravity.START);
                    txtMayo.setText(listaHistorial.get(i).getMayo());
                    txtMayo.setPadding(20, 20, 20, 20);
                    txtMayo.setTextColor(Color.WHITE);
                    txtMayo.setLayoutParams(layaoutDes);
                    fila.addView(txtMayo);


                    txtJunio = new TextView(getApplicationContext());
                    txtJunio.setBackgroundColor(Color.GRAY);
                    txtJunio.setGravity(Gravity.START);
                    txtJunio.setText(listaHistorial.get(i).getJunio());
                    txtJunio.setPadding(20, 20, 20, 20);
                    txtJunio.setTextColor(Color.WHITE);
                    txtJunio.setLayoutParams(layaoutDes);
                    fila.addView(txtJunio);


                    txtJulio = new TextView(getApplicationContext());
                    txtJulio.setBackgroundColor(Color.BLACK);
                    txtJulio.setGravity(Gravity.START);
                    txtJulio.setText(listaHistorial.get(i).getJulio());
                    txtJulio.setPadding(20, 20, 20, 20);
                    txtJulio.setTextColor(Color.WHITE);
                    txtJulio.setLayoutParams(layaoutDes);
                    fila.addView(txtJulio);


                    txtAgosto = new TextView(getApplicationContext());
                    txtAgosto.setBackgroundColor(Color.GRAY);
                    txtAgosto.setGravity(Gravity.START);
                    txtAgosto.setText(listaHistorial.get(i).getAgosto());
                    txtAgosto.setPadding(20, 20, 20, 20);
                    txtAgosto.setTextColor(Color.WHITE);
                    txtAgosto.setLayoutParams(layaoutDes);
                    fila.addView(txtAgosto);


                    txtSeptiembre = new TextView(getApplicationContext());
                    txtSeptiembre.setBackgroundColor(Color.BLACK);
                    txtSeptiembre.setGravity(Gravity.START);
                    txtSeptiembre.setText(listaHistorial.get(i).getSeptiembre());
                    txtSeptiembre.setPadding(20, 20, 20, 20);
                    txtSeptiembre.setTextColor(Color.WHITE);
                    txtSeptiembre.setLayoutParams(layaoutDes);
                    fila.addView(txtSeptiembre);


                    txtOctubre = new TextView(getApplicationContext());
                    txtOctubre.setBackgroundColor(Color.GRAY);
                    txtOctubre.setGravity(Gravity.START);
                    txtOctubre.setText(listaHistorial.get(i).getOctubre());
                    txtOctubre.setPadding(20, 20, 20, 20);
                    txtOctubre.setTextColor(Color.WHITE);
                    txtOctubre.setLayoutParams(layaoutDes);
                    fila.addView(txtOctubre);


                    txtNoviembre = new TextView(getApplicationContext());
                    txtNoviembre.setBackgroundColor(Color.BLACK);
                    txtNoviembre.setGravity(Gravity.START);
                    txtNoviembre.setText(listaHistorial.get(i).getNoviembre());
                    txtNoviembre.setPadding(20, 20, 20, 20);
                    txtNoviembre.setTextColor(Color.WHITE);
                    txtNoviembre.setLayoutParams(layaoutDes);
                    fila.addView(txtNoviembre);


                    txtDiciembre = new TextView(getApplicationContext());
                    txtDiciembre.setBackgroundColor(Color.GRAY);
                    txtDiciembre.setGravity(Gravity.START);
                    txtDiciembre.setText(listaHistorial.get(i).getDiciembre());
                    txtDiciembre.setPadding(20, 20, 20, 20);
                    txtDiciembre.setTextColor(Color.WHITE);
                    txtDiciembre.setLayoutParams(layaoutDes);
                    fila.addView(txtDiciembre);

                    txtCantidad = new TextView(getApplicationContext());
                    txtCantidad.setBackgroundColor(Color.BLACK);
                    txtCantidad.setGravity(Gravity.START);
                    txtCantidad.setText(listaHistorial.get(i).getCantidad());
                    txtCantidad.setPadding(20, 20, 20, 20);
                    txtCantidad.setTextColor(Color.WHITE);
                    txtCantidad.setLayoutParams(layaoutDes);
                    fila.addView(txtCantidad);

                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout.addView(fila);
            }
            mDialog.dismiss();
        }
    }
}
