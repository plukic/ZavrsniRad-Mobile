package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public class FuelPricesVM {
    @SerializedName("Id") public int Id ;
    @SerializedName("FuelType") public FuelTypeEnum FuelType ;
    @SerializedName("Currency") public CurrencyEnum Currency ;
    @SerializedName("Price") public double Price ;
}
