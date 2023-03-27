package com.example.kepler201.activities.Carrito;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorCarrito;
import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.CarritoBD;
import com.example.kepler201.SetterandGetter.CarritoVentasSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.SetterandGetter.listExistenciaSANG;
import com.example.kepler201.XMLS.xmlCarritoCompras;
import com.example.kepler201.XMLS.xmlCarritoCompras2;
import com.example.kepler201.XMLS.xmlCarritoVentas;
import com.example.kepler201.XMLS.xmlConsultaProductos;
import com.example.kepler201.XMLS.xmlEquiva;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.XMLS.xmlValidaPedColombia2;
import com.example.kepler201.XMLS.xmlValidaPedMexico2;
import com.example.kepler201.XMLS.xmlVerificacionPrecios;
import com.example.kepler201.activities.DetalladoProductosActivity;
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
import java.util.Objects;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class CarritoComprasActivity extends AppCompatActivity {

    int vald;
    int ban = 0;

    double DescProstr = 0;
    double Descuento = 0;
    double IvaVariado = 0;
    String Clave ="";
    String Empresa="";
    TextView txtClaveCliente;
    TextView txtSubtotal;
    TextView txtSubtotal2;
    TextView txtDescuento;
    TextView txtiva;
    TextView txtMontototal;
    TextView Infor;
    EditText comentario;
    EditText Cantidad123;
    EditText productosEd;

    Button ButtonResCarshop;
    Button ButtonAdd;
    Button ButtonCot;
    Button ButtonCliente;

    RecyclerView recyclerCarrtio;
    ArrayList<listExistenciaSANG> listaExistencia = new ArrayList<>();
    ArrayList<CarritoBD> listaCarShoping = new ArrayList<>();
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    ArrayList<CarritoVentasSANDG> listaCarShoping2 = new ArrayList<>();
    private SharedPreferences preferenceClie;


    ConexionSQLiteHelper conn;


    AlertDialog mDialog;

    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    String strClave = " ";
    String MenValPedi;
    String Desc1Valida = "";
    String DescuentoValida = "";
    String SubtotalValida = "";
    String SeCambiovalida = "";
    String strCantidad, strscliente = "", strscliente2 = "", strscliente3 = "";
    String K87;
    String Desc1fa;
    String Comentario1;
    String Comentario2;
    String Comentario3;
    String MensajePro;
    String ProductoEqui;
    String ValidaEqui;

    String descuEagle;
    String descuRodatech;
    String descuPartec;
    String descuShark;
    String descuTrackone;


    String ExistenciaProd;
    String ClaveProducto="";
    String DescripcionProd;
    String CodBarras;
    String Precios;

    String Producto;
    String Precio;
    String Existencia;
    String Descripcion;
    String Cantidad;


    String ivstr;
    String MontoStr;
    String Comentario;
    String mensaje = "";
    String Cliente="";
    String Nombre;
    String rfc;
    String plazo;
    String Calle;
    String Colonia;
    String Poblacion;
    String Via;
    String DescPro;
    String Desc1;
    String StrFechaActaul;
    String StrFechaVencimiento;
    String productoStr = "";
    String SubdescuentoValida;
    String Mensaje;
    String Documento="";
    String Folio="";
    String DescuentoStr;

    private SharedPreferences.Editor editor;

    Spinner spinerClie;

    Context context = this;

    LinearLayout CliOcul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);
        mDialog = new SpotsDialog.Builder().setContext(CarritoComprasActivity.this).setMessage("Espere un momento...").build();

        MyToolbar.show(this, "Carrito", true);
        recyclerCarrtio = findViewById(R.id.lisCarrito);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor = preferenceClie.edit();
        CliOcul = findViewById(R.id.ClienteLaya);
        ButtonResCarshop = findViewById(R.id.btnRest);
        ButtonAdd = findViewById(R.id.btnAgregar);
        ButtonCot =  findViewById(R.id.btnCotizar);
        txtClaveCliente =  findViewById(R.id.ClaveCliente);
        comentario = findViewById(R.id.edComentario);
        spinerClie =  findViewById(R.id.spinnerClie);
        txtSubtotal = findViewById(R.id.SubTotal);
        txtDescuento = findViewById(R.id.Descuento);
        txtiva = findViewById(R.id.iva);
        Infor = findViewById(R.id.Informacion);
        txtSubtotal2 = findViewById(R.id.SubTotal2);
        txtMontototal =  findViewById(R.id.MontoTotal);
        productosEd =  findViewById(R.id.Producto);
        Cantidad123 =  findViewById(R.id.Canti);
        ButtonCliente = findViewById(R.id.btnClientes);


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

        descuEagle = preferenceClie.getString("Eagle", "");
        descuRodatech = preferenceClie.getString("Rodatech", "");
        descuPartec = preferenceClie.getString("Partech", "");
        descuShark = preferenceClie.getString("Shark", "");
        descuTrackone = preferenceClie.getString("Trackone", "");


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

        Cantidad123.setText("1");

        IvaVariado =  ((!StrServer.equals("vazlocolombia.dyndns.org:9085")) ? 0.16 : 0.19);


        vald = getIntent().getIntExtra("val", 0);
        mostarCliente();
        productosEd.setFocusable(true);
        productosEd.requestFocus();


        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaclientG.size()];

                for (int i = 0; i < listaclientG.size(); i++) {
                    opciones[i] = listaclientG.get(i).getUserCliente() + ":" + listaclientG.get(i).getNombreCliente();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoComprasActivity.this);
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strscliente = listaclientG.get(which).getUserCliente();
                        strscliente3 = listaclientG.get(which).getNombreCliente();
                        Cliente = listaclientG.get(which).getUserCliente();

                        ButtonCliente.setText(listaclientG.get(which).getNombreCliente());
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                productoStr = productosEd.getText().toString();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (!productoStr.isEmpty() && !Cliente.equals("null")) {
                        if (listaCarShoping.size() > 0) {

                            for (int i = 0; i < listaCarShoping.size(); i++) {

                                if (listaCarShoping.get(i).getParte().equals(productoStr)) {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                                    alerta.setMessage("Ya cuentas con este producto en tu carrito de compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    AlertDialog titulo = alerta.create();
                                    titulo.setTitle(productoStr);
                                    titulo.show();
                                    ban = 0;
                                    break;
                                } else {
                                    ban = 1;
                                }
                            }
                            if (ban == 1) {
                                listaExistencia = new ArrayList<>();
                                CarritoComprasActivity.AsyncCallWSProductos task = new CarritoComprasActivity.AsyncCallWSProductos();
                                task.execute();
                            }


                        } else {
                            listaExistencia = new ArrayList<>();
                            CarritoComprasActivity.AsyncCallWSProductos task = new CarritoComprasActivity.AsyncCallWSProductos();
                            task.execute();

                        }
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                    alerta.setMessage("No hay Conexion a Internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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


        ButtonCot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonCot.setEnabled(false);
                mDialog.setMessage("Realizando Cotizacion");
                mDialog.show();

                if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {

                    if (listaCarShoping.size() != 0) {

                        Comentario = comentario.getText().toString();
                        Calendar c = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                        c.add(Calendar.DAY_OF_YEAR, 30);
                        StrFechaVencimiento = dateformatActually.format(c.getTime());
                        CarritoComprasActivity.AsyncCallWS2 task = new CarritoComprasActivity.AsyncCallWS2();
                        task.execute();


                    } else {
                        mDialog.dismiss();
                        ButtonCot.setEnabled(true);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta.setMessage("Agrege productos al Carrito de Compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("No hay productos en el carrito de compras");
                        titulo.show();
                    }
                } else {
                    if (listaCarShoping.size() != 0) {
                        Comentario = comentario.getText().toString();
                        Calendar c = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                        c.add(Calendar.DAY_OF_YEAR, 30);
                        StrFechaVencimiento = dateformatActually.format(c.getTime());
                        CarritoComprasActivity.ValidaPedidoMexico task = new CarritoComprasActivity.ValidaPedidoMexico();
                        task.execute();


                    } else {
                        mDialog.dismiss();
                        ButtonCot.setEnabled(true);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta.setMessage("Agrege productos al Carrito de Compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("No hay productos en el carrito de compras");
                        titulo.show();
                        ButtonCot.setEnabled(true);
                    }
                }

            }
        });

        if (!preferenceClie.contains("CodeClien")) {
            AsyncClientes task1 = new AsyncClientes();
            task1.execute();
        }


        Validacion();
        Consulta();
        Montototal();

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        StrFechaActaul = dateformatActually.format(c.getTime());


    }


    private void guardarDatos() {
        editor.putString("CodeClien", strscliente);
        editor.putString("NomClien", strscliente3);
        editor.commit();
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
        editor.putString("Eagle", descuEagle);
        editor.putString("Rodatech", descuRodatech);
        editor.putString("Partech", descuPartec);
        editor.putString("Shark", descuShark);
        editor.putString("Trackone", descuTrackone);

        editor.commit();
    }



    private void Validacion() {
        if (preferenceClie.contains("CodeClien")) {
            CliOcul.setVisibility(View.GONE);
        } else {
            CliOcul.setVisibility(View.VISIBLE);
        }
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWSProductos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConProd();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            productosEd.requestFocus();
            CarritoComprasActivity.EquivaProdu task5 = new CarritoComprasActivity.EquivaProdu();
            task5.execute();


        }


    }


    private void ConProd() {
        String SOAP_ACTION = "ConsultaProducto";
        String METHOD_NAME = "ConsultaProducto";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlConsultaProductos soapEnvelope = new xmlConsultaProductos(SoapEnvelope.VER11);
            soapEnvelope.xmlConsultaProduc(strusr, strpass, productoStr, strcodBra);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;


            response0 = (SoapObject) response0.getProperty("Item");
            ExistenciaProd = response0.getPropertyAsString("k_Exis").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_Exis");
            ClaveProducto = response0.getPropertyAsString("k_ClavePr").equals("anyType{}") ? "" : response0.getPropertyAsString("k_ClavePr");
            DescripcionProd = response0.getPropertyAsString("k_Descr").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Descr");
            CodBarras = response0.getPropertyAsString("k_CodBarra").equals("anyType{}") ? "" : response0.getPropertyAsString("k_CodBarra");
            Precios = response0.getPropertyAsString("k_Precio").equals("anyType{}") ? "0.00" : response0.getPropertyAsString("k_Precio");


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
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
            productosEd.requestFocus();
            strClave = null;
            if (!ClaveProducto.equals("No se encontraron Datos")) {
                strClave = ClaveProducto;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (preferenceClie.contains("CodeClien")) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        listaCarShoping2.clear();
                        strCantidad = Cantidad123.getText().toString();
                        strscliente2 = preferenceClie.getString("CodeClien", "null");
                        if (!strCantidad.isEmpty() && !strCantidad.equals("0") && !strClave.isEmpty()) {

                            int Diponibilidad = Integer.parseInt(ExistenciaProd);

                            if (Diponibilidad > 0) {
                                CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                task4.execute();

                            } else if (Diponibilidad == 0 && ValidaEqui.equals("0")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoComprasActivity.this);

                                builder.setTitle("Confirma");
                                builder.setMessage("El " + strClave + " tiene una equivalencia  con el producto " + ProductoEqui + "\n" +
                                        "Deseas utilizar este producto equivalente o deseas conservar el producto sin disponibilidad");

                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        strClave = ProductoEqui;
                                        CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                        task4.execute();
                                        dialog.dismiss();

                                    }
                                });

                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                        task4.execute();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                task4.execute();

                            }

                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta.setMessage("No hay Conexion a Internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("!ERROR! CONEXION");
                        titulo.show();

                    }
                } else {
                    if (!strscliente.equals("") && !strscliente3.equals("")) {

                        int Diponibilidad = Integer.parseInt(ExistenciaProd);

                        if (Diponibilidad > 0) {

                            guardarDatos();
                            listaCarShoping2.clear();
                            strCantidad = Cantidad123.getText().toString();
                            strscliente2 = preferenceClie.getString("CodeClien", "null");

                            CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                            task4.execute();

                        } else if (Diponibilidad == 0 && ValidaEqui.equals("0")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(CarritoComprasActivity.this);

                            builder.setTitle("Confirma");
                            builder.setMessage("El " + strClave + " tiene una equivalencia  con el producto " + ProductoEqui + "\n" +
                                    "Deseas utilizar este producto equivalente o deseas conservar el producto sin disponibilidad");

                            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    strClave = ProductoEqui;

                                    guardarDatos();
                                    listaCarShoping2.clear();
                                    strCantidad = Cantidad123.getText().toString();
                                    strscliente2 = preferenceClie.getString("CodeClien", "null");

                                    CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                    task4.execute();
                                    dialog.dismiss();


                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    guardarDatos();
                                    listaCarShoping2.clear();
                                    strCantidad = Cantidad123.getText().toString();
                                    strscliente2 = preferenceClie.getString("CodeClien", "null");

                                    CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                                    task4.execute();
                                    dialog.dismiss();

                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {


                            guardarDatos();
                            listaCarShoping2.clear();
                            strCantidad = Cantidad123.getText().toString();
                            strscliente2 = preferenceClie.getString("CodeClien", "null");

                            CarritoComprasActivity.CarritoCompras task4 = new CarritoComprasActivity.CarritoCompras();
                            task4.execute();

                        }

                    } else {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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


            } else {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta1.setMessage("No se encontro el producto buscado ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Producto no encontrado");
                titulo1.show();
            }


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
            soapEnvelope.xmlEquiva(strusr, strpass, ClaveProducto, strcodBra);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            response0 = (SoapObject) response0.getProperty("MENSAJE");
            MensajePro = response0.getPropertyAsString("k_mensaje");
            ProductoEqui = response0.getPropertyAsString("k_Producto");
            ValidaEqui = response0.getPropertyAsString("k_Val");


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


    @SuppressWarnings({"deprecation", "StatementWithEmptyBody"})
    @SuppressLint("StaticFieldLeak")
    private class CarritoCompras extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("Consultando Productos");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CarritoValidado();
            return null;
        }

        @SuppressWarnings("CatchMayIgnoreException")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            if (preferenceClie.contains("RFC") && preferenceClie.contains("PLAZO")) {

            } else {
                rfc = listaCarShoping2.get(0).getRfc();
                plazo = listaCarShoping2.get(0).getPlazo();
                Calle = listaCarShoping2.get(0).getCalle();
                Colonia = listaCarShoping2.get(0).getColonia();
                Poblacion = listaCarShoping2.get(0).getPoblacion();
                Via = listaCarShoping2.get(0).getVia();
                K87 = listaCarShoping2.get(0).getDescPro();
                Desc1fa = listaCarShoping2.get(0).getDesc1Fac();
                Comentario1 = listaCarShoping2.get(0).getComentario1();
                Comentario2 = listaCarShoping2.get(0).getComentario2();
                Comentario3 = listaCarShoping2.get(0).getComentario3();

                descuEagle = listaCarShoping2.get(0).getEagle();
                descuRodatech = listaCarShoping2.get(0).getRodatech();
                descuPartec = listaCarShoping2.get(0).getPartech();
                descuShark = listaCarShoping2.get(0).getShark();
                descuTrackone = listaCarShoping2.get(0).getTrackoone();
                guardarDatos2();
            }
            try {
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                for (int i = 0; i < listaCarShoping2.size(); i++) {

                    String Cli = listaCarShoping2.get(i).getCliente();
                    String Par = listaCarShoping2.get(i).getParte();
                    String Exi = listaCarShoping2.get(i).getExistencia();
                    String Can = listaCarShoping2.get(i).getCantidad();
                    String Uni = listaCarShoping2.get(i).getUnidad();
                    String Pre = listaCarShoping2.get(i).getPrecio();
                    String Des1 = listaCarShoping2.get(i).getDesc1();
                    String Des2 = listaCarShoping2.get(i).getDesc2();
                    String Des3 = listaCarShoping2.get(i).getDesc3();
                    String Monto = listaCarShoping2.get(i).getMonto();
                    String Des = listaCarShoping2.get(i).getDescr();

                    db.execSQL("INSERT INTO  carrito (Cliente,Parte,Existencia,Cantidad,Unidad,Precio,Desc1,Desc2,Desc3,Monto,Descri) values ('" + Cli + "','" + Par + "','" + Exi + "','" + Can + "','" + Uni + "','" + Pre + "','" + Des1 + "','" + Des2 + "','" + Des3 + "','" + Monto + "','" + Des + "')");
                }
                db.close();
                mDialog.dismiss();
                Intent carrito = new Intent(CarritoComprasActivity.this, CarritoComprasActivity.class);
                carrito.putExtra("val", 2);
                overridePendingTransition(0, 0);
                startActivity(carrito);
                overridePendingTransition(0, 0);
                finish();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }


    }

    private void CarritoValidado() {
        String SOAP_ACTION = "CarShop";
        String METHOD_NAME = "CarShop";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlCarritoVentas soapEnvelope = new xmlCarritoVentas(SoapEnvelope.VER11);
            soapEnvelope.xmlCarritoVen(strusr, strpass, strscliente2, strClave, strCantidad, "0", strcodBra);
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
                listaCarShoping2.add(new CarritoVentasSANDG(
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
                        (response0.getPropertyAsString("k_comentario3").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario3")),
                        (response0.getPropertyAsString("k_descEAGLE").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descEAGLE")),
                        (response0.getPropertyAsString("k_descRODATECH").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descRODATECH")),
                        (response0.getPropertyAsString("k_descPARTECH").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descPARTECH")),
                        (response0.getPropertyAsString("k_descSHARK").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descSHARK")),
                        (response0.getPropertyAsString("k_descTRACKONE").equals("anyType{}") ? "" : response0.getPropertyAsString("k_descTRACKONE"))));


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

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class AsyncClientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Clientes();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
        }


    }

    private void Clientes() {
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
            int json =response.getPropertyCount();
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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            conectar1();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            if (!Documento.equals("") && !Folio.equals("")) {
                mDialog.dismiss();
                AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta.setMessage("Documento = " + Documento + "\n" +
                        "Folio = " + Folio).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);

                        editor.clear();
                        editor.commit();
                        BorrarCarrito();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();

                        dialogInterface.cancel();

                    }


                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle(Mensaje);
                titulo.show();

                ButtonCot.setEnabled(true);

            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta.setMessage("Hubo un problema con la conexion verifique su conexion eh intentelo nuevamente").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Problemas");
                titulo.show();

                ButtonCot.setEnabled(true);

            }


        }


    }

    private void conectar1() {
        String SOAP_ACTION = "NewDoc";
        String METHOD_NAME = "NewDoc";
        String NAMESPACE = "http://" + StrServer + "/WSk80Docs/";
        String URL = "http://" + StrServer + "/WSk80Docs";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlCarritoCompras soapEnvelope = new xmlCarritoCompras(SoapEnvelope.VER11);
            soapEnvelope.xmlCarritoCompras(Comentario, strcode, Nombre, Cliente, StrFechaActaul, StrFechaVencimiento, strcodBra, strusr, strpass, rfc, plazo, MontoStr, ivstr, DescuentoStr, DescPro, Desc1, Calle, Colonia, Poblacion, listaCarShoping, StrServer,Clave,descuEagle,descuRodatech,descuPartec,descuShark,descuTrackone);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;

            Mensaje = (response0.getPropertyAsString("message").equals("anyType{}") ? "" : response0.getPropertyAsString("message"));
            Documento = (response0.getPropertyAsString("doc").equals("anyType{}") ? "" : response0.getPropertyAsString("doc"));
            Folio = (response0.getPropertyAsString("folio").equals("anyType{}") ? "" : response0.getPropertyAsString("folio"));
            


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class CarritoColombia extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            conectarColombia();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


            if (!Documento.equals("") && !Folio.equals("")) {
                mDialog.dismiss();
                AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta.setMessage("Documento = " + Documento + "\n" +
                        "Folio = " + Folio).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);

                        editor.clear();
                        editor.commit();
                        BorrarCarrito();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();

                        dialogInterface.cancel();

                    }


                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle(Mensaje);
                titulo.show();

                ButtonCot.setEnabled(true);

            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta.setMessage("Hubo un problema con la conexion porfavor intentelo nuevamente").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Problemas");
                titulo.show();

                ButtonCot.setEnabled(true);

            }




        }


    }

    private void conectarColombia() {
        String SOAP_ACTION = "NewDoc";
        String METHOD_NAME = "NewDoc";
        String NAMESPACE = "http://" + StrServer + "/WSk80Docs/";
        String URL = "http://" + StrServer + "/WSk80Docs";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlCarritoCompras2 soapEnvelope = new xmlCarritoCompras2(SoapEnvelope.VER11);
            soapEnvelope.xmlCarritoCompras(Comentario, strcode, Nombre, Cliente, StrFechaActaul, StrFechaVencimiento, strcodBra, strusr, strpass, rfc, plazo, MontoStr, ivstr, DescuentoStr, DescPro, Desc1, Calle, Colonia, Poblacion, listaCarShoping, StrServer);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            Mensaje = response0.getPropertyAsString("message");
            Documento = response0.getPropertyAsString("doc");
            Folio = response0.getPropertyAsString("folio");


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
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


            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
            alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    CarritoComprasActivity.VerificacionPrecios task4 = new CarritoComprasActivity.VerificacionPrecios();
                    task4.execute();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Pedido");
            titulo.show();


        }


    }


    private void conectar2() {
        String SOAP_ACTION = "ValidaPedColombia2";
        String METHOD_NAME = "ValidaPedColombia2";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlValidaPedColombia2 soapEnvelope = new xmlValidaPedColombia2(SoapEnvelope.VER11);
            soapEnvelope.xmlValidaPedColombia(strusr, strpass, Cliente, strcode, MontoStr, SubdescuentoValida, Desc1);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            response0 = (SoapObject) response0.getProperty("MENSAJE");
            MenValPedi = response0.getPropertyAsString("k_messenge");
            Desc1Valida = response0.getPropertyAsString("k_desc1");
            DescuentoValida = response0.getPropertyAsString("Descuento");
            SubtotalValida = response0.getPropertyAsString("Subtotal");
            SeCambiovalida = response0.getPropertyAsString("SeCambio");


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class ValidaPedidoMexico extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("Validando su pedido");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ValidaPed();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            mDialog.dismiss();
            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
            alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CarritoComprasActivity.VerificacionPrecios task4 = new CarritoComprasActivity.VerificacionPrecios();
                    task4.execute();


                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Pedido");
            titulo.show();


        }


    }


    private void ValidaPed() {
        String SOAP_ACTION = "ValidaPedMexico2";
        String METHOD_NAME = "ValidaPedMexico2";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlValidaPedMexico2 soapEnvelope = new xmlValidaPedMexico2(SoapEnvelope.VER11);
            soapEnvelope.xmlValidaPedi(strusr, strpass, Cliente, strcode, MontoStr);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            response0 = (SoapObject) response0.getProperty("MENSAJE");
            MenValPedi = response0.getPropertyAsString("k_messenge");


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


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class VerificacionPrecios extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("Generando su Folio");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!StrServer.equals("vazlocolombia.dyndns.org:9085" ) ) {
                Verifica();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {
                CarritoComprasActivity.CarritoColombia task4 = new CarritoComprasActivity.CarritoColombia();
                task4.execute();
            }else{
                CarritoComprasActivity.AsyncCallWS task4 = new CarritoComprasActivity.AsyncCallWS();
                task4.execute();
            }


        }


    }


    private void Verifica() {
        String SOAP_ACTION = "VerificaPrecios";
        String METHOD_NAME = "VerificaPrecios";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";
        String Producto;
        String Precio;


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlVerificacionPrecios soapEnvelope = new xmlVerificacionPrecios(SoapEnvelope.VER11);
            soapEnvelope.xmlVerificacionPrecios(strusr, strpass, Cliente,Desc1,listaCarShoping);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;

            Clave = response0.getPropertyAsString("Clave");
            mensaje =response0.getPropertyAsString("MENSAJE");

            if(!Clave .equals("1") ){

                response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty("iteams");
                int json=response0.getPropertyCount();
                for (int i = 0; i < json; i++) {
                    response0 = (SoapObject) soapEnvelope.bodyIn;
                    response0 = (SoapObject) response0.getProperty("iteams");
                    response0 = (SoapObject) response0.getProperty(i);
                    Producto =response0.getPropertyAsString("Producto");
                    for (int j = 0; j < listaCarShoping.size(); j++) {
                        if (Producto.equals(listaCarShoping.get(j).getParte())){
                            Precio =response0.getPropertyAsString("Precio");
                            listaCarShoping.get(j).setPrecio(Precio);
                            break;
                        }

                    }

                }
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




    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }

    private void Montototal() {
        double Subtotal = 0;
        String Subtotal1;
        for (int i = 0; i < listaCarShoping.size(); i++) {
            Subtotal = Subtotal + Double.parseDouble(listaCarShoping.get(i).getMonto());
        }

        SubdescuentoValida = String.valueOf(Subtotal);

        Subtotal1 = String.valueOf(Subtotal);
        txtSubtotal.setText(Html.fromHtml("Subtotal:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(Subtotal1) + "</font>"));
        DescProstr = Double.parseDouble(Desc1) / 100;
        Descuento = Subtotal * DescProstr;
        DescuentoStr = String.valueOf(Descuento);
        txtDescuento.setText(Html.fromHtml("Descuento:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(DescuentoStr) + "</font>"));

        Double Subtotal2;
        Subtotal2 = Subtotal - Descuento;


        Double ivaCal;
        Double MontoTotal;

        ivaCal = Subtotal2 * IvaVariado;
        MontoTotal = Subtotal2 + ivaCal;
        String SubtotalStr = String.valueOf(Subtotal2);
        ivstr = String.valueOf(ivaCal);
        MontoStr = String.valueOf(MontoTotal);
        txtSubtotal2.setText(Html.fromHtml("SubTotal:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(SubtotalStr) + "</font>"));
        txtiva.setText(Html.fromHtml("Iva:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(ivstr) + "</font>"));
        txtMontototal.setText(Html.fromHtml("Total:<font color=#000000>$</font><font color=#FF0000>" + formatNumberCurrency(MontoStr) + "</font>"));

    }


    public void Actualizar(final View view) {
        final int position = recyclerCarrtio.getChildAdapterPosition(Objects.requireNonNull(recyclerCarrtio.findContainingItemView(view)));
        String prod = listaCarShoping.get(position).getParte();
        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
        alerta.setMessage("¿Deseas modificar este producto?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (listaCarShoping.size() > 1) {
                    if (listaCarShoping.size() >= 3) {
                        int posiEnd = listaCarShoping.size() - 1;
                        if (position == 0) {
                            String Parte1 = listaCarShoping.get(position).getParte();
                            String Parte2 = listaCarShoping.get(position + 1).getParte();
                            double precio = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                            if (Parte1.equals(Parte2) && precio == 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(position + 1).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);

                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();
                                int ID = listaCarShoping.get(position).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        } else if (position == posiEnd) {
                            String Parte1 = listaCarShoping.get(position).getParte();
                            String Parte2 = listaCarShoping.get(position - 1).getParte();
                            double precio = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                            double precio1 = Double.parseDouble(listaCarShoping.get(position - 1).getPrecio());
                            if (Parte1.equals(Parte2) && precio == 0 && precio1 > 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(position - 1).getID();


                                Producto = listaCarShoping.get(position - 1).getParte();
                                Precio = listaCarShoping.get(position - 1).getPrecio();
                                Existencia = listaCarShoping.get(position - 1).getExistencia();
                                Descripcion = listaCarShoping.get(position - 1).getDescr();
                                Cantidad = listaCarShoping.get(position - 1).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();
                                int ID = listaCarShoping.get(position).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        } else {
                            String centro = listaCarShoping.get(position).getParte();
                            String abajo = listaCarShoping.get(position + 1).getParte();
                            String arriba = listaCarShoping.get(position - 1).getParte();
                            double precioCen = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                            double precioArr = Double.parseDouble(listaCarShoping.get(position - 1).getPrecio());
                            double precioAbajo = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                            if (centro.equals(arriba) && precioCen == 0 && precioArr > 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(position - 1).getID();

                                Producto = listaCarShoping.get(position - 1).getParte();
                                Precio = listaCarShoping.get(position - 1).getPrecio();
                                Existencia = listaCarShoping.get(position - 1).getExistencia();
                                Descripcion = listaCarShoping.get(position - 1).getDescr();
                                Cantidad = listaCarShoping.get(position - 1).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else if (centro.equals(abajo) && precioAbajo == 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(position + 1).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);
                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();
                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        }
                    } else {
                        if (position == 0) {
                            String PartePrec = listaCarShoping.get(position).getParte();
                            String ParAbajo = listaCarShoping.get(position + 1).getParte();
                            double PrecioAbajo = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                            if (PartePrec.equals(ParAbajo) && PrecioAbajo == 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();


                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(position + 1).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();

                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();

                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();
                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        } else if (position == 1) {
                            String PartePrec = listaCarShoping.get(position).getParte();
                            double PrecioPrec = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                            String ParArriba = listaCarShoping.get(0).getParte();
                            double PrecioArriba = Double.parseDouble(listaCarShoping.get(0).getPrecio());
                            if (PartePrec.equals(ParArriba) && PrecioArriba > 0 && PrecioPrec == 0) {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                int ID2 = listaCarShoping.get(0).getID();

                                Producto = listaCarShoping.get(0).getParte();
                                Precio = listaCarShoping.get(0).getPrecio();
                                Existencia = listaCarShoping.get(0).getExistencia();
                                Descripcion = listaCarShoping.get(0).getDescr();
                                Cantidad = listaCarShoping.get(0).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.delete("carrito", "ID=" + ID2, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();
                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                                SQLiteDatabase db = conn.getReadableDatabase();

                                int ID = listaCarShoping.get(position).getID();
                                Producto = listaCarShoping.get(position).getParte();
                                Precio = listaCarShoping.get(position).getPrecio();
                                Existencia = listaCarShoping.get(position).getExistencia();
                                Descripcion = listaCarShoping.get(position).getDescr();
                                Cantidad = listaCarShoping.get(position).getCantidad();

                                db.delete("carrito", "ID=" + ID, null);
                                db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                                db.close();
                                Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                                Modificar.putExtra("usuario", strusr);
                                Modificar.putExtra("pasword", strpass);
                                Modificar.putExtra("name", strname);
                                Modificar.putExtra("lname", strlname);
                                Modificar.putExtra("type", strtype);
                                Modificar.putExtra("branch", strbran);
                                Modificar.putExtra("email", strma);
                                Modificar.putExtra("codBra", strcodBra);
                                Modificar.putExtra("code", strcode);
                                Modificar.putExtra("Server", StrServer);


                                Modificar.putExtra("Producto", Producto);
                                Modificar.putExtra("Precio", Precio);
                                Modificar.putExtra("Existencia", Existencia);
                                Modificar.putExtra("Descripcion", Descripcion);
                                Modificar.putExtra("Cantidad", Cantidad);
                                overridePendingTransition(0, 0);
                                startActivity(Modificar);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        }
                    }
                } else {
                    conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                    SQLiteDatabase db = conn.getReadableDatabase();

                    int ID = listaCarShoping.get(position).getID();

                    Producto = listaCarShoping.get(position).getParte();
                    Precio = listaCarShoping.get(position).getPrecio();
                    Existencia = listaCarShoping.get(position).getExistencia();
                    Descripcion = listaCarShoping.get(position).getDescr();
                    Cantidad = listaCarShoping.get(position).getCantidad();

                    db.delete("carrito", "ID=" + ID, null);
                    db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                    db.close();

                    Intent Modificar = new Intent(CarritoComprasActivity.this, DetalladoProductosActivity.class);
                    Modificar.putExtra("usuario", strusr);
                    Modificar.putExtra("pasword", strpass);
                    Modificar.putExtra("name", strname);
                    Modificar.putExtra("lname", strlname);
                    Modificar.putExtra("type", strtype);
                    Modificar.putExtra("branch", strbran);
                    Modificar.putExtra("email", strma);
                    Modificar.putExtra("codBra", strcodBra);
                    Modificar.putExtra("code", strcode);
                    Modificar.putExtra("Server", StrServer);


                    Modificar.putExtra("Producto", Producto);
                    Modificar.putExtra("Precio", Precio);
                    Modificar.putExtra("Existencia", Existencia);
                    Modificar.putExtra("Descripcion", Descripcion);
                    Modificar.putExtra("Cantidad", Cantidad);
                    overridePendingTransition(0, 0);
                    startActivity(Modificar);
                    overridePendingTransition(0, 0);
                    finish();
                }

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CarritoComprasActivity.this, "El movimiento a sido Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("Modificar:" + prod);
        titulo.show();


    }


    public void Delete(View view) {
        int position = recyclerCarrtio.getChildAdapterPosition(Objects.requireNonNull(recyclerCarrtio.findContainingItemView(view)));


        if (listaCarShoping.size() > 1) {
            if (listaCarShoping.size() >= 3) {
                int posiEnd = listaCarShoping.size() - 1;
                if (position == 0) {
                    String Parte1 = listaCarShoping.get(position).getParte();
                    String Parte2 = listaCarShoping.get(position + 1).getParte();
                    double precio = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                    if (Parte1.equals(Parte2) && precio == 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(position + 1).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();
                        int ID = listaCarShoping.get(position).getID();
                        db.delete("carrito", "ID=" + ID, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();


                    }
                } else if (position == posiEnd) {
                    String Parte1 = listaCarShoping.get(position).getParte();
                    String Parte2 = listaCarShoping.get(position - 1).getParte();
                    double precio = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                    double precio1 = Double.parseDouble(listaCarShoping.get(position - 1).getPrecio());
                    if (Parte1.equals(Parte2) && precio == 0 && precio1 > 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(position - 1).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();
                        int ID = listaCarShoping.get(position).getID();
                        db.delete("carrito", "ID=" + ID, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    }
                } else {
                    String centro = listaCarShoping.get(position).getParte();
                    String abajo = listaCarShoping.get(position + 1).getParte();
                    String arriba = listaCarShoping.get(position - 1).getParte();
                    double precioCen = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                    double precioArr = Double.parseDouble(listaCarShoping.get(position - 1).getPrecio());
                    double precioAbajo = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                    if (centro.equals(arriba) && precioCen == 0 && precioArr > 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(position - 1).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else if (centro.equals(abajo) && precioAbajo == 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(position + 1).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        db.delete("carrito", "ID=" + ID, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
            } else {
                if (position == 0) {
                    String PartePrec = listaCarShoping.get(position).getParte();
                    String ParAbajo = listaCarShoping.get(position + 1).getParte();
                    double PrecioAbajo = Double.parseDouble(listaCarShoping.get(position + 1).getPrecio());
                    if (PartePrec.equals(ParAbajo) && PrecioAbajo == 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(position + 1).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();

                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        db.delete("carrito", "ID=" + ID, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    }
                } else if (position == 1) {
                    String PartePrec = listaCarShoping.get(position).getParte();
                    double PrecioPrec = Double.parseDouble(listaCarShoping.get(position).getPrecio());
                    String ParArriba = listaCarShoping.get(0).getParte();
                    double PrecioArriba = Double.parseDouble(listaCarShoping.get(0).getPrecio());
                    if (PartePrec.equals(ParArriba) && PrecioArriba > 0 && PrecioPrec == 0) {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        int ID2 = listaCarShoping.get(0).getID();

                        db.delete("carrito", "ID=" + ID, null);
                        db.delete("carrito", "ID=" + ID2, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();

                        int ID = listaCarShoping.get(position).getID();
                        db.delete("carrito", "ID=" + ID, null);
                        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
                        db.close();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
            }
        } else {
            conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();
            int ID = listaCarShoping.get(position).getID();
            db.delete("carrito", "ID=" + ID, null);
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
            db.close();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            finish();
        }
    }


    private void BorrarCarrito() {
        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        db.delete("carrito", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
        db.close();
    }

    private void Consulta() {
        recyclerCarrtio.setLayoutManager(new LinearLayoutManager(this));
        listaCarShoping = new ArrayList<>();
        conn = new ConexionSQLiteHelper(CarritoComprasActivity.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("select * from carrito ORDER BY Parte ", null);
        if (fila != null && fila.moveToFirst()) {
            do {
                listaCarShoping.add(new CarritoBD(fila.getInt(0),
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
        AdaptadorCarrito adapter = new AdaptadorCarrito(listaCarShoping,Desc1,StrServer,context,Empresa);
        recyclerCarrtio.setAdapter(adapter);
        db.close();

    }


    @SuppressLint("SetTextI18n")
    private void mostarCliente() {
        Cliente = preferenceClie.getString("CodeClien", "null");
        Nombre = preferenceClie.getString("NomClien", "null");
        txtClaveCliente.setText(Html.fromHtml((Cliente.equals("null") ? "No hay Cliente" : "<font color = #FF0000>" + Cliente + "</font> <br>") + (Nombre.equals("null") ? "" : Nombre)));

        if (!Cliente.equals("null")) {
            Infor.setVisibility(View.VISIBLE);
            Infor.setText(Html.fromHtml("Calle:" + Calle + "<br>" +
                    "Colonia:" + Colonia + "<br>" +
                    "Poblacion:" + Poblacion + "<br>" +
                    "Descuento:" + Desc1));

            comentario.setText(Comentario1 + "\n" + " " + Comentario2 + "\n" + " " + Comentario2 + "\n" + " " + Comentario3);

        } else {
            Infor.setVisibility(View.GONE);
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuflow6, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.back) {
            if (vald == 2) {
                Intent BackContuPro = new Intent(this, ActivityConsultaProductos.class);
                overridePendingTransition(0, 0);
                startActivity(BackContuPro);
                overridePendingTransition(0, 0);
                finish();
            } else if (vald == 3) {
                Intent BackContuPro = new Intent(this, ActivityConverciones.class);
                overridePendingTransition(0, 0);
                startActivity(BackContuPro);
                overridePendingTransition(0, 0);
                finish();

            } else if (vald == 1) {
                Intent BackContuPro = new Intent(this, ActivityScreenFirst.class);
                overridePendingTransition(0, 0);
                startActivity(BackContuPro);
                overridePendingTransition(0, 0);
                finish();
            }
        } else if (id == R.id.ConsultarPro) {
            Intent SearchPro = new Intent(CarritoComprasActivity.this, ActivityConsultaProductos.class);
            overridePendingTransition(0, 0);
            startActivity(SearchPro);
            overridePendingTransition(0, 0);
            finish();
        } else */
        if (id == R.id.ReiniCar) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
            alerta.setMessage("¿Desea reiniciar el carrito de compras? \n" +
                    "Se borrara todo lo que tenga").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.clear();
                    editor.commit();
                    BorrarCarrito();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Reiniciar el Carrito");
            titulo.show();

        }

        return super.onOptionsItemSelected(item);

    }
}