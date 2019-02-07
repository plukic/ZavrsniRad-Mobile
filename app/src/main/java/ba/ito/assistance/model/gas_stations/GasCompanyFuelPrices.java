package ba.ito.assistance.model.gas_stations;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

public class GasCompanyFuelPrices implements Parcelable{
    @SerializedName("GasCompanyId")
    public int GasCompanyId;
    @SerializedName("GasStationId")
    public int GasStationId;
    @SerializedName("CompanyLogoUrl")
    public String CompanyLogoUrl;
    @SerializedName("GasStationLocation")
    public String GasStationLocation;
    @SerializedName("GasCompanyDescription")
    public String GasCompanyDescription;
    @SerializedName("GasCompanyName")
    public String GasCompanyName;

    @SerializedName("Lat")
    public double Lat;
    @SerializedName("Long")
    public double Long;

    @SerializedName("Fuel")
    public List<GasStationFuel> Fuel;

    public GasCompanyFuelPrices() {
    }


    protected GasCompanyFuelPrices(Parcel in) {
        GasCompanyId = in.readInt();
        GasStationId = in.readInt();
        CompanyLogoUrl = in.readString();
        GasStationLocation = in.readString();
        GasCompanyDescription = in.readString();
        GasCompanyName = in.readString();
        Lat = in.readDouble();
        Long = in.readDouble();
        Fuel = in.createTypedArrayList(GasStationFuel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(GasCompanyId);
        dest.writeInt(GasStationId);
        dest.writeString(CompanyLogoUrl);
        dest.writeString(GasStationLocation);
        dest.writeString(GasCompanyDescription);
        dest.writeString(GasCompanyName);
        dest.writeDouble(Lat);
        dest.writeDouble(Long);
        dest.writeTypedList(Fuel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GasCompanyFuelPrices> CREATOR = new Creator<GasCompanyFuelPrices>() {
        @Override
        public GasCompanyFuelPrices createFromParcel(Parcel in) {
            return new GasCompanyFuelPrices(in);
        }

        @Override
        public GasCompanyFuelPrices[] newArray(int size) {
            return new GasCompanyFuelPrices[size];
        }
    };
}
