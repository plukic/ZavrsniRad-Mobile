package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public class WorkingHoursVM {

    @SerializedName("Id")
    public int Id;
    @SerializedName("DayOfWeek")
    public WorkingDayOfWeek DayOfWeek;
    @SerializedName("StartTime")
    public String StartTime;
    @SerializedName("EndTime")
    public String EndTime;
}
