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
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.activities.DetalladoProductosActivity;
import com.example.kepler201.activities.MainActivity;
import com.example.kepler201.activities.inicioActivity;
import com.example.kepler201.includes.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;
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
            default:
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
                    ProductosNuevosAscy();
                } else {
                    Consulta();
                }

            }else{

                editor.putString("Productosnuevos", "0");
                editor.apply();
                Consulta();

            }


        }else{
            ProductosNuevosAscy();

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

                        Listaclientes();
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

                            Listaclientes();
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

                            Listaclientes();
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


                            Listaclientes();
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


                            Listaclientes();
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

                            Listaclientes();
                            dato = 2;

                        }


                    }
                });

            }

        }

        db.close();

    }

    public void CarritoComprasASYC() {
        new HomeFragment.CarritoComprasay().execute();
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
            String parametros = "cliente=" + strscliente+"&producto=1000R&cantidad=1&existencia=0&sucursal="+strcodBra;
            String url = "http://" + StrServer + "/carritoapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Cotizacion");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("Cotizacion");
                        Numero = jitems.getJSONObject("" + i + "");

                        rfc = (Numero.getString("k_rfc").equals("anyType{}") ? " " : Numero.getString("k_rfc"));
                        plazo = (Numero.getString("k_plazo").equals("anyType{}") ? " " : Numero.getString("k_plazo"));
                        Calle = (Numero.getString("k_calle").equals("anyType{}") ? " " : Numero.getString("k_calle"));
                        Colonia = (Numero.getString("k_colo").equals("anyType{}") ? " " : Numero.getString("k_colo"));
                        Poblacion = (Numero.getString("k_pobla").equals("anyType{}") ? " " : Numero.getString("k_pobla"));
                        Via = (Numero.getString("k_via").equals("anyType{}") ? " " : Numero.getString("k_via"));
                        K87 = (Numero.getString("k_87").equals("anyType{}") ? "0" : Numero.getString("k_87"));
                        Desc1fa = (Numero.getString("k_desc1fac").equals("anyType{}") ? "0" : Numero.getString("k_desc1fac"));
                        Comentario1 = (Numero.getString("k_comentario1").equals("anyType{}") ? "" : Numero.getString("k_comentario1"));
                        Comentario2 = (Numero.getString("k_comentario2").equals("anyType{}") ? "" : Numero.getString("k_comentario2"));
                        Comentario3 = (Numero.getString("k_comentario3").equals("anyType{}") ? "" : Numero.getString("k_comentario3"));



                    }
                } catch (final JSONException e) {

                }//catch JSON EXCEPTION
            } else {

            }//else
            return null;

        }//doInBackground
        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            if (preferenceClie.contains("CodeClien")) {

            } else {
                guardarDatos();
            }
        }//onPost
    }


    public void Listaclientes() {
        new HomeFragment.Cliente().execute();
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
            String parametros = "vendedor=" + strco;
            String url = "http://" + StrServer + "/listaclientesapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);


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
                } catch (final JSONException e) {

                }//catch JSON EXCEPTION
            } else {

            }//else
            return null;

        }//doInBackground

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
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
                    CarritoComprasASYC();
                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            mDialog.dismiss();
        }//onPost
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



    public void ProductosNuevosAscy() {
        new HomeFragment.ProductosNuevosa().execute();
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class ProductosNuevosa extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/listapronuevosapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {



                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("Item");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Item");
                            Numero = jitems.getJSONObject("" + i + "");

                            ListaProductosGeneral.add(new ProductosNuevosSANDG((Numero.getString("k_Producto").equals("") ? "" : Numero.getString("k_Producto")),
                                    (Numero.getString("k_Descripcion").equals("") ? "" : Numero.getString("k_Descripcion")),
                                    (Numero.getString("k_Tipo").equals("") ? "" : Numero.getString("k_Tipo"))));
                        }
                    }
                } catch (final JSONException e) {
String mensaje =e.getMessage().toString();
                }//catch JSON EXCEPTION
            } else {

            }//else
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

                        Listaclientes();
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

                        Listaclientes();
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

                        Listaclientes();
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


                        Listaclientes();
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

                        Listaclientes();
                        dato = 2;

                    }


                }
            });


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
            HttpHandler sh = new HttpHandler();
            String url = "http://jacve.dyndns.org:9085/versionesapp?Clave=1";
            String jsonStr = sh.makeServiceCall(url, "WEBPETI", "W3B3P3T1");
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        version = jsonObject.getString("Version");


                        Resultado = 1;
                    }
                } catch (final JSONException e) {

                }//catch JSON EXCEPTION
            } else {

            }//else
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Resultado==1){
                if (version.equals("2.8")) {

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

}