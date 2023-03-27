package com.example.kepler201.includes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kepler201.R;

import java.util.Objects;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String title, boolean upButton) {

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
