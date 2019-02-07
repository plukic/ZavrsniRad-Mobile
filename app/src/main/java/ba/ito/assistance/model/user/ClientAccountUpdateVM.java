package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class ClientAccountUpdateVM {
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



    @SerializedName("BloodType")
    public BloodTypes BloodType;
    @SerializedName("ChronicDiseases")
    public String ChronicDiseases;
    @SerializedName("Diagnose")
    public String Diagnose;
    @SerializedName("HistoryOfCriticalIllness")
    public String HistoryOfCriticalIllness;
}
