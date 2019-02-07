package ba.ito.assistance.data.room.settings;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.settings.UserSettingsEntity;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

@Dao
public abstract class UserSettingsDao {
    @Query("SELECT * FROM UserSettings")
    public abstract Single<UserSettingsEntity> GetUserSettings();



    public Single<UserSettingsEntity> LoadUserSettings(){
        return GetUserSettings()
                .onErrorResumeNext(throwable -> {
                    UserSettingsEntity defaultEntity = new UserSettingsEntity();
                    defaultEntity.SelectedFuel= FuelTypeEnum.Diesel;
                return Single.just(defaultEntity);
                });

    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void saveUserSettings(UserSettingsEntity userSettingsEntity);

    @Update()
    public abstract void updateSettings(UserSettingsEntity userSettingsEntity);
}
