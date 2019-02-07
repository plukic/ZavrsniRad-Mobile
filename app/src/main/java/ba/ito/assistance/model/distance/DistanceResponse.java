package ba.ito.assistance.model.distance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistanceResponse {

    @Expose
    @SerializedName("status")
    public String status;
    @Expose
    @SerializedName("rows")
    public List<DistanceMatrixRow> rows;

    public static class DistanceMatrixRow {
        @Expose
        @SerializedName("elements")
        public List<DistanceMatrixElementStatus > elements;
    }

    public static class DistanceMatrixElementStatus  {
        @Expose
        @SerializedName("status")
        public String status;
        @Expose
        @SerializedName("duration")
        public Duration duration;
        @Expose
        @SerializedName("distance")
        public Distance distance;
        public boolean IsSuccessfull(){
            return status.equals("OK");// from https://github.com/googlemaps/google-maps-services-java/blob/master/src/main/java/com/google/maps/DistanceMatrixApi.java
        }
    }

    public static class Duration {
        @Expose
        @SerializedName("value")
        public int value;
        @Expose
        @SerializedName("text")
        public String text;
    }

    public static class Distance {
        @Expose
        @SerializedName("value")
        public int value;
        @Expose
        @SerializedName("text")
        public String text;
    }


    public boolean IsSuccessfull(){
        return status.equals("OK");// from https://github.com/googlemaps/google-maps-services-java/blob/master/src/main/java/com/google/maps/DistanceMatrixApi.java
    }
}
