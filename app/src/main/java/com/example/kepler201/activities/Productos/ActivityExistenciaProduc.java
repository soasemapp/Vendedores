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
import com.example.kepler201.Adapter.AdaptadorExisClaPro;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ExistClasifiSANDG;
import com.example.kepler201.SetterandGetter.ListExiPrdoSANDG;
import com.example.kepler201.SetterandGetter.ListLineaSANDG;
import com.example.kepler201.SetterandGetter.listDipoSucuSANDG;
import com.example.kepler201.XMLS.xmlDispoSuc;
import com.example.kepler201.XMLS.xmlExistClasif;
import com.example.kepler201.XMLS.xmlListExiPro;
import com.example.kepler201.XMLS.xmlListLine;
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

public class ActivityExistenciaProduc extends AppCompatActivity {

    private Spinner Spinnerclasifi;
    private Spinner SpinnerLinea;
    AlertDialog mDialog;
    String mensaje = "";
    RecyclerView recyclerExisteClasiFic;
    ImageView SearchProducto, Existencia, ListaPrecios, Conversiones;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    String strClasifi = "";
    String strLinea ="";
    Button btnSearch;
    ArrayList<ListExiPrdoSANDG> listaClasi = new ArrayList<>();
    ArrayList<ExistClasifiSANDG> listaClaExi = new ArrayList<>();
    ArrayList<listDipoSucuSANDG> listaExistencia = new ArrayList<>();
    ArrayList<ListLineaSANDG> listaLinea = new ArrayList<>();
    String Productoadd;
    String Precioadd;
    String Existenciaadd;
    String Descripcionadd;


    Context context = this;
    int n = 2000;
    String[] search1 = new String[n];

