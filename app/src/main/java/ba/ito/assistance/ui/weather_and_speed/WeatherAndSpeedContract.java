package ba.ito.assistance.ui.weather_and_speed;

import android.location.Location;

import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;
import ba.ito.assistance.model.weather.CurrentWeatherVM;

public interface WeatherAndSpeedContract  {
    interface Presenter extends BasePresenter<View>{
        void onLocationUpdate(Location location);
        void onLocationPermissionGranted();
        void onLocationPermissionDeclined();

        void onWeatherLocation(Location location);

    }
    interface View extends BaseView<Presenter> {

        void requestWeatherLocation();
        void displayNoWeatherInformation();
        void displayWeatherInformations(String city, double temperatureInCelsius);
        void displayLoadingWeather(boolean isLoading);

        void requestLocationPermission();
        void displayCurrentSpeedNotAvailable();
        void displayCurrentSpeed(float currentSpeedInKmH);


        void requestSpeedUpdates();

        void displayErrorWhileRefreshingWeather();
    }
}
