package com.example.kepler201.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorProductosNuevos;
import com.example.kepler201.Adapter.AdaptadorProductosPartech;
import com.example.kepler201.Adapter.AdaptadorProductosRodatech;
import com.example.kepler201.Adapter.AdaptadorProductosShark;
import com.example.kepler201.Adapter.AdaptadorProductostrackone;
import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ProductosNuevosSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.XMLS.xmlCarritoVentas;
import com.example.kepler201.XMLS.xmlProductosNuevos;
import com.example.kepler201.XMLS.xmlSearchClientesG;
import com.example.kepler201.XMLS.xmlVersiones;
import com.example.kepler201.activities.BusquedaActivity;
import com.example.kepler201.activities.DetalladoProductosActivity;
import com.example.kepler201.activities.MainActivity;
import com.example.kepler201.activities.inicioActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    RecyclerView recyclerViewEagle, recyclerViewTrackone, recyclerViewRodatech, recyclerViewPartech, recyclerViewShark;

    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;

    String ProductosNuevos;
    String  strscliente = "",  strscliente3 = "";
    String K87;
    String Desc1fa;
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
    String Desc1;
    String Comentario1;
    String Comentario2;
    String Comentario3;
    int dato = 0;
    ConexionSQLiteHelper conn;
    int datos;

    private String version;
    int  Resultado=0;

    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();

    private SharedPreferences preferenceClie;
    private SharedPreferences.Editor editor2;
    View view;

    AlertDialog mDialog;
    Context context = this.getActivity();

    ArrayList<ProductosNuevosSANDG> ListaProductosGeneral = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosEagle = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosTrackone = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosRodatech = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosPartech = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosShark = new ArrayList<>();


    EditText BusquedaProducto;
    String ProductosNuevosStr,Empresa;
    LinearLayout EagleOcultar, TrackOneOcultar, RodatechOcultar, PartechOcultar, SharkOcultar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mDialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Espere un momento...").build();


        //FindView
        recyclerViewEagle = view.findViewById(R.id.listProductosEagle);
        recyclerViewTrackone = view.findViewById(R.id.listProductTrackone);
        recyclerViewRodatech = view.findViewById(R.id.listProductosRodatech);
        recyclerViewPartech = view.findViewById(R.id.listProductosPartech);
        recyclerViewShark = view.findViewById(R.id.listProductosShark);
        BusquedaProducto = view.findViewById(R.id.idBusqueda);
        EagleOcultar = view.findViewById(R.id.EagleOcultar);
        TrackOneOcultar = view.findViewById(R.id.TrackoneOcultar);
        RodatechOcultar = view.findViewById(R.id.RodatechOcultar);
        PartechOcultar = view.findViewById(R.id.PartechOcultar);
        SharkOcultar = view.findViewById(R.id.SharkOcultar);

        //Preference
        SharedPreferences preference = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();

        preferenceClie = requireActivity().getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor2 = preferenceClie.edit();


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
        ProductosNuevosStr= preference.getString("Productosnuevos", "0");



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



        ListaProductosEagle = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEagle.setLayoutManager(horizontalLayoutManagaer);

        ListaProductosTrackone = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrackone.setLayoutManager(horizontalLayoutManagaer1);

        ListaProductosRodatech = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRodatech.setLayoutManager(horizontalLayoutManagaer2);

        ListaProductosPartech = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPartech.setLayoutManager(horizontalLayoutManagaer3);

        ListaProductosShark = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewShark.setLayoutManager(horizontalLayoutManagaer4);


        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        ConsultaComprobacion();

        Versiones task1 = new Versiones();
        task1.execute();

        if(datos>0){
            if( day==15 || day==1){
                if (ProductosNuevosStr.equals("0")) {
                    editor.putString("Productosnuevos", "1");
                    editor.apply();
                    HomeFragment.ProductosNuevos task = new HomeFragment.ProductosNuevos();
                    task.execute();
                } else {
                    Consulta();
                }

            }else{

                editor.putString("Productosnuevos", "0");
                editor.apply();
                Consulta();

            }


        }else{
            HomeFragment.ProductosNuevos task = new HomeFragment.ProductosNuevos();
            task.execute();

        }



        BusquedaProducto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) { //Do Something } return false; } });

                    // Check if no view has focus:

                    if (preferenceClie.contains("CodeClien")) {
                        String BusquedaProductoString;
                        BusquedaProductoString = BusquedaProducto.getText().toString();
                        Intent BusquedaProdcuto = new Intent(getActivity(), BusquedaActivity.class);
                        BusquedaProdcuto.putExtra("Producto", BusquedaProductoString);
                        startActivity(BusquedaProdcuto);
                    } else {

                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 1;

                    }


                }
                return false;
            }
        });

        switch (StrServer) {
            case "sprautomotive.servehttp.com:9090":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.VISIBLE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);


                break;
            case "sprautomotive.servehttp.com:9095":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.VISIBLE);
                SharkOcultar.setVisibility(View.GONE);
                break;
            case "sprautomotive.servehttp.com:9080":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.VISIBLE);
                break;
            case "vazlocolombia.dyndns.org:9085":
                EagleOcultar.setVisibility(View.VISIBLE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);
                break;
            default:
                EagleOcultar.setVisibility(View.VISIBLE);
                TrackOneOcultar.setVisibility(View.VISIBLE);
                RodatechOcultar.setVisibility(View.VISIBLE);
                PartechOcultar.setVisibility(View.VISIBLE);
                SharkOcultar.setVisibility(View.VISIBLE);

                break;
        }


        return view;
    }


    private void ConsultaComprobacion() {

        conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("SELECT COUNT(*) FROM productos", null);
        if (fila != null && fila.moveToFirst()) {
            do {
                datos=fila.getInt(0);
            } while (fila.moveToNext());
        }
        db.close();

    }
    private void BorrarCarrito() {
        conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        db.delete("productos", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='productos'");
        db.close();
    }




    private void Consulta() {
        ListaProductosEagle = new ArrayList<>();
        ListaProductosRodatech = new ArrayList<>();
        ListaProductosPartech = new ArrayList<>();
        ListaProductosShark = new ArrayList<>();
        ListaProductosTrackone = new ArrayList<>();
        conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("select * from productos ", null);
        if (fila != null && fila.moveToFirst()) {
            do {

                ListaProductosGeneral.add(new ProductosNuevosSANDG(fila.getString(1),
                        fila.getString(2),
                        fila.getString(3)));

                switch (fila.getString(3)) {
                    case "1":
                        ListaProductosEagle.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)));
                        break;
                    case "2":
                        ListaProductosRodatech.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)));
                        break;
                    case "3":
                        ListaProductosPartech.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)));
                        break;
                    case "4":
                        ListaProductosShark.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)));
                        break;
                    case "6":
                        ListaProductosTrackone.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3)));
                        break;
                    default:
                        break;
                }

            } while (fila.moveToNext());

            if (ListaProductosGeneral.size() > 0) {

                AdaptadorProductosNuevos adapter = new AdaptadorProductosNuevos(ListaProductosEagle, context,Empresa);
                recyclerViewEagle.setAdapter(adapter);
                AdaptadorProductostrackone adapter1 = new AdaptadorProductostrackone(ListaProductosTrackone, context,Empresa);
                recyclerViewTrackone.setAdapter(adapter1);
                AdaptadorProductosRodatech adapter2 = new AdaptadorProductosRodatech(ListaProductosRodatech, context,Empresa);
                recyclerViewRodatech.setAdapter(adapter2);
                AdaptadorProductosPartech adapter3 = new AdaptadorProductosPartech(ListaProductosPartech, context,Empresa);
                recyclerViewPartech.setAdapter(adapter3);
                AdaptadorProductosShark adapter4 = new AdaptadorProductosShark(ListaProductosShark, context,Empresa);
                recyclerViewShark.setAdapter(adapter4);


                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewEagle.getChildAdapterPosition(Objects.requireNonNull(recyclerViewEagle.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosEagle.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewEagle.getChildAdapterPosition(Objects.requireNonNull(recyclerViewEagle.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosEagle.get(position).getClave();
                            HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                            task1.execute();
                            dato = 2;

                        }


                    }
                });

                adapter1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewTrackone.getChildAdapterPosition(Objects.requireNonNull(recyclerViewTrackone.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosTrackone.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewTrackone.getChildAdapterPosition(Objects.requireNonNull(recyclerViewTrackone.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosTrackone.get(position).getClave();
                            HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                            task1.execute();
                            dato = 2;

                        }


                    }
                });

                adapter2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewRodatech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewRodatech.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosRodatech.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewRodatech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewRodatech.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosRodatech.get(position).getClave();

                            HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                            task1.execute();
                            dato = 2;

                        }
                    }
                });

                adapter3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewPartech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewPartech.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosPartech.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewPartech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewPartech.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosPartech.get(position).getClave();

                            HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                            task1.execute();
                            dato = 2;

                        }

                    }
                });

                adapter4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewShark.getChildAdapterPosition(Objects.requireNonNull(recyclerViewShark.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosShark.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewShark.getChildAdapterPosition(Objects.requireNonNull(recyclerViewShark.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosShark.get(position).getClave();
                            HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                            task1.execute();
                            dato = 2;

                        }


                    }
                });

            }

        }

        db.close();

    }

    @SuppressWarnings("deprecation")
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


    @SuppressWarnings("deprecation")
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


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    strscliente = listaclientG.get(which).getUserCliente();
                    strscliente3 = listaclientG.get(which).getNombreCliente();
                    HomeFragment.CarritoCompras task1 = new HomeFragment.CarritoCompras();
                    task1.execute();
                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            mDialog.dismiss();


        }


    }

    private void guardarDatos() {
        editor2.putString("CodeClien", strscliente);
        editor2.putString("NomClien", strscliente3);
        editor2.putString("RFC", rfc);
        editor2.putString("PLAZO", plazo);
        editor2.putString("Calle", Calle);
        editor2.putString("Colonia", Colonia);
        editor2.putString("Poblacion", Poblacion);
        editor2.putString("Via", Via);
        editor2.putString("DescPro", K87);
        editor2.putString("Desc1", Desc1fa);
        editor2.putString("Comentario1", Comentario1);
        editor2.putString("Comentario2", Comentario2);
        editor2.putString("Comentario3", Comentario3);
        editor2.commit();


        if (dato == 1) {

            String BusquedaProductoString;
            BusquedaProductoString = BusquedaProducto.getText().toString();
            Intent BusquedaProdcuto = new Intent(getActivity(), BusquedaActivity.class);
            BusquedaProdcuto.putExtra("Producto", BusquedaProductoString);
            startActivity(BusquedaProdcuto);
            mDialog.dismiss();

        } else if (dato == 2) {
            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
            ProductosDetallados.putExtra("Producto", ProductosNuevos);
            ProductosDetallados.putExtra("claveVentana", "1");
            startActivity(ProductosDetallados);
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


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class ProductosNuevos extends AsyncTask<Void, Void, Void> {

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
            BorrarCarrito();

            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            for (int i = 0; i < ListaProductosGeneral.size(); i++) {

                String Clave = ListaProductosGeneral.get(i).getClave();
                String Descripcion = ListaProductosGeneral.get(i).getDescripcion();
                String Tipo = ListaProductosGeneral.get(i).getTipo();

                db.execSQL("INSERT INTO  productos (Clave,Descripcion,Tipo) values ('" + Clave + "','" + Descripcion + "','" + Tipo + "')");
            }

            db.close();

            for (int i = 0; i < ListaProductosGeneral.size(); i++) {
                String Clave, Descripcion, Tipo;
                Clave = ListaProductosGeneral.get(i).getClave();
                Descripcion = ListaProductosGeneral.get(i).getDescripcion();
                Tipo = ListaProductosGeneral.get(i).getTipo();

                switch (Tipo) {
                    case "1":
                        ListaProductosEagle.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo));
                        break;
                    case "2":
                        ListaProductosRodatech.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo));
                        break;
                    case "3":
                        ListaProductosPartech.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo));
                        break;
                    case "4":
                        ListaProductosShark.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo));
                        break;
                    case "6":
                        ListaProductosTrackone.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo));
                        break;
                    default:
                        break;
                }
            }


            AdaptadorProductosNuevos adapter = new AdaptadorProductosNuevos(ListaProductosEagle, context,Empresa);
            recyclerViewEagle.setAdapter(adapter);
            AdaptadorProductostrackone adapter1 = new AdaptadorProductostrackone(ListaProductosTrackone, context,Empresa);
            recyclerViewTrackone.setAdapter(adapter1);
            AdaptadorProductosRodatech adapter2 = new AdaptadorProductosRodatech(ListaProductosRodatech, context,Empresa);
            recyclerViewRodatech.setAdapter(adapter2);
            AdaptadorProductosPartech adapter3 = new AdaptadorProductosPartech(ListaProductosPartech, context,Empresa);
            recyclerViewPartech.setAdapter(adapter3);
            AdaptadorProductosShark adapter4 = new AdaptadorProductosShark(ListaProductosShark, context,Empresa);
            recyclerViewShark.setAdapter(adapter4);


            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewEagle.getChildAdapterPosition(Objects.requireNonNull(recyclerViewEagle.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosEagle.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewEagle.getChildAdapterPosition(Objects.requireNonNull(recyclerViewEagle.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosEagle.get(position).getClave();
                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 2;

                    }


                }
            });

            adapter1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewTrackone.getChildAdapterPosition(Objects.requireNonNull(recyclerViewTrackone.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosTrackone.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewTrackone.getChildAdapterPosition(Objects.requireNonNull(recyclerViewTrackone.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosTrackone.get(position).getClave();
                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 2;

                    }


                }
            });

            adapter2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewRodatech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewRodatech.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosRodatech.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewRodatech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewRodatech.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosRodatech.get(position).getClave();

                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 2;

                    }
                }
            });

            adapter3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewPartech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewPartech.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosPartech.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewPartech.getChildAdapterPosition(Objects.requireNonNull(recyclerViewPartech.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosPartech.get(position).getClave();

                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 2;

                    }

                }
            });

            adapter4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewShark.getChildAdapterPosition(Objects.requireNonNull(recyclerViewShark.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosShark.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewShark.getChildAdapterPosition(Objects.requireNonNull(recyclerViewShark.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosShark.get(position).getClave();
                        HomeFragment.AsyncClientes task1 = new HomeFragment.AsyncClientes();
                        task1.execute();
                        dato = 2;

                    }


                }
            });


            mDialog.dismiss();

        }


    }


    private void conectar3() {
        String SOAP_ACTION = "ProdcutosNuevos";
        String METHOD_NAME = "ProdcutosNuevos";
        String NAMESPACE = "http://" + StrServer + "/WSk75ClientesSOAP/";
        String URL = "http://" + StrServer + "/WSk75ClientesSOAP";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlProductosNuevos soapEnvelope = new xmlProductosNuevos(SoapEnvelope.VER11);
            soapEnvelope.xmlProductosNuevos(strusr, strpass,strcodBra);
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

                ListaProductosGeneral.add(new ProductosNuevosSANDG((response0.getPropertyAsString("k_Producto").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Producto")),
                        (response0.getPropertyAsString("k_Descripcion").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Descripcion")),
                        (response0.getPropertyAsString("k_Tipo").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Tipo"))));

            }


        } catch (XmlPullParserException | IOException soapFault) {
            mDialog.dismiss();
            soapFault.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
        }
    }



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Versiones extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Void doInBackground(Void... params) {
            Versiones();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Resultado==1){
                if (version.equals("2.4")) {

                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                    alerta.setMessage("La versión instalada no está actualizada por favor comuníquese con su proveedor para actualizar.").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            System.exit(0);
                            getActivity().finish();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Version desactualizada");
                    titulo.show();
                }
            }else{

            }

        }

    }


    private void Versiones() {

        String SOAP_ACTION = "Versiones";
        String METHOD_NAME = "Versiones";
        String NAMESPACE = "http://guvi.ath.cx:9085/WSk75ClientesSOAP/";
        String URL = "http://guvi.ath.cx:9085/WSk75ClientesSOAP";


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlVersiones soapEnvelope = new xmlVersiones(SoapEnvelope.VER11);
            soapEnvelope.xmlVersiones("WEBPETI", "W3B3P3T1", "1");
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;

            version =response0.getPropertyAsString("Version");


            Resultado=1;


        } catch (SoapFault | XmlPullParserException soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();
            soapFault.printStackTrace();
            Resultado=0;
        } catch (IOException e) {
            mDialog.dismiss();
            mensaje = "No se encontro servidor";
            Resultado=0;
            e.printStackTrace();
        } catch (Exception ex) {
            mDialog.dismiss();
            mensaje = "Error:" + ex.getMessage();
            Resultado=0;
        }

    }




}