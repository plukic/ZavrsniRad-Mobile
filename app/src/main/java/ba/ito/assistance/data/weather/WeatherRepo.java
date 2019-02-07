package ba.ito.assistance.data.weather;

import com.jakewharton.rxrelay2.PublishRelay;

import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;

import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.model.weather.CurrentWeatherVM;
import ba.ito.assistance.services.cache.ICacheService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;

public class WeatherRepo implements IWeatherRepo {
    private IWeatherApi weatherApi;
    private String apikey;
    private SharedPrefsRepo sharedPrefsRepo;
    private ICacheService cacheService;

    private PublishRelay<CurrentWeatherVM> currentWeatherVMPublishRelay;

    @Inject
    public WeatherRepo(IWeatherApi weatherApi, @Named("weather_id") String apikey, SharedPrefsRepo sharedPrefsRepo, ICacheService cacheService) {
        this.weatherApi = weatherApi;
        this.apikey = apikey;
        this.sharedPrefsRepo = sharedPrefsRepo;
        this.cacheService = cacheService;
        currentWeatherVMPublishRelay = PublishRelay.create();

    }

    @Override
    public Completable RefreshWeatherData(double latitude, double longitude) {
        return cacheService.isWeatherDataStale()
                .flatMapCompletable(isStale -> {
                    return isStale ? getDataFromApiAndPopulateDb(latitude, longitude) : getDataFromLocalStorage();
                });
    }

    private CompletableSource getDataFromLocalStorage() {
        CurrentWeatherVM currentWeather = sharedPrefsRepo.getCurrentWeather();
        if (currentWeather != null)
            currentWeatherVMPublishRelay.accept(currentWeather);
        return Completable.complete();
    }

    @Override
    public Completable RefreshWeatherData() {
        return Completable.fromAction(() -> {
            CurrentWeatherVM currentWeather = sharedPrefsRepo.getCurrentWeather();
            if (currentWeather != null)
                currentWeatherVMPublishRelay.accept(currentWeather);
        });

    }

    @Override
    public PublishRelay<CurrentWeatherVM> GetWeatherData() {
        return currentWeatherVMPublishRelay;

    }

    private CompletableSource getDataFromApiAndPopulateDb(double latitude, double longitude) {
        return weatherApi.getCurrentWeather(latitude, longitude, apikey)
                .flatMapCompletable(weatherVM -> {
                    sharedPrefsRepo.setWeatherApi(weatherVM);
                    cacheService.OnWeatherDateUpdated(new DateTime());
                    currentWeatherVMPublishRelay.accept(weatherVM);
                    return Completable.complete();
                });
    }
}
