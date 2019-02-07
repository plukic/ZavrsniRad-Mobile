package ba.ito.assistance.model.highways.room;

import android.arch.persistence.room.Entity;

import ba.ito.assistance.model.gas_stations.CurrencyEnum;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;

@Entity(tableName = "HighwayRoutePriceEntity", primaryKeys = {"Id"})
public class HighwayRoutePriceEntity {
    public int Id;
    public int HighwayId;
    public int EntranceId;
    public int ExitId;
    public CurrencyEnum Currency;
    public double Price;
    public ba.ito.assistance.data.highway.VehiclesCategory VehiclesCategory;
    public boolean IsActive;

    public HighwayRoutePriceEntity() {
    }

    public HighwayRoutePriceEntity(HighwayRoutePriceVM vm, int highwayId) {
        Id = vm.Id;
        HighwayId = highwayId;
        EntranceId = vm.EntranceId;
        ExitId = vm.ExitId;
        Currency = vm.Currency;
        Price = vm.Price;
        VehiclesCategory = vm.VehiclesCategory;
        IsActive = true;
    }
}
