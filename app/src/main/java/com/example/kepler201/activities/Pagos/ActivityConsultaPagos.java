package com.example.kepler201.activities.Pagos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.RegistroPagosSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlConsultaPagos2;
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

public class ActivityConsultaPagos extends AppCompatActivity {

    TableRow fila;
    private boolean multicolor = true;
    private TableLayout tableLayout;
    TextView vendedor;
    EditText fechainicial;
    EditText fechafinal;
    Spinner clientes;
    CheckBox confirmarprob;
    Button searchConsult;
    String date;
    String date2;
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    String strusr = "", strpass = "", strname = "", strlname = "", strtype = "", strbran = "", strma = "", strco = "", StrServer = "";
    int n = 2000;
    String[] search1 = new String[n];
    String mensaje = "";
    String strFeinic = "", strFefina = "", strcodBra, strcliente = "";
    String check = "";
    ArrayList<RegistroPagosSANDG> listaAltasPa = new ArrayList<>();
    TextView txtFecha, txtHora, txtCcliente, txtNCliente, txtFacturas, txtImporte, txtNBanco, txtFpago, txtComentario1, txtComentario2, txtComentario3;
    AlertDialog mDialog;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_pagos);

        MyToolbar.show(this, "Pagos-Consulta de Pagos", true);

        mDialog = new SpotsDialog.Builder().setContext(ActivityConsultaPagos.this).setMessage("Espere un momento...").build();

        confirmarprob = findViewById(R.id.checkconfirmar);
        vendedor = findViewById(R.id.cVendedor);
        fechainicial = findViewById(R.id.fechaendtrada);
        fechafinal = findViewById(R.id.fechasalida);
        clientes = findViewById(R.id.spinnerClie);
        searchConsult = findViewById(R.id.btnSearch);
        tableLayout = findViewById(R.id.table);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

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


        vendedor.setText(strname + " " + strlname);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechainicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaPagos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        fechainicial.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        fechafinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaPagos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date2 = year + "-" + month + "-" + day;
                        fechafinal.setText(date2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        confirmarprob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmarprob.isChecked()) {
                    check = "1";
                } else if (!confirmarprob.isChecked()) {
                    check = "";


                }
            }
        });



        searchConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    strFefina = fechafinal.getText().toString();
                    strFeinic = fechainicial.getText().toString();

                    for (int i = 0; i < search1.length; i++) {
                        int posi = clientes.getSelectedItemPosition();
                        if (posi == i) {
                            strcliente = search1[i];
                            break;
                        }
                    }
                    if (!strFeinic.isEmpty() && !strFefina.isEmpty() && clientes.getSelectedItemPosition() == 0) {
                        strcliente = "";
                        tableLayout.removeAllViews();
                        listaAltasPa.clear();
                        ActivityConsultaPagos.AsyncCallWS2 task2 = new ActivityConsultaPagos.AsyncCallWS2();
                        task2.execute();
                    } else if (!strFeinic.isEmpty() && !strFefina.isEmpty() && clientes.getSelectedItemPosition() != 0) {
                        tableLayout.removeAllViews();
                        listaAltasPa.clear();
                        ActivityConsultaPagos.AsyncCallWS2 task2 = new ActivityConsultaPagos.AsyncCallWS2();
                        task2.execute();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaPagos.this);
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
        ActivityConsultaPagos.AsyncCallWS task = new ActivityConsultaPagos.AsyncCallWS();
        task.execute();

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c.getTime());


        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        c1.add(Calendar.MONTH,-1);
        String fechatreinta = dateformatActually1.format(c1.getTime());

        fechainicial.setText(fechatreinta);
        fechafinal.setText(fechaactual);


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
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "Todos Los Clientes";
            search1[0] = "Todos Los Clientes";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search1[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            clientes.setAdapter(adapter);
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
            soapEnvelope.xmlSearchG(strusr, strpass, strco);
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
                listaclientG.add(new SearachClientSANDG((response0.getPropertyAsString("k_dscr").equals("anyType{}") ?"" : response0.getPropertyAsString("k_dscr")), (response0.getPropertyAsString("k_line").equals("anyType{}") ?"" : response0.getPropertyAsString("k_line"))));


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
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar2();
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
                    multicolor = !multicolor;
                    txtFecha = new TextView(getApplicationContext());
                    txtFecha.setGravity(Gravity.START);
                    txtFecha.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtFecha.setText(listaAltasPa.get(i).getFecha());
                    txtFecha.setPadding(20, 20, 20, 20);
                    txtFecha.setTextColor(Color.WHITE);
                    txtFecha.setLayoutParams(layaoutDes);
                    fila.addView(txtFecha);

                    txtHora = new TextView(getApplicationContext());
                    txtHora.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtHora.setGravity(Gravity.START);
                    txtHora.setText(listaAltasPa.get(i).getHora());
                    txtHora.setPadding(20, 20, 20, 20);
                    txtHora.setTextColor(Color.WHITE);
                    txtHora.setLayoutParams(layaoutDes);
                    fila.addView(txtHora);

                    txtCcliente = new TextView(getApplicationContext());
                    txtCcliente.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtCcliente.setGravity(Gravity.START);
                    txtCcliente.setText(listaAltasPa.get(i).getCCliente());
                    txtCcliente.setPadding(20, 20, 20, 20);
                    txtCcliente.setTextColor(Color.WHITE);
                    txtCcliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCcliente);

                    txtNCliente = new TextView(getApplicationContext());
                    txtNCliente.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtNCliente.setGravity(Gravity.START);
                    txtNCliente.setMaxLines(1);
                    txtNCliente.setText(listaAltasPa.get(i).getNCliente());
                    txtNCliente.setPadding(20, 20, 20, 20);
                    txtNCliente.setTextColor(Color.WHITE);
                    txtNCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtNCliente);

                    txtFacturas = new TextView(getApplicationContext());
                    txtFacturas.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtFacturas.setGravity(Gravity.START);
                    txtFacturas.setText(listaAltasPa.get(i).getFacturas());
                    txtFacturas.setPadding(20, 20, 20, 20);
                    txtFacturas.setTextColor(Color.WHITE);
                    txtFacturas.setLayoutParams(layaoutDes);
                    fila.addView(txtFacturas);

                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtImporte.setGravity(Gravity.END);
                    txtImporte.setText("$" + formatNumberCurrency(listaAltasPa.get(i).getImporte()));
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);


                    txtNBanco = new TextView(getApplicationContext());
                    txtNBanco.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtNBanco.setGravity(Gravity.END);
                    txtNBanco.setText(listaAltasPa.get(i).getNBanco());
                    txtNBanco.setPadding(20, 20, 20, 20);
                    txtNBanco.setTextColor(Color.WHITE);
                    txtNBanco.setLayoutParams(layaoutDes);
                    fila.addView(txtNBanco);


                    txtFpago = new TextView(getApplicationContext());
                    txtFpago.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtFpago.setGravity(Gravity.END);
                    txtFpago.setText(listaAltasPa.get(i).getFDPago());
                    txtFpago.setPadding(20, 20, 20, 20);
                    txtFpago.setTextColor(Color.WHITE);
                    txtFpago.setLayoutParams(layaoutDes);
                    fila.addView(txtFpago);


                    txtComentario1 = new TextView(getApplicationContext());
                    txtComentario1.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtComentario1.setGravity(Gravity.END);
                    txtComentario1.setText(listaAltasPa.get(i).getComentario1());
                    txtComentario1.setPadding(20, 20, 20, 20);
                    txtComentario1.setTextColor(Color.WHITE);
                    txtComentario1.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario1);


                    txtComentario2 = new TextView(getApplicationContext());
                    txtComentario2.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtComentario2.setGravity(Gravity.END);
                    txtComentario2.setText(listaAltasPa.get(i).getComentario2());
                    txtComentario2.setPadding(20, 20, 20, 20);
                    txtComentario2.setTextColor(Color.WHITE);
                    txtComentario2.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario2);


                    txtComentario3 = new TextView(getApplicationContext());
                    txtComentario3.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtComentario3.setGravity(Gravity.END);
                    txtComentario3.setText(listaAltasPa.get(i).getComentario3());
                    txtComentario3.setPadding(20, 20, 20, 20);
                    txtComentario3.setTextColor(Color.WHITE);
                    txtComentario3.setLayoutParams(layaoutDes);
                    fila.addView(txtComentario3);

                    fila.setPadding(2, 2, 2, 2);

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

    private void conectar2() {
        String SOAP_ACTION = "ConsulPago";
        String METHOD_NAME = "ConsulPago";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConsultaPagos2 soapEnvelope = new xmlConsultaPagos2(SoapEnvelope.VER11);
            soapEnvelope.xmlConsultapago2(strusr, strpass, strco, strcliente, strFeinic, strFefina, check);
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

}