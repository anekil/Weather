package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        EditText input = findViewById(R.id.inputField);

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
            SwitchMaterial simpleSwitch = findViewById(R.id.units);
            intent.putExtra("units", simpleSwitch.isChecked());
            String cityName = input.getText().toString();
            intent.putExtra("cityName", cityName);
            startActivity(intent);
        });

        ListView list = findViewById(R.id.citiesList);
        CitiesListAdapter adapter = new CitiesListAdapter();
        list.setAdapter(adapter);

        findViewById(R.id.favouriteBtn).setOnClickListener(view -> {
            adapter.addItem(input.getText().toString());
        });
    }


    private class CitiesListAdapter extends BaseAdapter {
        ArrayList<String> favourites = new ArrayList<>();
        @Override
        public int getCount() {
            return favourites.size();
        }

        @Override
        public String getItem(int position) {
            return favourites.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void deleteItem(int position) {
            favourites.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(String cityName) {
            favourites.add(cityName);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.city_list_row, container, false);
            }
            TextView cityName =  convertView.findViewById(R.id.city_name);
            cityName.setText(getItem(position));
            convertView.findViewById(R.id.deleteBtn).setOnClickListener(v -> deleteItem(position));
            return convertView;
        }


    }
}

