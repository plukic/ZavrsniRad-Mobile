package ba.ito.assistance.data.room.road_condtions;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;


import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import ba.ito.assistance.model.room.RoadCondition.RoadConditionEntity;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import io.reactivex.Flowable;

@Dao
public abstract class RoadConditionsDao {

    @Transaction
    @Query("SELECT * FROM RoadConditions WHERE IsActive = 1")
    public abstract Flowable<List<RoadConditionEntity>> GetRoadConditions( );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  abstract void saveRoadCondition(List<RoadConditionEntity>roadConditionEntity);

    @Query("UPDATE RoadConditions SET IsActive =0")
    public  abstract void updateRoadConditionsToInactive();

    @Transaction
    public void saveRoadConditions(List<RoadConditionVM> items) {
        updateRoadConditionsToInactive();
        List<RoadConditionEntity> list = new ArrayList<>();
        for (RoadConditionVM vm: items) {
            RoadConditionEntity roadConditionItemEntity = new RoadConditionEntity(vm);
            roadConditionItemEntity.UpdatedAt= DateTime.now();
            list.add(roadConditionItemEntity);
        }
        saveRoadCondition(list);

    }

}
