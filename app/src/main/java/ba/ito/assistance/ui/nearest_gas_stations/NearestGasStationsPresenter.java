package ba.ito.assistance.ui.nearest_gas_stations;

import com.google.android.gms.maps.model.LatLng;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.data.gas_stations.IGasStationsRepo;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.BackpressureStrategy;
import io.reactivex.disposables.Disposable;

public class NearestGasStationsPresenter implements NearestGasStationsContract.Presenter {
    private NearestGasStationsContract.View view;
    private IGasStationsRepo gasStationsRepo;
    private Disposable subscribe;
    private ISchedulersProvider schedulersProvider;


    private List<GasStationListItemVM> items;
    private Disposable distanceApiSubscribe;
    private Disposable gasStationFuelTypeSubscribe;
    private Disposable changeFuelTypeSubscribe;
    private Disposable nearestGasStationSubscribe;
    private Disposable gasStationRefreshSubscribe;

    @Inject
    public NearestGasStationsPresenter(IGasStationsRepo gasStationsRepo, ISchedulersProvider schedulersProvider) {
        this.gasStationsRepo = gasStationsRepo;
        this.schedulersProvider = schedulersProvider;
        items = new ArrayList<>();
    }

    @Override
    public void loadGasStations() {
        if (gasStationRefreshSubscribe != null)
            gasStationRefreshSubscribe.dispose();
        gasStationRefreshSubscribe = gasStationsRepo.UserSelectedFuel()
                .subscribeOn(schedulersProvider.network())
                .flatMapCompletable(fuelTypeEnum -> gasStationsRepo.RefreshNearestGasStation(fuelTypeEnum))
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(() -> {
                    view.displayLoading(false);

                }, throwable ->
                {
                    view.displayLoading(false);
                    view.displayErrorWhileRefreshingData();
                });
    }

    private void subscribeForNearestGasStation() {
        if (nearestGasStationSubscribe != null)
            nearestGasStationSubscribe.dispose();

        nearestGasStationSubscribe = gasStationsRepo
                .UserSelectedFuel()
                .subscribeOn(schedulersProvider.network())
                .toFlowable(BackpressureStrategy.BUFFER)
                .flatMap(fuelTypeEnum -> gasStationsRepo.GetNearestGasStations(fuelTypeEnum))
                .observeOn(schedulersProvider.main())
                .subscribe(gasStationListItemVMS -> {
                    items = gasStationListItemVMS;
                    if (gasStationListItemVMS.isEmpty())
                        view.displayNoGasStations();
                    else {
                        view.displayGasStations(gasStationListItemVMS);
                        view.requestMyLocation();
                    }
                    displayGasStationsFuelType();
                }, throwable -> view.displayUnexpectedError());
    }

    @Override
    public void calculateGasStationLocations(double latitude, double longitude) {
        List<LatLng> distances = new ArrayList<>();
        for (GasStationListItemVM item : items) {
            distances.add(new LatLng(item.Lat, item.Long));
        }

        if (distanceApiSubscribe != null)
            distanceApiSubscribe.dispose();

        distanceApiSubscribe = gasStationsRepo
                .GetDistanceResponse(new LatLng(latitude, longitude), distances)
                .map(distanceResponse -> {
                    if (!distanceResponse.IsSuccessfull() || distanceResponse.rows == null || distanceResponse.rows.isEmpty()) {
                        throw new Exception("Distance response not successfull");
                    }
                    return distanceResponse.rows.get(0).elements;
                })
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(calculatedDistances -> {
                    for (int i = 0; i < calculatedDistances.size(); i++) {
                        GasStationListItemVM item = items.get(i);
                        if (!calculatedDistances.get(i).IsSuccessfull())
                            item.Distance = null;
                        else {
                            item.Distance = calculatedDistances.get(i).distance.text;
                            item.DistanceNumeric = calculatedDistances.get(i).distance.value;
                        }
                    }
                    Collections.sort(items, (o1, o2) -> o1.DistanceNumeric.compareTo(o2.DistanceNumeric));
                    view.displayDistancesCalculated(items);
                }, throwable -> {
                    view.displayErrorWhileLoadingDistance();
                });
    }

    @Override
    public void changeFuelType(FuelTypeEnum fuelType) {
        if (changeFuelTypeSubscribe != null)
            changeFuelTypeSubscribe.dispose();

        changeFuelTypeSubscribe = gasStationsRepo.changeFuelTypeSettings(fuelType)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(() -> {
                    subscribeForNearestGasStation();
                    loadGasStations();
                }, throwable -> view.displayUnexpectedError());
    }

    @Override
    public void refreshGasStations() {
        loadGasStations();
    }

    @Override
    public void takeView(NearestGasStationsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        subscribeForNearestGasStation();
    }

    private void displayGasStationsFuelType() {
        if (gasStationFuelTypeSubscribe != null)
            gasStationFuelTypeSubscribe.dispose();

        gasStationFuelTypeSubscribe = gasStationsRepo.UserSelectedFuel()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(fuelTypeEnum -> {
                    view.displaySubtitle(getSubtitle(fuelTypeEnum));
                }, throwable -> {
                    view.displayUnexpectedError();
                });
    }

    private int getSubtitle(FuelTypeEnum fuelTypeEnum) {
        switch (fuelTypeEnum) {
            case Petrol:
                return R.string.fuel_petrol_subtitle;
            case Diesel:
                return R.string.fuel_diesel_subtitle;
            case LPG:
                return R.string.fuel_lpg_subtitle;
            case Other:
                return R.string.fuel_other_subtitle;
        }
        throw new InvalidParameterException("Invalid fuelTypeEnum parameter");
    }


    @Override
    public void onStop() {
        if (subscribe != null)
            subscribe.dispose();

        if (distanceApiSubscribe != null)
            distanceApiSubscribe.dispose();

        if (gasStationFuelTypeSubscribe != null)
            gasStationFuelTypeSubscribe.dispose();

        if (changeFuelTypeSubscribe != null)
            changeFuelTypeSubscribe.dispose();

        if (gasStationRefreshSubscribe != null)
            gasStationRefreshSubscribe.dispose();

        if (nearestGasStationSubscribe != null)
            nearestGasStationSubscribe.dispose();

    }
}
