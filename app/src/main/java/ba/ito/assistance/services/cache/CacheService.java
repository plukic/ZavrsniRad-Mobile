package ba.ito.assistance.services.cache;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ba.ito.assistance.data.room.cache.ICacheDao;
import ba.ito.assistance.model.cache.CacheEntity;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class CacheService implements ICacheService {
    private static final String HIGHWAYS_LIST_LAST_UPDATE_KEY = "HIGHWAYS_LIST_LAST_UPDATE_KEY";
    private static final String HIGHWAYS_ENTRANCE_LIST_LAST_UPDATE_KEY = "HIGHWAYS_ENTRANCE_LIST_LAST_UPDATE_KEY_";
    private static final String HIGHWAYS_EXIT_LIST_LAST_UPDATE_KEY = "HIGHWAYS_EXIT_LIST_LAST_UPDATE_KEY_";
    private static final String HIGHWAYS_ROUTE_PRICES_LIST_LAST_UPDATE_KEY = "HIGHWAYS_ROUTE_PRICES_LIST_LAST_UPDATE_KEY_";
    private static final String ROAD_CONDITIONS_LAST_UPDATE_KEY = "ROAD_CONDITIONS_LAST_UPDATE_KEY";
    private static final String WEATHER_UPDATE_KEY ="WEATHER_UPDATE_KEY";
    private ICacheDao cacheDao;

    @Inject
    public CacheService(ICacheDao cacheDao) {
        this.cacheDao = cacheDao;
    }

    //region Highway list

    @Override
    public Single<Boolean> areHighwayListStale(boolean isForceRefresh) {
        if (isForceRefresh)
            return Single.just(true);

        return cacheDao.GetCacheEntity(HIGHWAYS_LIST_LAST_UPDATE_KEY)
                .flatMap(cacheValidityMapper())
                .onErrorReturnItem(true);
    }

    @Override
    public Single<DateTime> highwayListLastUpdate() {
        return cacheDao.GetCacheEntity(HIGHWAYS_LIST_LAST_UPDATE_KEY)
                .flatMap(entity -> Single.just(entity.LastUpdateTime))
                .onErrorReturnItem(new DateTime());
    }

    @Override
    public void OnHighwaysUpdate(DateTime now) {
        CacheEntity ce = new CacheEntity();
        ce.LastUpdateTime = now;
        ce.Id = HIGHWAYS_LIST_LAST_UPDATE_KEY;
        cacheDao.AddCacheEntity(ce);
    }

    //endregion

    //region HighwayEntrances
    @Override
    public void OnHighwayEntrancesUpdate(DateTime dateTime, int highwayId) {
        CacheEntity ce = new CacheEntity();
        ce.Id = HIGHWAYS_ENTRANCE_LIST_LAST_UPDATE_KEY + highwayId;
        ce.LastUpdateTime=dateTime;
        cacheDao.AddCacheEntity(ce);
    }


    @Override
    public Single<DateTime> highwayEntranceListLastUpdate(int id) {
        return cacheDao.GetCacheEntity(HIGHWAYS_ENTRANCE_LIST_LAST_UPDATE_KEY + id)
                .flatMap(entity -> Single.just(entity.LastUpdateTime))
                .onErrorReturnItem(new DateTime());
    }

    @Override
    public Single<Boolean> areHighwayEntrancesListStale(boolean isForceRefresh, int highwayId) {
        if (isForceRefresh)
            return Single.just(true);

        return cacheDao.GetCacheEntity(HIGHWAYS_ENTRANCE_LIST_LAST_UPDATE_KEY + highwayId)
                .flatMap(cacheValidityMapper())
                .onErrorReturnItem(true);
    }

    @NonNull
    private Function<CacheEntity, SingleSource<? extends Boolean>> cacheValidityMapper() {
        return entity -> {
            if (entity.LastUpdateTime == null)
                return Single.just(true);
            //if data is older than one day
            boolean isStale = entity.LastUpdateTime.plusDays(1).isBefore(DateTime.now());
            return Single.just(isStale);
        };
    }

    @NonNull
    private Function<CacheEntity, SingleSource<? extends Boolean>> cacheValidityWeatherMapper() {
        return entity -> {
            if (entity.LastUpdateTime == null)
                return Single.just(true);
            //if data is older than one day
            boolean isStale = entity.LastUpdateTime.plusMinutes(5).isBefore(DateTime.now());
            return Single.just(isStale);
        };
    }

    //endregion

    //region HighwayExits
    @Override
    public void OnHighwayExitsUpdate(DateTime dateTime, int highwayId, int entranceId) {
        CacheEntity ce = new CacheEntity();
        ce.Id = HIGHWAYS_EXIT_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId;
        ce.LastUpdateTime=dateTime;
        cacheDao.AddCacheEntity(ce);
    }

    @Override
    public Single<DateTime> highwayExitListLastUpdate(int highwayId, int entranceId) {
        return cacheDao.GetCacheEntity(HIGHWAYS_EXIT_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId)
                .flatMap(entity -> Single.just(entity.LastUpdateTime))
                .onErrorReturnItem(new DateTime());
    }

    @Override
    public Single<Boolean> areHighwayExitsListStale(boolean isForceRefresh, int highwayId, int entranceId) {
        return cacheDao.GetCacheEntity(HIGHWAYS_EXIT_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId)
                .flatMap(cacheValidityMapper())
                .onErrorReturnItem(true);
    }


    //endregion

    //region HighwayRoutePrices
    @Override
    public void OnHighwayRoutePricesUpdated(DateTime dateTime, int highwayId, int entranceId, int exitId) {
        CacheEntity ce = new CacheEntity();
        ce.Id =HIGHWAYS_ROUTE_PRICES_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId + "_" + exitId;
        ce.LastUpdateTime=dateTime;
        cacheDao.AddCacheEntity(ce);
    }

    @Override
    public Single<Boolean> areHighwayRoutePricesStale(boolean isForceRefresh, int highwayId, int entranceId, int exitId) {
        if (isForceRefresh)
            return Single.just(true);

        return cacheDao.GetCacheEntity(HIGHWAYS_ROUTE_PRICES_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId + "_" + exitId)
                .flatMap(cacheValidityMapper())
                .onErrorReturnItem(true);
    }

    @Override
    public Single<DateTime> highwayRoutePricesLastUpdate(int highwayId, int entranceId, int exitId) {
        return cacheDao.GetCacheEntity(HIGHWAYS_ROUTE_PRICES_LIST_LAST_UPDATE_KEY + highwayId + "_" + entranceId + "_" + exitId)
                .flatMap(entity -> Single.just(entity.LastUpdateTime))
                .onErrorReturnItem(new DateTime());
    }


    //endregion

    //region WEATHER
    @Override
    public void OnWeatherDateUpdated(DateTime dateTime) {
        CacheEntity ce = new CacheEntity();
        ce.Id =WEATHER_UPDATE_KEY;
        ce.LastUpdateTime=dateTime;
        cacheDao.AddCacheEntity(ce);
    }
    @Override
    public Single<Boolean> isWeatherDataStale() {
        return cacheDao.GetCacheEntity(WEATHER_UPDATE_KEY)
                .flatMap(cacheValidityWeatherMapper())
                .onErrorReturnItem(true);
    }
    //endregion

    //region RoadConditions
    @Override
    public void OnRoadConditionsUpdate(DateTime dateTime) {
        cacheDao.AddCacheEntity(new CacheEntity(ROAD_CONDITIONS_LAST_UPDATE_KEY,dateTime));
    }

    @Override
    public Single<Boolean> areRoadConditionsStale(boolean refreshStale) {
        if(refreshStale)
            return Single.just(true);
        return cacheDao.GetCacheEntity(ROAD_CONDITIONS_LAST_UPDATE_KEY)
                .flatMap(cacheValidityMapper())
                .onErrorReturnItem(true);
    }
    //endregion

}
