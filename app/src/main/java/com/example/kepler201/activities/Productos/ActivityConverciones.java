package com.example.kepler201.activities.Productos;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.CarritoBD;
import com.example.kepler201.SetterandGetter.CarritoVentasSANDG;
import com.example.kepler201.SetterandGetter.ConversionesSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.SetterandGetter.listExistencia2SANG;
import com.example.kepler201.XMLS.xmlCarritoVentas;
import com.example.kepler201.XMLS.xmlConver;
import com.example.kepler201.XMLS.xmlConverProdu;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.includes.MyToolbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

public class ActivityConverciones extends AppCompatActivity {

    TextView txtclaveCom, txtclave, txtdescripcion, txtCodBar, txtPrecio;

    ImageView imageIv, SearchProducto, Existencia, ListaPrecios, Conversiones;
    EditText productosEd;
    Button btnSearch;
    Button btnCarShoping;
    Button btnEscaner;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    String strCompetencia = "", strClave = "", strDesc = "", strCodeBar = "", strPrecio = "";
    String EmpresaEd;
    AlertDialog mDialog;
    EditText Cantidad;
    String mensaje = "";
    int ban = 0;
    ConexionSQLiteHelper conect;
    ArrayList<ConversionesSANDG> listaconversiones = new ArrayList<>();
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<listExistencia2SANG> listaExistencia = new ArrayList<>();
    ArrayList<CarritoVentasSANDG> listaCarShoping = new ArrayList<>();
    ArrayList<CarritoBD> listaCarShoping2 = new ArrayList<>();
    private SharedPreferences preferenceClie;
    private SharedPreferences.Editor editor;
    String rfc;
    String plazo;
    String Calle;
    String Colonia;
    String Poblacion;
    String Via;
    String K87;
    String Desc1fa;
    String Comentario1;
    String Comentario2;
    String Comentario3;

    String productoStr = "";
    private TableLayout tableLayout;
    TableRow fila;
    private boolean multicolor = true;
    LinearLayout TableConv;

    int n = 2000;
    String[] search2 = new String[n];

    private Spinner spinerClie, spinnerExistenias;
    String strCantidad, strscliente, strscliente2, strscliente3;
    LinearLayout CliOcul;

    TextView txtClave, txtClavecompetencia, txtnombreCompetencia;
    String Empresa;
    String EmpresaEd1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converciones);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);


        editor = preference.edit();
        CliOcul =  findViewById(R.id.ClienteLaya);
        productosEd =  findViewById(R.id.Producto);
        btnEscaner =  findViewById(R.id.Escaner);
        btnEscaner.setOnClickListener(mOnClickListener);

        mDialog = new SpotsDialog.Builder().setContext(ActivityConverciones.this).setMessage("Espere un momento...").build();

        MyToolbar.show(this, "Productos-Converciónes", true);
        imageIv =  findViewById(R.id.productoImag);
        btnSearch =  findViewById(R.id.btnSearch);
        TableConv =  findViewById(R.id.ConvTable);

        btnCarShoping =  findViewById(R.id.Add);
        txtclaveCom =  findViewById(R.id.txtComptencia);
        txtclave =  findViewById(R.id.txtProducto);
        txtdescripcion =  findViewById(R.id.txtDescripcion);
        txtCodBar =  findViewById(R.id.txtCodeBar);
        txtPrecio =  findViewById(R.id.txtPrecio);
        Cantidad =  findViewById(R.id.Canti);
        SearchProducto =  findViewById(R.id.SearchProducto);
        Existencia =  findViewById(R.id.Existencia);
        ListaPrecios =  findViewById(R.id.ListaPrecios);
        Conversiones =  findViewById(R.id.Conversiones);
        tableLayout =  findViewById(R.id.table);
        spinerClie =  findViewById(R.id.spinnerClie);
        spinnerExistenias = findViewById(R.id.spinnerExistencia);
        Cantidad.setText("1");


        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor = preferenceClie.edit();

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
        EmpresaEd=Empresa;

        btnCarShoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (preferenceClie.contains("CodeClien")) {

                        listaCarShoping.clear();

                        strCantidad = Cantidad.getText().toString();
                        strscliente2 = preferenceClie.getString("CodeClien", "null");
                        if (!strCantidad.isEmpty() && !strCantidad.equals("0")) {

                            if (listaCarShoping2.size() > 0) {

                                for (int i = 0; i < listaCarShoping2.size(); i++) {

                                    if (listaCarShoping2.get(i).getParte().equals(strClave)) {
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
                                        alerta.setMessage("Ya cuentas con este producto en tu carrito de compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });

                                        AlertDialog titulo = alerta.create();
                                        titulo.setTitle(strClave);
                                        titulo.show();
                                        ban = 0;
                                        break;
                                    } else {
                                        ban = 1;
                                    }
                                }
                                if (ban == 1) {
                                    listaExistencia = new ArrayList<>();
                                    ActivityConverciones.AsyncCallWS4 task4 = new ActivityConverciones.AsyncCallWS4();
                                    task4.execute();
                                }


                            } else {
                                listaExistencia = new ArrayList<>();
                                ActivityConverciones.AsyncCallWS4 task4 = new ActivityConverciones.AsyncCallWS4();
                                task4.execute();

                            }


                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
                            alerta.setMessage("Ingrese datos faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                    if (spinerClie.getSelectedItemPosition() != 0) {

                        for (int i = 0; i < search2.length; i++) {
                            int posi = spinerClie.getSelectedItemPosition();
                            if (posi == i) {
                                strscliente = search2[i];
                                strscliente3 = listaclientG.get(i - 1).getNombreCliente();
                                break;
                            }
                        }
                        guardarDatos();
                        listaCarShoping.clear();
                        strCantidad = Cantidad.getText().toString();
                        strscliente2 = preferenceClie.getString("CodeClien", "null");
                        ActivityConverciones.AsyncCallWS4 task4 = new ActivityConverciones.AsyncCallWS4();
                        task4.execute();


                    } else {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConverciones.this);
                        alerta1.setMessage("Seleccione un cliente ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Campo Vacio");
                        titulo1.show();

                    }
                }

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productoStr = productosEd.getText().toString();

                    if (!productoStr.isEmpty()) {
                        listaExistencia = new ArrayList<>();
                        spinnerExistenias.setAdapter(null);
                        tableLayout.removeAllViews();
                        listaconversiones.clear();
                        ActivityConverciones.AsyncCallWS task = new ActivityConverciones.AsyncCallWS();
                        task.execute();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
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


        SearchProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchProducto.setBackgroundColor(Color.GRAY);
                SearchProducto.setBackgroundColor(Color.TRANSPARENT);
                Intent SearchPro = new Intent(ActivityConverciones.this, ActivityConsultaProductos.class);
                overridePendingTransition(0, 0);
                startActivity(SearchPro);
                overridePendingTransition(0, 0);
                finish();
            }
        });
