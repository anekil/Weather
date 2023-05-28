package com.pam.weather.detailsfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pam.weather.R;
import com.pam.weather.weatherresponse.Weather;
import com.pam.weather.weatherresponse.WeatherResponse;

public class ForecastGeneralFragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forecast_general, container, false);
        return rootView;
    }

    public void loadWeather(WeatherResponse weather){
        if(rootView == null)
            return;

        TextView text = rootView.findViewById(R.id.status);
        text.setText(weather.list.get(0).weather.get(0).description);
        text = rootView.findViewById(R.id.temp);
        text.setText(Math.round(weather.list.get(0).main.temp) + weather.units.temp_label);
        text = rootView.findViewById(R.id.feels_like);
        text.setText(Math.round(weather.list.get(0).main.feels_like) + weather.units.temp_label);

        setIcon(rootView.findViewById(R.id.status_image), weather.list.get(0).weather.get(0));
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