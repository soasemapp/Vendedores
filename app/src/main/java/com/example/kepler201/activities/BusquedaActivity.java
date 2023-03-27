package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kepler201.Adapter.AdapterSearchProduct;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ListLineaSANDG;
import com.example.kepler201.SetterandGetter.ListLineaSANDG2;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.SetterandGetter.SetGetListMarca;
import com.example.kepler201.SetterandGetter.SetGetListMarca2;
import com.example.kepler201.SetterandGetter.SetGetListModelo;
import com.example.kepler201.SetterandGetter.SetGetListModelo2;
import com.example.kepler201.SetterandGetter.SetGetListProductos;
import com.example.kepler201.XMLS.xmlBusqueGeneral;
import com.example.kepler201.XMLS.xmlBusqueProductos;
import com.example.kepler201.XMLS.xmlCarritoVentas;
import com.example.kepler201.XMLS.xmlListLine2;
import com.example.kepler201.XMLS.xmlListMarca;
import com.example.kepler201.XMLS.xmlListModelo;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.includes.MyToolbar;
import com.google.android.material.checkbox.MaterialCheckBox;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class BusquedaActivity extends AppCompatActivity {

    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;
    private SharedPreferences.Editor editor;
    ArrayList<SetGetListMarca2> listMarca = new ArrayList<>();
    ArrayList<SetGetListModelo2> listModelo = new ArrayList<>();
    ArrayList<SetGetListProductos> listProdu1 = new ArrayList<>();
    ArrayList<ListLineaSANDG2> listaLinea = new ArrayList<>();
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();

    int dato = 0;
    private SharedPreferences preferenceClie;

    TextView txtmarca, txtmodelo, txtlinea;
    private Spinner spineryear;
    String claveMarca;
    String mensaje = "";
    Button btnMarca, btnModelo, btfLinea;
    AlertDialog mDialog;
    EditText yearInicial, yearFinal;
    MaterialCheckBox yearCheck;
    RecyclerView RecyclerProductos;
    Context context;
    Button btnBuscar, btnfiltro;
    String fechainicio = "", fechafinal = "", marca = "", modelo = "", linea = "", check = "";
    LinearLayout linearFiltro;
    int ban = 1;

    String[] search = new String[73];

    String BusquedaProducto = null;
    String strscliente = "" , strscliente3 = "";
    String K87;
    String Desc1fa;
    String Nombre;
    String rfc;
    String plazo;
    String Calle;
    String Colonia;
    String Poblacion;
    String Via;
    String DescPro;
    String Desc1;
    String Comentario1;
    String Comentario2;
    String Comentario3;
    EditText BusquedaProductoed;
    String Empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);


        mDialog = new SpotsDialog.Builder().setContext(BusquedaActivity.this).setMessage("Espere un momento...").build();
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        MyToolbar.show(this, "Busqueda", true);
        editor = preference.edit();
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
        strco = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");
        BusquedaProducto = getIntent().getStringExtra("Producto");


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
        strscliente = preferenceClie.getString("CodeClien", "null");
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


        btnMarca = findViewById(R.id.btnFMarca);
        btnModelo = findViewById(R.id.btnFModelo);
        btfLinea = findViewById(R.id.btfLinea);
        yearCheck = findViewById(R.id.checkyear);
        RecyclerProductos = findViewById(R.id.listProductos);
        btnBuscar = findViewById(R.id.btnBuscar);
        BusquedaProductoed = findViewById(R.id.idBusqueda);
        linearFiltro = findViewById(R.id.Filter);
        btnfiltro = findViewById(R.id.btnFilters);
        txtmarca = findViewById(R.id.marcatxt);
        txtmodelo = findViewById(R.id.modelotxt);
        txtlinea = findViewById(R.id.lineatxt);
        spineryear = findViewById(R.id.spinneryear);
        linearFiltro.setVisibility(View.GONE);
        spineryear.setEnabled(false);
        btnfiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ban == 0) {
                    ban = 1;
                    linearFiltro.setVisibility(View.GONE);
                } else {
                    ban = 0;
                    linearFiltro.setVisibility(View.VISIBLE);
                }

            }
        });

        String[] opciones = new String[73];
        for (int i=0;i<opciones.length;i++){
            opciones[i]=String.valueOf(1950+i);
            search[i]=String.valueOf(1950+i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
        spineryear.setAdapter(adapter);


        check = "0";
        listProdu1 = new ArrayList<>();
        RecyclerProductos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (preferenceClie.contains("CodeClien")) {
                    if (check.equals("1") && (!marca.equals("") && !modelo.equals(""))) {
                        for (int i = 0; i < search.length; i++) {
                            int posi = spineryear.getSelectedItemPosition();
                            if (posi == i) {
                                fechainicio = search[i];
                                break;
                            }
                        }
                        listProdu1.clear();
                        ListProductos task = new ListProductos();
                        task.execute();
                    } else if (check.equals("0") && (!marca.equals("") && !modelo.equals(""))) {

                        fechainicio = "";
                        listProdu1.clear();
                        ListProductos task = new ListProductos();
                        task.execute();
                    } else {
                        listProdu1.clear();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(BusquedaActivity.this);
                        alerta.setMessage("No has seleccionado una marca o modelo").setIcon(R.drawable.ic_baseline_error_24).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("!ERROR!");
                        titulo.show();
                    }
                } else {

                    BusquedaActivity.AsyncClientes task1 = new BusquedaActivity.AsyncClientes();
                    task1.execute();
                    dato = 2;

                }


            }
        });

        yearCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yearCheck.isChecked()) {
                   check="1";
                   spineryear.setEnabled(true);
                } else {
                    check="0";
                    spineryear.setEnabled(false);
                }
            }
        });

        btnModelo.setEnabled(false);
        btfLinea.setEnabled(false);
        ListMarca task = new ListMarca();
        task.execute();
        btnMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listMarca.size()];

                for (int i = 0; i < listMarca.size(); i++) {
                    opciones[i] = listMarca.get(i).getDescripcion();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaActivity.this);
                builder.setTitle("¿Que Marca Buscas?");


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnModelo.setText(Html.fromHtml("Modelo"));

                        modelo = "";
                        linea = "";

                        claveMarca = listMarca.get(which).getDescripcion();//Html.fromHtml("Marca" <br/> C/U:$<font color ='#4CAF50' size=4> +listMarca.get(which).getDescripcion())+"</font>")
                        btnModelo.setEnabled(true);
                        btfLinea.setEnabled(false);
                        txtmarca.setVisibility(View.VISIBLE);
                        txtmarca.setText(listMarca.get(which).getDescripcion() + " " + "X");
                        txtmodelo.setVisibility(View.GONE);
                        txtmodelo.setText("");
                        txtlinea.setVisibility(View.GONE);
                        txtlinea.setText("");
                        listModelo.clear();
                        listaLinea.clear();
                        marca = listMarca.get(which).getDescripcion();
                        ListModelo task = new ListModelo();
                        task.execute();
                        dialog.dismiss();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listModelo.size()];

                for (int i = 0; i < listModelo.size(); i++) {
                    opciones[i] = listModelo.get(i).getDescripcion();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaActivity.this);
                builder.setTitle("¿Que Modelo Buscas?");


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        modelo = listModelo.get(which).getDescripcion();
                        listaLinea.clear();
                        txtmodelo.setVisibility(View.VISIBLE);
                        txtmodelo.setText(listModelo.get(which).getDescripcion() + " " + "X");
                        txtlinea.setVisibility(View.GONE);
                        txtlinea.setText("");


                        ListLineas task = new ListLineas();
                        task.execute();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btfLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[listaLinea.size()];

                for (int i = 0; i < listaLinea.size(); i++) {
                    opciones[i] = listaLinea.get(i).getLinea();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaActivity.this);
                builder.setTitle("¿Que Linea Buscas?");


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linea = "";
                        linea = listaLinea.get(which).getLinea();
                        txtlinea.setVisibility(View.VISIBLE);
                        txtlinea.setText(listaLinea.get(which).getLinea() + " " + "X");
                        dialog.dismiss();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        BusquedaProductoed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) { //Do Something } return false; } });

                    if (preferenceClie.contains("CodeClien")) {
                        BusquedaProducto = BusquedaProductoed.getText().toString();
                        listProdu1.clear();
                        BusquedaGeneral task = new BusquedaGeneral();
                        task.execute();
                    } else {

                        BusquedaActivity.AsyncClientes task1 = new BusquedaActivity.AsyncClientes();
                        task1.execute();
                        dato = 1;

                    }


                }
                return false;
            }
        });


        if (BusquedaProducto != null) {
            listProdu1.clear();
            BusquedaGeneral task1 = new BusquedaGeneral();
            task1.execute();
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
            soapEnvelope.xmlSearchG(strusr, strpass, strco);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            for (int i = 0; i < response.getPropertyCount(); i++) {
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

            String[] opciones = new String[listaclientG.size()];

            for (int i = 0; i < listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i).getUserCliente() + ":" + listaclientG.get(i).getNombreCliente();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaActivity.this);
            builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    strscliente = listaclientG.get(which).getUserCliente();
                    strscliente3 = listaclientG.get(which).getNombreCliente();
                    BusquedaActivity.CarritoCompras task1 = new BusquedaActivity.CarritoCompras();
                    task1.execute();
                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            mDialog.dismiss();


        }


    }

    @SuppressLint("StaticFieldLeak")
    private class CarritoCompras extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CarritoValidado();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            if (preferenceClie.contains("CodeClien")) {

            } else {
                guardarDatos();
            }


        }
    }

    private void guardarDatos() {
        editor.putString("CodeClien", strscliente);
        editor.putString("NomClien", strscliente3);
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

        BusquedaProducto = BusquedaProductoed.getText().toString();
        listProdu1.clear();
        if (dato == 1) {
            BusquedaGeneral task = new BusquedaGeneral();
            task.execute();
        } else if (dato == 2) {
            if (check.equals("1") && (!marca.equals("") && !modelo.equals(""))) {
                fechainicio = yearInicial.getText().toString();
                fechafinal = yearFinal.getText().toString();
                listProdu1.clear();
                ListProductos task = new ListProductos();
                task.execute();
            } else if (check.equals("0") && (!marca.equals("") && !modelo.equals(""))) {
                fechainicio = "";
                fechafinal = "";
                ListProductos task = new ListProductos();
                task.execute();
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(BusquedaActivity.this);
                alerta.setMessage("No has seleccionado una marca o modelo").setIcon(R.drawable.ic_baseline_error_24).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

    private void CarritoValidado() {
        String SOAP_ACTION = "CarShop";
        String METHOD_NAME = "CarShop";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlCarritoVentas soapEnvelope = new xmlCarritoVentas(SoapEnvelope.VER11);
            soapEnvelope.xmlCarritoVen(strusr, strpass, strscliente, "1000R", "1", "0", strcodBra);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            response0 = (SoapObject) response0.getProperty(0);
            rfc = (response0.getPropertyAsString("k_rfc").equals("anyType{}") ? " " : response0.getPropertyAsString("k_rfc"));
            plazo = (response0.getPropertyAsString("k_plazo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_plazo"));
            Calle = (response0.getPropertyAsString("k_calle").equals("anyType{}") ? " " : response0.getPropertyAsString("k_calle"));
            Colonia = (response0.getPropertyAsString("k_colo").equals("anyType{}") ? " " : response0.getPropertyAsString("k_colo"));
            Poblacion = (response0.getPropertyAsString("k_pobla").equals("anyType{}") ? " " : response0.getPropertyAsString("k_pobla"));
            Via = (response0.getPropertyAsString("k_via").equals("anyType{}") ? " " : response0.getPropertyAsString("k_via"));
            K87 = (response0.getPropertyAsString("k_87").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_87"));
            Desc1fa = (response0.getPropertyAsString("k_desc1fac").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_desc1fac"));
            Comentario1 = (response0.getPropertyAsString("k_comentario1").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario1"));
            Comentario2 = (response0.getPropertyAsString("k_comentario2").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario2"));
            Comentario3 = (response0.getPropertyAsString("k_comentario3").equals("anyType{}") ? "" : response0.getPropertyAsString("k_comentario3"));


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


    public void borrarmarca(View view) {
        marca = "";
        modelo = "";
        linea = "";
        btnModelo.setEnabled(false);
        btfLinea.setEnabled(false);
        txtmarca.setVisibility(View.GONE);
        txtmodelo.setVisibility(View.GONE);
        txtmodelo.setText("");
        txtlinea.setVisibility(View.GONE);
        txtlinea.setText("");
        listModelo.clear();
        listaLinea.clear();
    }

    public void borrarmodelo(View view) {

        modelo = "";
        linea = "";
        btfLinea.setEnabled(false);
        txtmodelo.setVisibility(View.GONE);
        txtmodelo.setText("");
        txtlinea.setVisibility(View.GONE);
        txtlinea.setText("");
        listaLinea.clear();
    }

    public void borrarmlinea(View view) {
        linea = "";
        txtlinea.setVisibility(View.GONE);
        txtlinea.setText("");
    }

    @SuppressLint("StaticFieldLeak")
    private class ListLineas extends AsyncTask<Void, Void, Void> {

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

            btfLinea.setEnabled(true);
            mDialog.dismiss();

        }


    }

    private void conectar3() {
        String SOAP_ACTION = "listlineas";
        String METHOD_NAME = "listlineas";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListLine2 soapEnvelope = new xmlListLine2(SoapEnvelope.VER11);
            soapEnvelope.xmlListLine2(strusr, strpass,marca ,modelo);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                if (!response0.getPropertyAsString("k_Linea").equals("anyType{}")) {
                    listaLinea.add(new ListLineaSANDG2((response0.getPropertyAsString("k_Linea").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Linea"))));
                }

            }


        } catch (XmlPullParserException | IOException soapFault) {
            mDialog.dismiss();
            soapFault.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class ListMarca extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            WebServiceListMarca();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
mDialog.dismiss();

        }
    }


    @SuppressLint("StaticFieldLeak")
    private class ListModelo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            WebServiceListModelo();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
        }

    }


    @SuppressLint("StaticFieldLeak")
    private class ListProductos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            WebServiceListProducto();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (listProdu1.size()==0){
                AlertDialog.Builder alerta = new AlertDialog.Builder(BusquedaActivity.this);
                alerta.setMessage("No se ah encontrado ningun resultado").setIcon(R.drawable.ic_baseline_error_24).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("");
                titulo.show();
            }


            AdapterSearchProduct adapter = new AdapterSearchProduct(listProdu1, context,Empresa);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = RecyclerProductos.getChildAdapterPosition(Objects.requireNonNull(RecyclerProductos.findContainingItemView(view)));
                    Intent ProductosDetallados = new Intent(BusquedaActivity.this, DetalladoProductosActivity.class);
                    String Producto = listProdu1.get(position).getProductos();
                    String Descripcion = listProdu1.get(position).getDescripcion();
                    String PrecioAjuste =  listProdu1.get(position).getPrecioAjuste();
                    String PrecioBase =  listProdu1.get(position).getPrecioBase();
                    ProductosDetallados.putExtra("Producto", Producto);
                    ProductosDetallados.putExtra("Descripcion", Descripcion);
                    ProductosDetallados.putExtra("PrecioAjuste", PrecioAjuste);
                    ProductosDetallados.putExtra("PrecioBase", PrecioBase);
                    ProductosDetallados.putExtra("claveVentana", "1");
                    startActivity(ProductosDetallados);

                }
            });
            RecyclerProductos.setAdapter(adapter);
            mDialog.dismiss();

        }

    }

    private void WebServiceListProducto() {
        String SOAP_ACTION = "busqueProductos";
        String METHOD_NAME = "busqueProductos";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlBusqueProductos soapEnvelope = new xmlBusqueProductos(SoapEnvelope.VER11);
            soapEnvelope.xmlBusqueProductos(strusr, strpass, fechainicio, marca, modelo, linea, check, strscliente);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response = (SoapObject) soapEnvelope.bodyIn;

            String Producto;
            String Descripcion;
            String Linea;
            String precio_base;
            String precio_ajuste;



            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {

                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                Producto = response0.getPropertyAsString("Producto");
                Descripcion = response0.getPropertyAsString("Descripcion");
                Linea = response0.getPropertyAsString("Linea");
                response0 = (SoapObject) response0.getProperty("precio_base");
                precio_base = response0.getPropertyAsString("valor").equals("anyType{}") ? "0" : response0.getPropertyAsString("valor");
                response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                response0 = (SoapObject) response0.getProperty("precio_ajuste");
                precio_ajuste = response0.getPropertyAsString("valor").equals("anyType{}") ? "0" : response0.getPropertyAsString("valor");

                listProdu1.add(new SetGetListProductos(Producto, Descripcion,Linea, precio_base, precio_ajuste));


            }


        } catch (XmlPullParserException | IOException soapFault) {
            soapFault.printStackTrace();
        } catch (Exception ignored) {
        }
    }


    private void WebServiceListMarca() {
        String SOAP_ACTION = "listmarca";
        String METHOD_NAME = "listmarca";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListMarca soapEnvelope = new xmlListMarca(SoapEnvelope.VER11);
            soapEnvelope.xmlListMarca(strusr, strpass);
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

                listMarca.add(new SetGetListMarca2(
                        (response0.getPropertyAsString("k_Descripcion").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Descripcion"))));


            }

        } catch (XmlPullParserException | IOException soapFault) {
            soapFault.printStackTrace();
        } catch (Exception ignored) {
        }
    }

    private void WebServiceListModelo() {
        String SOAP_ACTION = "listmodelo";
        String METHOD_NAME = "listmodelo";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListModelo soapEnvelope = new xmlListModelo(SoapEnvelope.VER11);
            soapEnvelope.xmlListModelo(strusr, strpass, claveMarca);
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

                listModelo.add(new SetGetListModelo2(
                        (response0.getPropertyAsString("k_Descripcion").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Descripcion"))));


            }

        } catch (XmlPullParserException | IOException soapFault) {
            soapFault.printStackTrace();
        } catch (Exception ignored) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BusquedaGeneral extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BusquedaGeneral();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {


if (listProdu1.size()==0){
    AlertDialog.Builder alerta = new AlertDialog.Builder(BusquedaActivity.this);
    alerta.setMessage("No se ah encontrado ningun resultado").setIcon(R.drawable.ic_baseline_error_24).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    });

    AlertDialog titulo = alerta.create();
    titulo.setTitle("");
    titulo.show();
}
            AdapterSearchProduct adapter = new AdapterSearchProduct(listProdu1, context,Empresa);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = RecyclerProductos.getChildAdapterPosition(Objects.requireNonNull(RecyclerProductos.findContainingItemView(view)));
                    Intent ProductosDetallados = new Intent(BusquedaActivity.this, DetalladoProductosActivity.class);
                    String Producto = listProdu1.get(position).getProductos();
                    String Descripcion = listProdu1.get(position).getDescripcion();
                    String PrecioAjuste =  listProdu1.get(position).getPrecioAjuste();
                    String PrecioBase =  listProdu1.get(position).getPrecioBase();
                    ProductosDetallados.putExtra("Producto", Producto);
                    ProductosDetallados.putExtra("Descripcion", Descripcion);
                    ProductosDetallados.putExtra("PrecioAjuste", PrecioAjuste);
                    ProductosDetallados.putExtra("PrecioBase", PrecioBase);
                    ProductosDetallados.putExtra("claveVentana", "1");

                    startActivity(ProductosDetallados);
                }
            });
            RecyclerProductos.setAdapter(adapter);
            mDialog.dismiss();


        }


    }

    private void BusquedaGeneral() {
        String SOAP_ACTION = "ProductoConsultaApp";
        String METHOD_NAME = "ProductoConsultaApp";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClienteSSoap/";
        String URL = "http://" + StrServer + "/WSk75ClienteSSoap";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlBusqueGeneral soapEnvelope = new xmlBusqueGeneral(SoapEnvelope.VER11);
            soapEnvelope.xmlBusqueGeneral(strusr, strpass, strscliente, BusquedaProducto);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;

            String Producto;
            String Descripcion;
            String Linea;
            String precio_base;
            String precio_ajuste;
            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                Producto = response0.getPropertyAsString("Producto");
                Descripcion = response0.getPropertyAsString("Descripcion");
                Linea = response0.getPropertyAsString("Linea");
                response0 = (SoapObject) response0.getProperty("precio_base");
                precio_base = response0.getPropertyAsString("valor").equals("anyType{}") ? "0" : response0.getPropertyAsString("valor");
                response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);
                response0 = (SoapObject) response0.getProperty("precio_ajuste");
                precio_ajuste = response0.getPropertyAsString("valor").equals("anyType{}") ? "0" : response0.getPropertyAsString("valor");

                listProdu1.add(new SetGetListProductos(Producto, Descripcion,Linea, precio_base, precio_ajuste));


            }


        } catch (XmlPullParserException | IOException soapFault) {
            mDialog.dismiss();
            soapFault.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
        }
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
