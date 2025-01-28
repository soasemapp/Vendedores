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

import com.example.kepler201.Adapter.AdaptadorProductosGSP;
import com.example.kepler201.Adapter.AdaptadorProductosKFF;
import com.example.kepler201.Adapter.AdaptadorProductosMechanic;
import com.example.kepler201.Adapter.AdaptadorProductosNuevos;
import com.example.kepler201.Adapter.AdaptadorProductosPartech;
import com.example.kepler201.Adapter.AdaptadorProductosRodatech;
import com.example.kepler201.Adapter.AdaptadorProductosShark;
import com.example.kepler201.Adapter.AdaptadorProductosVazlo;
import com.example.kepler201.Adapter.AdaptadorProductosZoms;
import com.example.kepler201.Adapter.AdaptadorProductostrackone;
import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ProductosNuevosSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.activities.BusquedaActivity;
import com.example.kepler201.activities.DetalladoProductosActivity;
import com.example.kepler201.includes.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    RecyclerView recyclerViewEagle, recyclerViewTrackone, recyclerViewRodatech, recyclerViewPartech, recyclerViewShark,recyclerViewMechanic,recyclerViewGSP,recyclerViewVazlo,recyclerViewZoms,recyclerViewKFF;

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
    ArrayList<ProductosNuevosSANDG> ListaProductosMechanic = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosGSP = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductosVazlo = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductoszoms = new ArrayList<>();
    ArrayList<ProductosNuevosSANDG> ListaProductoskff = new ArrayList<>();


    EditText BusquedaProducto;
    String ProductosNuevosStr,Empresa;
    LinearLayout EagleOcultar, TrackOneOcultar, RodatechOcultar, PartechOcultar, SharkOcultar,MechanicOcultar,GspOcultar,VazloOcultar,ZoomsOcultar,KFFOcultar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mDialog = new SpotsDialog(getActivity());

        //FindView
        recyclerViewEagle = view.findViewById(R.id.listProductosEagle);
        recyclerViewTrackone = view.findViewById(R.id.listProductTrackone);
        recyclerViewRodatech = view.findViewById(R.id.listProductosRodatech);
        recyclerViewPartech = view.findViewById(R.id.listProductosPartech);
        recyclerViewShark = view.findViewById(R.id.listProductosShark);
        recyclerViewMechanic = view.findViewById(R.id.listProductosMechanic);
        recyclerViewGSP = view.findViewById(R.id.listProductosGSP);
        recyclerViewVazlo = view.findViewById(R.id.listProductosVazlo);
        recyclerViewZoms = view.findViewById(R.id.listProductoszoms);
        recyclerViewKFF = view.findViewById(R.id.listProductoskff);



        BusquedaProducto = view.findViewById(R.id.idBusqueda);
        EagleOcultar = view.findViewById(R.id.EagleOcultar);
        TrackOneOcultar = view.findViewById(R.id.TrackoneOcultar);
        RodatechOcultar = view.findViewById(R.id.RodatechOcultar);
        PartechOcultar = view.findViewById(R.id.PartechOcultar);
        SharkOcultar = view.findViewById(R.id.SharkOcultar);
        MechanicOcultar = view.findViewById(R.id.MechanicOcultar);
        GspOcultar = view.findViewById(R.id.GSPOcultar);
        VazloOcultar = view.findViewById(R.id.VazloOcultar);
        ZoomsOcultar = view.findViewById(R.id.ZoomsOcultar);
        KFFOcultar= view.findViewById(R.id.KFFOcultar);

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
                Empresa = "https://www.jacve.mx/tools/pictures-urlProductos?ids=";
                break;
            case "autodis.ath.cx:9085":
                Empresa = "https://www.cecra.mx/es-mx/img/products/xl/";
                break;
            case "cecra.ath.cx:9085":
                Empresa = "https://www.cecra.mx/es-mx/img/products/xl/";
                break;
            case "guvi.ath.cx:9085":
                Empresa = "https://www.guvi.mx/tools/pictures-urlProductos?ids=";
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

        ListaProductosMechanic = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMechanic.setLayoutManager(horizontalLayoutManagaer5);

        ListaProductosGSP = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer6 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGSP.setLayoutManager(horizontalLayoutManagaer6);


        ListaProductosVazlo = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer7 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVazlo.setLayoutManager(horizontalLayoutManagaer7);

        ListaProductoskff = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer8 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewKFF.setLayoutManager(horizontalLayoutManagaer8);

        ListaProductoszoms = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer9 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewZoms.setLayoutManager(horizontalLayoutManagaer9);




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
                VazloOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);

                break;
            case "sprautomotive.servehttp.com:9095":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.VISIBLE);
                VazloOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);
                break;
            case "sprautomotive.servehttp.com:9080":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.VISIBLE);
                VazloOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);
                break;
            case "vazlocolombia.dyndns.org:9085":
                EagleOcultar.setVisibility(View.VISIBLE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);
                VazloOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);
                break;
            case "autodis.ath.cx:9085":
                EagleOcultar.setVisibility(View.VISIBLE);
                TrackOneOcultar.setVisibility(View.VISIBLE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);
                VazloOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);
                break;
            case "jacve.dyndns.org:9085":
                EagleOcultar.setVisibility(View.GONE);
                TrackOneOcultar.setVisibility(View.GONE);
                RodatechOcultar.setVisibility(View.GONE);
                PartechOcultar.setVisibility(View.GONE);
                SharkOcultar.setVisibility(View.GONE);
                VazloOcultar.setVisibility(View.GONE);
                MechanicOcultar.setVisibility(View.VISIBLE);
                GspOcultar.setVisibility(View.VISIBLE);
                ZoomsOcultar.setVisibility(View.VISIBLE);
                KFFOcultar.setVisibility(View.VISIBLE);



                break;
            default:
                EagleOcultar.setVisibility(View.VISIBLE);
                TrackOneOcultar.setVisibility(View.VISIBLE);
                RodatechOcultar.setVisibility(View.VISIBLE);
                PartechOcultar.setVisibility(View.VISIBLE);
                SharkOcultar.setVisibility(View.VISIBLE);
                VazloOcultar.setVisibility(View.VISIBLE);
                MechanicOcultar.setVisibility(View.GONE);
                GspOcultar.setVisibility(View.GONE);
                ZoomsOcultar.setVisibility(View.GONE);
                KFFOcultar.setVisibility(View.GONE);

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
        ListaProductosMechanic = new ArrayList<>();
        ListaProductosGSP = new ArrayList<>();
        ListaProductosVazlo = new ArrayList<>();
        ListaProductoszoms = new ArrayList<>();
        ListaProductoskff = new ArrayList<>();
        conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("select * from productos ", null);
        if (fila != null && fila.moveToFirst()) {
            do {

                ListaProductosGeneral.add(new ProductosNuevosSANDG(fila.getString(1),
                        fila.getString(2),
                        fila.getString(3),
                        fila.getString(4)));

                switch (fila.getString(3)) {
                    case "1":
                        ListaProductosEagle.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;
                    case "2":
                        ListaProductosRodatech.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                    case "3":
                        ListaProductosPartech.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;
                    case "4":
                        ListaProductosShark.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;
                    case "6":
                        ListaProductosTrackone.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;

                    case "8":
                    case "9":
                    case "10":
                    case "11":
                    case "12":
                        ListaProductosGSP.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;
                    case "13":
                        ListaProductosMechanic.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
                        break;
                    case "14":
                       if (!StrServer.equals("jacve.dyndns.org:9085")){
                           ListaProductosVazlo.add(new ProductosNuevosSANDG(fila.getString(1),
                                   fila.getString(2),
                                   fila.getString(3),
                                   fila.getString(4)));
                       }else {
                           ListaProductoskff.add(new ProductosNuevosSANDG(fila.getString(1),
                                   fila.getString(2),
                                   fila.getString(3),
                                   fila.getString(4)));

                       }
                        break;
                    case "15":
                        ListaProductoszoms.add(new ProductosNuevosSANDG(fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)));
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
                AdaptadorProductosMechanic adapter5 = new AdaptadorProductosMechanic(ListaProductosMechanic, context,Empresa);
                recyclerViewMechanic.setAdapter(adapter5);
                AdaptadorProductosGSP adapter6 = new AdaptadorProductosGSP(ListaProductosGSP, context,Empresa);
                recyclerViewGSP.setAdapter(adapter6);
                AdaptadorProductosVazlo adapter7 = new AdaptadorProductosVazlo(ListaProductosVazlo, context,Empresa);
                recyclerViewVazlo.setAdapter(adapter7);
                AdaptadorProductosKFF adapter8= new AdaptadorProductosKFF(ListaProductoskff, context,Empresa);
                recyclerViewKFF.setAdapter(adapter8);
                AdaptadorProductosZoms adapter9 = new AdaptadorProductosZoms(ListaProductoszoms, context,Empresa);
                recyclerViewZoms.setAdapter(adapter9);




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

                adapter5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewMechanic.getChildAdapterPosition(Objects.requireNonNull(recyclerViewMechanic.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosMechanic.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewMechanic.getChildAdapterPosition(Objects.requireNonNull(recyclerViewMechanic.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosMechanic.get(position).getClave();

                            Listaclientes();
                            dato = 2;

                        }


                    }
                });

                adapter6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewGSP.getChildAdapterPosition(Objects.requireNonNull(recyclerViewGSP.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosGSP.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewGSP.getChildAdapterPosition(Objects.requireNonNull(recyclerViewGSP.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosGSP.get(position).getClave();

                            Listaclientes();
                            dato = 2;

                        }


                    }
                });

                adapter7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewVazlo.getChildAdapterPosition(Objects.requireNonNull(recyclerViewVazlo.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductosVazlo.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewVazlo.getChildAdapterPosition(Objects.requireNonNull(recyclerViewVazlo.findContainingItemView(view)));
                            ProductosNuevos = ListaProductosVazlo.get(position).getClave();

                            Listaclientes();
                            dato = 2;

                        }


                    }
                });


                adapter8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewKFF.getChildAdapterPosition(Objects.requireNonNull(recyclerViewKFF.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductoskff.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewKFF.getChildAdapterPosition(Objects.requireNonNull(recyclerViewKFF.findContainingItemView(view)));
                            ProductosNuevos = ListaProductoskff.get(position).getClave();

                            Listaclientes();
                            dato = 2;

                        }


                    }
                });


                adapter9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceClie.contains("CodeClien")) {
                            int position = recyclerViewZoms.getChildAdapterPosition(Objects.requireNonNull(recyclerViewZoms.findContainingItemView(view)));
                            Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                            String Producto = ListaProductoszoms.get(position).getClave();
                            ProductosDetallados.putExtra("Producto", Producto);
                            ProductosDetallados.putExtra("claveVentana", "1");
                            startActivity(ProductosDetallados);
                        } else {
                            int position = recyclerViewZoms.getChildAdapterPosition(Objects.requireNonNull(recyclerViewZoms.findContainingItemView(view)));
                            ProductosNuevos = ListaProductoszoms.get(position).getClave();

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
                                    (Numero.getString("k_Tipo").equals("") ? "" : Numero.getString("k_Tipo")),""));
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

 /*           String Productos="";
            for (int i = 0; i < ListaProductosGeneral.size(); i++) {

                if (i==0){
                    Productos = "{ \"ids\":\""+ListaProductosGeneral.get(i).getClave();
                }else if(i==ListaProductosGeneral.size()-1){
                    Productos =Productos+","+ ListaProductosGeneral.get(i).getClave()+"\"}";
                    }
                else{
                    Productos =Productos+","+ ListaProductosGeneral.get(i).getClave();
                }


                if (i==0){
                    Productos = ListaProductosGeneral.get(i).getClave();}
                else{
                    Productos =Productos+","+ ListaProductosGeneral.get(i).getClave();
                }


            }*/

         Imagenes task1 = new Imagenes();
            task1.execute();


        }


    }




    private class Imagenes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (StrServer.equals("jacve.dyndns.org:9085") || StrServer.equals("guvi.ath.cx:9085")) {
                String Productos = "";
                for (int i = 0; i < ListaProductosGeneral.size(); i++) {

                    if (i == 0) {
                        Productos = ListaProductosGeneral.get(i).getClave();
                    } else {
                        Productos = Productos + "," + ListaProductosGeneral.get(i).getClave();
                    }


                }

                HttpHandler sh = new HttpHandler();
                String url = Empresa + Productos;
                String jsonStr = sh.makeServiceCall(url, "", "");
                jsonStr = jsonStr.replace("\\", "");
                if (jsonStr != null) {
                    try {
                        // Convertir el JSON a un array
                        JSONArray jsonArray = new JSONArray(jsonStr);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objeto = jsonArray.getJSONObject(i);
                            objeto.getString("principal");
                            String url1 = objeto.getString("principal");
                            url1.replace("\\", "");
                            ListaProductosGeneral.get(i).setUrl(url1);
                        }


                    } catch (final JSONException e) {

                    }//catch JSON EXCEPTION
                } else {

                }//else


            }


            return null;

        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getActivity(), "bd_Carrito", null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            for (int i = 0; i < ListaProductosGeneral.size(); i++) {

                String Clave = ListaProductosGeneral.get(i).getClave();
                String Descripcion = ListaProductosGeneral.get(i).getDescripcion();
                String Tipo = ListaProductosGeneral.get(i).getTipo();
                String Url = ListaProductosGeneral.get(i).getUrl();

                db.execSQL("INSERT INTO  productos (Clave,Descripcion,Tipo,URL) values ('" + Clave + "','" + Descripcion + "','" + Tipo + "','" + Url + "')");
            }

            db.close();


            for (int i = 0; i < ListaProductosGeneral.size(); i++) {
                String Clave, Descripcion, Tipo,Url;
                Clave = ListaProductosGeneral.get(i).getClave();
                Descripcion = ListaProductosGeneral.get(i).getDescripcion();
                Tipo = ListaProductosGeneral.get(i).getTipo();
                Url=ListaProductosGeneral.get(i).getUrl();

                switch (Tipo) {
                    case "1":
                        ListaProductosEagle.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "2":
                        ListaProductosRodatech.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "3":
                        ListaProductosPartech.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "4":
                        ListaProductosShark.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "6":
                        ListaProductosTrackone.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "8":
                    case "9":
                    case "10":
                    case "11":
                    case "12":
                        ListaProductosGSP.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "13":
                        ListaProductosMechanic.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        break;
                    case "14":
                        if (!StrServer.equals("jacve.dyndns.org:9085")){
                            ListaProductosVazlo.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        }else{
                            ListaProductoskff.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
                        }
                        break;
                    case"15":
                        ListaProductoszoms.add(new ProductosNuevosSANDG(Clave, Descripcion, Tipo,Url));
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
            AdaptadorProductosMechanic adapter5 = new AdaptadorProductosMechanic(ListaProductosMechanic, context,Empresa);
            recyclerViewMechanic.setAdapter(adapter5);
            AdaptadorProductosGSP adapter6 = new AdaptadorProductosGSP(ListaProductosGSP, context,Empresa);
            recyclerViewGSP.setAdapter(adapter6);
            AdaptadorProductosVazlo adapter7 = new AdaptadorProductosVazlo(ListaProductosVazlo, context,Empresa);
            recyclerViewVazlo.setAdapter(adapter7);
            AdaptadorProductosKFF adapter8 = new AdaptadorProductosKFF(ListaProductoskff, context,Empresa);
            recyclerViewKFF.setAdapter(adapter8);
            AdaptadorProductosZoms adapter9 = new AdaptadorProductosZoms(ListaProductoszoms, context,Empresa);
            recyclerViewZoms.setAdapter(adapter9);


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

            adapter5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewMechanic.getChildAdapterPosition(Objects.requireNonNull(recyclerViewMechanic.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosMechanic.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewMechanic.getChildAdapterPosition(Objects.requireNonNull(recyclerViewMechanic.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosMechanic.get(position).getClave();

                        Listaclientes();
                        dato = 2;

                    }


                }
            });

            adapter6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewGSP.getChildAdapterPosition(Objects.requireNonNull(recyclerViewGSP.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosGSP.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewGSP.getChildAdapterPosition(Objects.requireNonNull(recyclerViewGSP.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosGSP.get(position).getClave();

                        Listaclientes();
                        dato = 2;

                    }


                }
            });

            adapter7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewVazlo.getChildAdapterPosition(Objects.requireNonNull(recyclerViewVazlo.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductosVazlo.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewVazlo.getChildAdapterPosition(Objects.requireNonNull(recyclerViewVazlo.findContainingItemView(view)));
                        ProductosNuevos = ListaProductosVazlo.get(position).getClave();

                        Listaclientes();
                        dato = 2;

                    }


                }
            });


            adapter8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewKFF.getChildAdapterPosition(Objects.requireNonNull(recyclerViewKFF.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductoskff.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewKFF.getChildAdapterPosition(Objects.requireNonNull(recyclerViewKFF.findContainingItemView(view)));
                        ProductosNuevos = ListaProductoskff.get(position).getClave();

                        Listaclientes();
                        dato = 2;

                    }


                }
            });

            adapter9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferenceClie.contains("CodeClien")) {
                        int position = recyclerViewZoms.getChildAdapterPosition(Objects.requireNonNull(recyclerViewZoms.findContainingItemView(view)));
                        Intent ProductosDetallados = new Intent(getActivity(), DetalladoProductosActivity.class);
                        String Producto = ListaProductoszoms.get(position).getClave();
                        ProductosDetallados.putExtra("Producto", Producto);
                        startActivity(ProductosDetallados);
                    } else {
                        int position = recyclerViewZoms.getChildAdapterPosition(Objects.requireNonNull(recyclerViewZoms.findContainingItemView(view)));
                        ProductosNuevos = ListaProductoszoms.get(position).getClave();

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
            String url = "http://"+StrServer+"/versionesapp?Clave=1";
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
                if (version.equals("2.9.8")) {

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