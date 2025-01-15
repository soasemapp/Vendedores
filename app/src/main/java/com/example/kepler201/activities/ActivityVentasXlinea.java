package com.example.kepler201.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.VentaLineaAdapter;
import com.example.kepler201.R;
import com.example.kepler201.activities.models.FechaUtils;
import com.example.kepler201.activities.models.VentaLinea;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityVentasXlinea extends AppCompatActivity {

    private static final String TAG = "ActivityVentasXlinea";
    private RecyclerView recyclerView;
    private VentaLineaAdapter adapter;
    TextView Vendedi;

    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_linea);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        Vendedi = findViewById(R.id.ediVend);

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

        Vendedi.setText(strname + " " + strlname);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        //String server= StrServer;
        String vendedor = strcode;
        String fecha = FechaUtils.obtenerPrimerDiaDelMesActualFormatoWebService();
        String ano = FechaUtils.obtenerAnoActualFormatoWebService();

        new ObtenerVentasTask().execute(vendedor, fecha, ano,StrServer);
    }

    private class ObtenerVentasTask extends AsyncTask<String, Void, List<VentaLinea>> {

        @Override
        protected List<VentaLinea> doInBackground(String... params) {
            String vendedor = params[0];
            String fecha = params[1];
            String ano = params[2];
            String server = params[3];
            List<VentaLinea> ventas = new ArrayList<>();

            try {
                String urlString = "http://"+server+"/presupuestoapp?vendedor=" + vendedor + "&fecha=" + fecha + "&ano=" + ano;
                //String urlString = "http://jacve.dyndns.org:9085/presupuestoapp?vendedor=084&fecha=2025-01-01&ano=21";
                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                StringBuilder response = new StringBuilder();
                int data = in.read();
                while (data != -1) {
                    response.append((char) data);
                    data = in.read();
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray jsonArray = jsonResponse.getJSONArray("Item");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ventaObject = jsonArray.getJSONObject(i);
                    VentaLinea venta = new VentaLinea();
                    venta.setLinea(ventaObject.getString("Linea"));
                    venta.setPresupuesto(ventaObject.getDouble("Presupuesto"));
                    venta.setFaltante(ventaObject.getDouble("Faltante"));
                    venta.setVendido(ventaObject.getDouble("Vendido"));
                    venta.setPorcentaje(ventaObject.getDouble("Porcentaje"));
                    ventas.add(venta);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error al obtener las ventas: ", e);
            }

            return ventas;
        }

        @Override
        protected void onPostExecute(List<VentaLinea> ventas) {
            if (ventas != null && !ventas.isEmpty()) {

                if (adapter == null) {
                    adapter = new VentaLineaAdapter(ventas);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setVentas(ventas);
                }
            } else {

                Toast.makeText(ActivityVentasXlinea.this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
