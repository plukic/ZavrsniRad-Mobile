package ba.ito.assistance.data.weather;

import ba.ito.assistance.model.distance.DistanceResponse;
import ba.ito.assistance.model.weather.CurrentWeatherVM;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherApi {

    @GET("data/2.5/weather")
    Observable<CurrentWeatherVM> getCurrentWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

}
