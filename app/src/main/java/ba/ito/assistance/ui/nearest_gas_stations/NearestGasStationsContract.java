package ba.ito.assistance.ui.nearest_gas_stations;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;

public interface NearestGasStationsContract {
    interface  View extends BaseAsyncView<Presenter>{
            void displayNoGasStations();
            void displayGasStations(List<GasStationListItemVM> gasStations);

        double getDistanceInKilometers(double lat, double longitude);

        void requestMyLocation();

        void displayErrorWhileLoadingDistance();

        void displayDistancesCalculated(List<GasStationListItemVM> items);

        void displaySubtitle(int subtitle);

        void displayErrorWhileRefreshingData();
    }
    interface Presenter extends BasePresenter<View>{
        void loadGasStations();


        void refreshGasStations();

        void calculateGasStationLocations(double latitude, double longitude);

        void changeFuelType(FuelTypeEnum fuelType);
    }
}
