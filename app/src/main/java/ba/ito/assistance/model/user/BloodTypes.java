package ba.ito.assistance.model.user;

import com.google.gson.annotations.SerializedName;

import ba.ito.assistance.R;

public enum BloodTypes {
    @SerializedName("1")
    Zero,
    @SerializedName("2")
    A,
    @SerializedName("3")
    B,
    @SerializedName("4")
    AB;

    public Integer getString() {
        switch (this) {
            case Zero:
                return R.string.zero_blood_type;
            case A:
                return R.string.a_blood_type;
            case B:
                return R.string.b_blood_type;
            case AB:
                return R.string.ab_blood_type;
        }
        return null;
    }
}
