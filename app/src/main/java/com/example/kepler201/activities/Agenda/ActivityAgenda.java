package com.example.kepler201.activities.Agenda;

import android.Manifest;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepler201.Adapter.AdapterAgenda;
import com.example.kepler201.R;
import com.example.kepler201.SetterandGetter.AgendaSANDG;
import com.example.kepler201.SetterandGetter.Envio2SANDG;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ActivityAgenda extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private static final double RADIO_ACTIVACION = 50.0; // metros


    private RecyclerView recyclerAgenda;
    private ArrayList<AgendaSANDG> listaAgenda = new ArrayList<>();
    private ArrayList<Envio2SANDG> listaUbicaciones = new ArrayList<>();
    private String strusr, strpass, strcode, StrServer;
    private String ClaveCliente = "";
    private String StrFecha;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private float distanciaMinimaActual;
    private boolean isActivityVisible = false;


    private AdapterAgenda adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        setupToolbar();
        loadSharedPreferences();
        initViews();
        setupRecyclerView();
        setupLocation();
        loadAgendaData();

    }


    private void loadAgendaData() {
        new AgendaTask().execute();
    }

    private void setupToolbar() {
        MyToolbar.show(this, "Agenda", true);
    }

    private void loadSharedPreferences() {
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        strusr = preference.getString("user", "null");
        strpass = preference.getString("pass", "null");
        strcode = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");
    }

    private void initViews() {
        recyclerAgenda = findViewById(R.id.lisAgenda);
        EditText fecha = findViewById(R.id.fecha);
        fecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Calendar c = Calendar.getInstance();
        StrFecha = fecha.getText().toString();
    }

    private void setupRecyclerView() {
        recyclerAgenda.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterAgenda(listaAgenda, listaUbicaciones, this, strcode, StrServer, strusr, strpass);
        recyclerAgenda.setAdapter(adapter);
    }

    private void setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    validarUbicaciones(locationResult.getLastLocation());
                }
            }
        };

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000) // 5 segundos
                .setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        validarUbicaciones(locationResult.getLastLocation());
                    }
                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }


    private void validarUbicaciones(Location location) {

        if (listaUbicaciones.isEmpty()) return;

        HashMap<String, Boolean> proximidad = new HashMap<>();
        boolean cercaDeAlguno = false;
        float distanciaMinima = Float.MAX_VALUE;
        String idMasCercano = null;

        // Primero identificar cuál es el más cercano
        for (Envio2SANDG ubicacion : listaUbicaciones) {
            try {
                float[] results = new float[1];
                Location.distanceBetween(
                        location.getLatitude(),
                        location.getLongitude(),
                        Double.parseDouble(ubicacion.getLatitud()),
                        Double.parseDouble(ubicacion.getLongitud()),
                        results);

                if (results[0] < distanciaMinima) {
                    distanciaMinima = results[0];
                    idMasCercano = ubicacion.getId();
                }
            } catch (NumberFormatException e) {
                Log.e("PROXIMIDAD", "Error en coordenadas", e);
            }
        }


        for (Envio2SANDG ubicacion : listaUbicaciones) {
            try {
                float[] results = new float[1];
                Location.distanceBetween(
                        location.getLatitude(),
                        location.getLongitude(),
                        Double.parseDouble(ubicacion.getLatitud()),
                        Double.parseDouble(ubicacion.getLongitud()),
                        results);


                boolean cerca = ubicacion.getId().equals(idMasCercano) && results[0] <= RADIO_ACTIVACION;
                proximidad.put(ubicacion.getId(), cerca);

                if (cerca) cercaDeAlguno = true;

            } catch (NumberFormatException e) {
                Log.e("PROXIMIDAD", "Error en coordenadas", e);
            }
        }

        this.distanciaMinimaActual = distanciaMinima;

/*
        if (!cercaDeAlguno && isActivityVisible) {
            runOnUiThread(() -> Toast.makeText(this,
                    String.format("Marcador mas cerca a: %.1f metros", distanciaMinimaActual),
                    Toast.LENGTH_SHORT).show());
        } */

        //adapter.actualizarProximidadClientes(proximidad);
    }

    public void cargarUbicacionesCliente() {

        new DireccionesTask().execute();
    }

    private class DireccionesTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                for (int j = 0; j < listaAgenda.size(); j++) {
                    ClaveCliente = listaAgenda.get(j).getCliente();
                    HttpHandler sh = new HttpHandler();
                    String url = "http://" + StrServer + "/enviomapaapp?cliente=" + URLEncoder.encode(ClaveCliente, "UTF-8");
                    String jsonStr = sh.makeServiceCall(url, strusr, strpass);

                    if (jsonStr != null) {
                        JSONObject json = new JSONObject(jsonStr);

                        if (json.has("Dir")) {
                            JSONObject jitems = json.getJSONObject("Dir");
                            for (int i = 0; i < jitems.length(); i++) {
                                String key = "" + i + "";
                                if (jitems.has(key)) {
                                    JSONObject item = jitems.getJSONObject(key);
                                    listaUbicaciones.add(new Envio2SANDG(
                                            ClaveCliente, // Usamos el ID del cliente
                                            item.optString("k_direcciones", ""),
                                            item.optString("k_latitud", "0"),
                                            item.optString("k_longitud", "0")
                                    ));
                                }
                            }

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("DireccionesTask", "Error: " + e.getMessage());
                return false;
            }
            return false;


        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                adapter.notifyDataSetChanged();
                adapter.actualizarUbicaciones(listaUbicaciones);

                if (listaUbicaciones.isEmpty()) {
                    //Toast.makeText(ActivityAgenda.this, "El cliente no tiene ubicaciones registradas.", Toast.LENGTH_LONG).show();
                    Toast.makeText(
                            ActivityAgenda.this,
                            "El cliente " + ClaveCliente + " no tiene ubicaciones registradas.",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                // Validar si todas las ubicaciones son inválidas (latitud y longitud inválidas)
                boolean todasInvalidas = true;
                for (Envio2SANDG ubicacion : listaUbicaciones) {
                    String lat = ubicacion.getLatitud();
                    String lon = ubicacion.getLongitud();

                    if (lat != null && lon != null &&
                            !lat.trim().isEmpty() && !lon.trim().isEmpty() &&
                            !lat.trim().equals("0") && !lon.trim().equals("0")) {
                        todasInvalidas = false; // Al menos una ubicación es válida
                        break;
                    }
                }

                if (todasInvalidas) {
                    //Toast.makeText(ActivityAgenda.this, "El cliente no tiene ubicaciones válidas (latitud y longitud requeridas).", Toast.LENGTH_LONG).show();
                    Toast.makeText(
                            ActivityAgenda.this,
                            "El cliente " + ClaveCliente + " no tiene ubicaciones válidas (latitud y longitud requeridas).",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                // Hay al menos una ubicación válida, continuar con validación
                if (ActivityCompat.checkSelfPermission(ActivityAgenda.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(location -> {
                                if (location != null) {
                                    validarUbicaciones(location);
                                }
                            });
                }
            } else {
                Toast.makeText(ActivityAgenda.this, "Error al obtener las direcciones del cliente"+ClaveCliente, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private class AgendaTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            //String url = "http://" + StrServer + "/agendaapp?vendedor=" + strcode + "&fecha=2025-07-02";
            //String parametros = "vendedor=" + strcode + "&fecha=" + StrFecha;
           String url = "http://" + StrServer + "/agendaapp?vendedor=" + strcode + "&fecha=" + StrFecha;

            try {
                String jsonStr = sh.makeServiceCall(url, strusr, strpass);
                JSONObject json = new JSONObject(jsonStr);

                if (json.has("Item")) {
                    JSONObject jitems = json.getJSONObject("Item");
                    listaAgenda.clear();

                    for (int i = 0; i < jitems.length(); i++) {
                        JSONObject item = jitems.getJSONObject("" + i + "");
                        listaAgenda.add(new AgendaSANDG(
                                item.getString("fecha"),
                                item.getString("cliente"),
                                item.getString("nombre"),
                                item.getString("actividad"),
                                item.getString("estatus"),
                                item.optString("comentario", "")
                        ));
                    }
                    return true;
                }
            } catch (Exception e) {
                Log.e("AgendaTask", "Error: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success && !listaAgenda.isEmpty()) {
                adapter.notifyDataSetChanged();
                listaUbicaciones.clear();
                // Cargar ubicaciones del primer cliente automáticamente

                    cargarUbicacionesCliente();

            }
        }
    }

    public static class EnviarStatusTask extends AsyncTask<Void, Void, Boolean> {
        private final String nuevoStatus;
        private final Context context;
        private final boolean mostrarToast;

        static String ClaveClienteStatic, Fecha2Static, ActividadStatic, StatusStatic;
        static String strcodeStatic, StrServerStatic, strusrStatic, strpassStatic;

        public EnviarStatusTask(String status, Context context, boolean mostrarToast) {
            this.nuevoStatus = status;
            this.context = context;
            this.mostrarToast = mostrarToast;
        }

        public static void setParametros(String claveCliente, String fecha2, String actividad,
                                         String status, String strcode, String server,
                                         String usr, String pass) {
            ClaveClienteStatic = claveCliente;
            Fecha2Static = fecha2;
            ActividadStatic = actividad;
            StatusStatic = status;
            strcodeStatic = strcode;
            StrServerStatic = server;
            strusrStatic = usr;
            strpassStatic = pass;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String parametros = "vendedor=" + strcodeStatic +
                    "&fecha=" + Fecha2Static +
                    "&cliente=" + ClaveClienteStatic +
                    "&actividad=" + ActividadStatic +
                    "&status=" + StatusStatic +
                    "&statuscambio=" + nuevoStatus;

            String url = "http://" + StrServerStatic + "/statusagendaapp?" + parametros;
            String response = sh.makeServiceCall(url, strusrStatic, strpassStatic);
            return response != null && response.contains("Los Cambios se Realizaron Correctamente");
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success && mostrarToast) {
                Toast.makeText(context, "Estado actualizado a " + nuevoStatus, Toast.LENGTH_SHORT).show();
            } else if (!success && mostrarToast) {
                Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuflow7, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addAgenda) {
            startActivity(new Intent(this, ActivityAgendaRegister.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
        // Actualizar ubicación al volver a la actividad
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            validarUbicaciones(location);
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }


}