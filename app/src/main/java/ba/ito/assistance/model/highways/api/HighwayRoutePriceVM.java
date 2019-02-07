package ba.ito.assistance.model.highways.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import ba.ito.assistance.data.highway.VehiclesCategory;
import ba.ito.assistance.model.gas_stations.CurrencyEnum;
import ba.ito.assistance.model.highways.room.HighwayRoutePriceEntity;

public class HighwayRoutePriceVM implements Parcelable{
    @SerializedName("Id")
    public int Id;
    @SerializedName("EntranceId")
    public int EntranceId;
    @SerializedName("ExitId")
    public int ExitId;
    @SerializedName("Currency")
    public CurrencyEnum Currency;
    @SerializedName("Price")
    public double Price;
    @SerializedName("VehiclesCategory")
    public ba.ito.assistance.data.highway.VehiclesCategory VehiclesCategory;

    public HighwayRoutePriceVM() {
    }

    public HighwayRoutePriceVM(HighwayRoutePriceEntity item) {
        this.Id=item.Id;
        this.EntranceId=item.EntranceId;
        this.ExitId=item.ExitId;
        this.Currency=item.Currency;
        this.Price=item.Price;
        this.VehiclesCategory=item.VehiclesCategory;
    }


    protected HighwayRoutePriceVM(Parcel in) {
        Id = in.readInt();
        EntranceId = in.readInt();
        ExitId = in.readInt();
        Price = in.readDouble();
        Currency= (CurrencyEnum) in.readSerializable();
        VehiclesCategory= (VehiclesCategory) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(EntranceId);
        dest.writeInt(ExitId);
        dest.writeDouble(Price);
        dest.writeSerializable(Currency);
        dest.writeSerializable(VehiclesCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HighwayRoutePriceVM> CREATOR = new Creator<HighwayRoutePriceVM>() {
        @Override
        public HighwayRoutePriceVM createFromParcel(Parcel in) {
            return new HighwayRoutePriceVM(in);
        }

        @Override
        public HighwayRoutePriceVM[] newArray(int size) {
            return new HighwayRoutePriceVM[size];
        }
    };
}
