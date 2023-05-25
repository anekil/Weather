package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.pam.weather.weatherresponse.WeatherResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    private static final String API_KEY = "a7801ab3bb1ab1a6e70f97bb4b575006";
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        input = findViewById(R.id.inputField);

        SwitchMaterial simpleSwitch = findViewById(R.id.units);
        simpleSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            if(isChecked)
                FavouritesData.setUnits(Units.IMPERIAL);
            else
                FavouritesData.setUnits(Units.METRIC);
        });

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
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

        findViewById(R.id.refreshBtn).setOnClickListener(view -> {
            if(checkInternetConnection()){
                refreshFavourites();
                saveFavourites();
            } else {
                loadFavourites();
            }
        });
    }

    void refreshFavourites(){
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        for (Map.Entry<String, WeatherResponse> entry : FavouritesData.getFavourites().entrySet()) {
            Call<WeatherResponse> call = apiService.getCurrentWeatherData(entry.getKey(), "4", FavouritesData.getUnits().name(), API_KEY);
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {
                        FavouritesData.addFavourite(entry.getKey(), response.body());
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Toast.makeText(MenuActivity.this, "Couldn't refresh", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
            if(favourites.contains(cityName))
                return;
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

    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}

