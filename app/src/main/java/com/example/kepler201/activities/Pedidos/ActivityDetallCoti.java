package com.example.kepler201.activities.Pedidos;

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
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.DetallCotiSANDG;
import com.example.kepler201.SetterandGetter.EnvioSANDG;
import com.example.kepler201.SetterandGetter.ListaViaSANDG;
import com.example.kepler201.XMLS.xmlNewDoc42;
import com.example.kepler201.XMLS.xmlPedido;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class ActivityDetallCoti extends AppCompatActivity {
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcode, strcodBra, StrServer;
    ArrayList<DetallCotiSANDG> listasearch2 = new ArrayList<>();
    ArrayList<ListaViaSANDG> listasearch4 = new ArrayList<>();
    ArrayList<EnvioSANDG> listasearch5 = new ArrayList<>();

    private TableLayout tableLayout;
    private Spinner spinnerVia;
    Button pedidoButton;
    TextView Sucursal, Folio, ClaClient, NomClient;
    String ivstr;
    String MontoStr;
    TextView txtSubtotal;
    TextView txtSubtotal2;
    TextView txtDescuento;
    TextView txtiva;
    TextView txtMontototal;
    TextView txtComentario;
    TableRow fila;
    AlertDialog mDialog;
    TextView txtClavePro, txtCant, txtPrecio, txtDesc, txtImporte;


    String ClaveFolDialog = "";
    String ClaveNumDialog = "";
    String mensaje = "";
    String strComentario;
    String strNombreCliente;
    String strClaveCli;
    String StrFechaActaul;
    String StrFechaVencimiento;
    String StrRFC;
    String StrPlazo;
    String StrDescuentoPP;
    String StrDescuento1;
    String StrCalle;
    String StrColonia;
    String StrPoblacion;
    String StrVia;
    String stridEnvio = "";
    String DescPro;
    String Desc1;
    String MenValPedi;
    String ValPedido = "";
    String Desc1Valida = "";
    String DescuentoValida = "";
    String SubtotalValida = "";
    String SeCambiovalida = "";
    String strVia;

    String id;
    String CalleSec;
    String ColoniaSec;
    String PoblacionSec;
    String NumeroTel;
    double IvaVariado = 0;

    double DescProstr = 0;
    double Descuento = 0;
    String DescuentoStr;
    int n = 2000;
    String[] search2 = new String[n];
    String[] search3 = new String[n];

    String Mensaje;
    String Documento;
    String Folio1;
    String FolioResul;
    String SubdescuentoValida;

    String Eagle;
    String Rodatech;
    String Partech;
    String Shark;
    String Trackoone;
    String DESCdOCUMENTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall_coti);

        tableLayout = findViewById(R.id.table);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        mDialog = new SpotsDialog(ActivityDetallCoti.this);
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
        ClaveFolDialog = getIntent().getStringExtra("Folio");
        ClaveNumDialog = getIntent().getStringExtra("NumSucu");

        Folio = findViewById(R.id.txtFolio);
        Sucursal = findViewById(R.id.txtSucursal);
        ClaClient = findViewById(R.id.txtCliente);
        NomClient = findViewById(R.id.txtNom);
        txtComentario = findViewById(R.id.txtComentario);


        txtSubtotal = findViewById(R.id.SubTotal);
        txtDescuento = findViewById(R.id.Descuento);
        txtiva = findViewById(R.id.iva);
        txtSubtotal2 = findViewById(R.id.SubTotal2);
        txtMontototal = findViewById(R.id.MontoTotal);
        spinnerVia = findViewById(R.id.spinnerVia);
        pedidoButton = findViewById(R.id.PedidoButton);

        IvaVariado = ((!StrServer.equals("vazlocolombia.dyndns.org:9085")) ? 0.16 : 0.19);

        MyToolbar.show(this, "Cotizacion:" + ClaveFolDialog, true);


        ActivityDetallCoti.AsyncCallWS task = new ActivityDetallCoti.AsyncCallWS();
        task.execute();

        ActivityDetallCoti.AsyncCallWS5 task2 = new ActivityDetallCoti.AsyncCallWS5();
        task2.execute();

        ActivityDetallCoti.AsyncCallWS6 task3 = new ActivityDetallCoti.AsyncCallWS6();
        task3.execute();


        pedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedidoButton.setEnabled(false);

                if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {

                    strClaveCli = listasearch2.get(0).getClaveC();
                    strNombreCliente = listasearch2.get(0).getNombreC();
                    strComentario = listasearch2.get(0).getComentario() + listasearch2.get(0).getComentario2() + listasearch2.get(0).getComentario3();
                    StrRFC = listasearch2.get(0).getRFC();
                    StrPlazo = listasearch2.get(0).getPLAZO();
                    StrDescuentoPP = listasearch2.get(0).getDESCUENTOPP();
                    StrDescuento1 = listasearch2.get(0).getDESCUENTO1();
                    StrCalle = listasearch2.get(0).getCALLE();
                    StrColonia = listasearch2.get(0).getCOLONIA();
                    StrPoblacion = listasearch2.get(0).getPOBLACION();
                    Folio1 = Folio.getText().toString();
                    Eagle = listasearch2.get(0).getEagle();
                    Rodatech = listasearch2.get(0).getRodatech();
                    Partech = listasearch2.get(0).getPartech();
                    Shark = listasearch2.get(0).getShark();
                    Trackoone = listasearch2.get(0).getTrackoone();
                    DESCdOCUMENTO = listasearch2.get(0).getDESCdOCUMENTO();


                    for (int i = 0; i < listasearch4.size(); i++) {
                        int posi = spinnerVia.getSelectedItemPosition();
                        if (posi == i) {
                            strVia = listasearch4.get(i).getClave();
                            break;
                        }
                    }

                    ActivityDetallCoti.AsyncCallWS2 task = new ActivityDetallCoti.AsyncCallWS2();
                    task.execute();

                }  else if(StrServer.equals("cedistabasco.ddns.net:9085")) {
int contadortg=0,contadortodos=0;

                    for (int i = 0; i < listasearch2.size(); i++) {
                       if(listasearch2.get(i).getLinea().equals("4")){
                           contadortg++;
                       }else{
                           contadortodos++;
                       }
                    }
                    if(contadortg>=1 && contadortodos==0){
                        strClaveCli = listasearch2.get(0).getClaveC();
                        strNombreCliente = listasearch2.get(0).getNombreC();
                        strComentario = listasearch2.get(0).getComentario() + listasearch2.get(0).getComentario2() + listasearch2.get(0).getComentario3();
                        StrRFC = listasearch2.get(0).getRFC();
                        StrPlazo = listasearch2.get(0).getPLAZO();
                        StrDescuentoPP = listasearch2.get(0).getDESCUENTOPP();
                        StrDescuento1 = listasearch2.get(0).getDESCUENTO1();
                        StrCalle = listasearch2.get(0).getCALLE();
                        StrColonia = listasearch2.get(0).getCOLONIA();
                        StrPoblacion = listasearch2.get(0).getPOBLACION();
                        Folio1 = Folio.getText().toString();
                        Eagle = listasearch2.get(0).getEagle();
                        Rodatech = listasearch2.get(0).getRodatech();
                        Partech = listasearch2.get(0).getPartech();
                        Shark = listasearch2.get(0).getShark();
                        Trackoone = listasearch2.get(0).getTrackoone();
                        DESCdOCUMENTO = listasearch2.get(0).getDESCdOCUMENTO();


                        for (int i = 0; i < listasearch4.size(); i++) {
                            int posi = spinnerVia.getSelectedItemPosition();
                            if (posi == i) {
                                strVia = listasearch4.get(i).getClave();
                                break;
                            }
                        }


                        ActivityDetallCoti.ValidaPedidoMexico task = new ActivityDetallCoti.ValidaPedidoMexico();
                        task.execute();
                    }else if(contadortg==0 && contadortodos>=1){
                        strClaveCli = listasearch2.get(0).getClaveC();
                        strNombreCliente = listasearch2.get(0).getNombreC();
                        strComentario = listasearch2.get(0).getComentario() + listasearch2.get(0).getComentario2() + listasearch2.get(0).getComentario3();
                        StrRFC = listasearch2.get(0).getRFC();
                        StrPlazo = listasearch2.get(0).getPLAZO();
                        StrDescuentoPP = listasearch2.get(0).getDESCUENTOPP();
                        StrDescuento1 = listasearch2.get(0).getDESCUENTO1();
                        StrCalle = listasearch2.get(0).getCALLE();
                        StrColonia = listasearch2.get(0).getCOLONIA();
                        StrPoblacion = listasearch2.get(0).getPOBLACION();
                        Folio1 = Folio.getText().toString();
                        Eagle = listasearch2.get(0).getEagle();
                        Rodatech = listasearch2.get(0).getRodatech();
                        Partech = listasearch2.get(0).getPartech();
                        Shark = listasearch2.get(0).getShark();
                        Trackoone = listasearch2.get(0).getTrackoone();
                        DESCdOCUMENTO = listasearch2.get(0).getDESCdOCUMENTO();


                        for (int i = 0; i < listasearch4.size(); i++) {
                            int posi = spinnerVia.getSelectedItemPosition();
                            if (posi == i) {
                                strVia = listasearch4.get(i).getClave();
                                break;
                            }
                        }


                        ActivityDetallCoti.ValidaPedidoMexico task = new ActivityDetallCoti.ValidaPedidoMexico();
                        task.execute();
                    }else{
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
                        alerta1.setMessage("Se estan mezclando productos SHARK con otros").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                pedidoButton.setEnabled(true);
                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Hubo un problema");
                        titulo1.show();
                    }

                }else{
                    strClaveCli = listasearch2.get(0).getClaveC();
                    strNombreCliente = listasearch2.get(0).getNombreC();
                    strComentario = listasearch2.get(0).getComentario() + listasearch2.get(0).getComentario2() + listasearch2.get(0).getComentario3();
                    StrRFC = listasearch2.get(0).getRFC();
                    StrPlazo = listasearch2.get(0).getPLAZO();
                    StrDescuentoPP = listasearch2.get(0).getDESCUENTOPP();
                    StrDescuento1 = listasearch2.get(0).getDESCUENTO1();
                    StrCalle = listasearch2.get(0).getCALLE();
                    StrColonia = listasearch2.get(0).getCOLONIA();
                    StrPoblacion = listasearch2.get(0).getPOBLACION();
                    Folio1 = Folio.getText().toString();
                    Eagle = listasearch2.get(0).getEagle();
                    Rodatech = listasearch2.get(0).getRodatech();
                    Partech = listasearch2.get(0).getPartech();
                    Shark = listasearch2.get(0).getShark();
                    Trackoone = listasearch2.get(0).getTrackoone();
                    DESCdOCUMENTO = listasearch2.get(0).getDESCdOCUMENTO();


                    for (int i = 0; i < listasearch4.size(); i++) {
                        int posi = spinnerVia.getSelectedItemPosition();
                        if (posi == i) {
                            strVia = listasearch4.get(i).getClave();
                            break;
                        }
                    }


                    ActivityDetallCoti.ValidaPedidoMexico task = new ActivityDetallCoti.ValidaPedidoMexico();
                    task.execute();
                }
            }
        });


        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
        StrFechaActaul = dateformatActually.format(c.getTime());

    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String saldo;
            int saldoint;
            HttpHandler sh = new HttpHandler();
            String parametros = "sucursal=" + ClaveNumDialog + "&folio=" + ClaveFolDialog;
            String url = "http://" + StrServer + "/cotizaciondetallapp?" + parametros;
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


                            saldo = (Numero.getString("k_saldo").equals("") ? "0" : Numero.getString("k_saldo"));
                            saldoint = Integer.parseInt(saldo);
                            if (saldoint > 0) {
                                listasearch2.add(new DetallCotiSANDG((Numero.getString("k_Sucursal").equals("") ? " " : Numero.getString("k_Sucursal")),
                                        (Numero.getString("k_Folio").equals("") ? "" : Numero.getString("k_Folio")),
                                        (Numero.getString("ClaveC").equals("") ? "" : Numero.getString("ClaveC")),
                                        (Numero.getString("k_NombreC").equals("") ? "" : Numero.getString("k_NombreC")),
                                        (Numero.getString("k_ClaveP").equals("") ? "" : Numero.getString("k_ClaveP")),
                                        (Numero.getString("k_Cant").equals("") ? "" : Numero.getString("k_Cant")),
                                        (Numero.getString("k_Precio").equals("") ? "" : Numero.getString("k_Precio")),
                                        (Numero.getString("k_Desc").equals("") ? "" : Numero.getString("k_Desc")),
                                        (Numero.getString("k_Import").equals("") ? "" : Numero.getString("k_Import")),
                                        (Numero.getString("k_RFC").equals("") ? "" : Numero.getString("k_RFC")),
                                        (Numero.getString("k_Plazo").equals("") ? "" : Numero.getString("k_Plazo")),
                                        (Numero.getString("k_87").equals("") ? "0" : Numero.getString("k_87")),
                                        (Numero.getString("k_desc1").equals("") ? "0" : Numero.getString("k_desc1")),
                                        (Numero.getString("k_calle").equals("") ? "" : Numero.getString("k_calle")),
                                        (Numero.getString("k_colonia").equals("") ? "" : Numero.getString("k_colonia")),
                                        (Numero.getString("k_poblacion").equals("") ? "" : Numero.getString("k_poblacion")),
                                        (Numero.getString("k_via").equals("") ? "" : Numero.getString("k_via")),
                                        (Numero.getString("k_comentario").equals("") ? "" : Numero.getString("k_comentario")),
                                        (Numero.getString("k_Disponible").equals("") ? "" : Numero.getString("k_Disponible")),
                                        (Numero.getString("k_BackOrder").equals("") ? "" : Numero.getString("k_BackOrder")),
                                        (Numero.getString("k_Surtido").equals("") ? "" : Numero.getString("k_Surtido")),
                                        (Numero.getString("k_precionuevo").equals("") ? "" : Numero.getString("k_precionuevo")),
                                        (Numero.getString("k_fecha").equals("") ? "" : Numero.getString("k_fecha")),
                                        (Numero.getString("k_hora").equals("") ? "" : Numero.getString("k_hora")),
                                        (Numero.getString("k_montoback").equals("") ? "" : Numero.getString("k_montoback")),
                                        (Numero.getString("k_saldo").equals("") ? "" : Numero.getString("k_saldo")),
                                        (Numero.getString("k_nomSucursal").equals("") ? "" : Numero.getString("k_nomSucursal")),
                                        (Numero.getString("k_Descripcion").equals("") ? "" : Numero.getString("k_Descripcion")),
                                        (Numero.getString("k_Unidad").equals("") ? "" : Numero.getString("k_Unidad")),
                                        (Numero.getString("k_Partida").equals("") ? "" : Numero.getString("k_Partida")),
                                        (Numero.getString("k_Comentario2").equals("") ? "" : Numero.getString("k_Comentario2")),
                                        (Numero.getString("k_Comentario3").equals("") ? "" : Numero.getString("k_Comentario3")),
                                        (Numero.getString("k_EagleDesc").equals("") ? "" : Numero.getString("k_EagleDesc")),
                                        (Numero.getString("k_TrackDesc").equals("") ? "" : Numero.getString("k_TrackDesc")),
                                        (Numero.getString("k_RodatDesc").equals("") ? "" : Numero.getString("k_RodatDesc")),
                                        (Numero.getString("k_PartechDesc").equals("") ? "" : Numero.getString("k_PartechDesc")),
                                        (Numero.getString("k_SharckDesc").equals("") ? "" : Numero.getString("k_SharckDesc")),
                                        (Numero.getString("k_Descdoc").equals("") ? "" : Numero.getString("k_Descdoc")),
                                        (StrServer.equals("cedistabasco.ddns.net:9085")?((Numero.getString("k_LInea").equals("") ? "" : Numero.getString("k_LInea"))):"")));
                            }
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            Folio.setText(listasearch2.get(0).getFolio());
            Sucursal.setText(listasearch2.get(0).getNombreSucursal());
            ClaClient.setText(listasearch2.get(0).getClaveC());
            NomClient.setText(listasearch2.get(0).getNombreC());
            txtComentario.setText(listasearch2.get(0).getComentario() + listasearch2.get(0).getComentario2() + listasearch2.get(0).getComentario3());
            StrVia = listasearch2.get(0).getVIA();
            strClaveCli = listasearch2.get(0).getClaveC();


            DescPro = listasearch2.get(0).getDESCUENTOPP();

            if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {

                Desc1 = listasearch2.get(0).getDESCUENTO1();
            } else {
                Desc1 = "0";

            }
            TableRow.LayoutParams layaoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams layaoutDes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = -1; i < listasearch2.size(); i++) {
                fila = new TableRow(getApplicationContext());
                fila.setLayoutParams(layaoutFila);
                txtClavePro = new TextView(getApplicationContext());
                if (i == -1) {

                    txtClavePro.setText("Producto");
                    txtClavePro.setGravity(Gravity.START);
                    txtClavePro.setBackgroundColor(Color.BLUE);
                    txtClavePro.setTextColor(Color.WHITE);
                    txtClavePro.setPadding(20, 20, 20, 20);
                    txtClavePro.setLayoutParams(layaoutDes);
                    fila.addView(txtClavePro);


                    txtDesc = new TextView(getApplicationContext());
                    txtDesc.setText("Descripcion");
                    txtDesc.setGravity(Gravity.START);
                    txtDesc.setBackgroundColor(Color.BLUE);
                    txtDesc.setTextColor(Color.WHITE);
                    txtDesc.setPadding(20, 20, 20, 20);
                    txtDesc.setLayoutParams(layaoutDes);
                    fila.addView(txtDesc);

                    txtCant = new TextView(getApplicationContext());
                    txtCant.setText("Cantidad");
                    txtCant.setGravity(Gravity.START);
                    txtCant.setBackgroundColor(Color.BLUE);
                    txtCant.setTextColor(Color.WHITE);
                    txtCant.setPadding(20, 20, 20, 20);
                    txtCant.setLayoutParams(layaoutDes);
                    fila.addView(txtCant);


                    txtPrecio = new TextView(getApplicationContext());
                    txtPrecio.setText("Precio");
                    txtPrecio.setGravity(Gravity.START);
                    txtPrecio.setBackgroundColor(Color.BLUE);
                    txtPrecio.setTextColor(Color.WHITE);
                    txtPrecio.setPadding(20, 20, 20, 20);
                    txtPrecio.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecio);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setText("Importe");
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setBackgroundColor(Color.BLUE);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);

                } else {


                    txtClavePro.setBackgroundColor(Color.BLACK);
                    txtClavePro.setGravity(Gravity.START);
                    txtClavePro.setText(listasearch2.get(i).getClaveP());
                    txtClavePro.setPadding(20, 20, 20, 20);
                    txtClavePro.setTextColor(Color.WHITE);
                    txtClavePro.setLayoutParams(layaoutDes);
                    fila.addView(txtClavePro);

                    txtDesc = new TextView(getApplicationContext());
                    txtDesc.setBackgroundColor(Color.BLACK);
                    txtDesc.setGravity(Gravity.START);
                    txtDesc.setText(listasearch2.get(i).getDescripcion());
                    txtDesc.setPadding(20, 20, 20, 20);
                    txtDesc.setTextColor(Color.WHITE);
                    txtDesc.setLayoutParams(layaoutDes);
                    fila.addView(txtDesc);

                    txtCant = new TextView(getApplicationContext());
                    txtCant.setBackgroundColor(Color.GRAY);
                    txtCant.setGravity(Gravity.START);
                    txtCant.setText(listasearch2.get(i).getSurtido());
                    txtCant.setPadding(20, 20, 20, 20);
                    txtCant.setTextColor(Color.WHITE);
                    txtCant.setLayoutParams(layaoutDes);
                    fila.addView(txtCant);

                    txtPrecio = new TextView(getApplicationContext());
                    txtPrecio.setBackgroundColor(Color.BLACK);
                    txtPrecio.setGravity(Gravity.START);
                    txtPrecio.setText("$" + formatNumberCurrency(listasearch2.get(i).getPrecio()));
                    txtPrecio.setPadding(20, 20, 20, 20);
                    txtPrecio.setTextColor(Color.WHITE);
                    txtPrecio.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecio);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setBackgroundColor(Color.GRAY);
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setText("$" + formatNumberCurrency(listasearch2.get(i).getPrecioNuevo()));
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);
                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout.addView(fila);
            }
            Montototal2();
            for (int i = 0; i < listasearch4.size(); i++) {
                int posi = spinnerVia.getSelectedItemPosition();
                if (posi == i) {
                    strVia = listasearch4.get(i).getClave();
                    break;
                }
            }

            mDialog.dismiss();
        }

    }

    @SuppressWarnings({"deprecation", "StatementWithEmptyBody"})
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strClaveCli + "&monto=" + MontoStr + "&vendedor=" + strcode + "&descuento=" + Desc1 + "&subtotal=" + SubdescuentoValida;
            String url = "http://" + StrServer + "/validapedcolapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("MENSAJE");

                        MenValPedi = jitems.getString("k_messenge");
                        ValPedido = jitems.getString("k_ValPe");
                        Desc1Valida = jitems.getString("k_desc1");
                        DescuentoValida = jitems.getString("Descuento");
                        SubtotalValida = jitems.getString("Subtotal");
                        SeCambiovalida = jitems.getString("SeCambio");
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
            if (Integer.parseInt(SeCambiovalida) == 1 && Integer.parseInt(ValPedido) == 1) {
                double monto;
                double ivasr;
                ivasr = Double.parseDouble(SubtotalValida) * IvaVariado;
                monto = Double.parseDouble(SubtotalValida) + ivasr;


                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                alerta.setMessage("El descuento sera modificado ¿Deseas realizar el pedido? \n" +
                        "Descuento=%" + Desc1Valida + "\n" +
                        "Descuento Total=$" + formatNumberCurrency(String.valueOf(DescuentoValida)) + "\n" +
                        "Subtotal = $" + formatNumberCurrency(String.valueOf(SubtotalValida)) + "\n" +
                        "Iva = $" + formatNumberCurrency(String.valueOf(ivasr)) + "\n" +
                        "Monto = $" + formatNumberCurrency(String.valueOf(monto))).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if ("1".equals(ValPedido)) {
                            Calendar c = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                            c.add(Calendar.DAY_OF_YEAR, 30);
                            StrFechaVencimiento = dateformatActually.format(c.getTime());

                            double monto;
                            double ivasr;
                            Desc1 = Desc1Valida;
                            DescuentoStr = DescuentoValida;
                            ivasr = Double.parseDouble(SubtotalValida) * IvaVariado;
                            ivstr = String.valueOf(ivasr);
                            monto = Double.parseDouble(SubtotalValida) + ivasr;
                            MontoStr = String.valueOf(monto);


                            int num;
                            int cont = 0;

                            for (int j = 0; j < listasearch2.size(); j++) {
                                num = Integer.parseInt(listasearch2.get(j).getSurtido());
                                if (num > 0) {

                                } else {
                                    cont++;
                                }
                            }

                            if (listasearch2.size() == cont) {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                                alerta.setMessage("El pedido no cuenta con ningun producto en existencia por el momento").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                AlertDialog titulo = alerta.create();
                                titulo.setTitle("AVISO");
                                titulo.show();
                                mDialog.dismiss();
                            } else {
                                AsyncCallWS3 task3 = new AsyncCallWS3();
                                task3.execute();
                            }
                        } else {
                            pedidoButton.setEnabled(true);
                            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                            alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent regreso = new Intent(ActivityDetallCoti.this, ActivityConsulCoti.class);
                                    overridePendingTransition(0, 0);
                                    startActivity(regreso);
                                    overridePendingTransition(0, 0);
                                    finish();


                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Error");
                            titulo.show();
                        }


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mDialog.dismiss();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Modificar");
                titulo.show();


            } else {
                if ("1".equals(ValPedido)) {
                    Calendar c = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                    c.add(Calendar.DAY_OF_YEAR, 30);
                    StrFechaVencimiento = dateformatActually.format(c.getTime());

                    int num;
                    int cont = 0;

                    for (int i = 0; i < listasearch2.size(); i++) {
                        num = Integer.parseInt(listasearch2.get(i).getSurtido());
                        if (num > 0) {

                        } else {
                            cont++;
                        }
                    }

                    if (listasearch2.size() == cont) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                        alerta.setMessage("El pedido no cuenta con ningun producto en existencia por el momento").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("AVISO");
                        titulo.show();
                        mDialog.dismiss();
                    } else {
                        AsyncCallWS3 task3 = new AsyncCallWS3();
                        task3.execute();
                    }
                } else {
                    pedidoButton.setEnabled(true);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                    alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent regreso = new Intent(ActivityDetallCoti.this, ActivityConsulCoti.class);
                            overridePendingTransition(0, 0);
                            startActivity(regreso);
                            overridePendingTransition(0, 0);
                            finish();

                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Error");
                    titulo.show();
                }
            }


        }

    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));


    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class ValidaPedidoMexico extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strClaveCli + "&monto=" + MontoStr + "&vendedor=" + strcode;
            String url = "http://" + StrServer + "/validapedmexapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {


                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jitems = jsonObject.getJSONObject("MENSAJE");

                        MenValPedi = jitems.getString("k_messenge");
                        ValPedido = jitems.getString("k_ValPe");
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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


            if ("1".equals(ValPedido)) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatActually = new SimpleDateFormat("yyyy-MM-dd");
                c.add(Calendar.DAY_OF_YEAR, 30);
                StrFechaVencimiento = dateformatActually.format(c.getTime());


                int num;
                int cont = 0;

                for (int i = 0; i < listasearch2.size(); i++) {
                    num = Integer.parseInt(listasearch2.get(i).getSurtido());
                    if (num > 0) {

                    } else {
                        cont++;
                    }
                }

                if (listasearch2.size() == cont) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                    alerta.setMessage("El pedido no cuenta con ningun producto en existencia por el momento").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("AVISO");
                    titulo.show();
                    mDialog.dismiss();
                } else {
                    AsyncCallWS3 task3 = new AsyncCallWS3();
                    task3.execute();
                }
            } else {
                pedidoButton.setEnabled(true);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                alerta.setMessage(MenValPedi).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent regreso = new Intent(ActivityDetallCoti.this, ActivityConsulCoti.class);
                        overridePendingTransition(0, 0);
                        startActivity(regreso);
                        overridePendingTransition(0, 0);
                        finish();

                        dialogInterface.cancel();
                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Error");
                titulo.show();
            }

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

            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
            alerta.setMessage(Documento).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int Cantidad = 0;
                    for (int j = 0; j < listasearch2.size(); j++) {

                        if (Integer.parseInt(listasearch2.get(j).getBackOrder()) > 0) {
                            Cantidad++;
                        } else {

                        }
                    }

                    if (Cantidad > 0) {
                        ActivityDetallCoti.AsyncCallWS7 task4 = new ActivityDetallCoti.AsyncCallWS7();
                        task4.execute();
                    } else {
                        Intent regreso = new Intent(ActivityDetallCoti.this, ActivityConsulCoti.class);
                        overridePendingTransition(0, 0);
                        startActivity(regreso);
                        overridePendingTransition(0, 0);
                        finish();
                        mDialog.dismiss();
                    }


                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Pedido:" + FolioResul);
            titulo.show();

            mDialog.dismiss();

        }

    }

    private void conectar3() {
        String SOAP_ACTION = "NewDoc";
        String METHOD_NAME = "NewDoc";
        String NAMESPACE = "http://" + StrServer + "/WSk80Docs/";
        String URL = "http://" + StrServer + "/WSk80Docs";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlPedido soapEnvelope = new xmlPedido(SoapEnvelope.VER11);
            soapEnvelope.xmlPedido(strComentario, strcode, strNombreCliente, strClaveCli, StrFechaActaul, StrFechaVencimiento, strcodBra, strusr, strpass,
                    StrRFC, StrPlazo, MontoStr, ivstr, DescuentoStr, DescPro, Desc1, StrCalle, StrColonia, StrPoblacion, Folio1, strVia, stridEnvio, listasearch2, StrServer, Eagle, Rodatech, Partech, Shark, Trackoone, DESCdOCUMENTO);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);

            SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
            Mensaje = response0.getPropertyAsString("message");
            Documento = response0.getPropertyAsString("doc");
            FolioResul = response0.getPropertyAsString("folio");


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
    private class AsyncCallWS5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String url = "http://" + StrServer + "/listaviaapp";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if(json.length()!=0) {

                        JSONObject jitems, Numero, Clave, Nombre;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Via");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Via");
                            Numero = jitems.getJSONObject("" + i + "");
                            listasearch4.add(new ListaViaSANDG((Numero.getString("k_claveVia").equals("") ? " " : Numero.getString("k_claveVia")),
                                    (Numero.getString("k_nombreVia").equals("") ? " " : Numero.getString("k_nombreVia"))));

                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
            String[] opciones = new String[listasearch4.size()];

            for (int i = 0; i < listasearch4.size(); i++) {
                opciones[i] = listasearch4.get(i).getNombre();
                search2[i] = listasearch4.get(i).getClave();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            spinnerVia.setAdapter(adapter);
            if (!StrVia.equals("")) {
                String clave;
                for (int i = 0; i < listasearch4.size(); i++) {
                    clave = listasearch4.get(i).getClave();
                    if (clave.equals(StrVia)) {
                        spinnerVia.setSelection(i);
                        break;
                    }
                }
            }

        }

    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS6 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + strClaveCli;
            String url = "http://" + StrServer + "/enviosapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.length() > 0) {
                        JSONObject jitems, Numero;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("Dir");

                        for (int i = 0; i < jitems.length(); i++) {
                            jitems = jsonObject.getJSONObject("Dir");
                            Numero = jitems.getJSONObject("" + i + "");

                            listasearch5.add(new EnvioSANDG((Numero.getString("k_id").equals("") ? " " : Numero.getString("k_id")),
                                    (Numero.getString("k_CalleyNumero").equals("") ? " " : Numero.getString("k_CalleyNumero")),
                                    (Numero.getString("k_ColoClinte").equals("") ? " " : Numero.getString("k_ColoClinte")),
                                    (Numero.getString("k_Poblacion").equals("") ? " " : Numero.getString("k_Poblacion")),
                                    (Numero.getString("k_Numero").equals("") ? " " : Numero.getString("k_Numero"))));


                        }

                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(ActivityDetallCoti.this);
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


        }

    }



    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS7 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conectar7();

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {

            Intent regreso = new Intent(ActivityDetallCoti.this, ActivityConsulCoti.class);
            overridePendingTransition(0, 0);
            startActivity(regreso);
            overridePendingTransition(0, 0);
            finish();
            mDialog.dismiss();

        }

    }


    private void conectar7() {
        String SOAP_ACTION = "NewDoc";
        String METHOD_NAME = "NewDoc";
        String NAMESPACE = "http://" + StrServer + "/WSk80Docs/";
        String URL = "http://" + StrServer + "/WSk80Docs";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlNewDoc42 soapEnvelope = new xmlNewDoc42(SoapEnvelope.VER11);
            soapEnvelope.xmlNewDoc42(strComentario, strcode, strNombreCliente, strClaveCli, StrFechaActaul, StrFechaVencimiento, strcodBra, strusr, strpass,
                    StrRFC, StrPlazo, MontoStr, ivstr, DescuentoStr, DescPro, Desc1, StrCalle, StrColonia, StrPoblacion, Folio1, strVia, stridEnvio, listasearch2, StrServer);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);


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

    private void Montototal2() {


        double Subtotal = 0;
        String Subtotal1;
        for (int i = 0; i < listasearch2.size(); i++) {
            Subtotal = Subtotal + Double.parseDouble(listasearch2.get(i).getPrecioNuevo());
        }


        Subtotal1 = String.valueOf(Subtotal);
        SubdescuentoValida = Subtotal1;
        txtSubtotal.setText(Html.fromHtml("Subtotal:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(Subtotal1) + "</font>"));
        DescProstr = Double.parseDouble(Desc1) / 100;
        Descuento = Subtotal * DescProstr;
        DescuentoStr = String.valueOf(Descuento);
        txtDescuento.setText(Html.fromHtml("Descuento:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(DescuentoStr) + "</font>"));

        double Subtotal2;
        Subtotal2 = Subtotal - Descuento;
        double ivaCal;
        double MontoTotal;

        ivaCal = Subtotal2 * IvaVariado;
        MontoTotal = Subtotal2 + ivaCal;
        String SubtotalStr = String.valueOf(Subtotal2);
        ivstr = String.valueOf(ivaCal);
        MontoStr = String.valueOf(MontoTotal);
        txtSubtotal2.setText(Html.fromHtml("SubTotal:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(SubtotalStr) + "</font>"));
        txtiva.setText(Html.fromHtml("Iva:<font color=#000000>$</font><font color=#000000>" + formatNumberCurrency(ivstr) + "</font>"));
        txtMontototal.setText(Html.fromHtml("Total:<font color=#000000>$</font><font color=#FF0000>" + formatNumberCurrency(MontoStr) + "</font>"));
    }


    public void DirEnvio(View view) {
        strClaveCli = listasearch2.get(0).getClaveC();

        String[] opciones = new String[listasearch5.size()];

        for (int i = 0; i < listasearch5.size(); i++) {
            opciones[i] = listasearch5.get(i).getCalle();
            search3[i] = listasearch5.get(i).getId();
        }


        mDialog.dismiss();

        if (listasearch5.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetallCoti.this);
            builder.setTitle("DIRECCION DE ENVIO ");


            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    for (int i = 0; i < listasearch5.size(); i++) {
                        if (which == i) {
                            id = listasearch5.get(i).getId();
                            CalleSec = listasearch5.get(i).getCalle();
                            ColoniaSec = listasearch5.get(i).getColonia();
                            PoblacionSec = listasearch5.get(i).getPoblacion();
                            NumeroTel = listasearch5.get(i).getNumero();
                            break;
                        }
                    }

                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                    alerta.setMessage("ID:" + id + "\n" +
                            "C:" + CalleSec + "\n" +
                            "COLONIA:" + ColoniaSec + "\n" +
                            "POBLACION:" + PoblacionSec + "\n" +
                            "NUMERO:" + NumeroTel + "\n").setCancelable(false).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stridEnvio = id;
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stridEnvio = "";
                            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
                            alerta.setMessage("EL PEDIDO SE MANDARA A LA DIRECCIÓN DE FACTURACIÓN").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("AVISO");
                            titulo.show();

                        }
                    });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("¿DESEAS MANDAR EL PEDIDO A?");
                    titulo.show();


                }

            });

// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetallCoti.this);
            alerta.setMessage("ESTE CLIENTE NO CUENTA CON DIRECCIONES SECUNDARIAS EL PEDIDO SE MANDARA A LA DIRECCION DE FACTURACION.").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("AVISO");
            titulo.show();
        }


    }

}