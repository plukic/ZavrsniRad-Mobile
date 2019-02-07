package ba.ito.assistance.model.help_request;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class HelpRequestResponseVM {
    @SerializedName("ClientId")
    public String ClientId;
    @SerializedName("ContactPhoneNumber")
    public String ContactPhoneNumber;
    @SerializedName("HelpRequestState")
    public String HelpRequestState;
    @SerializedName("ShortInstructions")
    public String ShortInstructions;

    @SerializedName("HelpIncomingDateTime")
    public DateTime HelpIncomingDateTime;

}
