package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pam.weather.FavouritesData;
import com.pam.weather.R;
import com.pam.weather.weatherresponse.Weather;
import com.pam.weather.weatherresponse.WeatherResponse;

public class ForecastNextDaysFragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forecast_next_days, container, false);
        return rootView;
    }

    @Override
    public void loadWeather(WeatherResponse weather) {
        if(rootView == null)
            return;

        TextView text = rootView.findViewById(R.id.date1);
        text.setText(String.valueOf(weather.list.get(1).dt));
        text = rootView.findViewById(R.id.temp1);
        text.setText(weather.list.get(1).main.temp + FavouritesData.getUnits().label);
        text = rootView.findViewById(R.id.status1);
        text.setText(String.valueOf(weather.list.get(1).weather.get(0).description));
        setIcon(rootView.findViewById(R.id.statusImage1), weather.list.get(1).weather.get(0));

        text = rootView.findViewById(R.id.date2);
        text.setText(String.valueOf(weather.list.get(2).dt));
        text = rootView.findViewById(R.id.temp2);
        text.setText(weather.list.get(2).main.temp + FavouritesData.getUnits().label);
        text = rootView.findViewById(R.id.status2);
        text.setText(String.valueOf(weather.list.get(2).weather.get(0).description));
        setIcon(rootView.findViewById(R.id.statusImage2), weather.list.get(2).weather.get(0));

        text = rootView.findViewById(R.id.date3);
        text.setText(String.valueOf(weather.list.get(3).dt));
        text = rootView.findViewById(R.id.temp3);
        text.setText(weather.list.get(3).main.temp + FavouritesData.getUnits().label);
        text = rootView.findViewById(R.id.status3);
        text.setText(String.valueOf(weather.list.get(3).weather.get(0).description));
        setIcon(rootView.findViewById(R.id.statusImage3), weather.list.get(3).weather.get(0));
    }

    void setIcon(ImageView image, Weather weather){
        switch (weather.icon){
            case "01d": case "01n": image.setImageResource(R.drawable.clear); break;
            case "02d": case "02n": image.setImageResource(R.drawable.cloudy); break;
            case "03d": case "03n": case "04d": case "04n": image.setImageResource(R.drawable.clouds); break;
            case "09d": case "09n": case "10d": case "10n": image.setImageResource(R.drawable.rain); break;
            case "11d": case "11n": image.setImageResource(R.drawable.thunderstorm); break;
            case "13d": case "13n": image.setImageResource(R.drawable.snow); break;
            case "50d": case "50n": image.setImageResource(R.drawable.mist); break;
            default: image.setImageResource(R.drawable.refresh);
        }
    }
}