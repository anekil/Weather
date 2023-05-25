package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.pam.weather.weatherresponse.WeatherResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        input = findViewById(R.id.inputField);

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
            SwitchMaterial simpleSwitch = findViewById(R.id.units);
            intent.putExtra("units", simpleSwitch.isChecked());
            String cityName = input.getText().toString().trim();
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

    void saveFavourites(){
        SharedPreferences.Editor prefsEditor = getPreferences(MODE_PRIVATE).edit();
        Gson gson = new Gson();
        for (Map.Entry<String, WeatherResponse> entry : FavouritesData.getFavourites().entrySet()) {
            String json = gson.toJson(entry.getValue());
            prefsEditor.putString(entry.getKey(), json);
        }
        prefsEditor.commit();
    }

    void loadFavourites(){
        Gson gson = new Gson();
        String json = getPreferences(MODE_PRIVATE).getString("favourites", "");
        HashMap<String, WeatherResponse> data = gson.fromJson(json, HashMap.class);
        FavouritesData.loadData(data);
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
            FavouritesData.deleteFavourite(favourites.get(position));
            favourites.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(String cityName) {
            FavouritesData.addFavourite(cityName, null);
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
            cityName.setOnClickListener(v -> input.setText(getItem(position)));
            convertView.findViewById(R.id.deleteBtn).setOnClickListener(v -> deleteItem(position));
            return convertView;
        }
    }
}

