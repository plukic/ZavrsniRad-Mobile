package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

public class ClientConfirmResetPasswordVM {
    @SerializedName("Token")
    private String Token;
    @SerializedName("NewPassword")
    private String NewPassword;
    @SerializedName("Username")
    private String Username;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
