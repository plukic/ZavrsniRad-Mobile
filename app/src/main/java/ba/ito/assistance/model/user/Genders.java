package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;

public enum Genders {
    @SerializedName("1")
    MALE,
    @SerializedName("2")
    FEMALE;

    public static Genders getType(int value) {
        switch (value) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
        }
        throw new InvalidParameterException("Invalid gender value");
    }

    public int getIntValue() {
        switch (this) {
            case MALE:
                return 1;
            case FEMALE:
                return 2;
        }
        throw new InvalidParameterException("Invalid gender value");

    }
}
