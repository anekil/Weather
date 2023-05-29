package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity implements ApiCallback {
    EditText input;
    ChipGroup unitsGroup;
    Chip metric;
    Chip imperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FavouritesManager.getInstance();
        FavouritesManager.setSharedPreferences(this);
        FavouritesManager.setCurrentCallback(this);
        FavouritesManager.loadAll();

        input = findViewById(R.id.inputField);
        unitsGroup = findViewById(R.id.units);
        unitsGroup.setSelectionRequired(true);
        unitsGroup.check(R.id.standard);

        metric = findViewById(R.id.metric);
        imperial = findViewById(R.id.imperial);

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            FavouritesManager.currentCity = input.getText().toString().trim();
            FavouritesManager.currentUnits = getCheckedUnits();
            if(!checkInternetConnection()) {
                showToast("No internet connection - Loading from memory");
                if (!FavouritesManager.loadCurrent()) {
                    showToast("Couldn't find in memory");
                    return;
                }
            }
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
            startActivity(intent);
        });

        ListView list = findViewById(R.id.citiesList);
        CitiesListAdapter adapter = new CitiesListAdapter();
        list.setAdapter(adapter);

        findViewById(R.id.favouriteBtn).setOnClickListener(view -> {
            if(checkInternetConnection())
                adapter.addItem(input.getText().toString().trim());
            else
                showToast("No internet connection");
        });

        findViewById(R.id.refreshAllBtn).setOnClickListener(view -> {
            if(checkInternetConnection()){
                FavouritesManager.refreshAll();
            } else {
                showToast("No internet connection - Loading from memory");
                FavouritesManager.loadAll();
            }
        });

        findViewById(R.id.clearBtn).setOnClickListener(view -> {
            adapter.clear();
            FavouritesManager.clearAll();
        });
    }

    Units getCheckedUnits() {
        int checkedChipId = unitsGroup.getCheckedChipId();
        if (checkedChipId == metric.getId()) {
            return Units. METRIC;
        } else if (checkedChipId == imperial.getId()) {
            return Units.IMPERIAL;
        } else {
            return Units.STANDARD;
        }
    }

    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    void showToast(String message){
        Toast.makeText(MenuActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onApiResponseAll(boolean received) {
        if(received){
            showToast("Successfully refreshed");
        } else {
            showToast("Couldn't connect with API");
        }
    }

    @Override
    public void onApiResponseCurrent(boolean received) {
        if(received){
            Intent intent = new Intent(MenuActivity.this, ForecastActivity.class);
            startActivity(intent);
        }
        showToast("Couldn't connect with API");
    }

    @Override
    public void onApiFailure(Throwable t) {
        showToast("Couldn't connect with API");
    }

    private class CitiesListAdapter extends BaseAdapter {
        ArrayList<String> favourites;

        CitiesListAdapter(){
            favourites = FavouritesManager.loadFavouritesList();
        }

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
            FavouritesManager.deleteFavourite(favourites.get(position));
            favourites.remove(position);
            notifyDataSetChanged();
        }

        public void clear(){
            favourites.clear();
            notifyDataSetChanged();
        }

        public void addItem(String cityName) {
            if(favourites.contains(cityName))
                return;
            FavouritesManager.refresh(cityName, getCheckedUnits());
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

