package ba.ito.assistance.di.module;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.model.gas_stations.NearestGasStationInfoEntity;
import ba.ito.assistance.model.road_condition.RoadConditionItemVM;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.model.room.RoadCondition.RoadConditionEntity;
import ba.ito.assistance.util.Mapper.Mapper;
import ba.ito.assistance.util.Mapper.RoadConditionsMapper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

@Module
public abstract class MappingModule {

    @Binds
    @Singleton
    abstract Mapper<RoadConditionVM, RoadConditionEntity> roadConditionEntityMapper(RoadConditionsMapper roadConditionsMapper);


    @Provides
    @Singleton
    static Function<List<RoadConditionEntity>,Flowable<List<RoadConditionVM>>>roadConditionMapper(RoadConditionsMapper roadConditionsMapper) {
        return entityItems -> {
            List<RoadConditionVM> result = new ArrayList<>();
            for (RoadConditionEntity entityItem : entityItems) {
                result.add(roadConditionsMapper.reverseMap(entityItem));
            }
            return Flowable.just(result);
        };
    }


    @Provides
    @Singleton
    static   Function<List<NearestGasStationInfoEntity>, Flowable<List<GasStationListItemVM>>> nearestGasStationsMapper() {
        return entityItems -> {
            List<GasStationListItemVM> result = new ArrayList<>();
            for (NearestGasStationInfoEntity entityItem : entityItems) {
                result.add(new GasStationListItemVM(entityItem));
            }
            return Flowable.just(result);
        };
    }


}
