package ba.ito.assistance.model.help_request;

import com.google.gson.annotations.SerializedName;

public class HelpRequest {
    @SerializedName("HelpRequestCategory")
    public HelpRequestCategory HelpRequestCategory;
    @SerializedName("Latitude")
    public double Latitude;
    @SerializedName("Longitude")
    public double Longitude;
}
