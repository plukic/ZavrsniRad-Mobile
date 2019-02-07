package ba.ito.assistance.model.road_condition;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

public class RoadConditionVM implements Parcelable {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Language")
    public String Language;
    @SerializedName("ValidUntil")
    public DateTime ValidUntil;
    @SerializedName("Description")
    public String Description;
    @SerializedName("RoadConditionType")
    public RoadConditionType RoadConditionType;

    @SerializedName("IsActive")
    public boolean IsActive;
    public DateTime UpdatedAt;

    public RoadConditionVM(){

    }

    protected RoadConditionVM(Parcel in) {
        Id = in.readInt();
        Language = in.readString();
        Description = in.readString();
        IsActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Language);
        dest.writeString(Description);
        dest.writeByte((byte) (IsActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoadConditionVM> CREATOR = new Creator<RoadConditionVM>() {
        @Override
        public RoadConditionVM createFromParcel(Parcel in) {
            return new RoadConditionVM(in);
        }

        @Override
        public RoadConditionVM[] newArray(int size) {
            return new RoadConditionVM[size];
        }
    };
}