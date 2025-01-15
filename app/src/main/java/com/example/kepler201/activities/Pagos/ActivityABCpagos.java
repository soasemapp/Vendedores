package com.example.kepler201.activities.Pagos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.BANKSANDG;
import com.example.kepler201.SetterandGetter.RegistroPagosSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class ActivityABCpagos extends AppCompatActivity {


    private Spinner spinerClie;
    private Spinner Banco;
    private Spinner formaPa;
    private EditText fecha;
    private EditText facturas;
    private EditText importe;
    private EditText comentarios1;
    private EditText comentarios2;
    private EditText comentarios3;
    private TableLayout tableLayout;

    AlertDialog mDialog;

    TextView txtFecha, txtHora, txtCcliente, txtNCliente, txtFacturas, txtImporte, txtNBanco, txtFpago, txtComentario1, txtComentario2, txtComentario3;
    TableRow fila;
    String fecha123 = "";
    String hora123 = "";
    String clave123 = "";

    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcode;
    String mensaje = "";

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<BANKSANDG> listaBancos = new ArrayList<>();
    ArrayList<RegistroPagosSANDG> listaAltasPa = new ArrayList<>();

    int n = 2000;
    String[] search1 = new String[n];

    int n1 = 2000;
    String[] search2 = new String[n1];

    String Strcodevendedor = "", strcodBra, Strcliente = "", Strdate = "", Strfacturas = "", Strimporte = "", StrBanco = "", StrForma = "", StrComentarios1 = "", StrComentarios2 = "", StrComentarios3 = "", StrServer = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas_pagos);

        MyToolbar.show(this, "Pagos-Registro/Eliminacion", true);

        mDialog = new SpotsDialog(ActivityABCpagos.this);
        mDialog.setCancelable(false);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        TextView vendedor = findViewById(R.id.cVendedor);
        spinerClie = findViewById(R.id.spinnerClie);
        Banco = findViewById(R.id.spinnerBank);
        formaPa = findViewById(R.id.spinnerFormaPa);
        fecha = findViewById(R.id.fecha);
        facturas = findViewById(R.id.facturas);
        importe = findViewById(R.id.importe);
        comentarios1 = findViewById(R.id.comentarios1);
        comentarios2 = findViewById(R.id.comentarios2);
        comentarios3 = findViewById(R.id.comentarios3);
        tableLayout = findViewById(R.id.table);
        Button btnRegistroPa = findViewById(R.id.btnRegistrar);
        Button btnEliminar = findViewById(R.id.btnEliminarPago);


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

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityABCpagos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        Strdate = year + "-" + month + "-" + day;
                        fecha.setText(Strdate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnRegistroPa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Strcodevendedor = strcode;
                Strfacturas = facturas.getText().toString();
                Strimporte = importe.getText().toString();
                StrForma = formaPa.getSelectedItem().toString();
                StrComentarios1 = comentarios1.getText().toString();
                StrComentarios2 = comentarios2.getText().toString();
                StrComentarios3 = comentarios3.getText().toString();
                Strdate = fecha.getText().toString();

                for (int i = 0; i < search1.length; i++) {
                    int posi = spinerClie.getSelectedItemPosition();
                    if (posi == i) {
                        Strcliente = search1[i];
                        break;
                    }
                }

                for (int i = 0; i < search2.length; i++) {
                    int posi = Banco.getSelectedItemPosition();
                    if (posi == i) {
                        StrBanco = search2[i];
                        break;
                    }
                }
                if (!strcode.isEmpty() && !Strfacturas.isEmpty() && !Strimporte.isEmpty() && !Strdate.isEmpty()
                        && formaPa.getSelectedItemId() != 0 && spinerClie.getSelectedItemId() != 0 && Banco.getSelectedItemId() != 0) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityABCpagos.this);
                    alerta.setMessage("¿Deseas Agregar el Siguiente pago?" + "\n" +
                            "Vendedor : " + Strcodevendedor + "\n" +
                            "Factura : " + Strfacturas + "\n" +
                            "Importe : " + Strimporte + "\n" +
                            "Forma de Pago : " + StrForma + "\n" +
                            "Comentario1 : " + StrComentarios1 + "\n" +
                            "Comentario2 : " + StrComentarios2 + "\n" +
                            "Comentario3 : " + StrComentarios3 + "\n" +
                            "Cliente : " + Strcliente + "\n" +
                            "Banco : " + StrBanco).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tableLayout.removeAllViews();
                            listaAltasPa.clear();
                            ActivityABCpagos.AsyncCallWS3 task3 = new ActivityABCpagos.AsyncCallWS3();
                            task3.execute();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(ActivityABCpagos.this, "El movimiento a sido Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Agregar Pago");
                    titulo.show();


                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityABCpagos.this);
                    alerta.setMessage("Porfavor ingrese todos los campos faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityABCpagos.this);
                if (!fecha123.isEmpty() && !hora123.isEmpty() && !clave123.isEmpty()) {
                    alerta.setMessage("¿Deseas realizar la siguiente eliminacion?" + "\n" +
                            "Fecha:" + fecha123 + "\n" +
                            "Hora:" + hora123 + "\n" +
                            "Cliente:" + clave123).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            tableLayout.removeAllViews();
                            listaAltasPa.clear();
                            ActivityABCpagos.AsyncCallWS5 task5 = new ActivityABCpagos.AsyncCallWS5();
                            task5.execute();

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(ActivityABCpagos.this, "El movimiento a sido Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Eliminar Dato");
                    titulo.show();
                } else {
                    alerta.setMessage("Porfavor seleccione un pago ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("No hay datos por borrar");
                    titulo.show();
                }


            }
        });

        vendedor.setText(strname + " " + strlname);


        String[] opciones1 = {"--Seleccione--", "Efectivo", "Cheque", "Transferencia"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones1);
        formaPa.setAdapter(adapter1);

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = dateformatActually1.format(c.getTime());
        fecha.setText(fechaActual);


        ActivityABCpagos.AsyncCallWS task = new ActivityABCpagos.AsyncCallWS();
        task.execute();

        ActivityABCpagos.AsyncCallWS2 task2 = new ActivityABCpagos.AsyncCallWS2();
        task2.execute();

        ActivityABCpagos.AsyncCallWS4 task3 = new ActivityABCpagos.AsyncCallWS4();
        task3.execute();


    }

    //WebService Clientes

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
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
            opciones[0] = "Clientes";
            search1[0] = "Clientes";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search1[i] = listaclientG.get(i - 1).getUserCliente();
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerClie.setPadding(5, 5, 5, 5);
            spinerClie.setAdapter(adapter);
        }


    }


    //WebService De bancos

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/bancoapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            String[] opciones = new String[listaBancos.size() + 1];
            opciones[0] = "--Selecciona--";
            search2[0] = "--Selecciona--";
            for (int i = 1; i <= listaBancos.size(); i++) {
                opciones[i] = listaBancos.get(i - 1).getNBanco();
                search2[i] = listaBancos.get(i - 1).getCode();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            Banco.setAdapter(adapter);
        }


    }



