package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.kepler201.ConexionSQLiteHelper;
import com.example.kepler201.R;
import com.example.kepler201.activities.Carrito.CarritoComprasActivity;
import com.example.kepler201.includes.HttpHandler;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class inicioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;


    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;


    String CONFIGURACION;

    ConexionSQLiteHelper conn;

String mensaje;

    //TextView
    String strusr, strpass, strname, strlname, strtype, strbran, strma, strco, strcodBra, StrServer;

    String Empresa;


    private AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDialog = new SpotsDialog(inicioActivity.this);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        SharedPreferences preferenceClie = getSharedPreferences("clienteCompra", Context.MODE_PRIVATE);
        editor2 = preferenceClie.edit();
        strusr = preference.getString("user", null);
        strpass = preference.getString("pass", null);
        strname = preference.getString("name", null);
        strlname = preference.getString("lname", null);
        strtype = preference.getString("type", null);
        strbran = preference.getString("branch", null);
        strma = preference.getString("email", null);
        strcodBra = preference.getString("codBra", null);
        strco = preference.getString("code", null);
        StrServer = preference.getString("Server", null);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.}

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.busquedaActivity,R.id.activityConsultaProductos, R.id.activityPerfil, R.id.activityAgenda, R.id.activityClientes0Ventas, R.id.activityAltasPagos,R.id.regitrodepagosActivity
                    , R.id.activityConsulCoti,  R.id.activityHistorial,R.id.activityValdatipo,R.id.activityScreenFirst,R.id.listaPreciosActivity)
                    .setDrawerLayout(drawer)
                    .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    hideItem();

    }
    private void hideItem()
    {

        if(strtype.equals("ADMIN")){

            navigationView = findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.activityValdatipo).setVisible(true);
        }else{
            navigationView = findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.activityValdatipo).setVisible(false);
        }

        inicioActivity.AsyncCallWS task = new inicioActivity.AsyncCallWS();
        task.execute();

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
            String url = "http://" + StrServer + "/configuracion";
            String jsonStr = sh.makeServiceCall(url, strusr, strpass);
            if (jsonStr != null) {
                try {
                    JSONObject jItem;
                    JSONObject jitems;
                    String Repartidores="";
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if(jsonObject.length()!=0) {
                        jItem = jsonObject.getJSONObject("Item");
                        for (int i = 0; i < jItem.length(); i++) {
                            jitems = jItem.getJSONObject("" + i);
                            CONFIGURACION = jitems.getString("RegistroPagos");
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alerta1 = new AlertDialog.Builder(inicioActivity.this);
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(inicioActivity.this);
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
            if(CONFIGURACION.equals("0")){

                navigationView = findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.activityAltasPagos).setVisible(true);
                nav_Menu.findItem(R.id.regitrodepagosActivity).setVisible(false);
            }else{
                navigationView = findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();

                nav_Menu.findItem(R.id.activityAltasPagos).setVisible(false);
                nav_Menu.findItem(R.id.regitrodepagosActivity).setVisible(true);
            }

        }


    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder mensaje = new AlertDialog.Builder(this);
        mensaje.setTitle("¿Desea salir de la aplicacion?").setIcon(R.drawable.ic_baseline_exit_to_app_24);
        mensaje.setCancelable(false);
        mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);

            }
        });
        mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mensaje.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);

        switch (StrServer) {
            case "sprautomotive.servehttp.com:9090": {
                MenuItem item = menu.findItem(R.id.MenuSPR);
                MenuItem itemRod = menu.findItem(R.id.RodatechMenu);
                MenuItem itemPartech = menu.findItem(R.id.PartechMenu);
                MenuItem itemSharck = menu.findItem(R.id.SharkMenu);
                itemRod.setVisible(false);
                itemPartech.setVisible(true);
                itemSharck.setVisible(true);
                item.setVisible(true);
                break;
            }
            case "sprautomotive.servehttp.com:9095": {
                MenuItem item = menu.findItem(R.id.MenuSPR);

                MenuItem itemRod = menu.findItem(R.id.RodatechMenu);
                MenuItem itemPartech = menu.findItem(R.id.PartechMenu);
                MenuItem itemSharck = menu.findItem(R.id.SharkMenu);
                itemRod.setVisible(true);
                itemPartech.setVisible(false);
                itemSharck.setVisible(true);

                item.setVisible(true);
                break;
            }
            case "sprautomotive.servehttp.com:9080": {
                MenuItem item = menu.findItem(R.id.MenuSPR);


                MenuItem itemRod = menu.findItem(R.id.RodatechMenu);
                MenuItem itemPartech = menu.findItem(R.id.PartechMenu);
                MenuItem itemSharck = menu.findItem(R.id.SharkMenu);
                itemRod.setVisible(true);
                itemPartech.setVisible(true);
                itemSharck.setVisible(false);


                item.setVisible(true);
                break;
            }
            default: {
                MenuItem item = menu.findItem(R.id.MenuSPR);
                item.setVisible(false);
                break;
            }
        }

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.cerrarSe) {
                editor.clear().commit();
                editor2.clear().commit();
                BorrarCarrito();
                BorrarProductos();
                Intent cerrar = new Intent(this, MainActivity.class);
                startActivity(cerrar);
                System.exit(0);
                finish();

            }else if (id == R.id.CarrComp){
                Intent Shoping = new Intent(this, CarritoComprasActivity.class);
                 startActivity(Shoping);


            }else if (id == R.id.RodatechMenu){
        StrServer = "sprautomotive.servehttp.com:9090";
        editor.putString("Server", StrServer);
        editor.commit();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        finish();
    }else if (id == R.id.PartechMenu){
        StrServer = "sprautomotive.servehttp.com:9095";
        editor.putString("Server", StrServer);
        editor.commit();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        finish();
    }else if (id == R.id.SharkMenu){
        StrServer = "sprautomotive.servehttp.com:9080";
        editor.putString("Server", StrServer);
        editor.commit();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        finish();
    }



        return super.onOptionsItemSelected(item);
    }

    private void BorrarCarrito() {
        conn = new ConexionSQLiteHelper(inicioActivity.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        db.delete("carrito", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='carrito'");
        db.close();
    }
    private void BorrarProductos() {
        conn = new ConexionSQLiteHelper(inicioActivity.this, "bd_Carrito", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        db.delete("productos", null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='productos'");
        db.close();
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @SuppressLint({"UnsafeExperimentalUsageError", "SetTextI18n"})
    private void loadData() {

        final View vistaHeader = navigationView.getHeaderView(0);
        final ImageView imageViewdrawer = vistaHeader.findViewById(R.id.imageViewdrawer);
        final TextView tvEmpresa = vistaHeader.findViewById(R.id.idEmpresa),
                tvNombre = vistaHeader.findViewById(R.id.idnombre);

        switch (StrServer) {
            case "jacve.dyndns.org:9085":
                Empresa = strbran;
                Picasso.with(getApplicationContext()).load(R.drawable.jacve)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);

                break;
            case "autodis.ath.cx:9085":
                Empresa = strbran;

                Picasso.with(getApplicationContext()).load(R.drawable.autodis)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);

                break;
            case "cecra.ath.cx:9085":
                Empresa = strbran;
                Picasso.with(getApplicationContext()).load(R.drawable.cecra)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                break;
            case "guvi.ath.cx:9085":
                Empresa = strbran;
                Picasso.with(getApplicationContext()).load(R.drawable.guvi)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                break;
            case "sprautomotive.servehttp.com:9085":
                Empresa = strbran;
                Picasso.with(getApplicationContext()).load(R.drawable.vipla)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                break;
            case "cedistabasco.ddns.net:9085":
                Empresa = strbran;
                Picasso.with(getApplicationContext()).load(R.drawable.pressa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                break;
            case "sprautomotive.servehttp.com:9090":
            case "sprautomotive.servehttp.com:9095":
            case "sprautomotive.servehttp.com:9080":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                Empresa = strbran;

                break;
            case "vazlocolombia.dyndns.org:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.colombia2)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imageViewdrawer);
                Empresa = strbran;
                break;
        }
        tvEmpresa.setText(Empresa);
        tvNombre.setText(strname + " " + strlname);







    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}