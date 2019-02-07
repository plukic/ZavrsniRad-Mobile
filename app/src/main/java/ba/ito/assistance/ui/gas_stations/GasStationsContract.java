package ba.ito.assistance.ui.gas_stations;

import java.util.List;
import java.util.Map;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;

public interface GasStationsContract  {
    interface Presenter extends BasePresenter<View>{
        void onLocationPermissionGranted();
        void onLatestLocation(double latitude,double longitude);
        void onLatestLocationNotAvailable();
    }
    interface View extends BaseAsyncView<Presenter> {
        boolean hasLocationPermissions();
        void requestLatestLocation();
        void requestLocationPremissions();

        void displayLatestLocationNotAvailable();

        void displayNoCompanyFuelInformations();

        void displayCompanyFuelInformations(Map<FuelTypeEnum, List<GasCompanyFuelPrices>> fuelTypeEnumListMap);
    }

}