/*
        Existencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Existencia.setBackgroundColor(Color.GRAY);
                Existencia.setBackgroundColor(Color.TRANSPARENT);
                Intent Existenc = new Intent(ActivityConverciones.this, ActivityExistenciaProduc.class);
                overridePendingTransition(0, 0);
                startActivity(Existenc);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        ListaPrecios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaPrecios.setBackgroundColor(Color.GRAY);
                ListaPrecios.setBackgroundColor(Color.TRANSPARENT);
                Intent Listpr = new Intent(ActivityConverciones.this, ListaPreciosActivity.class);
                overridePendingTransition(0, 0);
                startActivity(Listpr);
                overridePendingTransition(0, 0);
                finish();

            }
        });*/
        Conversiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conversiones.setBackgroundColor(Color.GRAY);
                Conversiones.setBackgroundColor(Color.TRANSPARENT);
                Intent Conver = new Intent(ActivityConverciones.this, ActivityConverciones.class);
                overridePendingTransition(0, 0);
                startActivity(Conver);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        Consulta();
        Validacion();
        ActivityConverciones.AsyncCallWS3 task = new ActivityConverciones.AsyncCallWS3();
        task.execute();

    }


    private void guardarDatos() {
        editor.putString("CodeClien", strscliente);
        editor.putString("NomClien", strscliente3);
        editor.commit();
    }

    private void Validacion() {
        if (preferenceClie.contains("CodeClien")) {
            CliOcul.setVisibility(View.GONE);
        } else {
            CliOcul.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                productosEd.setText(result.getContents());
                productoStr = productosEd.getText().toString();
                tableLayout.removeAllViews();
                listaconversiones.clear();
                ActivityConverciones.AsyncCallWS task = new ActivityConverciones.AsyncCallWS();
                task.execute();
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
                alerta.setMessage("No se pudo escanear el codigo").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("!ERROR!");
                titulo.show();
            }
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.Escaner) {
                new IntentIntegrator(ActivityConverciones.this).initiateScan();
            }
        }
    };

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
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

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


            String[] opciones = new String[listaExistencia.size()];

            for (int i = 0; i < listaExistencia.size(); i++) {
                opciones[i] = listaExistencia.get(i).getNombre() + " : " + listaExistencia.get(i).getExistencia() + "PZA";
            }

            strClave = null;
            strCompetencia = null;
            strDesc = null;
            strCodeBar = null;
            strPrecio = null;
            if (listaExistencia.size() > 0) {
                strClave = listaExistencia.get(0).getClaveProdu();
                strCompetencia = listaExistencia.get(0).getClaveCompetecia();
                strDesc = listaExistencia.get(0).getDescr();
                strCodeBar = listaExistencia.get(0).getCodBarras();
                strPrecio = listaExistencia.get(0).getPrecio();

            }


            if (strClave != null) {
                ActivityConverciones.AsyncCallWS2 task = new ActivityConverciones.AsyncCallWS2();
                task.execute();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
                spinnerExistenias.setAdapter(adapter);
                txtclaveCom.setText("Competencia:" + strCompetencia);
                txtclave.setText("Codigo: " + strClave);
                txtdescripcion.setText("Description: \n" + strDesc);
                txtCodBar.setText(strCodeBar);
                txtPrecio.setText("$" + formatNumberCurrency(strPrecio));


                    Empresa=EmpresaEd+strClave+"/4.webp";

                Picasso.with(getApplicationContext()).
                        load(Empresa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageIv);
                productosEd.setText("");
                mDialog.dismiss();


            } else {
                TableConv.setVisibility(View.GONE);
                txtclaveCom.setText("");
                txtclave.setText("");
                txtdescripcion.setText("");
                txtCodBar.setText("");
                txtPrecio.setText("$00.0");
                opciones = new String[]{"No hay existencia del producto seleccionado"};
                strClave = "";
                strDesc = "";
                strCodeBar = "";
                strPrecio = "";
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
                spinnerExistenias.setAdapter(adapter);

                if (!EmpresaEd.equals("https://vazlo.com.mx/assets/img/productos/chica/jpg/")){
                    Empresa="";
                    Empresa=EmpresaEd+strClave+"/4.webp";
                }else{
                    Empresa="";
                    Empresa=EmpresaEd+strClave+".jpg";

                }

                Picasso.with(getApplicationContext()).
                        load(Empresa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageIv);
                productosEd.setText("");
                mDialog.dismiss();

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
                alerta.setMessage(mensaje).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("!ERROR!");
                titulo.show();
            }
        }


    }


    private void conectar() {
        String SOAP_ACTION = "Conver";
        String METHOD_NAME = "Conver";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConver soapEnvelope = new xmlConver(SoapEnvelope.VER11);
            soapEnvelope.xmlConver(strusr, strpass, productoStr);
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
                listaExistencia.add(new listExistencia2SANG(
                        (response0.getPropertyAsString("k_ClaveSu").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ClaveSu")),
                        (response0.getPropertyAsString("k_NomSuc").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NomSuc")),
                        (response0.getPropertyAsString("k_Exis").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_Exis")),
                        (response0.getPropertyAsString("k_ClavePr").equals("anyType{}") ? " " : response0.getPropertyAsString("k_ClavePr")),
                        (response0.getPropertyAsString("k_Clave").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Clave")),
                        (response0.getPropertyAsString("k_Descr").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Descr")),
                        (response0.getPropertyAsString("k_CodBarra").equals("anyType{}") ? " " : response0.getPropertyAsString("k_CodBarra")),
                        (response0.getPropertyAsString("k_Precio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Precio"))));
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
            mensaje = "Error:No se encontro el producto";
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
            TableConv.setVisibility(View.VISIBLE);
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
                tableLayout.addView(fila);

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
            soapEnvelope.xmlConverPro(strusr, strpass, strClave);
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
                listaconversiones.add(new ConversionesSANDG(response0.getPropertyAsString("k_ClavePro"), response0.getPropertyAsString("k_ClaveCompe"), response0.getPropertyAsString("k_NomCompetencia")));


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
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar3();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            String[] opciones = new String[listaclientG.size() + 1];
            opciones[0] = "Cliente";
            search2[0] = "Cliente";
            for (int i = 1; i <= listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i - 1).getUserCliente() + ":" + listaclientG.get(i - 1).getNombreCliente();
                search2[i] = listaclientG.get(i - 1).getUserCliente();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinerClie.setAdapter(adapter);
        }


    }

    private void conectar3() {
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
                listaclientG.add(new SearachClientSANDG(response0.getPropertyAsString("k_dscr"), response0.getPropertyAsString("k_line")));


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

        editor.commit();
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

            try {
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(ActivityConverciones.this, "bd_Carrito", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();


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
                Intent carrito = new Intent(ActivityConverciones.this, CarritoComprasActivity.class);
                carrito.putExtra("val", 3);
                startActivity(carrito);
                db.close();
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConverciones.this);
                alerta.setMessage("Se agrego el producto al carrito").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Producto agregado");
                titulo.show();
            } catch (Exception ignored) {

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
            soapEnvelope.xmlCarritoVen(strusr, strpass, strscliente2, strClave, strCantidad, "11", strcodBra);
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
                listaCarShoping.add(new CarritoVentasSANDG((response0.getPropertyAsString("k_Cliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cliente")),
                        (response0.getPropertyAsString("k_parte").equals("anyType{}") ? " " : response0.getPropertyAsString("k_parte")),
                        (response0.getPropertyAsString("k_exis").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_exis")),
                        (response0.getPropertyAsString("k_Q").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Q")),
                        (response0.getPropertyAsString("k_unidad").equals("anyType{}") ? " " : response0.getPropertyAsString("k_unidad")),
                        (response0.getPropertyAsString("k_precio").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_precio")),
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


    private void Consulta() {
        listaCarShoping2 = new ArrayList<>();
        conect = new ConexionSQLiteHelper(ActivityConverciones.this, "bd_Carrito", null, 1);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuflow5, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.CarrComp) {
            Intent Shoping = new Intent(this, CarritoComprasActivity.class);
            Shoping.putExtra("val", 3);
            startActivity(Shoping);
            finish();

        }


        return super.onOptionsItemSelected(item);

    }

}