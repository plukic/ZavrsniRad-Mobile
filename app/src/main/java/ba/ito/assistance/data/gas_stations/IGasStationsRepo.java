package ba.ito.assistance.data.gas_stations;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import ba.ito.assistance.model.distance.DistanceResponse;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.model.gas_stations.GasStationLocationVM;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface IGasStationsRepo {

    Flowable<List<GasStationListItemVM>> GetNearestGasStations(FuelTypeEnum fuelTypeEnum);

    Completable RefreshNearestGasStation(FuelTypeEnum fuelTypeEnum);

    Observable<DistanceResponse> GetDistanceResponse(LatLng myLocation, List<LatLng> myDestinations);
    Observable<Map<FuelTypeEnum, List<GasCompanyFuelPrices>>> LoadGasCompanyFuelPrices(double latitude, double longitude);

    Observable<FuelTypeEnum> UserSelectedFuel();

    Observable<List<GasStationLocationVM>> GetGasStationWithLocations();

    Completable changeFuelTypeSettings(FuelTypeEnum fuelTypeEnum);
}