//WebServise Ingresar

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
            String parametros = "agente=" + strcode + "&clave=" + Strcliente + "&fecha=" + Strdate +"&importe=" + String.valueOf(Strimporte) + "&facturas=" + Strfacturas + "&banco=" + StrBanco + "&formapago=" + StrForma + "&comentario1=" + StrComentarios1 + "&comentario2=" + StrComentarios2 + "&comentario3=" + StrComentarios3;
            String url = "http://" + StrServer + "/registropagoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        String Registrado;
                        Registrado = jsonObject.getString("Resultado");
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            Toast.makeText(ActivityABCpagos.this, "Movimiento Exitoso", Toast.LENGTH_SHORT).show();
            ActivityABCpagos.AsyncCallWS4 task4 = new ActivityABCpagos.AsyncCallWS4();
            task4.execute();

            spinerClie.setSelection(0);
            Banco.setSelection(0);
            formaPa.setSelection(0);
            fecha.setText("");
            facturas.setText("");
            importe.setText("");
            comentarios1.setText("");
            comentarios2.setText("");
            comentarios3.setText("");
            mDialog.dismiss();
        }


    }




//WebServise Ingresar

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
            String parametros = "agente=" + strcode;
            String url = "http://" + StrServer + "/consultapagoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0){
                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Item");

    for (int i = 0; i < jitems.length(); i++) {
        jitems = jsonObject.getJSONObject("Item");
        Numero = jitems.getJSONObject("" + i + "");
        listaAltasPa.add(new RegistroPagosSANDG((Numero.getString("k_Fecha").equals("") ? " " : Numero.getString("k_Fecha")),
                (Numero.getString("k_Hora").equals("") ? " " : Numero.getString("k_Hora")),
                (Numero.getString("k_Ccliente").equals("") ? " " : Numero.getString("k_Ccliente")),
                (Numero.getString("k_Ncliente").equals("") ? " " : Numero.getString("k_Ncliente")),
                (Numero.getString("k_facturas").equals("") ? " " : Numero.getString("k_facturas")),
                (Numero.getString("k_Importe").equals("") ? " " : Numero.getString("k_Importe")),
                (Numero.getString("k_Nbanco").equals("") ? " " : Numero.getString("k_Nbanco")),
                (Numero.getString("k_FormaP").equals("") ? " " : Numero.getString("k_FormaP")),
                (Numero.getString("k_Com1").equals("") ? " " : Numero.getString("k_Com1")),
                (Numero.getString("k_Com2").equals("") ? " " : Numero.getString("k_Com2")),
                (Numero.getString("k_Com3").equals("") ? " " : Numero.getString("k_Com3"))));
    }
}
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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


            for (int i = -1; i < listaAltasPa.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                if (i == -1) {


                    fila.setId(0);
                    txtFecha = new TextView(getApplicationContext());
                    txtFecha.setText("Fecha");
                    txtFecha.setGravity(Gravity.START);
                    txtFecha.setBackgroundColor(Color.BLUE);
                    txtFecha.setTextColor(Color.WHITE);
                    txtFecha.setPadding(20, 20, 20, 20);
                    txtFecha.setLayoutParams(layaoutDes);
                    fila.addView(txtFecha);

                    txtHora = new TextView(getApplicationContext());
                    txtHora.setText("Hora");
                    txtHora.setGravity(Gravity.START);
                    txtHora.setBackgroundColor(Color.BLUE);
                    txtHora.setTextColor(Color.WHITE);
                    txtHora.setPadding(20, 20, 20, 20);
                    txtHora.setLayoutParams(layaoutDes);
                    fila.addView(txtHora);

                    txtCcliente = new TextView(getApplicationContext());
                    txtCcliente.setText(" Clave del Cliente");
                    txtCcliente.setGravity(Gravity.START);
                    txtCcliente.setBackgroundColor(Color.BLUE);
                    txtCcliente.setTextColor(Color.WHITE);
                    txtCcliente.setPadding(20, 20, 20, 20);
                    txtCcliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCcliente);

                    txtNCliente = new TextView(getApplicationContext());
                    txtNCliente.setText("Nombre Del Cliente");
                    txtNCliente.setGravity(Gravity.START);
                    txtNCliente.setBackgroundColor(Color.BLUE);
                    txtNCliente.setTextColor(Color.WHITE);
                    txtNCliente.setPadding(20, 20, 20, 20);
                    txtNCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtNCliente);

                    txtFacturas = new TextView(getApplicationContext());
                    txtFacturas.setText("Facturas");
                    txtFacturas.setGravity(Gravity.START);
                    txtFacturas.setBackgroundColor(Color.BLUE);
                    txtFacturas.setTextColor(Color.WHITE);
                    txtFacturas.setPadding(20, 20, 20, 20);
                    txtFacturas.setLayoutParams(layaoutDes);
                    fila.addView(txtFacturas);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setText("Importe");
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setBackgroundColor(Color.BLUE);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);

                    txtNBanco = new TextView(getApplicationContext());
                    txtNBanco.setText("Banco");
                    txtNBanco.setGravity(Gravity.START);
                    txtNBanco.setBackgroundColor(Color.BLUE);
                    txtNBanco.setTextColor(Color.WHITE);
                    txtNBanco.setPadding(20, 20, 20, 20);
                    txtNBanco.setLayoutParams(layaoutDes);
                    fila.addView(txtNBanco);

                    txtFpago = new TextView(getApplicationContext());
                    txtFpago.setText("Forma de Pago");
                    txtFpago.setGravity(Gravity.START);
                    txtFpago.setBackgroundColor(Color.BLUE);
                    txtFpago.setTextColor(Color.WHITE);
                    txtFpago.setPadding(20, 20, 20, 20);
                    txtFpago.setLayoutParams(layaoutDes);
                    fila.addView(txtFpago);

                    txtComentario1 = new TextView(getApplicationContext());
                    txtComentario1.setText("Comentario 1");
                    txtComentario1.setGravity(Gravity.START);
                    txtComentario1.setBackgroundColor(Color.BLUE);
                    txtComentario1.setTextColor(Color.WHITE);
                    txtComentario1.setPadding(20, 20, 20, 20);
                    txtComentario1.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario1);

                    txtComentario2 = new TextView(getApplicationContext());
                    txtComentario2.setText("Comentario 2");
                    txtComentario2.setGravity(Gravity.START);
                    txtComentario2.setBackgroundColor(Color.BLUE);
                    txtComentario2.setTextColor(Color.WHITE);
                    txtComentario2.setPadding(20, 20, 20, 20);
                    txtComentario2.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario2);

                    txtComentario3 = new TextView(getApplicationContext());
                    txtComentario3.setText("Comentario 3");
                    txtComentario3.setGravity(Gravity.START);
                    txtComentario3.setBackgroundColor(Color.BLUE);
                    txtComentario3.setTextColor(Color.WHITE);
                    txtComentario3.setPadding(20, 20, 20, 20);
                    txtComentario3.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario3);

                } else {
                    fila.setId(i + 1);
                    txtFecha = new TextView(getApplicationContext());
                    txtFecha.setGravity(Gravity.START);
                    txtFecha.setBackgroundColor(Color.BLACK);
                    txtFecha.setText(listaAltasPa.get(i).getFecha());
                    txtFecha.setPadding(20, 20, 20, 20);
                    txtFecha.setTextColor(Color.WHITE);
                    txtFecha.setLayoutParams(layaoutDes);
                    fila.addView(txtFecha);

                    txtHora = new TextView(getApplicationContext());
                    txtHora.setBackgroundColor(Color.GRAY);
                    txtHora.setGravity(Gravity.START);
                    txtHora.setText(listaAltasPa.get(i).getHora());
                    txtHora.setPadding(20, 20, 20, 20);
                    txtHora.setTextColor(Color.WHITE);
                    txtHora.setLayoutParams(layaoutDes);
                    fila.addView(txtHora);

                    txtCcliente = new TextView(getApplicationContext());
                    txtCcliente.setBackgroundColor(Color.BLACK);
                    txtCcliente.setGravity(Gravity.START);
                    txtCcliente.setText(listaAltasPa.get(i).getCCliente());
                    txtCcliente.setPadding(20, 20, 20, 20);
                    txtCcliente.setTextColor(Color.WHITE);
                    txtCcliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCcliente);

                    txtNCliente = new TextView(getApplicationContext());
                    txtNCliente.setBackgroundColor(Color.GRAY);
                    txtNCliente.setGravity(Gravity.START);
                    txtNCliente.setMaxLines(1);
                    txtNCliente.setText(listaAltasPa.get(i).getNCliente());
                    txtNCliente.setPadding(20, 20, 20, 20);
                    txtNCliente.setTextColor(Color.WHITE);
                    txtNCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtNCliente);

                    txtFacturas = new TextView(getApplicationContext());
                    txtFacturas.setBackgroundColor(Color.BLACK);
                    txtFacturas.setGravity(Gravity.START);
                    txtFacturas.setText(listaAltasPa.get(i).getFacturas());
                    txtFacturas.setPadding(20, 20, 20, 20);
                    txtFacturas.setTextColor(Color.WHITE);
                    txtFacturas.setLayoutParams(layaoutDes);
                    fila.addView(txtFacturas);

                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setBackgroundColor(Color.GRAY);
                    txtImporte.setGravity(Gravity.END);
                    txtImporte.setText("$" + formatNumberCurrency(listaAltasPa.get(i).getImporte()));
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);


                    txtNBanco = new TextView(getApplicationContext());
                    txtNBanco.setBackgroundColor(Color.BLACK);
                    txtNBanco.setGravity(Gravity.END);
                    txtNBanco.setText(listaAltasPa.get(i).getNBanco());
                    txtNBanco.setPadding(20, 20, 20, 20);
                    txtNBanco.setTextColor(Color.WHITE);
                    txtNBanco.setLayoutParams(layaoutDes);
                    fila.addView(txtNBanco);


                    txtFpago = new TextView(getApplicationContext());
                    txtFpago.setBackgroundColor(Color.GRAY);
                    txtFpago.setGravity(Gravity.END);
                    txtFpago.setText(listaAltasPa.get(i).getFDPago());
                    txtFpago.setPadding(20, 20, 20, 20);
                    txtFpago.setTextColor(Color.WHITE);
                    txtFpago.setLayoutParams(layaoutDes);
                    fila.addView(txtFpago);


                    txtComentario1 = new TextView(getApplicationContext());
                    txtComentario1.setBackgroundColor(Color.BLACK);
                    txtComentario1.setGravity(Gravity.END);
                    txtComentario1.setText(listaAltasPa.get(i).getComentario1());
                    txtComentario1.setPadding(20, 20, 20, 20);
                    txtComentario1.setTextColor(Color.WHITE);
                    txtComentario1.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario1);


                    txtComentario2 = new TextView(getApplicationContext());
                    txtComentario2.setBackgroundColor(Color.GRAY);
                    txtComentario2.setGravity(Gravity.END);
                    txtComentario2.setText(listaAltasPa.get(i).getComentario2());
                    txtComentario2.setPadding(20, 20, 20, 20);
                    txtComentario2.setTextColor(Color.WHITE);
                    txtComentario2.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario2);


                    txtComentario3 = new TextView(getApplicationContext());
                    txtComentario3.setBackgroundColor(Color.BLACK);
                    txtComentario3.setGravity(Gravity.END);
                    txtComentario3.setText(listaAltasPa.get(i).getComentario3());
                    txtComentario3.setPadding(20, 20, 20, 20);
                    txtComentario3.setTextColor(Color.WHITE);
                    txtComentario3.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario3);

                    fila.setPadding(2, 2, 2, 2);
                    fila.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int idfila = view.getId();
                            for (int i = -1; i < listaAltasPa.size(); i++) {
                                int selector = tableLayout.getChildAt(i + 1).getId();
                                if (idfila == selector) {
                                    tableLayout.getChildAt(i + 1).setBackgroundColor(Color.YELLOW);
                                    for (int j = 0; j < fila.getChildCount(); j++) {

                                        TableRow valorfecha = (TableRow) tableLayout.getChildAt(i + 1);
                                        TableRow valorhora = (TableRow) tableLayout.getChildAt(i + 1);
                                        TableRow valorclave = (TableRow) tableLayout.getChildAt(i + 1);

                                        TextView txfecha = (TextView) valorfecha.getChildAt(0);
                                        TextView txhora = (TextView) valorhora.getChildAt(1);
                                        TextView txclave = (TextView) valorclave.getChildAt(2);

                                        fecha123 = txfecha.getText().toString();
                                        hora123 = txhora.getText().toString();
                                        clave123 = txclave.getText().toString();

                                    }
                                } else {
                                    tableLayout.getChildAt(i + 1).setBackgroundColor(Color.rgb(241, 238, 237));
                                }
                            }

                        }

                    });

                }
                tableLayout.addView(fila);

            }

            mDialog.dismiss();
        }


    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "fecha="+fecha123+"&hora="+hora123+"&clave="+clave123;
            String url = "http://" + StrServer + "/eliminarpagounoapp?"+parametros;
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityABCpagos.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            Toast.makeText(ActivityABCpagos.this, "Se Borro con Exito", Toast.LENGTH_SHORT).show();
            ActivityABCpagos.AsyncCallWS4 task4 = new ActivityABCpagos.AsyncCallWS4();
            task4.execute();
            fecha123 = "";
            hora123 = "";
            clave123 = "";
            mDialog.dismiss();
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