package ba.ito.assistance.model.road_condition;

import com.google.gson.annotations.SerializedName;

public enum RoadConditionType {
    @SerializedName("1")
    TrafficFlow,
    @SerializedName("2")
    BorderCrossings,
    @SerializedName("3")
    FerryTrafic,
    @SerializedName("4")
    RailwayTraffic,
    @SerializedName("5")
    RoadWorks,
    @SerializedName("6")
    TrafficForecast;

    public String getStringName() {
        switch (this) {
            case TrafficFlow:
                return "TrafficFlow";
            case BorderCrossings:
                return "BorderCrossings";
            case FerryTrafic:
                return "FerryTrafic";

            case RailwayTraffic:
                return "RailwayTraffic";

            case RoadWorks:
                return "RoadWorks";

            case TrafficForecast:
                return "TrafficForecast";

        }
        return null;
    }

    public static RoadConditionType getType(String roadConditionName) {
        switch (roadConditionName){
            case "TrafficFlow":return TrafficFlow;
            case "BorderCrossings":return BorderCrossings;
            case "FerryTrafic":return FerryTrafic;
            case "RailwayTraffic":return RailwayTraffic;
            case "RoadWorks":return RoadWorks;
            case "TrafficForecast":return TrafficForecast;
        }
        return null;
    }
}
