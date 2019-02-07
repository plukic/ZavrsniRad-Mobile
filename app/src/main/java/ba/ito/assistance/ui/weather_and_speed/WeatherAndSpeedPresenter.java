package ba.ito.assistance.ui.weather_and_speed;

import android.location.Location;

import javax.inject.Inject;

import ba.ito.assistance.data.weather.IWeatherRepo;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class WeatherAndSpeedPresenter implements WeatherAndSpeedContract.Presenter {


    private WeatherAndSpeedContract.View view;
    private IWeatherRepo weatherRepo;


    private ISchedulersProvider schedulersProvider;
    private Disposable weatherRefreshSubscribe;
    private Disposable weatherSubscribe;


    private Location lastLocation;

    @Inject
    public WeatherAndSpeedPresenter(IWeatherRepo weatherRepo, ISchedulersProvider schedulersProvider) {
        this.weatherRepo = weatherRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void onLocationUpdate(Location location) {
        if(view==null)
            return;


        if(location==null)
            view.displayCurrentSpeedNotAvailable();
        else{
            view.displayCurrentSpeed(getSpeedInKm(location,lastLocation));
        }
        lastLocation=location;
    }

    private float getSpeedInKm(Location newLocation, Location oldLocations) {
        if(newLocation.hasSpeed())
            return newLocation.getSpeed()*3.6f;
        if(oldLocations==null)
            return 0f;

        double elapsedTime = (newLocation.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
        double calculatedSpeed = lastLocation.distanceTo(newLocation) / elapsedTime;

        return (float) (calculatedSpeed*3.6f);
    }

    @Override
    public void onLocationPermissionGranted() {
        view.requestWeatherLocation();
        view.requestSpeedUpdates();
    }

    @Override
    public void onLocationPermissionDeclined() {
        view.displayCurrentSpeedNotAvailable();
        onWeatherLocation(null);
    }

    @Override
    public void onWeatherLocation(Location location) {
        Completable currentWeatherVMObservable;
        if (location == null) {
            currentWeatherVMObservable = weatherRepo.RefreshWeatherData();
        } else
            currentWeatherVMObservable = weatherRepo.RefreshWeatherData(location.getLatitude(), location.getLongitude());

        weatherRefreshSubscribe = currentWeatherVMObservable
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable ->view.displayLoadingWeather(true))
                .subscribe(() -> {
                    view.displayLoadingWeather(false);
                }, throwable -> {
                    view.displayLoadingWeather(false);
                    view.displayErrorWhileRefreshingWeather();
                });


    }

    private double getTemperatureInCelsius(double tempInKelvins) {
        return tempInKelvins - 273.15;
    }

    @Override
    public void takeView(WeatherAndSpeedContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        weatherSubscribe = weatherRepo.GetWeatherData()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe((currentWeatherVM) -> {
                    view.displayWeatherInformations(currentWeatherVM.getName(),getTemperatureInCelsius(currentWeatherVM.getMain().getTemp()));
                    view.displayLoadingWeather(false);
                }, throwable -> {
                    view.displayLoadingWeather(false);
                    view.displayNoWeatherInformation();
                });


        view.requestLocationPermission();
    }

    @Override
    public void onStop() {
        if(weatherRefreshSubscribe !=null)
            weatherRefreshSubscribe.dispose();
        if(weatherSubscribe !=null)
            weatherSubscribe.dispose();
    }
}
