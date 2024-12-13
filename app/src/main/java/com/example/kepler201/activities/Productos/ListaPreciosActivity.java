package com.example.kepler201.activities.Productos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.ActivityBackOrdersAdd;
import com.example.kepler201.Adapter.AdapterListPrecio;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ListLineaSANDG;
import com.example.kepler201.SetterandGetter.ListPrecSANDG;
import com.example.kepler201.SetterandGetter.ListTypeSANDG;
import com.example.kepler201.SetterandGetter.listDipoSucuSANDG;
import com.example.kepler201.XMLS.xmlDispoSuc;
import com.example.kepler201.XMLS.xmlListLine;
import com.example.kepler201.XMLS.xmlListPrecio;
import com.example.kepler201.XMLS.xmlListType;
import com.example.kepler201.activities.DetalladoProductosActivity;
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
import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ListaPreciosActivity extends AppCompatActivity {

    private Spinner SpinnerLinea;
    private Spinner SpinnerTipo;
    AlertDialog mDialog;
    String mensaje = "";
    RecyclerView recyclerPrecios;
    ImageView SearchProducto, Existencia, ListaPrecios, Conversiones;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    String strLinea = "", strType = "";
    Button btnSearch;
    ArrayList<ListLineaSANDG> listaLinea = new ArrayList<>();
    ArrayList<ListTypeSANDG> listaTipo = new ArrayList<>();
    ArrayList<listDipoSucuSANDG> listaExistencia = new ArrayList<>();
    ArrayList<ListPrecSANDG> listaLisPrec = new ArrayList<>();
    Context context=this ;
    String Productoadd;
    String Precioadd;
    String Existenciaadd;
    String Descripcionadd;
    String Empresa="";
    CheckBox linea;
    CheckBox tipo;

    int n = 2000;
    String[] search1 = new String[n];

    int n1 = 2000;
    String[] search2 = new String[n1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_precios);

        MyToolbar.show(this, "Productos-Lista de Precios", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mDialog = new SpotsDialog(ListaPreciosActivity.this);
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

        recyclerPrecios =  findViewById(R.id.listprec);
        SpinnerLinea =  findViewById(R.id.spinnerLinea);
        SpinnerTipo =  findViewById(R.id.spinnerType);
        btnSearch =  findViewById(R.id.btnSearch);
        linea =  findViewById(R.id.checkLinea);
        tipo =  findViewById(R.id.checkType);


        switch (StrServer) {
            case "jacve.dyndns.org:9085":
                Empresa = "https://www.jacve.mx/imagenes/";
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
                Empresa = "https://vazlo.com.mx/assets/img/productos/chica/jpg/";
                break;
        }


        SearchProducto =  findViewById(R.id.SearchProducto);
        Existencia =  findViewById(R.id.Existencia);
        ListaPrecios =  findViewById(R.id.ListaPrecios);
        Conversiones =  findViewById(R.id.Conversiones);

        tipo.setChecked(false);
        SpinnerTipo.setEnabled(false);
        linea.setChecked(true);
        SpinnerLinea.setEnabled(true);

        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linea.setChecked(true);
                if (linea.isChecked()) {
                    SpinnerLinea.setEnabled(true);
                    tipo.setChecked(false);
                    linea.setChecked(true);
                    SpinnerTipo.setEnabled(false);
                    SpinnerTipo.setSelection(0);
                }
            }
        });
        tipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipo.setChecked(true);
                if (tipo.isChecked()) {
                    SpinnerTipo.setEnabled(true);
                    linea.setChecked(false);
                    tipo.setChecked(true);
                    SpinnerLinea.setEnabled(false);
                    SpinnerLinea.setSelection(0);
                }
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    listaLisPrec = new ArrayList<>();
                    recyclerPrecios.setLayoutManager(new LinearLayoutManager(ListaPreciosActivity.this));

                    for (int i = 0; i < search1.length; i++) {
                        int posi = SpinnerLinea.getSelectedItemPosition();
                        if (posi == i) {
                            strLinea = search1[i];
                            break;
                        }
                    }


                    for (int i = 0; i < search2.length; i++) {
                        int posi = SpinnerTipo.getSelectedItemPosition();
                        if (posi == i) {
                            strType = search2[i];
                            break;
                        }
                    }

                    if (SpinnerLinea.getSelectedItemPosition() == 0 && SpinnerTipo.getSelectedItemPosition() != 0 && tipo.isChecked()) {
                        listaLisPrec = new ArrayList<>();
                        recyclerPrecios.setLayoutManager(new LinearLayoutManager(ListaPreciosActivity.this));
                        strLinea = "";
                        ListaPreciosActivity.AsyncCallWS3 task3 = new ListaPreciosActivity.AsyncCallWS3();
                        task3.execute();

                    } else if (SpinnerLinea.getSelectedItemPosition() != 0 && SpinnerTipo.getSelectedItemPosition() == 0 && linea.isChecked()) {
                        strType = "";
                        ListaPreciosActivity.AsyncCallWS3 task3 = new ListaPreciosActivity.AsyncCallWS3();
                        task3.execute();

                    } else if (SpinnerLinea.getSelectedItemPosition() == 0 && SpinnerTipo.getSelectedItemPosition() == 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ListaPreciosActivity.this);
                        alerta.setMessage("Selecciona una linea o tipo para realizar la busqueda ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("¡ERROR DE BUSQUEDA!");
                        titulo.show();
                    }


            }
        });

        SearchProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchProducto.setBackgroundColor(Color.GRAY);
                SearchProducto.setBackgroundColor(Color.TRANSPARENT);
                Intent SearchPro = new Intent(ListaPreciosActivity.this, ActivityConsultaProductos.class);
                overridePendingTransition(0, 0);
                startActivity(SearchPro);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        Existencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Existencia.setBackgroundColor(Color.GRAY);
                Existencia.setBackgroundColor(Color.TRANSPARENT);
                Intent Existenc = new Intent(ListaPreciosActivity.this, ActivityExistenciaProduc.class);
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
                Intent Listpr = new Intent(ListaPreciosActivity.this, ListaPreciosActivity.class);
                overridePendingTransition(0, 0);
                startActivity(Listpr);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        Conversiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conversiones.setBackgroundColor(Color.GRAY);
                Conversiones.setBackgroundColor(Color.TRANSPARENT);
                Intent Conver = new Intent(ListaPreciosActivity.this, ActivityConverciones.class);
                overridePendingTransition(0, 0);
                startActivity(Conver);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ListaPreciosActivity.AsyncCallWS task = new ListaPreciosActivity.AsyncCallWS();
        task.execute();
        ListaPreciosActivity.AsyncCallWS2 task2 = new ListaPreciosActivity.AsyncCallWS2();
        task2.execute();


    }


    public void addCarListPrecio(View view) {
        int position = recyclerPrecios.getChildPosition(Objects.requireNonNull(recyclerPrecios.findContainingItemView(view)));
        Productoadd = listaLisPrec.get(position).getCodeProdu();

        Intent Coti = new Intent(ListaPreciosActivity.this, DetalladoProductosActivity.class);
        Coti.putExtra("Producto", Productoadd);
        Coti.putExtra("claveVentana", "1");

        overridePendingTransition(0, 0);
        startActivity(Coti);
        overridePendingTransition(0, 0);
        finish();
    }



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/ListLiPreapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("item");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("item");
                        Numero = jitems.getJSONObject("" + i + "");

                        if(!Numero.getString("k_Linea").equals("")) {
                            listaLinea.add(new ListLineaSANDG((Numero.getString("k_CodeLinea").equals("") ? "" : Numero.getString("k_CodeLinea")),
                                    (Numero.getString("k_Linea").equals("") ? "" : Numero.getString("k_Linea"))));
                        }


                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            String[] opciones = new String[listaLinea.size() + 1];
            opciones[0] = "--Todas las Lineas--";
            search1[0] = "--Todas las Lineas--";

            for (int i = 1; i <= listaLinea.size(); i++) {
                opciones[i] = listaLinea.get(i - 1).getLinea();
                search1[i] = listaLinea.get(i - 1).getCodeLinea();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            SpinnerLinea.setAdapter(adapter);
        }


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer + "/ListLiTyapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("item");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("item");
                        Numero = jitems.getJSONObject("" + i + "");

                        if(!Numero.getString("k_Type").equals("")){

                            listaTipo.add(new ListTypeSANDG((Numero.getString("k_CodeType").equals("")?"":Numero.getString("k_CodeType")),
                                    (Numero.getString("k_Type").equals("")?"":Numero.getString("k_Type"))));

                        }


                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            String[] opciones = new String[listaTipo.size() + 1];
            opciones[0] = "--Todos los Tipos--";
            search2[0] = "--Todas las Lineas--";


            for (int i = 1; i <= listaTipo.size(); i++) {
                opciones[i] = listaTipo.get(i - 1).getType();
                search2[i] = listaTipo.get(i - 1).getCodeType();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            SpinnerTipo.setAdapter(adapter);
            mDialog.dismiss();
        }


    }



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String parametros ;
            if(linea.isChecked()){
                parametros = "linea=" + strLinea+"&sucursal="+strcodBra;
            }else{
                parametros = "tipo="+strType+"&sucursal="+strcodBra;
            }

            String url = "http://" + StrServer + "/lisprecioapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Item");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("Item");
                        Numero = jitems.getJSONObject("" + i + "");

                        listaLisPrec.add(new ListPrecSANDG((Numero.getString("k_CodeProd").equals("") ? " " : Numero.getString("k_CodeProd")),
                                (Numero.getString("k_NomPro").equals("") ? " " : Numero.getString("k_NomPro")),
                                (Numero.getString("k_CodBarras").equals("") ? "S/N" : Numero.getString("k_CodBarras")),
                                (Numero.getString("k_Existencia1").equals("") ? "" : Numero.getString("k_Existencia1")),
                                (Numero.getString("k_Importe").equals("") ? " " : Numero.getString("k_Importe"))));



                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            AdapterListPrecio adapter = new AdapterListPrecio(listaLisPrec,context,Empresa);
            recyclerPrecios.setAdapter(adapter);
            mDialog.dismiss();
        }


    }


    public void ListaPrecExiste (View view){
        listaExistencia.clear();
        int position = recyclerPrecios.getChildAdapterPosition(Objects.requireNonNull(recyclerPrecios.findContainingItemView(view)));
        Productoadd = listaLisPrec.get(position).getCodeProdu();
        ListaPreciosActivity.ListaPre task = new ListaPreciosActivity.ListaPre();
        task.execute();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class ListaPre extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "sucursal=" + strcodBra+"&producto="+Productoadd;
            String url = "http://" + StrServer + "/disposucapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jitems, Numero, Clave, Nombre;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jitems = jsonObject.getJSONObject("Item");

                    for (int i = 0; i < jitems.length(); i++) {
                        jitems = jsonObject.getJSONObject("Item");
                        Numero = jitems.getJSONObject("" + i + "");

                        listaExistencia.add(new listDipoSucuSANDG(
                                (Numero.getString("k_Clave").equals("")? " " : Numero.getString("k_Clave")),
                                (Numero.getString("k_Nom").equals("")? " " : Numero.getString("k_Nom")),
                                (Numero.getString("k_Disp").equals("")? " " : Numero.getString("k_Disp"))));



                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ListaPreciosActivity.this);
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
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            if (listaExistencia.size()>0){
                StringBuilder mensaje= new StringBuilder();
                for (int i = 0; i < listaExistencia.size(); i++) {
                    mensaje.append(listaExistencia.get(i).getNombre()).append(" = ").append(listaExistencia.get(i).getDisponibilidad()).append(" PZA ").append("\n");
                }

                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaPreciosActivity.this);
                alerta.setMessage(mensaje.toString()).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Producto:"+Productoadd);
                titulo.show();

            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(ListaPreciosActivity.this);
                alerta.setMessage("No hay existencia para este producto").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Error");
                titulo.show();
            }

            mDialog.dismiss();
        }


    }

}