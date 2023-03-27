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
import com.example.kepler201.XMLS.xmlBankSearch;
import com.example.kepler201.XMLS.xmlConsultaPagos;
import com.example.kepler201.XMLS.xmlDeletePagos;
import com.example.kepler201.XMLS.xmlRegistroPagos;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.includes.MyToolbar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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

        mDialog = new SpotsDialog.Builder().setContext(ActivityABCpagos.this).setMessage("Espere un momento...").build();
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
                            "Forma de Pago : " + Strimporte + "\n" +
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
            conectar();
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

    private void conectar() {
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
            int json = response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                listaclientG.add(new SearachClientSANDG((response0.getPropertyAsString("k_dscr").equals("anyType{}") ? "" : response0.getPropertyAsString("k_dscr")), (response0.getPropertyAsString("k_line").equals("anyType{}") ? "" : response0.getPropertyAsString("k_line"))));
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


    //WebService De bancos

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

    private void conectar2() {
        String SOAP_ACTION = "BankSearch";
        String METHOD_NAME = "BankSearch";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlBankSearch soapEnvelope = new xmlBankSearch(SoapEnvelope.VER11);
            soapEnvelope.xmlBankSe(strusr, strpass);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            int json = response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                listaBancos.add(new BANKSANDG((response0.getPropertyAsString("k_Clave").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Clave")),
                        (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Nombre"))));


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
            conectar3();
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


    private void conectar3() {
        String SOAP_ACTION = "RegiPago";
        String METHOD_NAME = "RegiPago";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlRegistroPagos soapEnvelope = new xmlRegistroPagos(SoapEnvelope.VER11);
            soapEnvelope.xmlRegistroP(strusr, strpass, strcode, Strcliente, Strdate, Strfacturas, Strimporte, StrBanco, StrForma, StrComentarios1, StrComentarios2, StrComentarios3);
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
            conectar4();
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

    private void conectar4() {
        String SOAP_ACTION = "ConsulPago";
        String METHOD_NAME = "ConsulPago";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConsultaPagos soapEnvelope = new xmlConsultaPagos(SoapEnvelope.VER11);
            soapEnvelope.xmlConsultaP(strusr, strpass, strcode);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            int json = response.getPropertyCount();

            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                //(response0.getPropertyAsString("k_FormaP").equals("anyType{}")?" ":response0.getPropertyAsString("k_Hora"))
                listaAltasPa.add(new RegistroPagosSANDG((response0.getPropertyAsString("k_Fecha").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Fecha")),
                        (response0.getPropertyAsString("k_Hora").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Hora")),
                        (response0.getPropertyAsString("k_Ccliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Ccliente")),
                        (response0.getPropertyAsString("k_Ncliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Ncliente")),
                        (response0.getPropertyAsString("k_facturas").equals("anyType{}") ? " " : response0.getPropertyAsString("k_facturas")),
                        (response0.getPropertyAsString("k_Importe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Importe")),
                        (response0.getPropertyAsString("k_Nbanco").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nbanco")),
                        (response0.getPropertyAsString("k_FormaP").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FormaP")),
                        (response0.getPropertyAsString("k_Com1").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Com1")),
                        (response0.getPropertyAsString("k_Com2").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Com2")),
                        (response0.getPropertyAsString("k_Com3").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Com3"))));


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
    private class AsyncCallWS5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar5();
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

    private void conectar5() {
        String SOAP_ACTION = "DeletPay";
        String METHOD_NAME = "DeletPay";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlDeletePagos soapEnvelope = new xmlDeletePagos(SoapEnvelope.VER11);
            soapEnvelope.xmlDeletePag(strusr, strpass, fecha123, hora123, clave123);
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