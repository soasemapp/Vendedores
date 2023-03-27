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
import com.example.kepler201.includes.MyToolbar;

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

        mDialog = new SpotsDialog.Builder().setContext(ListaPreciosActivity.this).setMessage("Espere un momento...").build();
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
        int position = recyclerPrecios.getChildAdapterPosition(Objects.requireNonNull(recyclerPrecios.findContainingItemView(view)));
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
            conectar();
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

    private void conectar() {
        String SOAP_ACTION = "ListLiPre";
        String METHOD_NAME = "ListLiPre";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListLine soapEnvelope = new xmlListLine(SoapEnvelope.VER11);
            soapEnvelope.xmlListLine(strusr, strpass);
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

                if(!response0.getPropertyAsString("k_Linea").equals("anyType{}")) {
                    listaLinea.add(new ListLineaSANDG((response0.getPropertyAsString("k_CodeLinea").equals("anyType{}") ? "" : response0.getPropertyAsString("k_CodeLinea")),
                            (response0.getPropertyAsString("k_Linea").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Linea"))));
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


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
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

    private void conectar2() {
        String SOAP_ACTION = "ListLiTy";
        String METHOD_NAME = "ListLiTy";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListType soapEnvelope = new xmlListType(SoapEnvelope.VER11);
            soapEnvelope.xmlListType(strusr, strpass);
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
                if(!response0.getPropertyAsString("k_Type").equals("anyType{}")){

                    listaTipo.add(new ListTypeSANDG((response0.getPropertyAsString("k_CodeType").equals("anyType{}")?"":response0.getPropertyAsString("k_CodeType")),
                            (response0.getPropertyAsString("k_Type").equals("anyType{}")?"":response0.getPropertyAsString("k_Type"))));

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



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

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

            AdapterListPrecio adapter = new AdapterListPrecio(listaLisPrec,context,Empresa);
            recyclerPrecios.setAdapter(adapter);
            mDialog.dismiss();
        }


    }

    private void conectar3() {
        String SOAP_ACTION = "ListPrec";
        String METHOD_NAME = "ListPrec";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListPrecio soapEnvelope = new xmlListPrecio(SoapEnvelope.VER11);
            soapEnvelope.xmlListPrecio(strusr, strpass, strLinea, strType,strcodBra);
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
                listaLisPrec.add(new ListPrecSANDG((response0.getPropertyAsString("k_CodeProd").equals("anyType{}") ? " " : response0.getPropertyAsString("k_CodeProd")),
                        (response0.getPropertyAsString("k_NomPro").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NomPro")),
                        (response0.getPropertyAsString("k_CodBarras").equals("anyType{}") ? "S/N" : response0.getPropertyAsString("k_CodBarras")),
                        (response0.getPropertyAsString("k_Existencia1").equals("anyType{}") ? "" : response0.getPropertyAsString("k_Existencia1")),
                        (response0.getPropertyAsString("k_Importe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Importe"))));
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
            ListPrec();
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

        }


    }

    private void ListPrec() {
        String SOAP_ACTION = "DispoSuc";
        String METHOD_NAME = "DispoSuc";
        String NAMESPACE = "http://" + StrServer + "/WSk75items/";
        String URL = "http://" + StrServer + "/WSk75items";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlDispoSuc soapEnvelope = new xmlDispoSuc(SoapEnvelope.VER11);
            soapEnvelope.xmlDispoSuc(strusr, strpass, Productoadd, strcodBra);
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
                listaExistencia.add(new listDipoSucuSANDG(
                        (response0.getPropertyAsString("k_Clave").equals("anyType{}")? " " : response0.getPropertyAsString("k_Clave")),
                        (response0.getPropertyAsString("k_Nom").equals("anyType{}")? " " : response0.getPropertyAsString("k_Nom")),
                        (response0.getPropertyAsString("k_Disp").equals("anyType{}")? " " : response0.getPropertyAsString("k_Disp"))));

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
}