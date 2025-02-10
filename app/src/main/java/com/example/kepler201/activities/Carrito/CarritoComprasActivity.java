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
import com.example.kepler201.activities.DetalladoProductosActivity;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    String Clave = "";
    String Empresa = "";
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
    String ClaveProducto = "";
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
    String Cliente = "";
    String Nombre;
    String rfc;
    String plazo;
    String Calle;
    String Colonia;
    String Poblacion;
    String Via;
    String DescPro;
    String Desc1="0";
    String StrFechaActaul;
    String StrFechaVencimiento;
    String productoStr = "";
    String SubdescuentoValida;
    String Mensaje;
    String Documento = "";
    String Folio = "";
    String DescuentoStr;

    private SharedPreferences.Editor editor;

    Spinner spinerClie;

    Context context = this;

    LinearLayout CliOcul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);
        mDialog = new SpotsDialog(CarritoComprasActivity.this);
        mDialog.setCancelable(false);
        MyToolbar.show(this, "Carrito", true);
        recyclerCarrtio = findViewById(R.id.lisCarrito);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor = preferenceClie.edit();
        CliOcul = findViewById(R.id.ClienteLaya);
        ButtonResCarshop = findViewById(R.id.btnRest);
        ButtonAdd = findViewById(R.id.btnAgregar);
        ButtonCot = findViewById(R.id.btnCotizar);
        txtClaveCliente = findViewById(R.id.ClaveCliente);
        comentario = findViewById(R.id.edComentario);
        spinerClie = findViewById(R.id.spinnerClie);
        txtSubtotal = findViewById(R.id.SubTotal);
        txtDescuento = findViewById(R.id.Descuento);
        txtiva = findViewById(R.id.iva);
        Infor = findViewById(R.id.Informacion);
        txtSubtotal2 = findViewById(R.id.SubTotal2);
        txtMontototal = findViewById(R.id.MontoTotal);
        productosEd = findViewById(R.id.Producto);
        Cantidad123 = findViewById(R.id.Canti);
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
                Empresa = "https://www.jacve.mx/tools/pictures-urlProductos?ids=";
                break;
            case "autodis.ath.cx:9085":
                Empresa = "https://www.autodis.mx/es-mx/img/products/xl/";
                break;
            case "cecra.ath.cx:9085":
                Empresa = "https://www.cecra.mx/tools/pictures-urlProductos?ids=";
                break;
            case "guvi.ath.cx:9085":
                Empresa = "https://www.guvi.mx/tools/pictures-urlProductos?ids=";
                break;
            case "cedistabasco.ddns.net:9085":
                Empresa = "https://www.pressa.mx/es-mx/img/products/xl/";
                break;
            case "sprautomotive.servehttp.com:9090":
            case "sprautomotive.servehttp.com:9095":
            case "sprautomotive.servehttp.com:9080":
            case "sprautomotive.servehttp.com:9085":
                Empresa = "https://www.vipla.mx/tools/pictures-urlProductos?ids=";
                break;
            case "vazlocolombia.dyndns.org:9085":
                Empresa = "https://vazlo.com.mx/assets/img/productos/chica/jpg/";
                break;
            default:
                Empresa = "https://www.pressa.mx/es-mx/img/products/xl/";
                break;
        }

        Cantidad123.setText("1");

        IvaVariado = ((!StrServer.equals("vazlocolombia.dyndns.org:9085")) ? 0.16 : 0.19);


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
ButtonAdd.setEnabled(false);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                productoStr = productosEd.getText().toString();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (!productoStr.isEmpty() && !Cliente.equals("null") && !Cantidad123.getText().toString().equals("")) {
                        if (listaCarShoping.size() > 0) {

                            for (int i = 0; i < listaCarShoping.size(); i++) {

                                if (listaCarShoping.get(i).getParte().equals(productoStr)) {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                                    alerta.setMessage("Ya cuentas con este producto en tu carrito de compras").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            mDialog.dismiss();
                                            ButtonAdd.setEnabled(true);
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

                                new AsyncBackOrder().execute();


                            }


                        } else {
                            listaExistencia = new ArrayList<>();
                            new AsyncBackOrder().execute();


                        }
                    } else {
                        mDialog.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta.setMessage("Porfavor ingrese todos los campos faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ButtonAdd.setEnabled(true);
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Faltan Datos");
                        titulo.show();

                    }
                } else {
                    mDialog.dismiss();
                    AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                    alerta.setMessage("No hay Conexion a Internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ButtonAdd.setEnabled(true);
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

                mDialog.show();

                if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {

                    if (listaCarShoping.size() != 0) {

                        Comentario = comentario.getText().toString();
                        Calendar c = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                        c.add(Calendar.DAY_OF_YEAR, 30);
                        StrFechaVencimiento = dateformatActually.format(c.getTime());
                        ValidaPedColAsyc();


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
                        ValidaPedmex2Asyc();


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
            Listaclientes();
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
    public void ConsultaProductosAsyc() {
        new CarritoComprasActivity.ConsultaProductos().execute();
    }

    private class ConsultaProductos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "producto="+productoStr+"&sucursal="+strcodBra;
            String url = "http://" + StrServer + "/productoconsultaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("Item");


                        ExistenciaProd = jitems.getString("k_Exis");
                        ClaveProducto = jitems.getString("k_ClavePr");
                        ;
                        DescripcionProd = jitems.getString("k_Descr");
                        ;
                        CodBarras = jitems.getString("k_CodBarra");
                        ;
                        Precios = jitems.getString("k_Precio");
                        ;
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                            alerta1.setMessage("El Json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ButtonAdd.setEnabled(true);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ButtonAdd.setEnabled(true);
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
        // @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            productosEd.requestFocus();
            EquivalenciaAsyc();
        }//onPost
    }


    public void EquivalenciaAsyc() {
        new CarritoComprasActivity.Equivalencia().execute();
    }

    private class Equivalencia extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "producto="+ClaveProducto+"&sucursal="+strcodBra;
            String url = "http://" + StrServer + "/equivalenciaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("MENSAJE");
                        MensajePro = jitems.getString("k_mensaje");
                        ProductoEqui = jitems.getString("k_Producto");
                        ValidaEqui = jitems.getString("k_Val");
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                            alerta1.setMessage("El Json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ButtonAdd.setEnabled(true);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ButtonAdd.setEnabled(true);
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
        // @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
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
                                CarritoComprasASYC();

                            } else if (Diponibilidad == 0 && ValidaEqui.equals("0")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoComprasActivity.this);

                                builder.setTitle("Confirma");
                                builder.setMessage("El " + strClave + " tiene una equivalencia  con el producto " + ProductoEqui + "\n" +
                                        "Deseas utilizar este producto equivalente o deseas conservar el producto sin disponibilidad");

                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        strClave = ProductoEqui;
                                        CarritoComprasASYC();
                                        dialog.dismiss();

                                    }
                                });

                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        CarritoComprasASYC();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                CarritoComprasASYC();

                            }

                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
                            alerta.setMessage("Ingrese datos faltantes").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    mDialog.dismiss();
                                    ButtonAdd.setEnabled(true);
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
                                mDialog.dismiss();
                                ButtonAdd.setEnabled(true);
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

                            CarritoComprasASYC();

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

                                    CarritoComprasASYC();
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

                                    CarritoComprasASYC();
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

                            CarritoComprasASYC();

                        }

                    } else {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta1.setMessage("Seleccione un cliente ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                mDialog.dismiss();
                                ButtonAdd.setEnabled(true);
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Campo Vacio");
                        titulo1.show();
                    }
                }


            } else {
                mDialog.dismiss();
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                alerta1.setMessage("No se encontro el producto buscado ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        productosEd.setText("");
                        Cantidad123.setText("");
                        mDialog.dismiss();
                        ButtonAdd.setEnabled(true);
                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Producto no encontrado");
                titulo1.show();
            }

        }//onPost
    }


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")



    private class AsyncBackOrder extends AsyncTask<Void, Void, Void> {


        private boolean conn;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {


            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strscliente + "&sucursal=" + strcodBra + "&producto=" + productoStr;
            String url = "http://" + StrServer + "/backorder?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jfacturas = jsonObject.getJSONObject("Response");
                        jitems = jfacturas.getJSONObject("Back");
                        mensaje = jitems.getString("Valida");
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                            alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ButtonAdd.setEnabled(true);

                                }
                            });
                            AlertDialog titulo1 = alerta1.create();
                            titulo1.setTitle("Hubo un problema");
                            titulo1.show();
                            mDialog.dismiss();
                        }//run
                    });
                }//catch JSON EXCEPTION
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ButtonAdd.setEnabled(true);

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

        }//doInBackground

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            if (mensaje.equals("0")) {
                ConsultaProductosAsyc();

            } else if(mensaje.equals("1")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoComprasActivity.this);

                builder.setTitle("Confirma");
                builder.setMessage("El producto ya cuenta  con BackOrder,¿Deseas agregar este producto a tu pedido?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ConsultaProductosAsyc();
                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mDialog.dismiss();
                        dialog.dismiss();
                        ButtonAdd.setEnabled(true);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        }//onPost
    }//CambiarCajas



    public void CarritoComprasASYC() {
        new CarritoComprasActivity.CarritoComprasay().execute();
    }


    private class CarritoComprasay extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strscliente2+"&producto="+strClave+"&cantidad="+strCantidad+"&existencia=0&sucursal="+strcodBra;
            String url = "http://" + StrServer + "/carritoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);


                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("Cotizacion");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Cotizacion");
                            Numero = jitems.getJSONObject("" + i + "");


                            listaCarShoping2.add(new CarritoVentasSANDG(
                                    (Numero.getString("k_Cliente").equals("") ? "" : Numero.getString("k_Cliente")),
                                    (Numero.getString("k_parte").equals("") ? "" : Numero.getString("k_parte")),
                                    (Numero.getString("k_exis").equals("") ? "" : Numero.getString("k_exis")),
                                    (Numero.getString("k_Q").equals("") ? "" : Numero.getString("k_Q")),
                                    (Numero.getString("k_unidad").equals("") ? "" : Numero.getString("k_unidad")),
                                    (Numero.getString("k_precio").equals("") ? "" : Numero.getString("k_precio")),
                                    (Numero.getString("k_desc1").equals("") ? "0" : Numero.getString("k_desc1")),
                                    (Numero.getString("k_desc2").equals("") ? "0" : Numero.getString("k_desc2")),
                                    (Numero.getString("k_desc3").equals("") ? "0" : Numero.getString("k_desc3")),
                                    (Numero.getString("k_monto").equals("") ? "0" : Numero.getString("k_monto")),
                                    (Numero.getString("k_descr").equals("") ? "" : Numero.getString("k_descr")),
                                    (Numero.getString("k_rfc").equals("") ? "" : Numero.getString("k_rfc")),
                                    (Numero.getString("k_plazo").equals("") ? "0" : Numero.getString("k_plazo")),
                                    (Numero.getString("k_calle").equals("") ? "" : Numero.getString("k_calle")),
                                    (Numero.getString("k_colo").equals("") ? "" : Numero.getString("k_colo")),
                                    (Numero.getString("k_pobla").equals("") ? "" : Numero.getString("k_pobla")),
                                    (Numero.getString("k_via").equals("") ? "" : Numero.getString("k_via")),
                                    (Numero.getString("k_87").equals("") ? "" : Numero.getString("k_87")),
                                    (Numero.getString("k_desc1fac").equals("") ? "0" : Numero.getString("k_desc1fac")),
                                    (Numero.getString("k_comentario1").equals("") ? "" : Numero.getString("k_comentario1")),
                                    (Numero.getString("k_comentario2").equals("") ? "" : Numero.getString("k_comentario2")),
                                    (Numero.getString("k_comentario3").equals("") ? "" : Numero.getString("k_comentario3")),
                                    (Numero.getString("k_descEAGLE").equals("") ? "0" : Numero.getString("k_descEAGLE")),
                                    (Numero.getString("k_descRODATECH").equals("") ? "0" : Numero.getString("k_descRODATECH")),
                                    (Numero.getString("k_descPARTECH").equals("") ? "0" : Numero.getString("k_descPARTECH")),
                                    (Numero.getString("k_descSHARK").equals("") ? "0" : Numero.getString("k_descSHARK")),
                                    (Numero.getString("k_descTRACKONE").equals("") ? "0" : Numero.getString("k_descTRACKONE")),""));

                           }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                            alerta1.setMessage("El Json tiene un problema").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ButtonAdd.setEnabled(true);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
                        alerta1.setMessage("Upss hubo un problema verifica tu conexion a internet").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ButtonAdd.setEnabled(true);
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
            CarritoComprasActivity.Imagenes2 task1 = new CarritoComprasActivity.Imagenes2();
            task1.execute();



        }//onPost
    }


    public void Listaclientes() {
        new CarritoComprasActivity.Cliente().execute();
    }


    private class Cliente extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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
            mDialog.dismiss();
        }//onPost
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

            } else {
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
            soapEnvelope.xmlCarritoCompras(Comentario, strcode, Nombre, Cliente, StrFechaActaul, StrFechaVencimiento, strcodBra, strusr, strpass, rfc, plazo, MontoStr, ivstr, DescuentoStr, DescPro, Desc1, Calle, Colonia, Poblacion, listaCarShoping, StrServer, Clave, descuEagle, descuRodatech, descuPartec, descuShark, descuTrackone);
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

            } else {
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



    public void ValidaPedColAsyc() {
        new CarritoComprasActivity.validapedcol2().execute();
    }


    private class validapedcol2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + Cliente+"&monto="+MontoStr+"&vendedor="+strcode+"&descuento="+Desc1+"&subtotal="+SubdescuentoValida;
            String url = "http://" + StrServer + "/validapedcol2app?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("MENSAJE");

                        MenValPedi = jitems.getString("k_messenge");
                        Desc1Valida = jitems.getString("k_desc1");
                        DescuentoValida = jitems.getString("Descuento");
                        SubtotalValida = jitems.getString("Subtotal");
                        SeCambiovalida = jitems.getString("SeCambio");

                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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

            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
            alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                    mDialog.show();

                    if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {
                        CarritoComprasActivity.CarritoColombia task4 = new CarritoComprasActivity.CarritoColombia();
                        task4.execute();
                    } else {
                        CarritoComprasActivity.AsyncCallWS task4 = new CarritoComprasActivity.AsyncCallWS();
                        task4.execute();
                    }
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Pedido");
            titulo.show();
        }//onPost
    }


    public void ValidaPedmex2Asyc() {
        new CarritoComprasActivity.validapedmex2().execute();
    }


    private class validapedmex2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + Cliente+"&monto="+MontoStr+"&vendedor="+strcode;
            String url = "http://" + StrServer + "/validapedmex2app?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("MENSAJE");

                        MenValPedi = jitems.getString("k_messenge");
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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


            mDialog.dismiss();
            AlertDialog.Builder alerta = new AlertDialog.Builder(CarritoComprasActivity.this);
            alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    mDialog.show();

                    if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {
                        CarritoComprasActivity.CarritoColombia task4 = new CarritoComprasActivity.CarritoColombia();
                        task4.execute();
                    } else {
                        CarritoComprasActivity.AsyncCallWS task4 = new CarritoComprasActivity.AsyncCallWS();
                        task4.execute();
                    }


                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Pedido");
            titulo.show();
        }//onPost
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
        mDialog.show();
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
        mDialog.dismiss();

    }


    public void Delete(View view) {
        mDialog.show();
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
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
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
                        fila.getString(11),
                        fila.getString(12)));
            } while (fila.moveToNext());
        }
        AdaptadorCarrito adapter = new AdaptadorCarrito(listaCarShoping, Desc1, StrServer, context, Empresa);
        recyclerCarrtio.setAdapter(adapter);
        mDialog.dismiss();


        db.close();

    }

    private class Imagenes2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (StrServer.equals("jacve.dyndns.org:9085") || StrServer.equals("guvi.ath.cx:9085") || StrServer.equals("cecra.ath.cx:9085") || StrServer.equals("sprautomotive.servehttp.com:9085")){
            for (int i = 0; i < listaCarShoping2.size(); i++) {

                String Producto = listaCarShoping2.get(i).getParte();


                HttpHandler sh = new HttpHandler();
                String url = Empresa + Producto;
                String jsonStr = sh.makeServiceCall(url, "", "");
                jsonStr = jsonStr.replace("\\", "");
                if (jsonStr != null) {
                    try {
                        // Convertir el JSON a un array
                        JSONArray jsonArray = new JSONArray(jsonStr);

                        JSONObject objeto = jsonArray.getJSONObject(0);
                        objeto.getString("principal");
                        String url1 = objeto.getString("principal");
                        listaCarShoping2.get(i).setUrl(url1);


                    } catch (final JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(CarritoComprasActivity.this);
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


            }
            }
            return null;

        }

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
                    String URL = listaCarShoping2.get(i).getUrl();

                    db.execSQL("INSERT INTO  carrito (Cliente,Parte,Existencia,Cantidad,Unidad,Precio,Desc1,Desc2,Desc3,Monto,Descri,URL) values ('" + Cli + "','" + Par + "','" + Exi + "','" + Can + "','" + Uni + "','" + Pre + "','" + Des1 + "','" + Des2 + "','" + Des3 + "','" + Monto + "','" + Des + "','"+URL+"')");
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





    @SuppressLint("SetTextI18n")
    private void mostarCliente() {
        Cliente = preferenceClie.getString("CodeClien", "null");
        Nombre = preferenceClie.getString("NomClien", "null");
        txtClaveCliente.setText(Html.fromHtml((Cliente.equals("null") ? "No hay Cliente" : "<font color = #FF0000>" + Cliente + "</font> <br>") + (Nombre.equals("null") ? "" : Nombre)));

        if (!Cliente.equals("null")) {
            if(StrServer.equals("vazlocolombia.dyndns.org:9085")){
                Infor.setVisibility(View.VISIBLE);
                Infor.setText(Html.fromHtml("Calle:" + Calle + "<br>" +
                        "RFC:" + rfc + "<br>" +
                        "Poblacion:" + Poblacion + "<br>" +
                        "Descuento:" + Desc1));

                comentario.setText(Comentario1 + "\n" + " " + Comentario2 + "\n" + " " + Comentario3);

            }else{
                Infor.setVisibility(View.VISIBLE);
                Infor.setText(Html.fromHtml("Calle:" + Calle + "<br>" +
                        "Colonia:" + Colonia + "<br>" +
                        "Poblacion:" + Poblacion + "<br>" +
                        "Descuento:" + Desc1));

                comentario.setText(Comentario1 + "\n" + " " + Comentario2 + "\n" + " " + Comentario3);

            }

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