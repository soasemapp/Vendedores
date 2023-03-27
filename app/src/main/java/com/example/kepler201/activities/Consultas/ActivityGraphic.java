package com.example.kepler201.activities.Consultas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.kepler201.R;
import com.example.kepler201.includes.MyToolbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraphic extends AppCompatActivity {
    String ClientesInac;
    String ClientesActi;
    AnyChartView Graphic;
    String[] Client = {"Inactivos", "Activos"};
    int[] earnig = new int[2];
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        Graphic = findViewById(R.id.Graphic);
        SharedPreferences preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        MyToolbar.show(this, "Grafica-Cliente 0 Ventas", true);

        ClientesInac = getIntent().getStringExtra("ClInativos");
        ClientesActi = getIntent().getStringExtra("ClIActivos");
        strusr = preference.getString("user", "null");
        strpass = preference.getString("pass", "null");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strtype = preference.getString("type", "null");
        strbran = preference.getString("branch", "null");
        strma = preference.getString("email", "null");
        strcodBra = preference.getString("codBra", "null");
        strco = preference.getString("code", "null");
        StrServer = preference.getString("Server", "null");

        setupPiechart();
    }

    private void setupPiechart() {
        int dato;
        int dato2;
        dato = Integer.parseInt(ClientesInac);
        dato2 = Integer.parseInt(ClientesActi);

        dato2 = dato2 - dato;

        earnig[0] = dato;
        earnig[1] = dato2;
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < Client.length; i++) {
            dataEntries.add(new ValueDataEntry(Client[i], earnig[i]));
        }
        pie.data(dataEntries);
        Graphic.setChart(pie);
    }

}