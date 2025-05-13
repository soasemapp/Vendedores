package com.example.kepler201.activities.Maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepler201.R;
import com.example.kepler201.includes.HttpHandler;
import com.example.kepler201.includes.MyToolbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


public class MapsActivityAgenda extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 0;
    private GoogleMap mMap;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    private Marker mMarker;
    int vald;
    AlertDialog mDialog;
    double Latitud=0, Longitud=0;
    String NombreCliente = "", ClaveCliente = "", idDireccion = "",Direccion="";
    FloatingActionButton botsave;
    String MensajeUpdate;
    String mensaje = "";
    String Validacion;
    String DirLatitud, DirLongitud;
    TextView Vendedortxt, Clientetxt, Direcciontxt;

    private Context context = this;
    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_agenda);

        MyToolbar.show(this, " ", true);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        Vendedortxt = findViewById(R.id.txtVendedor);
        Clientetxt = findViewById(R.id.txtCliente);
        Direcciontxt = findViewById(R.id.txtDireccion);

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

        vald = getIntent().getIntExtra("val", 0);
        NombreCliente = getIntent().getStringExtra("NomCliente");
        ClaveCliente = getIntent().getStringExtra("ClaveCliente");
        idDireccion = getIntent().getStringExtra("idDireccion");
        Direccion = getIntent().getStringExtra("Direccion");
        DirLatitud = getIntent().getStringExtra("DirLatitud");
        DirLongitud = getIntent().getStringExtra("DirLongitud");

        Latitud = Double.parseDouble(DirLatitud != null ? DirLatitud : "0");
        Longitud = Double.parseDouble(DirLongitud != null ? DirLongitud : "0");

        Vendedortxt.setText(strname + " " + strlname);
        Clientetxt.setText(NombreCliente);
        Direcciontxt.setText(Direccion != null ? Direccion : "");

        botsave = findViewById(R.id.botsave);
        botsave.setVisibility(View.GONE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mDialog = new SpotsDialog(MapsActivityAgenda.this);
    }

    Boolean actualposition = true;
    JSONObject jso;
    Double longitudorigen, latitudorigen;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng ubicacionCliente = new LatLng(Latitud, Longitud);
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(ubicacionCliente)
                .title(NombreCliente)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapasclientes)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ubicacionCliente)
                .zoom(17f)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }




    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i = 0; i < jRoutes.length(); i++) {

                jLegs = ((JSONObject) (jRoutes.get(i))).getJSONArray("legs");

                for (int j = 0; j < jLegs.length(); j++) {

                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < jSteps.length(); k++) {


                        String polyline = "" + ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end", "" + polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(8));


                    }


                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Drag Listener
    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        if (marker.equals(mMarker)) {
            Toast.makeText(this, "Levantaste el Marcador", Toast.LENGTH_SHORT).show();
            mMarker.showInfoWindow();
        }
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        if (marker.equals(mMarker)) {
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);
            setTitle(newTitle);


            Latitud = marker.getPosition().latitude;
            Longitud = marker.getPosition().longitude;


            MyToolbar.show(this, newTitle, true);
        }
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        if (marker.equals(mMarker)) {
            Toast.makeText(this, "Soltaste el Marcador", Toast.LENGTH_SHORT).show();
        }
    }


    private class ActualizarLocali extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String parametros = "cliente=" + ClaveCliente+"&direccion="+idDireccion+"&latitud="+String.valueOf(Latitud)+"&longitud="+String.valueOf(Longitud)+"&valcon=1";
            String url = "http://" + StrServer + "/asigncordenadasapp?" + parametros;
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);

                    if(json.length()!=0) {
                        JSONObject jitems, Numero, Clave, Nombre;
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jitems = jsonObject.getJSONObject("MENSAJE");

                        MensajeUpdate = jitems.getString("k_mensaje").equals("anyType{}") ? " " : jitems.getString("k_mensaje");
                        Validacion = jitems.getString("k_Val").equals("anyType{}") ? " " : jitems.getString("k_Val");
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsActivityAgenda.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsActivityAgenda.this);
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
            if (Validacion.equals("1")) {


                AlertDialog.Builder alerta = new AlertDialog.Builder(MapsActivityAgenda.this);
                alerta.setMessage(MensajeUpdate).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("!Error! Actualizacion");
                titulo.show();

            } else if (Validacion.equals("2")) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MapsActivityAgenda.this);
                alerta.setMessage(MensajeUpdate).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Ubicacion Actualizada");
                titulo.show();


            }
            mDialog.dismiss();
        }
    }


    public void saveLoca(View view) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(MapsActivityAgenda.this);
        alerta.setMessage("Deseas cambiar la marca de la ubicacion del cliente").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Latitud = mMarker.getPosition().latitude;
                Longitud = mMarker.getPosition().longitude;
                MapsActivityAgenda.ActualizarLocali task = new MapsActivityAgenda.ActualizarLocali();
                task.execute();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("Cambiar Ubicacion");
        titulo.show();


    }
}
