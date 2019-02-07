package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

public class ClientResetPasswordVM {
    @SerializedName("Username")
    private String Username;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
