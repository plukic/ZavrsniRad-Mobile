package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public class GasStationLocationVM  {
    @SerializedName("GasCompanyId")
    public int GasCompanyId;
    @SerializedName("GasStationId")
    public int GasStationId;
    @SerializedName("CompanyName")
    public String CompanyName;
    @SerializedName("Address")
    public String Address;
    @SerializedName("City")
    public String City;
    @SerializedName("Lat")
    public double Lat;
    @SerializedName("Long")
    public double Long;


}
