package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GasCompanyVM {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Name")
    public String Name;
    @SerializedName("WebAddress")
    public String WebAddress;
    @SerializedName("IconUrl")
    public String IconUrl;
    @SerializedName("GasStations")
    public List<GasStationVM> GasStations;
}
