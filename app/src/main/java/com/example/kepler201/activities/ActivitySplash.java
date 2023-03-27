package com.example.kepler201.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.squareup.picasso.Picasso;


public class ActivitySplash extends AppCompatActivity {

    ImageView imgVi;
    String StrServer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);

        imgVi = findViewById(R.id.imgEmpresa);
        StrServer = preference.getString("Server", "null");


        switch (StrServer) {
            case "jacve.dyndns.org:9085":
                Picasso.with(getApplicationContext()).
                        load(R.drawable.jacve)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);
                break;
            case "autodis.ath.cx:9085":
                Picasso.with(getApplicationContext()).
                        load(R.drawable.autodis)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);
                break;
            case "cecra.ath.cx:9085":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.cecra)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
            case "guvi.ath.cx:9085":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.guvi)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
            case "cedistabasco.ddns.net:9085":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.pressa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
            case "sprautomotive.servehttp.com:9085":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.vipla)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
            case "sprautomotive.servehttp.com:9090":
            case "sprautomotive.servehttp.com:9095":

            case "sprautomotive.servehttp.com:9080":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;

            case "vazlocolombia.dyndns.org:9085":

                Picasso.with(getApplicationContext()).
                        load(R.drawable.colombia2)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;

            default:
                Picasso.with(getApplicationContext()).
                        load(R.drawable.aboutlogo)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent ScreenFir = new Intent(ActivitySplash.this, ActivityValdatipo.class);
                startActivity(ScreenFir);
                finish();

            }
        }, 4000);

    }
}