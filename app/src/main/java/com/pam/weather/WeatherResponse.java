package com.pam.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse implements Parcelable {
    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("visibility")
    public float visibility;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("dt")
    public int dt;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;

    protected WeatherResponse(Parcel in) {
        coord = in.readParcelable(Coord.class.getClassLoader());
        weather = in.createTypedArrayList(Weather.CREATOR);
        main = in.readParcelable(Main.class.getClassLoader());
        wind = in.readParcelable(Wind.class.getClassLoader());
        visibility = in.readFloat();
        sys = in.readParcelable(Sys.class.getClassLoader());
        dt = in.readInt();
        name = in.readString();
        cod = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(coord, flags);
        dest.writeTypedList(weather);
        dest.writeParcelable(main, flags);
        dest.writeParcelable(wind, flags);
        dest.writeFloat(visibility);
        dest.writeParcelable(sys, flags);
        dest.writeInt(dt);
        dest.writeString(name);
        dest.writeFloat(cod);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherResponse> CREATOR = new Creator<WeatherResponse>() {
        @Override
        public WeatherResponse createFromParcel(Parcel in) {
            return new WeatherResponse(in);
        }

        @Override
        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };
}

class Coord implements Parcelable {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;

    protected Coord(Parcel in) {
        lon = in.readFloat();
        lat = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(lon);
        dest.writeFloat(lat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coord> CREATOR = new Creator<Coord>() {
        @Override
        public Coord createFromParcel(Parcel in) {
            return new Coord(in);
        }

        @Override
        public Coord[] newArray(int size) {
            return new Coord[size];
        }
    };
}

class Weather implements Parcelable {
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;

    protected Weather(Parcel in) {
        main = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}

class Main implements Parcelable {
    @SerializedName("temp")
    public float temp;
    @SerializedName("feels_like")
    public float feels_like;
    @SerializedName("temp_min")
    public float temp_min;
    @SerializedName("temp_max")
    public float temp_max;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;

    protected Main(Parcel in) {
        temp = in.readFloat();
        feels_like = in.readFloat();
        temp_min = in.readFloat();
        temp_max = in.readFloat();
        humidity = in.readFloat();
        pressure = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(temp);
        dest.writeFloat(feels_like);
        dest.writeFloat(temp_min);
        dest.writeFloat(temp_max);
        dest.writeFloat(humidity);
        dest.writeFloat(pressure);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Main> CREATOR = new Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };
}

class Wind implements Parcelable {
    @SerializedName("speed")
    public float speed;

    protected Wind(Parcel in) {
        speed = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(speed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };
}

class Sys implements Parcelable {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;

    protected Sys(Parcel in) {
        country = in.readString();
        sunrise = in.readLong();
        sunset = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeLong(sunrise);
        dest.writeLong(sunset);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sys> CREATOR = new Creator<Sys>() {
        @Override
        public Sys createFromParcel(Parcel in) {
            return new Sys(in);
        }

        @Override
        public Sys[] newArray(int size) {
            return new Sys[size];
        }
    };
}


