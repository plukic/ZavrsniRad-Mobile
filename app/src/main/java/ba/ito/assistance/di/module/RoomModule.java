package ba.ito.assistance.di.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import ba.ito.assistance.data.room.AssistanceDb;
import ba.ito.assistance.data.room.cache.ICacheDao;
import ba.ito.assistance.data.room.gas_stations.GasStationsDao;
import ba.ito.assistance.data.room.highways.HighwaysDao;
import ba.ito.assistance.data.room.road_condtions.RoadConditionsDao;
import ba.ito.assistance.data.room.settings.UserSettingsDao;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RoomModule {

    @Singleton
    @Provides
    static AssistanceDb db(Context context) {
        return Room.databaseBuilder(context, AssistanceDb.class, "AssistanceDb")
                .fallbackToDestructiveMigration().build();
    }


    @Singleton
    @Provides
    static RoadConditionsDao roadConditionsDao(AssistanceDb assistanceDb) {
        return assistanceDb.roadConditionsDao();
    }

    @Singleton
    @Provides
    static UserSettingsDao userSettingsDao(AssistanceDb assistanceDb) {
        return assistanceDb.userSettingsDao();
    }

    @Singleton
    @Provides
    static GasStationsDao gasStationsDao(AssistanceDb assistanceDb) {
        return assistanceDb.gasStationsDao();
    }

    @Singleton
    @Provides
    static HighwaysDao highwaysDao(AssistanceDb assistanceDb) {
        return assistanceDb.highwaysDao();
    }

    @Singleton
    @Provides
    static ICacheDao cacheDao(AssistanceDb assistanceDb) {
        return assistanceDb.cacheDao();
    }

}
