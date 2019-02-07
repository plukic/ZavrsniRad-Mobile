package ba.ito.assistance.data.distance_api;

import ba.ito.assistance.model.distance.DistanceResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IDistanceApi {
    /*
    https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=43.3432625,17.7991661&destinations=43.4474734,17.2172534|44.340703,17.2691941&key=APIKEY
     */
    @GET("maps/api/distancematrix/json")
    Observable<DistanceResponse> getDistanceResponse(@Query("units") String units, @Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String key);
}
