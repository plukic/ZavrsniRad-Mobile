package ba.ito.assistance.model.settings;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.DateTime;

import ba.ito.assistance.model.gas_stations.FuelTypeEnum;


@Entity(tableName = "UserSettings")
public class UserSettingsEntity {
    @PrimaryKey
    public int Id;
    public FuelTypeEnum SelectedFuel;
}
