package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdapterDetalleExistencia;
import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AplicacionesSANDG;
import com.example.kepler201.SetterandGetter.CarritoBD;
import com.example.kepler201.SetterandGetter.CarritoVentasSANDG;
import com.example.kepler201.SetterandGetter.ConversionesSANDG;
import com.example.kepler201.SetterandGetter.DisponibilidadSANDG;
import com.example.kepler201.XMLS.xmlAplicaciones;
import com.example.kepler201.XMLS.xmlCarritoVentas;
import com.example.kepler201.XMLS.xmlConverProdu;
import com.example.kepler201.XMLS.xmlEquiva;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.includes.MyToolbar;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("CatchMayIgnoreException")
public class DetalladoProductosActivity extends AppCompatActivity {


    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;
    String Producto="", claveVentana = null, Descripcion="", PrecioAjustado="0", PrecioBase="0",Linea="";
    TextView  txtClavePro, txtCant, txtPrecio, txtDesc, txtImporte;
    TextView txtClave, txtClavecompetencia, txtnombreCompetencia;
    LinearLayout TableProd;

    private boolean multicolor = true;
    TableRow fila;
    private TableLayout tableLayout;
    private TableLayout tableLayout2;

    ArrayList<ConversionesSANDG> listaconversiones = new ArrayList<>();

