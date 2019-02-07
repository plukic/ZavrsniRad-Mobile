package ba.ito.assistance.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ba.ito.assistance.data.room.cache.ICacheDao;
import ba.ito.assistance.data.room.gas_stations.GasStationsDao;
import ba.ito.assistance.data.room.highways.HighwaysDao;
import ba.ito.assistance.data.room.road_condtions.RoadConditionsDao;
import ba.ito.assistance.data.room.settings.UserSettingsDao;
import ba.ito.assistance.model.cache.CacheEntity;
import ba.ito.assistance.model.gas_stations.NearestGasStationInfoEntity;
import ba.ito.assistance.model.highways.room.HighwayEntity;
import ba.ito.assistance.model.highways.room.HighwayRoutePriceEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothEntrancesEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothExitsEntity;
import ba.ito.assistance.model.room.RoadCondition.RoadConditionEntity;
import ba.ito.assistance.model.settings.UserSettingsEntity;

@Database(entities = {
        RoadConditionEntity.class,
        UserSettingsEntity.class,
        NearestGasStationInfoEntity.class,
        HighwayEntity.class,
        HighwayTollboothEntrancesEntity.class,
        HighwayTollboothExitsEntity.class,
        HighwayRoutePriceEntity.class,
        CacheEntity.class},

        version = 11,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AssistanceDb extends RoomDatabase {
    public abstract RoadConditionsDao roadConditionsDao();

    public abstract UserSettingsDao userSettingsDao();

    public abstract GasStationsDao gasStationsDao();

    public abstract HighwaysDao highwaysDao();

    public abstract ICacheDao cacheDao();
}
