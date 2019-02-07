package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public enum WorkingDayOfWeek {
    @SerializedName("1")
    MON_FRIDAY,
    @SerializedName("2")
    SATURDAY,
    @SerializedName("3")
    SUNDAY
}
