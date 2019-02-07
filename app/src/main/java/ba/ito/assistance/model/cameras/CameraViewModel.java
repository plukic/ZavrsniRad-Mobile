package ba.ito.assistance.model.cameras;

import com.google.gson.annotations.SerializedName;

public class CameraViewModel {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Name")
    public String Name;
    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("Langitude")
    public double Langitude;
    @SerializedName("Longitude")
    public double Longitude;
    @SerializedName("WebImageLocation")
    public String WebImageLocation;
}
