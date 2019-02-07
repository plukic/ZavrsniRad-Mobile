package ba.ito.assistance.data.room.gas_stations;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.model.gas_stations.NearestGasStationInfoEntity;
import io.reactivex.Flowable;

@Dao
public abstract class GasStationsDao {
    @Query("SELECT * FROM NearestGasStationInfo WHERE FuelType =:fuelTypeEnum")
    public abstract Flowable<List<NearestGasStationInfoEntity>> GetNearestGasStations(FuelTypeEnum fuelTypeEnum);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveNearestGasStationItem(NearestGasStationInfoEntity items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveNearestGasStationItems(List<NearestGasStationInfoEntity> items);

    public void saveGasStationItem(GasStationListItemVM items, FuelTypeEnum fuelTypeEnum) {
        saveNearestGasStationItem(new NearestGasStationInfoEntity(items, fuelTypeEnum));
    }

    public void saveGasStationItems(List<GasStationListItemVM> items, FuelTypeEnum fuelTypeEnum) {
        List<NearestGasStationInfoEntity> entities = new ArrayList<>();
        for (GasStationListItemVM item : items) {
            entities.add(new NearestGasStationInfoEntity(item, fuelTypeEnum));
        }

        saveNearestGasStationItems(entities);
    }


}
