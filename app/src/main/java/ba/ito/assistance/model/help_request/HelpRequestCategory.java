package ba.ito.assistance.model.help_request;

import com.google.gson.annotations.SerializedName;

public enum HelpRequestCategory {
    @SerializedName("1")
    Accident,
    @SerializedName("2")
    Malfunction,
    @SerializedName("3")
    Other
}
