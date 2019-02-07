package ba.ito.assistance.model.highways.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import ba.ito.assistance.model.highways.room.HighwayEntity;

public class HighwayVM implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Name")
    public String Name;

    public HighwayVM() {

    }

    public HighwayVM(HighwayEntity entity) {
        this.Id = entity.Id;
        this.Name = entity.Name;
    }

    protected HighwayVM(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HighwayVM> CREATOR = new Creator<HighwayVM>() {
        @Override
        public HighwayVM createFromParcel(Parcel in) {
            return new HighwayVM(in);
        }

        @Override
        public HighwayVM[] newArray(int size) {
            return new HighwayVM[size];
        }
    };
}
