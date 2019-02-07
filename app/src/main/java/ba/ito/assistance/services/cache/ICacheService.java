package ba.ito.assistance.services.cache;

import org.joda.time.DateTime;

import io.reactivex.Single;

public interface ICacheService {
    Single<Boolean> areHighwayListStale(boolean isForceRefresh);
    Single<Boolean> areHighwayEntrancesListStale(boolean isForceRefresh, int highwayId);
    Single<Boolean> areHighwayExitsListStale(boolean isForceRefresh, int highwayId, int entranceId);
    Single<Boolean> areHighwayRoutePricesStale(boolean isForceRefresh, int highwayId, int entranceId, int exitId);
    Single<Boolean> areRoadConditionsStale(boolean refreshStale);

    void OnHighwaysUpdate(DateTime now);
    void OnHighwayExitsUpdate(DateTime dateTime, int highwayId, int entranceId);
    void OnHighwayEntrancesUpdate(DateTime dateTime, int highwayId);
    void OnHighwayRoutePricesUpdated(DateTime dateTime, int highwayId, int entranceId, int exitId);
    void OnRoadConditionsUpdate(DateTime dateTime);

    Single<DateTime> highwayListLastUpdate();
    Single<DateTime> highwayEntranceListLastUpdate(int id);
    Single<DateTime> highwayExitListLastUpdate(int highwayId, int entranceId);
    Single<DateTime> highwayRoutePricesLastUpdate(int highwayId, int entranceId, int exitId);


    void OnWeatherDateUpdated(DateTime dateTime);

    Single<Boolean> isWeatherDataStale();
}
