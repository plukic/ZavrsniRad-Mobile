package ba.ito.assistance.model.road_condition;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class RoadConditionItemVM {

    @SerializedName("Id")
    public int Id;
    @SerializedName("DateFrom")
    public DateTime DateFrom;
    @SerializedName("DateTo")
    public DateTime DateTo;
    @SerializedName("Title")
    public String Title;
    @SerializedName("Description")
    public String Description;

}
