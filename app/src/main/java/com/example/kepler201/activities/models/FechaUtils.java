package com.example.kepler201.activities.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FechaUtils {


    public static String obtenerPrimerDiaDelMesActualFormatoWebService() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }
    public static String obtenerAnoActualFormatoWebService() {

        Calendar calendar = Calendar.getInstance();


        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
        return yearFormat.format(calendar.getTime());
    }
}

