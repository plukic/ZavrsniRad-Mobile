package ba.ito.assistance.data.gas_stations;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import ba.ito.assistance.BuildConfig;
import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.distance_api.IDistanceApi;
import ba.ito.assistance.data.room.gas_stations.GasStationsDao;
import ba.ito.assistance.data.room.settings.UserSettingsDao;
import ba.ito.assistance.model.distance.DistanceResponse;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.model.gas_stations.GasStationLocationVM;
import ba.ito.assistance.model.gas_stations.NearestGasStationInfoEntity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class GasStationRepo implements IGasStationsRepo {

    private String googleMapsApiKey;
    private IDistanceApi distanceApi;
    private IApiService apiService;
    private UserSettingsDao userSettingsDao;

    private GasStationsDao gasStationsDao;

    private Function<List<NearestGasStationInfoEntity>, Flowable<List<GasStationListItemVM>>> nearestGasStationsMapper;

    @Inject
    public GasStationRepo(@Named("google_maps_api_key") String googleMapsApiKey, IDistanceApi distanceApi, IApiService apiService, UserSettingsDao userSettingsDao, GasStationsDao gasStationsDao, Function<List<NearestGasStationInfoEntity>, Flowable<List<GasStationListItemVM>>> nearestGasStationsMapper) {
        this.googleMapsApiKey = googleMapsApiKey;
        this.distanceApi = distanceApi;
        this.apiService = apiService;
        this.userSettingsDao = userSettingsDao;
        this.gasStationsDao = gasStationsDao;
        this.nearestGasStationsMapper = nearestGasStationsMapper;
    }


    @Override
    public Flowable<List<GasStationListItemVM>> GetNearestGasStations(FuelTypeEnum fuelTypeEnum) {
        return gasStationsDao.GetNearestGasStations(fuelTypeEnum)
                .flatMap(nearestGasStationsMapper)
                .flatMap(gasStationListItemVMS -> {
                    for (GasStationListItemVM gasStationListItemVM : gasStationListItemVMS) {
                        gasStationListItemVM.IconUrl = BuildConfig.BASE_URL + "/" + gasStationListItemVM.IconUrl;
                    }
                    return Flowable.just(gasStationListItemVMS);
                });
    }

    @Override
    public Completable RefreshNearestGasStation(FuelTypeEnum fuelTypeEnum) {
        return apiService
                .GetGasStationsInfo(fuelTypeEnum)
                .flatMapCompletable(gasStationListItemVMS -> {
                    gasStationsDao.saveGasStationItems(gasStationListItemVMS, fuelTypeEnum);
                    return Completable.complete();
                });
    }


    @Override
    public Observable<DistanceResponse> GetDistanceResponse(LatLng myLocation, List<LatLng> myDestinations) {
        String origins = myLocation.latitude + "," + myLocation.longitude;
        StringBuilder stringBuilder = new StringBuilder();
        for (LatLng item : myDestinations) {
            stringBuilder.append(item.latitude).append(",").append(item.longitude).append("|");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);//remove last |
        String destinations = stringBuilder.toString();
        return distanceApi.getDistanceResponse("metric", origins, destinations, googleMapsApiKey);
    }

    @Override
    public Observable<Map<FuelTypeEnum, List<GasCompanyFuelPrices>>> LoadGasCompanyFuelPrices(double latitude, double longitude) {
        return apiService
                .GetGasCompanyFuelPrices(latitude, longitude)
                .flatMap(mapForImagesUrl());
    }

    @NonNull
    private Function<Map<FuelTypeEnum, List<GasCompanyFuelPrices>>, ObservableSource<? extends Map<FuelTypeEnum, List<GasCompanyFuelPrices>>>> mapForImagesUrl() {
        return fuelTypeEnumListMap -> {
            Set<FuelTypeEnum> fuelTypeEnums = fuelTypeEnumListMap.keySet();
            for (FuelTypeEnum key : fuelTypeEnums) {
                List<GasCompanyFuelPrices> gasCompanyFuelPrices = fuelTypeEnumListMap.get(key);
                for (GasCompanyFuelPrices gasCompanyFuelPrice : gasCompanyFuelPrices) {
                    gasCompanyFuelPrice.CompanyLogoUrl = BuildConfig.BASE_URL + "/" + gasCompanyFuelPrice.CompanyLogoUrl;
                }
            }

            return Observable.just(fuelTypeEnumListMap);
        };
    }

    @Override
    public Observable<FuelTypeEnum> UserSelectedFuel() {
        return userSettingsDao.LoadUserSettings()
                .flatMapObservable(userSettingsEntity -> Observable.just(userSettingsEntity.SelectedFuel));
    }

    @Override
    public Observable<List<GasStationLocationVM>> GetGasStationWithLocations() {

        return apiService.GetGasStationWithLocations();

    }

    @Override
    public Completable changeFuelTypeSettings(FuelTypeEnum fuelTypeEnum) {
        return userSettingsDao.LoadUserSettings()
                .flatMapCompletable(userSettingsEntity -> {
                    userSettingsEntity.SelectedFuel = fuelTypeEnum;
                    userSettingsDao.saveUserSettings(userSettingsEntity);
                    return Completable.complete();
                });
    }
}
