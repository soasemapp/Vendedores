package com.example.kepler201.activities.Consultas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.Adapter.AdaptadorConsulFacturas;
import com.example.kepler201.SetterandGetter.ConFaDetSANDG;
import com.example.kepler201.SetterandGetter.ConsulFacfturasSANDG;
import com.example.kepler201.XMLS.xmlConFactDetall;
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
import java.text.DecimalFormat;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static com.example.kepler201.R.id;
import static com.example.kepler201.R.layout;

public class ActivityFactuDetall extends AppCompatActivity {
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcode, strcodBra, StrServer;
    ArrayList<ConFaDetSANDG> listasearch2 = new ArrayList<>();
    private TableLayout tableLayout;
    TableRow fila;
    AlertDialog mDialog;
    TextView txtProducto, txtDescripcion, txtCantidad, txtPrecioU, txtDescuento, txtPrecioTXP;
    String ClaveFolDialog = "";
    String ClaveNumDialog = "";
    String Cliente = "";
    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_factu_detall);

        tableLayout =  findViewById(id.table);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog(ActivityFactuDetall.this);

        mDialog.setCancelable(false);
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
        ClaveFolDialog = getIntent().getStringExtra("Folio");
        ClaveNumDialog = getIntent().getStringExtra("NumSucu");
        Cliente = getIntent().getStringExtra("Cliente");
        MyToolbar.show(this, "Factura:" + ClaveFolDialog, true);





        ListadeFacturasdetall();

    }





    public void ListadeFacturasdetall() {
        new ActivityFactuDetall.Facturasdetallado().execute();
    }


    private class Facturasdetallado extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "folio=" + ClaveFolDialog + "&sucursal=" + ClaveNumDialog + "&cliente=" + Cliente;
            String url = "http://" + StrServer + "/facturadetalladaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if(json.length()!=0) {
                        JSONObject jitems, Numero, Clave, Nombre;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");
                            listasearch2.add(new ConFaDetSANDG(Numero.getString("producto"),
                                    Numero.getString("descripcion"),
                                    Numero.getString("cantidad"),
                                    Numero.getString("precioU"),
                                    Numero.getString("descuento"),
                                    Numero.getString("precioTXP"),
                                    Numero.getString("sucursal")));
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityFactuDetall.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityFactuDetall.this);
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

            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listasearch2.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                txtProducto = new TextView(getApplicationContext());
                if (i == -1) {
                    txtProducto.setText("Producto");
                    txtProducto.setGravity(Gravity.START);
                    txtProducto.setBackgroundColor(Color.BLUE);
                    txtProducto.setTextColor(Color.WHITE);
                    txtProducto.setPadding(20, 20, 20, 20);
                    txtProducto.setLayoutParams(layaoutDes);
                    fila.addView(txtProducto);

                    txtDescripcion = new TextView(getApplicationContext());
                    txtDescripcion.setText("Descripcion");
                    txtDescripcion.setGravity(Gravity.START);
                    txtDescripcion.setBackgroundColor(Color.BLUE);
                    txtDescripcion.setTextColor(Color.WHITE);
                    txtDescripcion.setPadding(20, 20, 20, 20);
                    txtDescripcion.setLayoutParams(layaoutDes);
                    fila.addView(txtDescripcion);

                    txtCantidad = new TextView(getApplicationContext());
                    txtCantidad.setText("Cant");
                    txtCantidad.setGravity(Gravity.START);
                    txtCantidad.setBackgroundColor(Color.BLUE);
                    txtCantidad.setTextColor(Color.WHITE);
                    txtCantidad.setPadding(20, 20, 20, 20);
                    txtCantidad.setLayoutParams(layaoutDes);
                    fila.addView(txtCantidad);

                    txtPrecioU = new TextView(getApplicationContext());
                    txtPrecioU.setText("P/U");
                    txtPrecioU.setGravity(Gravity.START);
                    txtPrecioU.setBackgroundColor(Color.BLUE);
                    txtPrecioU.setTextColor(Color.WHITE);
                    txtPrecioU.setPadding(20, 20, 20, 20);
                    txtPrecioU.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecioU);

                    txtDescuento = new TextView(getApplicationContext());
                    txtDescuento.setText("Descu");
                    txtDescuento.setGravity(Gravity.START);
                    txtDescuento.setBackgroundColor(Color.BLUE);
                    txtDescuento.setTextColor(Color.WHITE);
                    txtDescuento.setPadding(20, 20, 20, 20);
                    txtDescuento.setLayoutParams(layaoutDes);
                    fila.addView(txtDescuento);


                    txtPrecioTXP = new TextView(getApplicationContext());
                    txtPrecioTXP.setText("Import");
                    txtPrecioTXP.setGravity(Gravity.END);
                    txtPrecioTXP.setBackgroundColor(Color.BLUE);
                    txtPrecioTXP.setTextColor(Color.WHITE);
                    txtPrecioTXP.setPadding(20, 20, 20, 20);
                    txtPrecioTXP.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecioTXP);
                } else {

                    txtProducto.setGravity(Gravity.START);
                    txtProducto.setBackgroundColor(Color.BLACK);
                    txtProducto.setText(listasearch2.get(i).getProductor());
                    txtProducto.setPadding(20, 20, 20, 20);
                    txtProducto.setTextColor(Color.WHITE);
                    txtProducto.setLayoutParams(layaoutDes);
                    fila.addView(txtProducto);


                    txtDescripcion = new TextView(getApplicationContext());
                    txtDescripcion.setBackgroundColor(Color.GRAY);
                    txtDescripcion.setGravity(Gravity.START);
                    txtDescripcion.setText(listasearch2.get(i).getDescripcion());
                    txtDescripcion.setPadding(20, 20, 20, 20);
                    txtDescripcion.setTextColor(Color.WHITE);
                    txtDescripcion.setLayoutParams(layaoutDes);
                    fila.addView(txtDescripcion);

                    txtCantidad = new TextView(getApplicationContext());
                    txtCantidad.setBackgroundColor(Color.BLACK);
                    txtCantidad.setGravity(Gravity.START);
                    txtCantidad.setText(listasearch2.get(i).getCantidad());
                    txtCantidad.setPadding(20, 20, 20, 20);
                    txtCantidad.setTextColor(Color.WHITE);
                    txtCantidad.setLayoutParams(layaoutDes);
                    fila.addView(txtCantidad);

                    txtPrecioU = new TextView(getApplicationContext());
                    txtPrecioU.setBackgroundColor(Color.GRAY);
                    txtPrecioU.setGravity(Gravity.END);
                    txtPrecioU.setText("$" + formatNumberCurrency(listasearch2.get(i).getPrecioU()));
                    txtPrecioU.setPadding(20, 20, 20, 20);
                    txtPrecioU.setTextColor(Color.WHITE);
                    txtPrecioU.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecioU);
                    txtDescuento = new TextView(getApplicationContext());
                    txtDescuento.setBackgroundColor(Color.BLACK);
                    txtDescuento.setGravity(Gravity.START);
                    txtDescuento.setText(listasearch2.get(i).getDescuento() + "%");
                    txtDescuento.setPadding(20, 20, 20, 20);
                    txtDescuento.setTextColor(Color.WHITE);
                    txtDescuento.setLayoutParams(layaoutDes);
                    fila.addView(txtDescuento);

                    txtPrecioTXP = new TextView(getApplicationContext());
                    txtPrecioTXP.setBackgroundColor(Color.GRAY);
                    txtPrecioTXP.setGravity(Gravity.END);
                    txtPrecioTXP.setText("$" + formatNumberCurrency(listasearch2.get(i).getPrecioTXP()));
                    txtPrecioTXP.setPadding(20, 20, 20, 20);
                    txtPrecioTXP.setTextColor(Color.WHITE);
                    txtPrecioTXP.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecioTXP);


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


}