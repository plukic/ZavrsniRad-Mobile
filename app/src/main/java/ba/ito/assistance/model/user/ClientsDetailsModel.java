package ba.ito.assistance.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class ClientsDetailsModel implements Parcelable {


    @SerializedName("UserName")
    public String UserName;
    @SerializedName("OriginUserName")
    public String OriginUserName;
    @SerializedName("FirstName")
    public String FirstName;
    @SerializedName("LastName")
    public String LastName;
    @SerializedName("PhoneNumber")
    public String PhoneNumber;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Address")
    public String Address;
    @SerializedName("City")
    public String City;
    @SerializedName("ConfigurationGroup")
    public ConfigurationGroupEnum ConfigurationGroup;
    @SerializedName("BloodType")
    public BloodTypes BloodType;
    @SerializedName("ChronicDiseases")
    public String ChronicDiseases;
    @SerializedName("Diagnose")
    public String Diagnose;
    @SerializedName("HistoryOfCriticalIllness")
    public String HistoryOfCriticalIllness;
    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("SupportNumber")
    public String SupportNumber;



    protected ClientsDetailsModel(Parcel in) {
        UserName = in.readString();
        OriginUserName = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        PhoneNumber = in.readString();
        Email = in.readString();
        Address = in.readString();
        City = in.readString();
        ChronicDiseases = in.readString();
        Diagnose = in.readString();
        HistoryOfCriticalIllness = in.readString();
        IsActive = in.readByte() != 0;
        SupportNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(OriginUserName);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(PhoneNumber);
        dest.writeString(Email);
        dest.writeString(Address);
        dest.writeString(City);
        dest.writeString(ChronicDiseases);
        dest.writeString(Diagnose);
        dest.writeString(HistoryOfCriticalIllness);
        dest.writeByte((byte) (IsActive ? 1 : 0));
        dest.writeString(SupportNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientsDetailsModel> CREATOR = new Creator<ClientsDetailsModel>() {
        @Override
        public ClientsDetailsModel createFromParcel(Parcel in) {
            return new ClientsDetailsModel(in);
        }

        @Override
        public ClientsDetailsModel[] newArray(int size) {
            return new ClientsDetailsModel[size];
        }
    };
}
