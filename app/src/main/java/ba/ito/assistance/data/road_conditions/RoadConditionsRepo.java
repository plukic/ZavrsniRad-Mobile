package ba.ito.assistance.data.road_conditions;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.room.road_condtions.RoadConditionsDao;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.model.room.RoadCondition.RoadConditionEntity;
import ba.ito.assistance.services.cache.ICacheService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;

import io.reactivex.functions.Function;

public class RoadConditionsRepo implements IRoadConditionsRepo {
    private IApiService apiService;
    private RoadConditionsDao roadConditionsDao;
    private ICacheService cacheService;


    private Function<List<RoadConditionEntity>, Flowable<List<RoadConditionVM>>> roadConditionMapper;


    @Inject
    public RoadConditionsRepo(IApiService apiService, RoadConditionsDao roadConditionsDao, ICacheService cacheService, Function<List<RoadConditionEntity>, Flowable<List<RoadConditionVM>>> roadConditionMapper) {
        this.apiService = apiService;
        this.roadConditionsDao = roadConditionsDao;
        this.cacheService = cacheService;
        this.roadConditionMapper = roadConditionMapper;
    }

    @Override
    public Flowable<List<RoadConditionVM>> GetRoadConditions() {
        return roadConditionsDao
                .GetRoadConditions()
                .flatMap(roadConditionMapper);
    }


    @Override
    public Completable LoadRoadConditions(boolean refreshStale) {
        return cacheService.areRoadConditionsStale(refreshStale)
                .flatMapCompletable(isStale -> isStale?getDataFromApiAndPopulateDb():Completable.complete());
    }

    private CompletableSource getDataFromApiAndPopulateDb() {
        return apiService.GetRoadConditions()
                .flatMapCompletable(roadConditionVMS -> {
                    roadConditionsDao.saveRoadConditions(roadConditionVMS);
                    cacheService.OnRoadConditionsUpdate(new DateTime());
                    return Completable.complete();
                });
    }



}
