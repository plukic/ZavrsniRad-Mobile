package ba.ito.assistance.data.highway;

import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;

public enum VehiclesCategory {

    @SerializedName("1")
    I,
    @SerializedName("2")
    II,
    @SerializedName("3")
    III,
    @SerializedName("4")
    IV;

    public String getStringName() {
        switch (this) {
            case I:
                return "1";
            case II:
                return "2";
            case III:
                return "3";
            case IV:
                return "4";
        }
        throw  new InvalidParameterException();
    }

    public static VehiclesCategory getType(String currency) {
        switch (currency){
            case "1":return I;
            case "2":return II;
            case "3":return III;
            case "4":return IV;
        }
        throw  new InvalidParameterException();
    }
}