package ba.ito.assistance.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentWeatherVM implements Parcelable {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("main")
    private Main main;

    public static class Main implements Parcelable{
        @Expose
        @SerializedName("temp_max")
        private double temp_max;
        @Expose
        @SerializedName("temp_min")
        private double temp_min;
        @Expose
        @SerializedName("pressure")
        private double pressure;
        @Expose
        @SerializedName("humidity")
        private double humidity;
        @Expose
        @SerializedName("temp")
        private double temp;

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        protected Main(Parcel in) {
            temp_max = in.readDouble();
            temp_min = in.readDouble();
            pressure = in.readDouble();
            humidity = in.readDouble();
            temp = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(temp_max);
            dest.writeDouble(temp_min);
            dest.writeDouble(pressure);
            dest.writeDouble(humidity);
            dest.writeDouble(temp);
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

    protected CurrentWeatherVM(Parcel in) {
        name = in.readString();
        main = in.readParcelable(Main.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(main, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrentWeatherVM> CREATOR = new Creator<CurrentWeatherVM>() {
        @Override
        public CurrentWeatherVM createFromParcel(Parcel in) {
            return new CurrentWeatherVM(in);
        }

        @Override
        public CurrentWeatherVM[] newArray(int size) {
            return new CurrentWeatherVM[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
