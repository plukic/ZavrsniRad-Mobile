package ba.ito.assistance.data.road_conditions;


import java.util.List;

import ba.ito.assistance.model.road_condition.RoadConditionVM;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface IRoadConditionsRepo {
    Flowable<List<RoadConditionVM>> GetRoadConditions();


    Completable LoadRoadConditions(boolean refreshStale);
}
