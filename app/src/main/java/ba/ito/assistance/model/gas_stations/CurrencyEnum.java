package ba.ito.assistance.model.gas_stations;

import com.google.gson.annotations.SerializedName;

public enum CurrencyEnum {
    @SerializedName("1")
    KM,
    @SerializedName("2")
    KN,
    @SerializedName("3")
    EURO;

    public static CurrencyEnum getType(String currencyName) {
        switch (currencyName) {
            case "KM":
                return KM;
            case "KN":
                return KN;
            case "EURO":
                return EURO;
        }
        return null;

    }

    public String getStringName() {
        switch (this) {

            case KM:
                return "KM";
            case KN:
                return "KN";
            case EURO:
                return "EURO";
        }
        return null;
    }
}
