package ba.ito.assistance.model.gas_stations;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class GasStationFuel implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Currency")
    public CurrencyEnum Currency;
    @SerializedName("Price")
    public double Price;
    @SerializedName("FuelName")
    public String FuelName;
    @SerializedName("FuelType")
    public FuelTypeEnum FuelType;
    @SerializedName("UpdateAt")
    public DateTime UpdateAt;

    public GasStationFuel() {

    }

    protected GasStationFuel(Parcel in) {
        Id = in.readInt();
        Price = in.readDouble();
        FuelName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeDouble(Price);
        dest.writeString(FuelName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GasStationFuel> CREATOR = new Creator<GasStationFuel>() {
        @Override
        public GasStationFuel createFromParcel(Parcel in) {
            return new GasStationFuel(in);
        }

        @Override
        public GasStationFuel[] newArray(int size) {
            return new GasStationFuel[size];
        }
    };
}
