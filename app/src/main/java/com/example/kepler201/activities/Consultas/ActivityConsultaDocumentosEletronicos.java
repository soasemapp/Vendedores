package com.example.kepler201.activities.Consultas;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdaptadorConsulDocuEle;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.ConsulFacfturasSANDG;
import com.example.kepler201.SetterandGetter.SearachClientSANDG;
import com.example.kepler201.SetterandGetter.SucursalSANDG;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import dmax.dialog.SpotsDialog;


public class ActivityConsultaDocumentosEletronicos extends AppCompatActivity {

    private EditText fechaEn, fechaSa;
    TextView txttipodocumento;
    String TIPO = "";
    AlertDialog mDialog;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcodBra, strcode, StrServer;
    ArrayList<SearachClientSANDG> listaclientG = new ArrayList<>();
    String clientestr="",sucursalstr="";
    String date;
    String date2;
    Button btntipdocu,btncliente,btnsucursal;
    String FechaIncial, FechaFinal;
    ArrayList<SucursalSANDG> listasucursal = new ArrayList<>();
    String parametrosfactura="";
    EditText edfolio,uuided;
    String strfolio="",struuid="";
    RecyclerView recyclerConsulta;
    ArrayList<ConsulFacfturasSANDG> listasearch = new ArrayList<>();
    String ClaveFolDialog,ClaveNumDialog,Cliente;
    String base64pdf="",base64xml="";
    Context context=this;
    String tipodialog;

    String ptoOp,ptoCons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_docelect);

        MyToolbar.show(this, "E-Documentos", true);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        mDialog = new SpotsDialog(ActivityConsultaDocumentosEletronicos.this);
        mDialog.setCancelable(false);

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

        ptoOp = preference.getString("PuertoOperativo", "");
        ptoCons = preference.getString("PuertoConsulta", "");

        txttipodocumento = findViewById(R.id.tipdoc);
        btntipdocu = findViewById(R.id.btntipodocu);
        btncliente = findViewById(R.id.btncliente);
        btnsucursal = findViewById(R.id.btnsucursal);
        fechaEn = findViewById(R.id.fechaendtrada);
        fechaSa = findViewById(R.id.fechasalida);
        edfolio = findViewById(R.id.edfolio);
        uuided = findViewById(R.id.uuided);
        recyclerConsulta = findViewById(R.id.lisFacturas);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaDocumentosEletronicos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        fechaEn.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        fechaSa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityConsultaDocumentosEletronicos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date2 = year + "-" + month + "-" + day;
                        fechaSa.setText(date2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually1 = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.MONTH, -1);
        String fechaasalll = dateformatActually1.format(c.getTime());

        Calendar c1 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        String fechaactual = dateformatActually.format(c1.getTime());
        fechaEn.setText(fechaasalll);
        fechaSa.setText(fechaactual);


        FechaIncial = fechaEn.getText().toString();
        FechaFinal = fechaSa.getText().toString();

    }

    public void tipodoc(View view) {

        String[] opciones1 = {"1.-Facturas", "2.-Notas de Credito", "3.-Devoluciones", "4.-Complementos"};


        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
        builder.setTitle("Seleccione el tipo de documento").setIcon(R.drawable.icon_documento);


        builder.setItems(opciones1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        txttipodocumento.setText("Facturas");
                        TIPO = "1";
                        btntipdocu.setText("Facturas");
                        tipodialog="Facturas";
                        break;
                    case 1:
                        txttipodocumento.setText("Notas de Credito");
                        TIPO = "2";
                        btntipdocu.setText("Notas de Credito");
                        tipodialog="Notas de Credito";
                        break;
                    case 2:
                        txttipodocumento.setText("Devoluciones");
                        TIPO = "3";
                        btntipdocu.setText("Devoluciones");
                        tipodialog="Devoluciones";
                        break;
                    case 3:
                        txttipodocumento.setText("Complementos");
                        TIPO = "4";
                        btntipdocu.setText("Complementos");
                        tipodialog="Complementos";
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void ListaSucursales(View view) {
        listasucursal.clear();
        new SucursalesLista().execute();
    }

    private class SucursalesLista extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url = "http://" + StrServer+ptoCons + "/listasucursalapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {
                        if (json.length() != 0) {
                            JSONObject jitems, Numero;
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            jitems = jsonObject.getJSONObject("Listado");

                            for (int i = 0; i < jitems.length(); i++) {
                                jitems = jsonObject.getJSONObject("Listado");
                                Numero = jitems.getJSONObject("" + i + "");
                                listasucursal.add(new SucursalSANDG(
                                        Numero.getString("clave"),
                                        Numero.getString("nombre")));
                            }
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
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

                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
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
            String[] opciones = new String[listasucursal.size()];

            for (int i = 0; i < listasucursal.size(); i++) {
                opciones[i] = listasucursal.get(i).getNombre();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
            builder.setTitle("Seleccione una Sucursal").setIcon(R.drawable.icons_edificio12);


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    sucursalstr = listasucursal.get(which).getClave();
                    btnsucursal.setText(listasucursal.get(which).getNombre());

                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
mDialog.dismiss();

        }//onPost

    }


    public void Listaclientes(View view) {
        listaclientG.clear();

        new Cliente().execute();
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
            String url = "http://" + StrServer+ptoOp + "/listaclientesapp?" + parametros;
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
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
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

            String[] opciones = new String[listaclientG.size()];

            for (int i = 0; i < listaclientG.size(); i++) {
                opciones[i] = listaclientG.get(i).getUserCliente() + ":" + listaclientG.get(i).getNombreCliente();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
            builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.icons_gesti_n_de_clientes);


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    clientestr = listaclientG.get(which).getUserCliente();
                    btncliente.setText(listaclientG.get(which).getNombreCliente());
                }
            });
// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            mDialog.dismiss();
        }//onPost
    }

    public void buscardoc(View view){

        listasearch = new ArrayList<>();
        recyclerConsulta.setLayoutManager(new LinearLayoutManager(ActivityConsultaDocumentosEletronicos.this));
        FechaIncial = fechaEn.getText().toString();
        FechaFinal = fechaSa.getText().toString();
        struuid = uuided.getText().toString();
        strfolio = edfolio.getText().toString();

        if(!TIPO.equals("")){
            if(!clientestr.equals("")){
                parametrosfactura="cliente=" + clientestr + "&tipo=" + TIPO;

                if(!struuid.equals("")){
                    parametrosfactura = parametrosfactura+"&uuid=" + uuided;
                }else if(!strfolio.equals("")){

                    if(!sucursalstr.equals("")){
                        parametrosfactura = parametrosfactura+"&sucursal=" + sucursalstr ;
                        parametrosfactura =parametrosfactura+ "&folio=" + strfolio;
                    }else{
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
                        alerta.setMessage("Si buscas por folio coloca una sucursal porfavor").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("¡ERROR!");
                        titulo.show();
                    }

                }else{
                    if(!sucursalstr.equals("")){
                        parametrosfactura = parametrosfactura+"&sucursal=" + sucursalstr ;
                    }
                    parametrosfactura = parametrosfactura+"&fi=" + FechaIncial + "&ff=" + FechaFinal;

                }






                ListadeFacturas();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
                alerta.setMessage("No has seleccionado un cliente es necesario seleccionar el cliente primero ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("¡ERROR!");
                titulo.show();
            }



        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
            alerta.setMessage("No has seleccionado un tipo de documento aun ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("¡ERROR!");
            titulo.show();
        }





    }

    public void ListadeFacturas() {
        new Facturas().execute();
    }


    private class Facturas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String url = "http://" + StrServer+ptoOp + "/docelect?" + parametrosfactura;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);


                    JSONObject jitems, Numero;
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if(jsonObject.length()>0){
                        jitems = jsonObject.getJSONObject("datos");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("datos");
                            Numero = jitems.getJSONObject("" + i + "");
                            listasearch.add(new ConsulFacfturasSANDG(Numero.getString("num_cliente"),
                                    Numero.getString("nom_cliente"),
                                    Numero.getString("folio_factura"),
                                    Numero.getString("fecha_factura"),
                                    Numero.getString("plazo"),
                                    Numero.getString("fechav"),
                                    Numero.getString("saldo_factura"),
                                    Numero.getString("monto_factura"),
                                    Numero.getString("sucursal"),
                                    Numero.getString("nom_sucursal"),
                                    Numero.getString("auelctronico")));

                        }
                    }else{

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

            if (listasearch.size() > 0) {
                AdaptadorConsulDocuEle adapter = new AdaptadorConsulDocuEle(listasearch);
                recyclerConsulta.setAdapter(adapter);
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
                alerta.setMessage("No exiten documentos por mostrar de "+tipodialog).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("No existen "+tipodialog);
                titulo.show();
            }//onPost
        }
    }

    private class PDFFACTURA extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + Cliente + "&sucursal=" + ClaveNumDialog + "&folio=" + ClaveFolDialog+ "&tipo="+TIPO  ;
            String url = "http://" + StrServer+ptoOp + "/pdffacturaapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.has("Item")) {
                        JSONObject item = jsonObject.getJSONObject("Item");

                        // PDF
                        JSONObject pdfObj = item.getJSONObject("PDF");
                        if ("1".equals(pdfObj.optString("ok"))) {
                            base64pdf = pdfObj.optString("base64", "");
                        } else {
                            base64pdf = ""; // o mensaje de error
                            Log.e("PDF", "El PDF no está disponible");
                        }

                        // XML
                        JSONObject xmlObj = item.getJSONObject("XML");
                        if ("1".equals(xmlObj.optString("ok"))) {
                            base64xml = xmlObj.optString("base64", "");
                        } else {
                            base64xml = "";
                            Log.e("XML", "El XML no está disponible");
                        }
                    } else {
                        Log.e("JSON_ERROR", "No existe 'Item' en el JSON");
                    }

                } catch (JSONException e) {
                    Log.e("JSON_EXCEPTION", "Error procesando JSON: " + e.getMessage());
                }
            } else {
            }//else
            return null;

        }//doInBackground

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);


            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
            alerta.setTitle("¿Qué deseas abrir?")
                    .setMessage("Selecciona el tipo de archivo a visualizar.")
                    .setPositiveButton("Ver XML", (dialog, which) -> {

                        if(!base64xml.equals("")){
                            try {
                                byte[] decoded = Base64.decode(base64xml, Base64.DEFAULT);
                                File file = new File(getExternalFilesDir(null), Cliente+"_"+ClaveFolDialog+"_"+ClaveNumDialog+".xml");
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(decoded);
                                fos.close();

                                Uri uri = FileProvider.getUriForFile(
                                        ActivityConsultaDocumentosEletronicos.this,
                                        "com.example.kepler201.fileprovider",
                                        file
                                );

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "text/xml");
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(ActivityConsultaDocumentosEletronicos.this, "Error al abrir XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            AlertDialog.Builder alerta2 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
                            alerta2.setMessage("El XML no esta disponible ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta2.create();
                            titulo.setTitle("¡ERROR!");
                            titulo.show();
                        }





                    })
                    .setNegativeButton("Ver PDF", (dialog, which) -> {

                    if(!base64pdf.equals("")){
                        try {
                            byte[] decoded = Base64.decode(base64pdf, Base64.DEFAULT);
                            File file = new File(getExternalFilesDir(null), Cliente+"_"+ClaveFolDialog+"_"+ClaveNumDialog+".pdf");
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(decoded);
                            fos.close();

                            Uri uri = FileProvider.getUriForFile(
                                    ActivityConsultaDocumentosEletronicos.this,
                                    "com.example.kepler201.fileprovider",
                                    file
                            );

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(ActivityConsultaDocumentosEletronicos.this, "Error al abrir PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        AlertDialog.Builder alerta2 = new AlertDialog.Builder(ActivityConsultaDocumentosEletronicos.this);
                        alerta2.setMessage("El PDF no esta disponible ").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta2.create();
                        titulo.setTitle("¡ERROR!");
                        titulo.show();
                    }



                    })
                    .setCancelable(true)
                    .show();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    public void verfacturaspdfxml(View view){


        int position = recyclerConsulta.getChildAdapterPosition(Objects.requireNonNull(recyclerConsulta.findContainingItemView(view)));
        ClaveFolDialog = listasearch.get(position).getFoliodelDocumento();
        ClaveNumDialog = listasearch.get(position).getNumSuc();
        Cliente = listasearch.get(position).getCliente();
        new PDFFACTURA().execute();




    }

}