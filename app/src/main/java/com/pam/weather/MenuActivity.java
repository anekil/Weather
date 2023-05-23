package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
            SwitchMaterial simpleSwitch = findViewById(R.id.units);
            intent.putExtra("type", simpleSwitch.isChecked());
            startActivity(intent);
        });

        /*
        ListView list = findViewById(R.id.citiesList);

        String[] cars = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        ArrayList<String> carL = new ArrayList<>(Arrays.asList(cars));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_list_row, carL);

        list.setAdapter(adapter);*/
    }

}