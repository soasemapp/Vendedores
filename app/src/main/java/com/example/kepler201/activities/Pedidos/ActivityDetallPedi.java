package com.example.kepler201.activities.Pedidos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.DetallPediSANDG;
import com.example.kepler201.XMLS.xmlDetallPe;
import com.example.kepler201.includes.MyToolbar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class ActivityDetallPedi extends AppCompatActivity {
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strcode, strcodBra, StrServer;
    ArrayList<DetallPediSANDG> listasearch2 = new ArrayList<>();
    private TableLayout tableLayout;
    TextView Sucursal, Folio, ClaClient, NomClient, Comentario;
    TableRow fila;
    AlertDialog mDialog;
    TextView txtClavePro, txtCant, txtPrecio, txtDesc, txtImporte;
    String ClaveFolDialog = "";
    String ClaveNumDialog = "";
    String mensaje = "";
    TextView txtSubtotal;
    TextView txtSubtotal2;
    TextView txtDescuento;
    TextView txtiva;
    TextView txtMontototal;

    String SubdescuentoValida;
    double DescProstr = 0;
    double Descuento = 0;
    String DescuentoStr;
    String ivstr;
    String MontoStr;
    String Desc1;
    double IvaVariado = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall_pedi);

        tableLayout = findViewById(R.id.table);


        mDialog = new SpotsDialog.Builder().setContext(ActivityDetallPedi.this).setMessage("Espere un momento...").build();
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

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
        Comentario = findViewById(R.id.txtcomentario);

        txtSubtotal = findViewById(R.id.SubTotal);
        txtDescuento = findViewById(R.id.Descuento);
        txtiva = findViewById(R.id.iva);
        txtSubtotal2 = findViewById(R.id.SubTotal2);
        txtMontototal = findViewById(R.id.MontoTotal);

        IvaVariado = ((!StrServer.equals("vazlocolombia.dyndns.org:9085")) ? 0.16 : 0.19);

        MyToolbar.show(this, "Pedido:" + ClaveFolDialog, true);


        ActivityDetallPedi.AsyncCallWS task = new ActivityDetallPedi.AsyncCallWS();
        task.execute();

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
            conecta3();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void result) {
            Folio.setText(listasearch2.get(0).getFolio());
            Sucursal.setText(listasearch2.get(0).getNomSucursal());
            ClaClient.setText(listasearch2.get(0).getClaveC());
            NomClient.setText(listasearch2.get(0).getNombreC());
            Comentario.setText(listasearch2.get(0).getComentario());
            if (StrServer.equals("vazlocolombia.dyndns.org:9085")) {

                Desc1 = listasearch2.get(0).getDesc1();
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
                    txtDesc.setText("Descuento");
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
                    txtDesc.setBackgroundColor(Color.GRAY);
                    txtDesc.setGravity(Gravity.START);
                    txtDesc.setText(listasearch2.get(i).getDescripcion());
                    txtDesc.setPadding(20, 20, 20, 20);
                    txtDesc.setTextColor(Color.WHITE);
                    txtDesc.setLayoutParams(layaoutDes);
                    fila.addView(txtDesc);


                    txtCant = new TextView(getApplicationContext());
                    txtCant.setBackgroundColor(Color.BLACK);
                    txtCant.setGravity(Gravity.START);
                    txtCant.setText(listasearch2.get(i).getCant());
                    txtCant.setPadding(20, 20, 20, 20);
                    txtCant.setTextColor(Color.WHITE);
                    txtCant.setLayoutParams(layaoutDes);
                    fila.addView(txtCant);

                    txtPrecio = new TextView(getApplicationContext());
                    txtPrecio.setBackgroundColor(Color.GRAY);
                    txtPrecio.setGravity(Gravity.START);
                    txtPrecio.setText("$" + formatNumberCurrency(listasearch2.get(i).getPrecio()));
                    txtPrecio.setPadding(20, 20, 20, 20);
                    txtPrecio.setTextColor(Color.WHITE);
                    txtPrecio.setLayoutParams(layaoutDes);
                    fila.addView(txtPrecio);


                    txtImporte = new TextView(getApplicationContext());
                    txtImporte.setBackgroundColor(Color.BLACK);
                    txtImporte.setGravity(Gravity.START);
                    txtImporte.setText("$" + formatNumberCurrency(listasearch2.get(i).getImporte()));
                    txtImporte.setPadding(20, 20, 20, 20);
                    txtImporte.setTextColor(Color.WHITE);
                    txtImporte.setLayoutParams(layaoutDes);
                    fila.addView(txtImporte);
                    fila.setPadding(2, 2, 2, 2);

                }
                tableLayout.addView(fila);
            }
            Montototal2();
            mDialog.dismiss();
        }

    }

    private static String formatNumberCurrency(String number) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));


    }

    private void conecta3() {


        String SOAP_ACTION = "DetallPe";
        String METHOD_NAME = "DetallPe";
        String NAMESPACE = "http://" + StrServer + "/WSk75Branch/";
        String URL = "http://" + StrServer + "/WSk75Branch";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            xmlDetallPe soapEnvelope = new xmlDetallPe(SoapEnvelope.VER11);
            soapEnvelope.xmlDetallP(strusr, strpass, ClaveFolDialog, ClaveNumDialog);
            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE trasport = new HttpTransportSE(URL);
            trasport.debug = true;
            trasport.call(SOAP_ACTION, soapEnvelope);
            SoapObject response = (SoapObject) soapEnvelope.bodyIn;
            int json = response.getPropertyCount();
            for (int i = 0; i < json; i++) {
                SoapObject response0 = (SoapObject) soapEnvelope.bodyIn;
                response0 = (SoapObject) response0.getProperty(i);

                listasearch2.add(new DetallPediSANDG((response0.getPropertyAsString("k_Sucursal").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Sucursal")),
                        (response0.getPropertyAsString("k_Folio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Folio")),
                        (response0.getPropertyAsString("k_cCliente").equals("anyType{}") ? " " : response0.getPropertyAsString("k_cCliente")),
                        (response0.getPropertyAsString("k_Nombre").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Nombre")),
                        (response0.getPropertyAsString("k_cProduct").equals("anyType{}") ? " " : response0.getPropertyAsString("k_cProduct")),
                        (response0.getPropertyAsString("k_Cantidad").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Cantidad")),
                        (response0.getPropertyAsString("k_Precio").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Precio")),
                        (response0.getPropertyAsString("k_Descuento").equals("anyType{}") ? " " : response0.getPropertyAsString("k_Descuento")),
                        (response0.getPropertyAsString("k_importe").equals("anyType{}") ? " " : response0.getPropertyAsString("k_importe")),
                        (response0.getPropertyAsString("k_comentario").equals("anyType{}") ? " " : response0.getPropertyAsString("k_comentario")),
                        (response0.getPropertyAsString("k_NomSucursal").equals("anyType{}") ? " " : response0.getPropertyAsString("k_NomSucursal")),
                        (response0.getPropertyAsString("k_descripcion").equals("anyType{}") ? " " : response0.getPropertyAsString("k_descripcion")),
                        (response0.getPropertyAsString("k_desc1").equals("anyType{}") ? " " : response0.getPropertyAsString("k_desc1"))));


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

    private void Montototal2() {


        double Subtotal = 0;
        String Subtotal1;
        for (int i = 0; i < listasearch2.size(); i++) {
            Subtotal = Subtotal + Double.parseDouble(listasearch2.get(i).getImporte());
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

}