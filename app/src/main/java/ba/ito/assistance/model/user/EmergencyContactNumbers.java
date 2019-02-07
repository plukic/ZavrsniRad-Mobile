package ba.ito.assistance.model.user;

import android.os.Parcel;
import android.os.Parcelable;

public class EmergencyContactNumbers implements Parcelable {
    public int Id;
    public String ContactFullName;
    public String PhoneNumber;

    protected EmergencyContactNumbers(Parcel in) {
        Id = in.readInt();
        ContactFullName = in.readString();
        PhoneNumber = in.readString();
    }

    public EmergencyContactNumbers() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(ContactFullName);
        dest.writeString(PhoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmergencyContactNumbers> CREATOR = new Creator<EmergencyContactNumbers>() {
        @Override
        public EmergencyContactNumbers createFromParcel(Parcel in) {
            return new EmergencyContactNumbers(in);
        }

        @Override
        public EmergencyContactNumbers[] newArray(int size) {
            return new EmergencyContactNumbers[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmergencyContactNumbers that = (EmergencyContactNumbers) o;

        if (ContactFullName != null ? !ContactFullName.equals(that.ContactFullName) : that.ContactFullName != null)
            return false;
        return PhoneNumber != null ? PhoneNumber.equals(that.PhoneNumber) : that.PhoneNumber == null;
    }

    @Override
    public int hashCode() {
        int result = ContactFullName != null ? ContactFullName.hashCode() : 0;
        result = 31 * result + (PhoneNumber != null ? PhoneNumber.hashCode() : 0);
        return result;
    }
}
