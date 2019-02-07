package ba.ito.assistance.data.weather;

import com.jakewharton.rxrelay2.PublishRelay;

import ba.ito.assistance.model.weather.CurrentWeatherVM;
import io.reactivex.Completable;

public interface IWeatherRepo {
    Completable RefreshWeatherData(double latitude, double longitude);
    Completable RefreshWeatherData();
//    Observable<CurrentWeatherVM> RefreshWeatherData();

    PublishRelay<CurrentWeatherVM> GetWeatherData();


}
