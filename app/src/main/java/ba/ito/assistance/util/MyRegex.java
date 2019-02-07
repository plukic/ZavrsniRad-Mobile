package ba.ito.assistance.util;

import java.util.regex.Pattern;

import javax.inject.Inject;

import ba.ito.assistance.ui.user_information.UserInformationsController;

public class MyRegex {
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%])).{6,50}";
    private static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9+._%\\-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private static final Pattern PHONE
            = Pattern.compile(                      // sdd = space, dot, or dash
            "^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?");
    private Pattern PasswordPattern;

    @Inject
    public MyRegex() {
        PasswordPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean isValidPassword(final String password) {
        return PasswordPattern.matcher(password).matches();
    }


    public boolean isValidEmail(String text) {
        return EMAIL_ADDRESS.matcher(text).matches();
    }

    public boolean isValidPhoneNumber(String text) {
        return PHONE.matcher(text).matches();
    }

    public boolean isValidProfileInput(String value, UserInformationsController.UserInformationUpdateType updateType, String oldValue) {
        if (value == null || value.isEmpty() || value.equals(oldValue))
            return false;
        switch (updateType) {
            case FIRST_NAME:
                return true;
            case LAST_NAME:
                return true;
            case EMAIL:
                return isValidEmail(value);
            case PHONE_NUMBER:
                return isValidPhoneNumber(value);
            case ADDRESS:
                return true;
            case CITY:
                return true;
        }
        return true;
    }

    public boolean isValidFullName(String fullName, String oldFullname) {
        if (fullName == null || fullName.isEmpty())
            return false;
        return true;
    }

    public boolean isValidEmergencyContactNumber(String newValue, String oldValue) {
        if (newValue == null || newValue.isEmpty())
            return false;

        return isValidPhoneNumber(newValue);
    }

    public boolean isValidUsername(String value) {
        return value != null && value.length() >= 4;
    }

    public boolean isValidLastName(String value) {
        return value!=null && value.length()>2;
    }

    public boolean isValidFirstName(String value) {
        return value!=null && value.length()>2;
    }
}
