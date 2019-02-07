package ba.ito.assistance.model.highways.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import ba.ito.assistance.model.highways.room.HighwayTollboothEntrancesEntity;
import ba.ito.assistance.model.highways.room.HighwayTollboothExitsEntity;

public class HighwayTollboothVM implements Parcelable{
    @SerializedName("Id")
    public int Id;
    @SerializedName("Name")
    public String Name;

    public HighwayTollboothVM(){

    }
    public HighwayTollboothVM(HighwayTollboothEntrancesEntity item){
        this.Id=item.Id;
        this.Name=item.Name;
    }
    protected HighwayTollboothVM(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
    }

    public HighwayTollboothVM(HighwayTollboothExitsEntity item) {
        this.Id=item.Id;
        this.Name=item.Name;
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

    public static final Creator<HighwayTollboothVM> CREATOR = new Creator<HighwayTollboothVM>() {
        @Override
        public HighwayTollboothVM createFromParcel(Parcel in) {
            return new HighwayTollboothVM(in);
        }

        @Override
        public HighwayTollboothVM[] newArray(int size) {
            return new HighwayTollboothVM[size];
        }
    };
}