    private SharedPreferences.Editor editor;
    ArrayList<AplicacionesSANDG> Aplicaciones = new ArrayList<>();
    ArrayList<DisponibilidadSANDG> Existencias = new ArrayList<>();
    TextView Descripciontxt, ClaveProdcutotxt, Preciotxt,Lineatxt;
    String strClave = null;
    String strCantidad = "1";
    EditText Cantidad;
    RecyclerView RecyclerProductos;
    Context context = this;
    ImageView imageproducto;
    Button btnCarShoping;
    private SharedPreferences preferenceClie;
    ArrayList<CarritoVentasSANDG> listaCarShoping = new ArrayList<>();
    ArrayList<CarritoBD> listaCarShoping2 = new ArrayList<>();
    AlertDialog mDialog;
    String mensaje = "";
    String MensajePro;
    String ProductoEqui;
    String ValidaEqui;
    int ban = 1;
    String rfc;
    String plazo;
    String Vendedor;
    String Nombre;
    String Calle;
    String Colonia;
    String Poblacion;
    String Via;
    String K87;
    ConexionSQLiteHelper conect;
    String Desc1fa;
    String Comentario1;
    String Comentario2;
    String Comentario3;
    String Cliente = "";
    String DescPro;
    String Desc1;
    String Empresa;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallado_productos);

        MyToolbar.show(this, " ", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor = preferenceClie.edit();
        mDialog = new SpotsDialog.Builder().setContext(DetalladoProductosActivity.this).setMessage("Espere un momento...").build();

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




        switch (StrServer) {
            case "jacve.dyndns.org:9085":
                Empresa = "https://www.jacve.mx/es-mx/img/products/xl/";
                break;
            case "autodis.ath.cx:9085":
                Empresa = "https://www.autodis.mx/es-mx/img/products/xl/";
                break;
            case "cecra.ath.cx:9085":
                Empresa = "https://www.cecra.mx/es-mx/img/products/xl/";
                break;
            case "guvi.ath.cx:9085":
                Empresa = "https://www.guvi.mx/es-mx/img/products/xl/";
                break;
            case "cedistabasco.ddns.net:9085":
                Empresa = "https://www.pressa.mx/es-mx/img/products/xl/";
                break;
            case "sprautomotive.servehttp.com:9090":
                Empresa = "https://www.vipla.mx/es-mx/img/products/xl/";
                break;
            case "sprautomotive.servehttp.com:9095":
                Empresa = "https://www.vipla.mx/es-mx/img/products/xl/";
                break;
            case "sprautomotive.servehttp.com:9080":
                Empresa = "https://www.vipla.mx/es-mx/img/products/xl/";
                break;
            case "sprautomotive.servehttp.com:9085":
                Empresa = "https://www.vipla.mx/es-mx/img/products/xl/";
                break;
            case "vazlocolombia.dyndns.org:9085":
                Empresa = "https://www.pressa.mx/es-mx/img/products/xl/";
                break;
        }

        Cliente = preferenceClie.getString("CodeClien", "null");
        Nombre = preferenceClie.getString("NomClien", "null");
        rfc = preferenceClie.getString("RFC", "null");
        plazo = preferenceClie.getString("PLAZO", "null");
        Calle = preferenceClie.getString("Calle", "null");
        Colonia = preferenceClie.getString("Colonia", "null");
        Poblacion = preferenceClie.getString("Poblacion", "null");
        Via = preferenceClie.getString("Via", "null");
        DescPro = preferenceClie.getString("DescPro", "0");
        Desc1 = preferenceClie.getString("Desc1", "0");
        Comentario1 = preferenceClie.getString("Comentario1", "");
        Comentario2 = preferenceClie.getString("Comentario2", "");
        Comentario3 = preferenceClie.getString("Comentario3", "");

        TableProd = findViewById(R.id.ProTable);


        Producto = getIntent().getStringExtra("Producto");

        claveVentana = getIntent().getStringExtra("claveVentana");
        strCantidad = getIntent().getStringExtra("Cantidad");
        if (claveVentana == null) {
            strClave = Producto;
        }
        RecyclerProductos = findViewById(R.id.listExistencias);
        Descripciontxt = findViewById(R.id.Descr);
        Lineatxt = findViewById(R.id.Linea);
        ClaveProdcutotxt = findViewById(R.id.Clave);
        Preciotxt = findViewById(R.id.Precio);
        imageproducto = findViewById(R.id.imageproducto);
        Cantidad =  findViewById(R.id.Canti);
        btnCarShoping =  findViewById(R.id.Add);
        tableLayout =  findViewById(R.id.table);
        tableLayout2 =  findViewById(R.id.table2);

        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor = preferenceClie.edit();

        Aplicaciones = new ArrayList<>();
        RecyclerProductos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        if (strClave == null && strCantidad == null) {
            ListProductosPrecios task = new ListProductosPrecios();
            task.execute();
            Cantidad.setText((strCantidad == null) ? "1" : strCantidad);
        } else if (strClave != null && strCantidad.equals("1")) {
            Producto = strClave;
            ListProductosPrecios task = new ListProductosPrecios();
            task.execute();
            Cantidad.setText((strCantidad == null) ? "1" : strCantidad);
        } else if (strClave != null) {
            Producto = strClave;
            ListProductosPrecios task = new ListProductosPrecios();
            task.execute();
            Cantidad.setText((strCantidad == null) ? "1" : strCantidad);

        } else {
            ListProductosPrecios task = new ListProductosPrecios();
            task.execute();
        }


        btnCarShoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    for (int i = 0; i < listaCarShoping2.size(); i++) {

                        if (listaCarShoping2.get(i).getParte().equals(Producto)) {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(DetalladoProductosActivity.this);
                            alerta.setMessage("Ya cuentas con este producto en tu carrito de compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle(Producto);
                            titulo.show();
                            ban = 0;
                            break;
                        } else {
                            ban = 1;
                        }
                    }
                    if (ban == 1) {

                        String strExistencia = "";
                        int Existencia;
                        listaCarShoping.clear();
                        strCantidad = Cantidad.getText().toString();


                        if (!strCantidad.isEmpty() && !strCantidad.equals("0")) {
                            for (int i = 0; i < Existencias.size(); i++) {
                                if (strcodBra.equals(Existencias.get(i).getClave())) {
                                    strExistencia = Existencias.get(i).getDisponibilidad();
                                    break;
                                }
                            }
                            Existencia = Integer.parseInt(strExistencia);
                            if (Existencia == 0 && ValidaEqui.equals("0")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(DetalladoProductosActivity.this);

                                builder.setTitle("Confirma");
                                builder.setMessage("El " + Producto + " tiene una equivalencia  con el producto " + ProductoEqui + "\n" +
                                        "Deseas utilizar este producto equivalente o deseas conservar el producto sin disponibilidad");

                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        Producto = ProductoEqui;

                                        AsyncCallWS4 task4 = new AsyncCallWS4();
                                        task4.execute();

                                    }
                                });

                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        AsyncCallWS4 task4 = new AsyncCallWS4();
                                        task4.execute();

                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();


                            } else {

                                AsyncCallWS4 task4 = new AsyncCallWS4();
                                task4.execute();

                            }


                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(DetalladoProductosActivity.this);
                            alerta.setMessage("No has ingresado una cantidad").setCancelable(false).setIcon(R.drawable.ic_baseline_error_24).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("¡Error datos!");
                            titulo.show();
                        }

                    }


            }
        });


        Consulta();

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Converciones extends AsyncTask<Void, Void, Void> {

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
            TableProd.setVisibility(View.VISIBLE);
            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listaconversiones.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                if (i == -1) {
                    txtClave = new TextView(getApplicationContext());
                    txtClave.setText("Clave");
                    txtClave.setGravity(Gravity.START);
                    txtClave.setBackgroundColor(Color.BLUE);
                    txtClave.setTextColor(Color.WHITE);
                    txtClave.setPadding(20, 20, 20, 20);
                    txtClave.setLayoutParams(layaoutDes);
                    fila.addView(txtClave);

                    txtClavecompetencia = new TextView(getApplicationContext());
                    txtClavecompetencia.setText("Clave de la Competencia");
                    txtClavecompetencia.setGravity(Gravity.START);
                    txtClavecompetencia.setBackgroundColor(Color.BLUE);
                    txtClavecompetencia.setTextColor(Color.WHITE);
                    txtClavecompetencia.setPadding(20, 20, 20, 20);
                    txtClavecompetencia.setLayoutParams(layaoutDes);
                    fila.addView(txtClavecompetencia);

                    txtnombreCompetencia = new TextView(getApplicationContext());
                    txtnombreCompetencia.setText("Nombre de la Competencia");
                    txtnombreCompetencia.setGravity(Gravity.START);
                    txtnombreCompetencia.setBackgroundColor(Color.BLUE);
                    txtnombreCompetencia.setTextColor(Color.WHITE);
                    txtnombreCompetencia.setPadding(20, 20, 20, 20);
                    txtnombreCompetencia.setLayoutParams(layaoutDes);
                    fila.addView(txtnombreCompetencia);

                } else {
                    multicolor = !multicolor;
                    txtClave = new TextView(getApplicationContext());
                    txtClave.setGravity(Gravity.START);
                    txtClave.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtClave.setText(listaconversiones.get(i).getClave());
                    txtClave.setPadding(20, 20, 20, 20);
                    txtClave.setTextColor(Color.WHITE);
                    txtClave.setLayoutParams(layaoutDes);
                    fila.addView(txtClave);

                    txtClavecompetencia = new TextView(getApplicationContext());
                    txtClavecompetencia.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtClavecompetencia.setGravity(Gravity.START);
                    txtClavecompetencia.setText(listaconversiones.get(i).getClaveCompetencia());
                    txtClavecompetencia.setPadding(20, 20, 20, 20);
                    txtClavecompetencia.setTextColor(Color.WHITE);
                    txtClavecompetencia.setLayoutParams(layaoutDes);
                    fila.addView(txtClavecompetencia);

                    txtnombreCompetencia = new TextView(getApplicationContext());
                    txtnombreCompetencia.setBackgroundColor((multicolor) ? Color.GRAY : Color.BLACK);
                    txtnombreCompetencia.setGravity(Gravity.START);
                    txtnombreCompetencia.setText(listaconversiones.get(i).getNombreCompetencia());
                    txtnombreCompetencia.setPadding(20, 20, 20, 20);
                    txtnombreCompetencia.setTextColor(Color.WHITE);
                    txtnombreCompetencia.setLayoutParams(layaoutDes);
                    fila.addView(txtnombreCompetencia);


                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout2.addView(fila);

            }
            mDialog.dismiss();


        }
    }


    private void conecta2() {
        String SOAP_ACTION = "ConverProdu";
        String METHOD_NAME = "ConverProdu";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConverProdu soapEnvelope = new xmlConverProdu(SoapEnvelope.VER11);
            soapEnvelope.xmlConverPro(strusr, strpass, Producto);
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
                listaconversiones.add(new ConversionesSANDG(
                        (response0.getPropertyAsString("k_ClavePro").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ClavePro")),
                        (response0.getPropertyAsString("k_ClaveCompe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ClaveCompe")),
                        (response0.getPropertyAsString("k_NomCompetencia").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NomCompetencia"))));

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
    private class EquivaProdu extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Equiva();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


            Converciones task1 = new Converciones();
            task1.execute();
            mDialog.dismiss();

        }


    }


    private void Equiva() {
        String SOAP_ACTION = "Equiva";
        String METHOD_NAME = "Equiva";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlEquiva soapEnvelope = new xmlEquiva(SoapEnvelope.VER11);
            soapEnvelope.xmlEquiva(strusr, strpass, Producto, strcodBra);
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
                response0 = (SoapObject) response0.getProperty("MENSAJE");
                MensajePro = response0.getPropertyAsString("k_mensaje");
                ProductoEqui = response0.getPropertyAsString("k_Producto");
                ValidaEqui = response0.getPropertyAsString("k_Val");

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
    private class ListProductosPrecios extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            WebServiceListProductoprecios();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {


            if (!Empresa.equals("https://vazlo.com.mx/assets/img/productos/chica/jpg/")){
                Empresa=Empresa+Producto+"/4.webp";
            }else{
                Empresa=Empresa+Producto+".jpg";

            }


            Picasso.with(context).
                    load(Empresa)
                    .error(R.drawable.ic_baseline_error_24)
                    .fit()
                    .centerInside()
                    .into(imageproducto);

            Descripciontxt.setText(Descripcion);
            Lineatxt.setText(Linea);
            ClaveProdcutotxt.setText(Producto);
            Preciotxt.setText("$" + (Double.parseDouble(PrecioAjustado) == 0 ? formatNumberCurrency(PrecioBase) : formatNumberCurrency(PrecioAjustado)));

            AdapterDetalleExistencia adapter = new AdapterDetalleExistencia(Existencias);
            RecyclerProductos.setAdapter(adapter);

            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < Aplicaciones.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                txtClavePro = new TextView(getApplicationContext());
                if (i == -1) {

                    txtClavePro.setText("Marca");
                    txtClavePro.setGravity(Gravity.START);
                    txtClavePro.setBackgroundColor(Color.BLUE);
                    txtClavePro.setTextColor(Color.WHITE);
                    txtClavePro.setPadding(20, 20, 20, 20);
                    txtClavePro.setLayoutParams(layaoutDes);
                    fila.addView(txtClavePro);


                    txtCant = new TextView(getApplicationContext());
                    txtCant.setText("Modelo");
                    txtCant.setGravity(Gravity.START);
                    txtCant.setBackgroundColor(Color.BLUE);
                    txtCant.setTextColor(Color.WHITE);
                    txtCant.setPadding(20, 20, 20, 20);
                    txtCant.setLayoutParams(layaoutDes);
                    fila.addView(txtCant);


                    txtPrecio = new TextView(getApplicationContext());
                    txtPrecio.setText("Año");
                    txtPrecio.setGravity(Gravity.START);
                    txtPrecio.setBackgroundColor(Color.BLUE);
                    txtPrecio.setTextColor(Color.WHITE);
                    txtPrecio.setPadding(20, 20, 20, 20);
                    txtPrecio.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecio);


                    txtDesc = new TextView(getApplicationContext());
                    txtDesc.setText("Motor");
                    txtDesc.setGravity(Gravity.START);
                    txtDesc.setBackgroundColor(Color.BLUE);
                    txtDesc.setTextColor(Color.WHITE);
                    txtDesc.setPadding(20, 20, 20, 20);
                    txtDesc.setLayoutParams(layaoutDes);
                    fila.addView(txtDesc);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setText("Posición");
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setBackgroundColor(Color.BLUE);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);

                } else {


                    txtClavePro.setBackgroundColor(Color.BLACK);
                    txtClavePro.setGravity(Gravity.START);
                    txtClavePro.setText(Aplicaciones.get(i).getMarca());
                    txtClavePro.setPadding(20, 20, 20, 20);
                    txtClavePro.setTextColor(Color.WHITE);
                    txtClavePro.setLayoutParams(layaoutDes);
                    fila.addView(txtClavePro);

                    txtCant = new TextView(getApplicationContext());
                    txtCant.setBackgroundColor(Color.GRAY);
                    txtCant.setGravity(Gravity.START);
                    txtCant.setText(Aplicaciones.get(i).getModelo());
                    txtCant.setPadding(20, 20, 20, 20);
                    txtCant.setTextColor(Color.WHITE);
                    txtCant.setLayoutParams(layaoutDes);
                    fila.addView(txtCant);

                    txtPrecio = new TextView(getApplicationContext());
                    txtPrecio.setBackgroundColor(Color.BLACK);
                    txtPrecio.setGravity(Gravity.START);
                    txtPrecio.setText(Aplicaciones.get(i).getAno());
                    txtPrecio.setPadding(20, 20, 20, 20);
                    txtPrecio.setTextColor(Color.WHITE);
                    txtPrecio.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecio);

                    txtDesc = new TextView(getApplicationContext());
                    txtDesc.setBackgroundColor(Color.GRAY);
                    txtDesc.setGravity(Gravity.START);
                    txtDesc.setText(Aplicaciones.get(i).getMotor());
                    txtDesc.setPadding(20, 20, 20, 20);
                    txtDesc.setTextColor(Color.WHITE);
                    txtDesc.setLayoutParams(layaoutDes);
                    fila.addView(txtDesc);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setBackgroundColor(Color.BLACK);
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setText(Aplicaciones.get(i).getPosicion());
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);
                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout.addView(fila);
            }

            EquivaProdu task1 = new EquivaProdu();
            task1.execute();
        }

    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    private void WebServiceListProductoprecios() {
        String SOAP_ACTION = "Aplicaciones";
        String METHOD_NAME = "Aplicaciones";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";

        String Marca;
        String Modelo;
        String Ano;
        String Motor;
        String Posicion;

        String clavesuc;
        String existencia;
        String nomsucursal;

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlAplicaciones soapEnvelope = new xmlAplicaciones(SoapEnvelope.VER11);
            soapEnvelope.xmlAplicaciones(strusr, strpass,  Producto ,Cliente);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            response = (SoapObject) response.getProperty("Item");
            response = (SoapObject) response.getProperty("Aplicaciones");



            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty("Item");
                response0 = (SoapObject) response0.getProperty("Aplicaciones");
                response0 = (SoapObject) response0.getProperty(i);

                Marca=(response0.getPropertyAsString("Marca").equals("anyType{}") ? " " : response0.getPropertyAsString("Marca"));
                Modelo=(response0.getPropertyAsString("Modelo").equals("anyType{}") ? " " : response0.getPropertyAsString("Modelo"));
                Motor=(response0.getPropertyAsString("Motor").equals("anyType{}") ? " " : response0.getPropertyAsString("Motor"));
                Ano=(response0.getPropertyAsString("Ano").equals("anyType{}") ? " " : response0.getPropertyAsString("Ano"));
                Posicion=(response0.getPropertyAsString("Posicion").equals("anyType{}") ? " " : response0.getPropertyAsString("Posicion"));
                Aplicaciones.add(new AplicacionesSANDG(Marca, Modelo, Ano,Motor, Posicion));
            }

            SoapObject response1 = (SoapObject) soapEnvelope.bodyIn;
            response1 = (SoapObject) response1.getProperty("Item");
            response1 = (SoapObject) response1.getProperty("Disponibilidad");

           json=response1.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty("Item");
                response0 = (SoapObject) response0.getProperty("Disponibilidad");
                response0 = (SoapObject) response0.getProperty(i);

                clavesuc=(response0.getPropertyAsString("clavesuc").equals("anyType{}") ? " " : response0.getPropertyAsString("clavesuc"));
                existencia=(response0.getPropertyAsString("existencia").equals("anyType{}") ? " " : response0.getPropertyAsString("existencia"));
                nomsucursal=(response0.getPropertyAsString("nomSucursal").equals("anyType{}") ? " " : response0.getPropertyAsString("nomSucursal"));
                Existencias.add(new DisponibilidadSANDG(clavesuc, existencia, nomsucursal));
            }

            response1 = (SoapObject) soapEnvelope.bodyIn;
            response1 = (SoapObject) response1.getProperty("Item");
            response1 = (SoapObject) response1.getProperty("Precios");
            PrecioAjustado=(response1.getPropertyAsString("precio_ajuste").equals("anyType{}") ? " " : response1.getPropertyAsString("precio_ajuste"));
            PrecioBase=(response1.getPropertyAsString("precio_base").equals("anyType{}") ? " " : response1.getPropertyAsString("precio_base"));
            Descripcion=(response1.getPropertyAsString("Descripcion").equals("anyType{}") ? " " : response1.getPropertyAsString("Descripcion"));
            Linea=(response1.getPropertyAsString("Linea").equals("anyType{}") ? " " : response1.getPropertyAsString("Linea"));

        } catch (XmlPullParserException | IOException soapFault) {
            soapFault.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS4 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar4();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            if (preferenceClie.contains("RFC") && preferenceClie.contains("PLAZO")) {

            } else {
                rfc = listaCarShoping.get(0).getRfc();
                plazo = listaCarShoping.get(0).getPlazo();
                Calle = listaCarShoping.get(0).getCalle();
                Colonia = listaCarShoping.get(0).getColonia();
                Poblacion = listaCarShoping.get(0).getPoblacion();
                Via = listaCarShoping.get(0).getVia();
                K87 = listaCarShoping.get(0).getDescPro();
                Desc1fa = listaCarShoping.get(0).getDesc1Fac();
                Comentario1 = listaCarShoping.get(0).getComentario1();
                Comentario2 = listaCarShoping.get(0).getComentario2();
                Comentario3 = listaCarShoping.get(0).getComentario3();


                guardarDatos2();
            }
            try {
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(DetalladoProductosActivity.this, "bd_Carrito", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                for (int i = 0; i < listaCarShoping.size(); i++) {

                    String Cli = listaCarShoping.get(i).getCliente();
                    String Par = listaCarShoping.get(i).getParte();
                    String Exi = listaCarShoping.get(i).getExistencia();
                    String Can = listaCarShoping.get(i).getCantidad();
                    String Uni = listaCarShoping.get(i).getUnidad();
                    String Pre = listaCarShoping.get(i).getPrecio();
                    String Des1 = listaCarShoping.get(i).getDesc1();
                    String Des2 = listaCarShoping.get(i).getDesc2();
                    String Des3 = listaCarShoping.get(i).getDesc3();
                    String Monto = listaCarShoping.get(i).getMonto();
                    String Des = listaCarShoping.get(i).getDescr();

                    db.execSQL("INSERT INTO  carrito (Cliente,Parte,Existencia,Cantidad,Unidad,Precio,Desc1,Desc2,Desc3,Monto,Descri) values ('" + Cli + "','" + Par + "','" + Exi + "','" + Can + "','" + Uni + "','" + Pre + "','" + Des1 + "','" + Des2 + "','" + Des3 + "','" + Monto + "','" + Des + "')");
                }
                db.close();
                Intent carrito = new Intent(DetalladoProductosActivity.this, CarritoComprasActivity.class);
                startActivity(carrito);
                finish();
            } catch (Exception e) {

            }
        }


    }

    private void conectar4() {
        String SOAP_ACTION = "CarShop";
        String METHOD_NAME = "CarShop";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlCarritoVentas soapEnvelope = new xmlCarritoVentas(SoapEnvelope.VER11);
            soapEnvelope.xmlCarritoVen(strusr, strpass, Cliente, Producto, strCantidad, "0", strcodBra);
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
                listaCarShoping.add(new CarritoVentasSANDG(
                        (response0.getPropertyAsString("k_Cliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cliente")),
                        (response0.getPropertyAsString("k_parte").equals("anyType{}") ? " " : response0.getPropertyAsString("k_parte")),
                        (response0.getPropertyAsString("k_exis").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_exis")),
                        (response0.getPropertyAsString("k_Q").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Q")),
                        (response0.getPropertyAsString("k_unidad").equals("anyType{}") ? " " : response0.getPropertyAsString("k_unidad")),
                        (response0.getPropertyAsString("k_precio").equals("anyType{}") ? "" : response0.getPropertyAsString("k_precio")),
                        (response0.getPropertyAsString("k_desc1").equals("anyType{}") ? " " : response0.getPropertyAsString("k_desc1")),
                        (response0.getPropertyAsString("k_desc2").equals("anyType{}") ? " " : response0.getPropertyAsString("k_desc2")),
                        (response0.getPropertyAsString("k_desc3").equals("anyType{}") ? " " : response0.getPropertyAsString("k_desc3")),
                        (response0.getPropertyAsString("k_monto").equals("anyType{}") ? " " : response0.getPropertyAsString("k_monto")),
                        (response0.getPropertyAsString("k_descr").equals("anyType{}") ? " " : response0.getPropertyAsString("k_descr")),
                        (response0.getPropertyAsString("k_rfc").equals("anyType{}") ? " " : response0.getPropertyAsString("k_rfc")),
                        (response0.getPropertyAsString("k_plazo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_plazo")),
                        (response0.getPropertyAsString("k_calle").equals("anyType{}") ? " " : response0.getPropertyAsString("k_calle")),
                        (response0.getPropertyAsString("k_colo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_colo")),
                        (response0.getPropertyAsString("k_pobla").equals("anyType{}") ? " " : response0.getPropertyAsString("k_pobla")),
                        (response0.getPropertyAsString("k_via").equals("anyType{}") ? " " : response0.getPropertyAsString("k_via")),
                        (response0.getPropertyAsString("k_87").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_87")),
                        (response0.getPropertyAsString("k_desc1fac").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_desc1fac")),
                        (response0.getPropertyAsString("k_comentario1").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario1")),
                        (response0.getPropertyAsString("k_comentario2").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario2")),
                        (response0.getPropertyAsString("k_comentario3").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario3"))));


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


    private void guardarDatos2() {

        editor.putString("Nombre", Nombre);
        editor.putString("RFC", rfc);
        editor.putString("PLAZO", plazo);
        editor.putString("Calle", Calle);
        editor.putString("Colonia", Colonia);
        editor.putString("Poblacion", Poblacion);
        editor.putString("Via", Via);
        editor.putString("DescPro", K87);
        editor.putString("Desc1", Desc1fa);
        editor.putString("Comentario1", Comentario1);
        editor.putString("Comentario2", Comentario2);
        editor.putString("Comentario3", Comentario3);
        editor.putString("Vendedor", Vendedor);


        editor.commit();
    }

    private void Consulta() {
        listaCarShoping2 = new ArrayList<>();
        conect = new ConexionSQLiteHelper(DetalladoProductosActivity.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conect.getReadableDatabase();
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("select * from carrito", null);
        if (fila != null && fila.moveToFirst()) {
            do {
                listaCarShoping2.add(new CarritoBD(fila.getInt(0),
                        fila.getString(1),
                        fila.getString(2),
                        fila.getString(3),
                        fila.getString(4),
                        fila.getString(5),
                        fila.getString(6),
                        fila.getString(7),
                        fila.getString(8),
                        fila.getString(9),
                        fila.getString(10),
                        fila.getString(11)));
            } while (fila.moveToNext());
        }
        db.close();

    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        //No se porqué puse lo mismo O.o
        if (count == 0) {
            super.onBackPressed();
        }
        getFragmentManager().popBackStack();

    }
}