package ba.ito.assistance.data.highway;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.room.cache.ICacheDao;
import ba.ito.assistance.data.room.highways.HighwaysDao;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.model.highways.room.HighwayEntity;
import ba.ito.assistance.model.highways.room.HighwayRoutePriceEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothEntrancesEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothExitsEntity;
import ba.ito.assistance.services.cache.ICacheService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class HighwayRepo implements IHighwayRepo {
    private IApiService apiService;
    private HighwaysDao highwaysDao;
    private ICacheService cacheService;

    @Inject
    public HighwayRepo(IApiService apiService, HighwaysDao highwaysDao, ICacheDao cacheDao, ICacheService cacheServise) {
        this.apiService = apiService;
        this.highwaysDao = highwaysDao;
        this.cacheService = cacheServise;
    }

    //region highways
    @Override
    public Flowable<List<HighwayVM>> GetHighways() {
        return highwaysDao.GetHighways()
                .flatMap((Function<List<HighwayEntity>, Flowable<List<HighwayVM>>>) highwayEntities -> {
                    List<HighwayVM> highways = new ArrayList<>();
                    for (HighwayEntity item : highwayEntities) {
                        highways.add(new HighwayVM(item));
                    }
                    return Flowable.just(highways);
                });
    }

    @Override
    public Completable LoadHighways(boolean forceRefresh) {
        return cacheService
                .areHighwayListStale(forceRefresh)
                .flatMapCompletable(isStale -> isStale ? getHighwaysFromApiAndSaveToDb() : Completable.complete());
    }

    private CompletableSource getHighwaysFromApiAndSaveToDb() {
        return apiService.GetHighways()
                .flatMapCompletable(highwayVMS ->
                {
                    highwaysDao.saveHighways(highwayVMS);
                    cacheService.OnHighwaysUpdate(DateTime.now());
                    return Completable.complete();
                });
    }
    //endregion

    //region HighwaysExits
    @Override
    public Flowable<List<HighwayTollboothVM>> GetHighwaysExits(int highwayId, int entranceId) {
        return highwaysDao
                .GetExits(highwayId, entranceId)
                .flatMap(highwayTollboothExitsEntities -> {
                    List<HighwayTollboothVM> highwayTollboothVMS = new ArrayList<>();
                    for (HighwayTollboothExitsEntity item : highwayTollboothExitsEntities) {
                        highwayTollboothVMS.add(new HighwayTollboothVM(item));
                    }
                    return Flowable.just(highwayTollboothVMS);
                });
    }

    @Override
    public Completable LoadExits(int highwayId, int entranceId, boolean forceRefresh) {
        return cacheService.areHighwayExitsListStale(forceRefresh, highwayId, entranceId)
                .flatMapCompletable(isStale -> isStale ? getHighwayExitsFromApiAndUpdateDb(highwayId, entranceId) : Completable.complete());
    }

    private Completable getHighwayExitsFromApiAndUpdateDb(int highwayId, int entranceId) {
        return apiService
                .GetHighwayExits(highwayId, entranceId)
                .flatMapCompletable(highwayExits ->
                {
                    highwaysDao.saveExits(highwayExits, entranceId, highwayId);
                    cacheService.OnHighwayExitsUpdate(new DateTime(), highwayId, entranceId);
                    return Completable.complete();
                });
    }
    //endregion

    //region HighwayEntrances
    @Override
    public Flowable<List<HighwayTollboothVM>> GetHighwaysEntrances(int highwayId) {
        return highwaysDao
                .GetEntrances(highwayId)
                .flatMap(highwayTollboothEntrancesEntities -> {
                    List<HighwayTollboothVM> highwayTollboothVMS = new ArrayList<>();
                    for (HighwayTollboothEntrancesEntity item : highwayTollboothEntrancesEntities) {
                        highwayTollboothVMS.add(new HighwayTollboothVM(item));
                    }
                    return Flowable.just(highwayTollboothVMS);
                });
    }

    @Override
    public Completable LoadEntrances(int highwayId, boolean forceRefresh) {
        return cacheService
                .areHighwayEntrancesListStale(forceRefresh, highwayId)
                .flatMapCompletable(isStale -> isStale ? getHighwayEntrancesFromApiAndUpdateDb(highwayId) : Completable.complete());
    }

    private CompletableSource getHighwayEntrancesFromApiAndUpdateDb(int highwayId) {
        return apiService
                .GetHighwayEntries(highwayId)
                .flatMapCompletable(highwayEntries ->
                {
                    highwaysDao.saveEntrances(highwayEntries, highwayId);
                    cacheService.OnHighwayEntrancesUpdate(new DateTime(), highwayId);
                    return Completable.complete();
                });
    }


    //endregion


    @Override
    public Flowable<List<HighwayRoutePriceVM>> GetHighwayPrices(int highwayId, int entranceId, int exitId) {
        return
                highwaysDao.GetPrices(highwayId, entranceId, exitId)
                        .flatMap(highwayRoutePriceEntities -> {
                            List<HighwayRoutePriceVM> result = new ArrayList<>();
                            for (HighwayRoutePriceEntity vm : highwayRoutePriceEntities) {
                                result.add(new HighwayRoutePriceVM(vm));
                            }
                            return Flowable.just(result);
                        });
    }


    @Override
    public Completable LoadHighwayPrices(int highwayId, int entranceId, int exitId, boolean forceRefresh) {
        return cacheService.areHighwayRoutePricesStale(forceRefresh, highwayId, entranceId, exitId)
                        .flatMapCompletable((isStale) -> isStale ? getHighwayRoutePricesAndUpdateDb(highwayId, entranceId, exitId) : Completable.complete());
    }


    private CompletableSource getHighwayRoutePricesAndUpdateDb(int highwayId, int entranceId, int exitId) {
        return apiService
                .GetHighwayPrices(highwayId, entranceId, exitId)
                .flatMapCompletable(highwayRoutePriceVMS -> {
                    highwaysDao.savePrices(highwayRoutePriceVMS, highwayId, entranceId, exitId);
                    cacheService.OnHighwayRoutePricesUpdated(new DateTime(),highwayId,entranceId,exitId);
                    return Completable.complete();
                });
    }


}
