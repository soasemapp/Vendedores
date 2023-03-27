package com.example.kepler201.activities.Pedidos;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.SetterandGetter.SeguimientoPedidosSANDG;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.XMLS.xmlSegumientoPedidos;
import com.example.kepler201.includes.MyToolbar;

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

public class ActivitySegumientoPedidos extends AppCompatActivity {

    ImageView SeguimiPed, ConsulPed, ConsulCot;
    private Spinner spinerClie;
    private EditText fechaEn, fechaSa;
    private TableLayout tableLayout;
    TextView txtPedido, txtFechaPed, txtCliente, txtLiberacion, txtAduana, txtFactura, txtFechaFacturacion , txtHora ,txtFolio;
    TableRow fila;
    private boolean multicolor = true;

    String FechaIncial, FechaFinal;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    String strscliente;
    String mensaje = "";

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<SeguimientoPedidosSANDG> listaSeguimientoPe = new ArrayList<>();

    int n = 2000;
    String[] search2 = new String[n];
    String date;
    String date2;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segumiento_pedidos);

        MyToolbar.show(this, "Pedidos-Seguimiento de Pedido", true);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog.Builder().setContext(ActivitySegumientoPedidos.this).setMessage("Espere un momento...").build();
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


        tableLayout =  findViewById(R.id.table);
        spinerClie =  findViewById(R.id.spinnerClie);
        fechaEn =  findViewById(R.id.fechaendtrada);
        fechaSa =  findViewById(R.id.fechasalida);
        Button btnsearch =  findViewById(R.id.btnSearch);
        SeguimiPed =  findViewById(R.id.seguPed);
        ConsulCot =  findViewById(R.id.ConsulCoti);
        ConsulPed =  findViewById(R.id.ConsulPed);


        SeguimiPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeguimiPed.setBackgroundColor(Color.GRAY);
                SeguimiPed.setBackgroundColor(Color.TRANSPARENT);
                Intent SeguPedi = new Intent(ActivitySegumientoPedidos.this, ActivitySegumientoPedidos.class);

                overridePendingTransition(0, 0);
                startActivity(SeguPedi);
                overridePendingTransition(0, 0);
                finish();


            }
        });
        ConsulPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsulPed.setBackgroundColor(Color.GRAY);
                ConsulPed.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsulPed = new Intent(ActivitySegumientoPedidos.this, ActivityConsulPedi.class);
                overridePendingTransition(0, 0);
                startActivity(ConsulPed);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ConsulCot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsulCot.setBackgroundColor(Color.GRAY);
                ConsulCot.setBackgroundColor(Color.TRANSPARENT);
                Intent ConsulCoti = new Intent(ActivitySegumientoPedidos.this, ActivityConsulCoti.class);
                overridePendingTransition(0, 0);
                startActivity(ConsulCoti);
                overridePendingTransition(0, 0);
                finish();
            }
        });



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivitySegumientoPedidos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        fechaEn.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        fechaSa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivitySegumientoPedidos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date2 = year + "-" + month + "-" + day;
                        fechaSa.setText(date2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    tableLayout.removeAllViews();
                    listaSeguimientoPe.clear();

                    FechaIncial = fechaEn.getText().toString();
                    FechaFinal = fechaSa.getText().toString();
                    for (int i = 0; i < search2.length; i++) {
                        int posi = spinerClie.getSelectedItemPosition();
                        if (posi == i) {
                            strscliente = search2[i];
                            break;
                        }
                    }
                    if (!FechaIncial.isEmpty() && !FechaFinal.isEmpty() && spinerClie.getSelectedItemPosition() == 0) {
                        strscliente = "";
                        tableLayout.removeAllViews();
                        listaSeguimientoPe.clear();
                        ActivitySegumientoPedidos.AsyncCallWS2 task2 = new ActivitySegumientoPedidos.AsyncCallWS2();
                        task2.execute();
                    } else if (!FechaIncial.isEmpty() && !FechaFinal.isEmpty() && spinerClie.getSelectedItemPosition() != 0) {
                        tableLayout.removeAllViews();
                        listaSeguimientoPe.clear();
                        ActivitySegumientoPedidos.AsyncCallWS2 task2 = new ActivitySegumientoPedidos.AsyncCallWS2();
                        task2.execute();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivitySegumientoPedidos.this);
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

        Calendar c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.DAY_OF_YEAR, -2);
        String fechaasalll = dateformatActually1.format(c.getTime());


        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c1.getTime());

        fechaEn.setText(fechaasalll);
        fechaSa.setText(fechaactual);
        strscliente = "";
        FechaIncial = fechaEn.getText().toString();
        FechaFinal = fechaSa.getText().toString();
        ActivitySegumientoPedidos.AsyncCallWS task = new ActivitySegumientoPedidos.AsyncCallWS();
        task.execute();

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
            conectar();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "Todos los clientes";
            search2[0] = "Todos  Los Clientes";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search2[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
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
            conecta2();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listaSeguimientoPe.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                if (i == -1) {
                    txtPedido = new TextView(getApplicationContext());
                    txtPedido.setText("Pedido");
                    txtPedido.setGravity(Gravity.START);
                    txtPedido.setBackgroundColor(Color.BLUE);
                    txtPedido.setTextColor(Color.WHITE);
                    txtPedido.setPadding(20, 20, 20, 20);
                    txtPedido.setLayoutParams(layaoutDes);
                    fila.addView(txtPedido);

                    txtFechaPed = new TextView(getApplicationContext());
                    txtFechaPed.setText("Fecha de Pedido");
                    txtFechaPed.setGravity(Gravity.START);
                    txtFechaPed.setBackgroundColor(Color.BLUE);
                    txtFechaPed.setTextColor(Color.WHITE);
                    txtFechaPed.setPadding(20, 20, 20, 20);
                    txtFechaPed.setLayoutParams(layaoutDes);
                    fila.addView(txtFechaPed);

                    txtCliente = new TextView(getApplicationContext());
                    txtCliente.setText("Cliente");
                    txtCliente.setGravity(Gravity.START);
                    txtCliente.setBackgroundColor(Color.BLUE);
                    txtCliente.setTextColor(Color.WHITE);
                    txtCliente.setPadding(20, 20, 20, 20);
                    txtCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCliente);

                    txtLiberacion = new TextView(getApplicationContext());
                    txtLiberacion.setText("Liberacion");
                    txtLiberacion.setGravity(Gravity.START);
                    txtLiberacion.setBackgroundColor(Color.BLUE);
                    txtLiberacion.setTextColor(Color.WHITE);
                    txtLiberacion.setPadding(20, 20, 20, 20);
                    txtLiberacion.setLayoutParams(layaoutDes);
                    fila.addView(txtLiberacion);

                    txtAduana = new TextView(getApplicationContext());
                    txtAduana.setText("Aduana");
                    txtAduana.setGravity(Gravity.START);
                    txtAduana.setBackgroundColor(Color.BLUE);
                    txtAduana.setTextColor(Color.WHITE);
                    txtAduana.setPadding(20, 20, 20, 20);
                    txtAduana.setLayoutParams(layaoutDes);
                    fila.addView(txtAduana);


                    txtFactura = new TextView(getApplicationContext());
                    txtFactura.setText("Factura");
                    txtFactura.setGravity(Gravity.START);
                    txtFactura.setBackgroundColor(Color.BLUE);
                    txtFactura.setTextColor(Color.WHITE);
                    txtFactura.setPadding(20, 20, 20, 20);
                    txtFactura.setLayoutParams(layaoutDes);
                    fila.addView(txtFactura);

                    txtFechaFacturacion = new TextView(getApplicationContext());
                    txtFechaFacturacion.setText("Fecha de Facturacion");
                    txtFechaFacturacion.setGravity(Gravity.START);
                    txtFechaFacturacion.setBackgroundColor(Color.BLUE);
                    txtFechaFacturacion.setTextColor(Color.WHITE);
                    txtFechaFacturacion.setPadding(20, 20, 20, 20);
                    txtFechaFacturacion.setLayoutParams(layaoutDes);
                    fila.addView(txtFechaFacturacion);

                    txtHora = new TextView(getApplicationContext());
                    txtHora.setText("Hora Factura");
                    txtHora.setGravity(Gravity.START);
                    txtHora.setBackgroundColor(Color.BLUE);
                    txtHora.setTextColor(Color.WHITE);
                    txtHora.setPadding(20, 20, 20, 20);
                    txtHora.setLayoutParams(layaoutDes);
                    fila.addView(txtHora);

                    txtFolio = new TextView(getApplicationContext());
                    txtFolio.setText("Folio Web");
                    txtFolio.setGravity(Gravity.START);
                    txtFolio.setBackgroundColor(Color.BLUE);
                    txtFolio.setTextColor(Color.WHITE);
                    txtFolio.setPadding(20, 20, 20, 20);
                    txtFolio.setLayoutParams(layaoutDes);
                    fila.addView(txtFolio);

                    tableLayout.addView(fila);
                } else {
                    multicolor = !multicolor;
                    txtPedido = new TextView(getApplicationContext());
                    txtPedido.setGravity(Gravity.START);
                    txtPedido.setBackgroundColor(Color.BLACK);
                    txtPedido.setText(listaSeguimientoPe.get(i).getPedidos());
                    txtPedido.setPadding(20, 20, 20, 20);
                    txtPedido.setTextColor(Color.WHITE);
                    txtPedido.setLayoutParams(layaoutDes);
                    fila.addView(txtPedido);

                    txtFechaPed = new TextView(getApplicationContext());
                    txtFechaPed.setBackgroundColor(Color.GRAY);
                    txtFechaPed.setGravity(Gravity.START);
                    txtFechaPed.setText(listaSeguimientoPe.get(i).getFechaPedido());
                    txtFechaPed.setPadding(20, 20, 20, 20);
                    txtFechaPed.setTextColor(Color.WHITE);
                    txtFechaPed.setLayoutParams(layaoutDes);
                    fila.addView(txtFechaPed);

                    txtCliente = new TextView(getApplicationContext());
                    txtCliente.setBackgroundColor(Color.BLACK);
                    txtCliente.setGravity(Gravity.START);
                    txtCliente.setText(listaSeguimientoPe.get(i).getCliente());
                    txtCliente.setPadding(20, 20, 20, 20);
                    txtCliente.setTextColor(Color.WHITE);
                    txtCliente.setLayoutParams(layaoutDes);
                    fila.addView(txtCliente);

                    txtLiberacion = new TextView(getApplicationContext());
                    txtLiberacion.setBackgroundColor(Color.GRAY);
                    txtLiberacion.setGravity(Gravity.START);
                    txtLiberacion.setText(listaSeguimientoPe.get(i).getLiberacion());
                    txtLiberacion.setPadding(20, 20, 20, 20);
                    txtLiberacion.setTextColor(Color.WHITE);
                    txtLiberacion.setLayoutParams(layaoutDes);
                    fila.addView(txtLiberacion);

                    txtAduana = new TextView(getApplicationContext());
                    txtAduana.setBackgroundColor(Color.BLACK);
                    txtAduana.setGravity(Gravity.START);
                    txtAduana.setText(listaSeguimientoPe.get(i).getAduana());
                    txtAduana.setPadding(20, 20, 20, 20);
                    txtAduana.setTextColor(Color.WHITE);
                    txtAduana.setLayoutParams(layaoutDes);
                    fila.addView(txtAduana);

                    txtFactura = new TextView(getApplicationContext());
                    txtFactura.setBackgroundColor(Color.GRAY);
                    txtFactura.setGravity(Gravity.START);
                    txtFactura.setText(listaSeguimientoPe.get(i).getFacturas());
                    txtFactura.setPadding(20, 20, 20, 20);
                    txtFactura.setTextColor(Color.WHITE);
                    txtFactura.setLayoutParams(layaoutDes);
                    fila.addView(txtFactura);


                    txtFechaFacturacion = new TextView(getApplicationContext());
                    txtFechaFacturacion.setBackgroundColor(Color.BLACK);
                    txtFechaFacturacion.setGravity(Gravity.END);
                    txtFechaFacturacion.setText(listaSeguimientoPe.get(i).getFecha_Facturacion());
                    txtFechaFacturacion.setPadding(20, 20, 20, 20);
                    txtFechaFacturacion.setTextColor(Color.WHITE);
                    txtFechaFacturacion.setLayoutParams(layaoutDes);
                    fila.addView(txtFechaFacturacion);

                    txtHora = new TextView(getApplicationContext());
                    txtHora.setBackgroundColor(Color.GRAY);
                    txtHora.setGravity(Gravity.END);
                    txtHora.setText(listaSeguimientoPe.get(i).getHora());
                    txtHora.setPadding(20, 20, 20, 20);
                    txtHora.setTextColor(Color.WHITE);
                    txtHora.setLayoutParams(layaoutDes);
                    fila.addView(txtHora);

                    txtFolio = new TextView(getApplicationContext());
                    txtFolio.setBackgroundColor(Color.BLACK);
                    txtFolio.setGravity(Gravity.END);
                    txtFolio.setText(listaSeguimientoPe.get(i).getFolioWeb());
                    txtFolio.setPadding(20, 20, 20, 20);
                    txtFolio.setTextColor(Color.WHITE);
                    txtFolio.setLayoutParams(layaoutDes);
                    fila.addView(txtFolio);

                    fila.setPadding(2, 2, 2, 2);

                    tableLayout.addView(fila);

                }

            }
            mDialog.dismiss();


        }


        private void conecta2() {
            String SOAP_ACTION = "SeguidoPedi";
            String METHOD_NAME = "SeguidoPedi";
            String NAMESPACE = "http://" + StrServer + "/WSk75items/";
            String URL = "http://" + StrServer + "/WSk75items";


            try {

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                xmlSegumientoPedidos soapEnvelope = new xmlSegumientoPedidos(SoapEnvelope.VER11);
                soapEnvelope.xmlSegumientoPedi(strusr, strpass, strcode, strscliente, FechaIncial, FechaFinal);
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
                    //(response0.getPropertyAsString("k_FECHA_FACT").equals("anyType{}")?" ":response0.getPropertyAsString("k_FECHA_FACT"))
                    listaSeguimientoPe.add(new SeguimientoPedidosSANDG((response0.getPropertyAsString("k_Pedido").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Pedido")),
                            (response0.getPropertyAsString("k_FECHA_PED").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FECHA_PED")),
                            (response0.getPropertyAsString("k_CLIENTE").equals("anyType{}") ? " " : response0.getPropertyAsString("k_CLIENTE")),
                            (response0.getPropertyAsString("k_LIBERACION").equals("anyType{}") ? " " : response0.getPropertyAsString("k_LIBERACION")),
                            (response0.getPropertyAsString("k_ADUANA").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ADUANA")),
                            (response0.getPropertyAsString("k_FACTURA").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FACTURA")),
                            (response0.getPropertyAsString("k_FECHA_FACT").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FECHA_FACT")),
                            (response0.getPropertyAsString("k_FOLIOWEB").equals("anyType{}") ? " " : response0.getPropertyAsString("k_FOLIOWEB")),
                            (response0.getPropertyAsString("k_HORA").equals("anyType{}") ? " " : response0.getPropertyAsString("k_HORA"))));


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

}