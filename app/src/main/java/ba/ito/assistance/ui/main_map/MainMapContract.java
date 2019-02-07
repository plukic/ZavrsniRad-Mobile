package ba.ito.assistance.ui.main_map;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;
import ba.ito.assistance.model.gas_stations.GasStationLocationVM;
import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import ba.ito.assistance.model.road_condition.RoadConditionVM;

public interface MainMapContract {

    interface View extends BaseView<Presenter>{
        void displayError(int error);
        void displayLoading(boolean isLoading);
        void requestLocationPermission();
        void onLocationPermissionGranted();

        void checkPhonePermissions();

        void callPhoneNumber(String phoneNumber);

        void setMapStyleOverlay(int theme);


        void displayLocationNotAvailable();

        void displayHelpRequestLocationNotAvailable(HelpRequestCategory category);


        void displayNoHelpRequestInProgress();

        void displayHelpRequestInProgress(HelpRequestResponseVM helpRequestResponseVM);

        void displayMinutesUntilHelpArives(int minutes);

        void displayUserParkedCarLocation(LatLng latLng);

        void displayUnableToSetParkedCarLocation(LatLng latLng);

        void deleteParkedCarLocation();

        boolean isServiceRunning();

        void setCrashDetectionToggleOverlay(boolean isServiceRunning);
    }
    interface Presenter extends BasePresenter<View>{
        void onMapReady();
        void makeRequest(LatLng location, HelpRequestCategory helpRequestCategory);
        void onLocationPermissionReady();

        void makePhoneRequest();

        void unableToGetLocation();

        void onHelpRequestLocationNotAvailable(HelpRequestCategory category);


        void parkMyCar(LatLng latLng);

        void deleteParkedCarLocation();

        boolean isExpanding(float slideOffset);
    }
}
