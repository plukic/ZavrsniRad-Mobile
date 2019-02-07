package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public enum FuelTypeEnum {
    @SerializedName("1")
    Petrol,
    @SerializedName("2")
    Diesel,
    @SerializedName("3")
    LPG,
    @SerializedName("4")
    Other;



    public static FuelTypeEnum getType(String fuelTypeName) {
        switch (fuelTypeName) {
            case "Petrol":
                return Petrol;
            case "Diesel":
                return Diesel;
            case "LPG":
                return LPG;
            case "Other":
                return Other;
        }
        return null;

    }

    public String getStringName() {
        switch (this) {
            case Petrol:
                return "Petrol";
            case Diesel:
                return "Diesel";
            case LPG:
                return "LPG";
            case Other:
                return "Other";
        }
        return null;
    }
}
