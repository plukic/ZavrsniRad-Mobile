package ba.ito.assistance.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {
    @Expose
    @SerializedName(".expires")
    public String expires;
    @Expose
    @SerializedName(".issued")
    public String issued;
    @Expose
    @SerializedName("userName")
    public String userName;
    @Expose
    @SerializedName("refresh_token")
    public String refresh_token;
    @Expose
    @SerializedName("expires_in")
    public int expires_in;
    @Expose
    @SerializedName("token_type")
    public String token_type;
    @Expose
    @SerializedName("access_token")
    public String access_token;


}
