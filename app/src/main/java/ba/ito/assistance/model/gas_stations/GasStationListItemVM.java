package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public class GasStationListItemVM {

    @SerializedName("GasStationId")
    public int GasStationId;
    @SerializedName("GasCompanyId")
    public int GasCompanyId;
    @SerializedName("IconUrl")
    public String IconUrl;
    @SerializedName("GasCompanyName")
    public String GasCompanyName;
    @SerializedName("GasCompanyCity")
    public String GasCompanyCity;
    @SerializedName("GasCompanyAddress")
    public String GasCompanyAddress;
    @SerializedName("Price")
    public double Price;
    @SerializedName("Currency")
    public CurrencyEnum Currency;
    @SerializedName("Lat")
    public double Lat;
    @SerializedName("Long")
    public double Long;

    public Integer DistanceNumeric;
    public String Distance;

    public GasStationListItemVM(){

    }
    public GasStationListItemVM(NearestGasStationInfoEntity entityItem) {
        GasStationId = entityItem.GasStationId;
        GasCompanyId = entityItem.GasCompanyId;
        IconUrl = entityItem.IconUrl;
        GasCompanyName = entityItem.GasCompanyName;
        GasCompanyCity = entityItem.GasCompanyCity;
        GasCompanyAddress = entityItem.GasCompanyAddress;
        Price = entityItem.Price;
        Currency = entityItem.Currency;
        Lat = entityItem.Lat;
        Long = entityItem.Long;

    }
}
