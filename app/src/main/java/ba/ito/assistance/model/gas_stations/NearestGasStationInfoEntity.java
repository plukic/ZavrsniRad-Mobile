package ba.ito.assistance.model.gas_stations;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "NearestGasStationInfo",primaryKeys = {"GasStationId","FuelType"})
public class NearestGasStationInfoEntity {
    public int GasStationId;
    public int GasCompanyId;
    public String IconUrl;
    public String GasCompanyName;
    public String GasCompanyCity;
    public String GasCompanyAddress;
    public double Price;
    public CurrencyEnum Currency;
    public double Lat;
    public double Long;
    @NonNull
    public FuelTypeEnum FuelType;

    public NearestGasStationInfoEntity(){

    }
    public NearestGasStationInfoEntity(GasStationListItemVM items,@NonNull FuelTypeEnum fuelTypeEnum) {
        GasStationId = items.GasStationId;
        GasCompanyId = items.GasCompanyId;
        IconUrl = items.IconUrl;
        GasCompanyName = items.GasCompanyName;
        GasCompanyCity = items.GasCompanyCity;
        GasCompanyAddress = items.GasCompanyAddress;
        Price = items.Price;
        Currency = items.Currency;
        Lat = items.Lat;
        Long = items.Long;
        FuelType = fuelTypeEnum;
    }
}