    CheckBox linea;
    CheckBox Clasificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existencia_produc);

        MyToolbar.show(this, "Productos-Existencia", true);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        mDialog = new SpotsDialog(ActivityExistenciaProduc.this);

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

        recyclerExisteClasiFic =  findViewById(R.id.listExiste);
        Spinnerclasifi =  findViewById(R.id.SpinnerClasi);
        btnSearch =  findViewById(R.id.btnSearch);
        SpinnerLinea =  findViewById(R.id.spinnerLinea);
        SearchProducto =  findViewById(R.id.SearchProducto);
        Existencia =  findViewById(R.id.Existencia);
        ListaPrecios =  findViewById(R.id.ListaPrecios);
        Conversiones =  findViewById(R.id.Conversiones);


        linea =  findViewById(R.id.chckLinea);
        Clasificacion =  findViewById(R.id.checkClas);

        Clasificacion.setChecked(false);
        Spinnerclasifi.setEnabled(false);
        linea.setChecked(true);
        SpinnerLinea.setEnabled(true);

        linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linea.setChecked(true);
                if (linea.isChecked()) {
                    SpinnerLinea.setEnabled(true);
                    Clasificacion.setChecked(false);
                    linea.setChecked(true);
                    Spinnerclasifi.setEnabled(false);
                    Spinnerclasifi.setSelection(0);
                }
            }
        });
        Clasificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clasificacion.setChecked(true);
                if (Clasificacion.isChecked()) {


                    Spinnerclasifi.setEnabled(true);
                    linea.setChecked(false);
                    Clasificacion.setChecked(true);
                    SpinnerLinea.setEnabled(false);
                    SpinnerLinea.setSelection(0);
                }

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listaClaExi = new ArrayList<>();
                    recyclerExisteClasiFic.setLayoutManager(new LinearLayoutManager(ActivityExistenciaProduc.this));

                    for (int i = 0; i < search1.length; i++) {
                        int posi = SpinnerLinea.getSelectedItemPosition();
                        if (posi == i) {
                            strLinea = search1[i];
                            break;
                        }
                    }
                    strClasifi =Spinnerclasifi.getSelectedItem().toString();


                    if (SpinnerLinea.getSelectedItemPosition() == 0 && Spinnerclasifi.getSelectedItemPosition() != 0 && Clasificacion.isChecked()) {
                        listaClaExi = new ArrayList<>();
                        recyclerExisteClasiFic.setLayoutManager(new LinearLayoutManager(ActivityExistenciaProduc.this));
                        strLinea = "";
                        ActivityExistenciaProduc.AsyncCallWS2 task2 = new ActivityExistenciaProduc.AsyncCallWS2();
                        task2.execute();

                    } else if (SpinnerLinea.getSelectedItemPosition() != 0 && Spinnerclasifi.getSelectedItemPosition() == 0 && linea.isChecked()) {
                        strClasifi = "";
                        ActivityExistenciaProduc.AsyncCallWS2 task2 = new ActivityExistenciaProduc.AsyncCallWS2();
                        task2.execute();

                    } else if (SpinnerLinea.getSelectedItemPosition() == 0 && Spinnerclasifi.getSelectedItemPosition() == 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityExistenciaProduc.this);
                        alerta.setMessage("Selecciona una Clasificacion o Linea para realizar la busqueda ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                Intent SearchPro = new Intent(ActivityExistenciaProduc.this, ActivityConsultaProductos.class);
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
                Intent Existenc = new Intent(ActivityExistenciaProduc.this, ActivityExistenciaProduc.class);
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
                Intent Listpr = new Intent(ActivityExistenciaProduc.this, ListaPreciosActivity.class);
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
                Intent Conver = new Intent(ActivityExistenciaProduc.this, ActivityConverciones.class);
                overridePendingTransition(0, 0);
                startActivity(Conver);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ActivityExistenciaProduc.AsyncCallWS task = new ActivityExistenciaProduc.AsyncCallWS();
        task.execute();
        ActivityExistenciaProduc.AsyncCallWS3 task3 = new ActivityExistenciaProduc.AsyncCallWS3();
        task3.execute();

        listaClaExi = new ArrayList<>();
        recyclerExisteClasiFic.setLayoutManager(new LinearLayoutManager(this));


    }



   public void addCarExis(View view) {
        int position = recyclerExisteClasiFic.getChildAdapterPosition(Objects.requireNonNull(recyclerExisteClasiFic.findContainingItemView(view)));
        Productoadd = listaClaExi.get(position).getProducto();
        Precioadd = listaClaExi.get(position).getPrecio();
        Existenciaadd = listaClaExi.get(position).getExistencia1();
        Descripcionadd = listaClaExi.get(position).getDescripcion();
        Intent Coti = new Intent(ActivityExistenciaProduc.this, ActivityConsultaProductos.class);
        Coti.putExtra("Producto", Productoadd);
        Coti.putExtra("Precio", Precioadd);
        Coti.putExtra("Existencia", "");
        Coti.putExtra("Descripcion", Descripcionadd);
        Coti.putExtra("Cantidad", "1");
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
            conectar3();
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

    private void conectar3() {
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
            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
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
    private class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

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

            String[] opciones = new String[listaClasi.size() + 1];
            opciones[0] = "--Todas las clasificaciónes--";

            for (int i = 1; i <= listaClasi.size(); i++) {
                opciones[i] = listaClasi.get(i - 1).getClasifi();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            Spinnerclasifi.setAdapter(adapter);
        }


    }

    private void conectar() {
        String SOAP_ACTION = "ListClasi";
        String METHOD_NAME = "ListClasi";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlListExiPro soapEnvelope = new xmlListExiPro(SoapEnvelope.VER11);
            soapEnvelope.xmlListExiP(strusr, strpass);
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

                if(!response0.getPropertyAsString("k_Clasificacion").equals("anyType{}")) {

                    listaClasi.add(new ListExiPrdoSANDG((response0.getPropertyAsString("k_Clasificacion").equals("anyType{}")? "":response0.getPropertyAsString("k_Clasificacion"))));
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
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar2();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            AdaptadorExisClaPro adapter = new AdaptadorExisClaPro(listaClaExi,context);
            recyclerExisteClasiFic.setAdapter(adapter);
            mDialog.dismiss();
        }
    }

    public void EXISTENCIAProduc (View view){
        listaExistencia.clear();
        int position = recyclerExisteClasiFic.getChildAdapterPosition(Objects.requireNonNull(recyclerExisteClasiFic.findContainingItemView(view)));
        Productoadd = listaClaExi.get(position).getProducto();
        ActivityExistenciaProduc.Existencia task = new ActivityExistenciaProduc.Existencia();
        task.execute();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Existencia extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ExistenciaCla();
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

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityExistenciaProduc.this);
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
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityExistenciaProduc.this);
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

    private void ExistenciaCla() {
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

            int json=response.getPropertyCount();
            for (int i = 0; i < json; i++) {
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
    private void conectar2() {

        String SOAP_ACTION = "ExistClasif";
        String METHOD_NAME = "ExistClasif";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlExistClasif soapEnvelope = new xmlExistClasif(SoapEnvelope.VER11);
            soapEnvelope.xmlExistClas(strusr, strpass, strClasifi,strcodBra,strLinea);
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

                listaClaExi.add(new ExistClasifiSANDG((response0.getPropertyAsString("k_Producto").equals("anyType{}") ? "S/N" : response0.getPropertyAsString("k_Producto")),
                        (response0.getPropertyAsString("k_Precio").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_Precio")),
                        (response0.getPropertyAsString("k_Descripcion").equals("anyType{}") ? "S/N" : response0.getPropertyAsString("k_Descripcion")),
                        (response0.getPropertyAsString("k_CodBar").equals("anyType{}") ? "S/N" : response0.getPropertyAsString("k_CodBar")),
                        (response0.getPropertyAsString("k_EXISTENCIA1").equals("anyType{}") ? "0" : response0.getPropertyAsString("k_EXISTENCIA1"))));


            }


        } catch (SoapFault | XmlPullParserException soapFault) {
            mDialog.dismiss();
            mensaje = "Error:" + soapFault.getMessage();

        } catch (IOException e) {
            mDialog.dismiss();
            mensaje = "No se encontro servidor";

        } catch (Exception ex) {
            mDialog.dismiss();
            mensaje = "Error:" + ex.getMessage();
        }
    }

}