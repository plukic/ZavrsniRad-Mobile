package ba.ito.assistance.data.room.highways;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.model.highways.room.HighwayEntity;
import ba.ito.assistance.model.highways.room.HighwayRoutePriceEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothEntrancesEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothExitsEntity;
import io.reactivex.Flowable;

@Dao
public abstract class HighwaysDao {
    @Query("SELECT * FROM Highways WHERE IsActive=1")
    public abstract Flowable<List<HighwayEntity>> GetHighways();

    @Query("SELECT * FROM HighwayTollboothEntrancesEntity WHERE IsActive=1 AND HighwayId=:highwayId")
    public abstract Flowable<List<HighwayTollboothEntrancesEntity>> GetEntrances(int highwayId);

    @Query("SELECT * FROM HighwayTollboothExitsEntity WHERE IsActive=1 AND HighwayId=:highwayId AND EntranceId=:entranceId")
    public abstract Flowable<List<HighwayTollboothExitsEntity>> GetExits(int highwayId, int entranceId);

    @Query("SELECT * FROM HighwayRoutePriceEntity WHERE IsActive=1 AND HighwayId=:highwayId AND EntranceId=:entranceId AND ExitId=:exitId")
    public abstract Flowable<List<HighwayRoutePriceEntity>> GetPrices(int highwayId, int entranceId, int exitId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveHighway(List<HighwayEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveEntrances(List<HighwayTollboothEntrancesEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveExits(List<HighwayTollboothExitsEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void savePrices(List<HighwayRoutePriceEntity> entities);


    @Query("UPDATE Highways SET IsActive =0 WHERE Id NOT IN(:activeIds)")
    public abstract void updateHighwaysToInactive(int[] activeIds);

    @Query("UPDATE HighwayTollboothEntrancesEntity SET IsActive =0 WHERE HighwayId=:highwayId AND Id NOT IN(:ids)")
    public abstract void updateEntrancesToInactive(int[] ids,int highwayId);

    @Query("UPDATE HighwayTollboothExitsEntity SET IsActive =0 WHERE HighwayId=:highwayId AND EntranceId=:entranceId AND Id NOT IN(:ids)")
    public abstract void updateExitsToInactive(int[] ids,int highwayId, int entranceId);

    @Query("UPDATE HighwayRoutePriceEntity SET IsActive =0 WHERE HighwayId=:highwayId AND EntranceId=:entranceId AND ExitId=:exitId AND Id NOT IN(:ids)")
    public abstract void updatePricesToInactive(int[] ids, int highwayId, int entranceId, int exitId);


    public void saveHighways(List<HighwayVM> items) {
        List<HighwayEntity> entities = new ArrayList<>();

        int[] ids = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            entities.add(new HighwayEntity(items.get(i)));
            ids[i] = items.get(i).Id;
        }

        updateHighwaysToInactive(ids);
        saveHighway(entities);
    }

    public void saveExits(List<HighwayTollboothVM> items, int entranceId, int highwayId) {
        List<HighwayTollboothExitsEntity> entities = new ArrayList<>();
        int ids[]= new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            entities.add(new HighwayTollboothExitsEntity(items.get(i), highwayId, entranceId));
            ids[i]=items.get(i).Id;
        }

        updateExitsToInactive(ids,highwayId, entranceId);
        saveExits(entities);
    }

    public void saveEntrances(List<HighwayTollboothVM> items, int highwayId) {
        List<HighwayTollboothEntrancesEntity> entities = new ArrayList<>();
        int ids[]= new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            entities.add(new HighwayTollboothEntrancesEntity(items.get(i), highwayId));
            ids[i]=items.get(i).Id;
        }

        updateEntrancesToInactive(ids,highwayId);
        saveEntrances(entities);
    }

    public void savePrices(List<HighwayRoutePriceVM> items, int highwayId, int entranceId, int exitId) {
        List<HighwayRoutePriceEntity> entities = new ArrayList<>();
        int ids[]= new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            entities.add(new HighwayRoutePriceEntity(items.get(i), highwayId));
            ids[i]=items.get(i).Id;
        }

        updatePricesToInactive(ids,highwayId, entranceId, exitId);
        savePrices(entities);
    }

}
