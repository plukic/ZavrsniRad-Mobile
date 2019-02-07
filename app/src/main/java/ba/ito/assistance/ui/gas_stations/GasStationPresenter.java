package ba.ito.assistance.ui.gas_stations;

import javax.inject.Inject;

import ba.ito.assistance.data.gas_stations.IGasStationsRepo;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class GasStationPresenter implements GasStationsContract.Presenter {
    private GasStationsContract.View view;

    private IGasStationsRepo gasStationsRepo;
    private ISchedulersProvider schedulersProvider;
    private Disposable subscribe;

    @Inject
    public GasStationPresenter(IGasStationsRepo gasStationsRepo, ISchedulersProvider schedulersProvider) {
        this.gasStationsRepo = gasStationsRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void takeView(GasStationsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        if (view.hasLocationPermissions()) {
            view.requestLatestLocation();
        } else {
            view.requestLocationPremissions();
        }
    }

    @Override
    public void onStop() {
        if (subscribe != null)
            subscribe.dispose();
    }

    @Override
    public void onLocationPermissionGranted() {
        view.requestLatestLocation();
    }

    @Override
    public void onLatestLocation(double latitude, double longitude) {
        subscribe = gasStationsRepo.LoadGasCompanyFuelPrices(latitude, longitude)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(fuelTypeEnumListMap -> {
                    view.displayLoading(false);
                    if (fuelTypeEnumListMap.isEmpty())
                        view.displayNoCompanyFuelInformations();
                    else
                        view.displayCompanyFuelInformations(fuelTypeEnumListMap);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

    @Override
    public void onLatestLocationNotAvailable() {
        view.displayLatestLocationNotAvailable();
    }
}
