package com.example.kepler201.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kepler201.R;
import com.example.kepler201.includes.MyToolbar;
import com.squareup.picasso.Picasso;

public class ActivityPerfil extends AppCompatActivity {


    ImageView imgEmpresa;

    //TextView
    String strusr, strpass, strname, strlname, strtype, strtype2, strbran, strma, strco, strcodBra, StrServer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        SharedPreferences preference;
        MyToolbar.show(this, "Perfil", true);

        TextView usr = findViewById(R.id.txtusr);
        TextView password =  findViewById(R.id.txtpass);
        TextView name =  findViewById(R.id.txtname);
        TextView apellido =  findViewById(R.id.txtlname);
        TextView tipo =  findViewById(R.id.txttype);
        TextView sucursal =  findViewById(R.id.txtbranch);
        TextView mail =  findViewById(R.id.txtemail);
        TextView code =  findViewById(R.id.txtk_code);
        imgEmpresa= findViewById(R.id.imgEmpresa);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
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
        strtype2 = preference.getString("type2", null);

        usr.setText("                                          " + strusr);
        password.setText("                                          " + strpass);
        name.setText(strname);
        apellido.setText(strlname);
        tipo.setText((strtype2 != null )?strtype2:strtype);
        sucursal.setText(strbran);
        mail.setText(strma);
        code.setText(strco);

        switch (StrServer) {
            case "jacve.dyndns.org:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.jacve)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
            case "autodis.ath.cx:9085":

                Picasso.with(getApplicationContext()).load(R.drawable.autodis)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
            case "cecra.ath.cx:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.cecra)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "guvi.ath.cx:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.guvi)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.vipla)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "cedistabasco.ddns.net:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.pressa)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9090":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9095":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "sprautomotive.servehttp.com:9080":
                Picasso.with(getApplicationContext()).load(R.drawable.sprimage)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);
                break;
            case "vazlocolombia.dyndns.org:9085":
                Picasso.with(getApplicationContext()).load(R.drawable.colombia2)
                        .error(R.drawable.ic_baseline_error_24)
                        .fit()
                        .centerInside()
                        .into(imgEmpresa);

                break;
        }

    }

}