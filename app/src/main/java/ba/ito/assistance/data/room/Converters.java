package ba.ito.assistance.data.room;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;

import ba.ito.assistance.data.highway.VehiclesCategory;
import ba.ito.assistance.model.gas_stations.CurrencyEnum;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.road_condition.RoadConditionType;

public class Converters {
    @TypeConverter
    public DateTime fromTimestamp(Long value) {
        return value == null ? null : new DateTime(value);
    }

    @TypeConverter
    public Long dateToTimestamp(DateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.getMillis();
        }
    }

    @TypeConverter
    public static RoadConditionType RoadConditionTypeFromString(String roadConditionName) {
        return RoadConditionType.getType(roadConditionName);
    }

    @TypeConverter
    public static String RoadConditionTypeToString(RoadConditionType status) {
        return status.getStringName();
    }

    @TypeConverter
    public static FuelTypeEnum FuelTypeEnumFromString(String fuelTypeName) {
        return FuelTypeEnum.getType(fuelTypeName);
    }
    @TypeConverter
    public static String FuelTypeEnumToString(FuelTypeEnum fuel) {
        return fuel.getStringName();
    }

    @TypeConverter
    public static CurrencyEnum currencyFromString(String currency) {
        return CurrencyEnum.getType(currency);
    }

    @TypeConverter
    public static String currencyToString(CurrencyEnum currency) {
        return currency.getStringName();
    }


    @TypeConverter
    public static VehiclesCategory vehiclesCategoryFromString(String currency) {
        return VehiclesCategory.getType(currency);
    }
    @TypeConverter
    public static String vehiclesCategoryToString(VehiclesCategory currency) {
        return currency.getStringName();
    }
}