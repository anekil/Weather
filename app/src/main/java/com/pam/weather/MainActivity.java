package com.pam.weather;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ListView list = findViewById(R.id.citiesList);

        String[] cars = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        ArrayList<String> carL = new ArrayList<>(Arrays.asList(cars));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_list_row, carL);

        list.setAdapter(adapter);
    }
}
