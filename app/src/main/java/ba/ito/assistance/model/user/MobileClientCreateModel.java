package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

public class MobileClientCreateModel {
    @SerializedName("FirstName") public String FirstName;
    @SerializedName("LastName") public String LastName;
    @SerializedName("CardNumber") public String CardNumber;
    @SerializedName("Password") public String Password;
    @SerializedName("PhoneNumber") public String PhoneNumber;
    @SerializedName("Email") public String Email;
    @SerializedName("Address") public String Address;
    @SerializedName("City") public String City;
    @SerializedName("ConfigurationGroup") public ConfigurationGroupEnum ConfigurationGroup;
}
