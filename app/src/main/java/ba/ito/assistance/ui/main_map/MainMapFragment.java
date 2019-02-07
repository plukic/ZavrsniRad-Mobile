package ba.ito.assistance.ui.main_map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerFragment;
import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectionService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectionState;
import ba.ito.assistance.ui.help_request_response.HelpRequestResponseController;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ServicesUtil;
import ba.ito.assistance.util.ViewUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

public class MainMapFragment extends BaseDaggerFragment implements OnMapReadyCallback, MainMapContract.View, HelpRequestResponseController.IHelpRequestInteraction {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 2;
    private static final int CALL_PHONE_HELP_REQUEST_BOTTOM_PERMISSION_REQUEST_CODE = 3;
    private static final String KEY_PARCELABLE_MY_LOCATION_MARKER = "KEY_PARCELABLE_MY_LOCATION_MARKER";
    private static final String KEY_PARCELABLE_USER_PARKED_LOCATION_MARKER = "KEY_PARCELABLE_USER_PARKED_LOCATION_MARKER";
    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 4;


    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    @BindView(R.id.bottom_sheet)
    LinearLayout panicBottomSheet;

    @BindView(R.id.bottom_sheet_help_request_response)
    ConstraintLayout bottomSheetHelpRequestResponse;

    Unbinder unbinder;

    BottomSheetBehavior panicButtonSheetBehaviour;
    BottomSheetBehavior helpRequestBottomSheetBehaviour;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.tv_malfunction_category)
    TextView tvMalfunctionCategory;
    @BindView(R.id.tv_accident_category)
    TextView tvAccidentCategory;
    @BindView(R.id.tv_other_category)
    TextView tvOtherCategory;

    @Inject
    MainMapContract.Presenter presenter;
    @BindView(R.id.pb_bottom_sheet)
    ProgressBar pbBottomSheet;

    @Inject
    ViewUtil viewUtil;
    @BindView(R.id.lbl_help_title)
    TextView lblHelpTitle;
    @BindView(R.id.lbl_title_help_incoming_time)
    TextView lblTitleHelpIncomingTime;
    @BindView(R.id.tv_help_incoming_time)
    TextView tvHelpIncomingTime;
    @BindView(R.id.rv_help_request_response_items)
    EpoxyRecyclerView rvHelpRequestResponseItems;
    @BindView(R.id.imbHelpRequestBottomToggle)
    ImageButton imbHelpRequestBottomToggle;
    @BindView(R.id.guideline_vertical_center)
    Guideline guidelineVerticalCenter;
    @BindView(R.id.tv_crash_detection_toggle)
    TextView tvCrashDetectionToggle;
//    @BindView(R.id.tv_park_my_car)
//    TextView tvParkMyCar;
    @BindView(R.id.fl_help_and_progressbar)
    FrameLayout flHelpAndProgressbar;
    @BindView(R.id.tv_panic_select_category_label)
    TextView tvPanicSelectCategoryLabel;
    @BindView(R.id.cl_crash_detection_parent)
    ConstraintLayout clCrashDetectionParent;

    @Inject
    ServicesUtil servicesUtil;
    @Inject
    DialogFactory dialogFactory;
    @BindView(R.id.fab_my_location)
    FloatingActionButton fabMyLocation;


    private GoogleMap map;
//    private FusedLocationProviderClient mFusedLocationClient;
    private View parent;
    private boolean isCrashDetectionVisible = false;

    private LatLng myLocation;
    private Marker myLocationMarker;


//    private Marker userParkedCarMarker;
//    private LatLng userParkedCarLocation;

    private BitmapDescriptor myLocationIcon;
//    private LocationRequest mLocationRequest;
//    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;


    @Inject
    public MainMapFragment() {
        // Required empty public constructor
    }

    public static MainMapFragment newInstance() {
        MainMapFragment fragment = new MainMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_main_map, container, false);
        unbinder = ButterKnife.bind(this, parent);
        animateCrashDetectionLayout(true);

        if (savedInstanceState != null) {
            myLocation = savedInstanceState.getParcelable(KEY_PARCELABLE_MY_LOCATION_MARKER);
//            userParkedCarLocation = savedInstanceState.getParcelable(KEY_PARCELABLE_USER_PARKED_LOCATION_MARKER);
        }

        int size = (int) viewUtil.convertDpToPixel(60, getContext());
        myLocationIcon = bitmapDescriptorFromVector(getContext(), R.drawable.ic_location_on_black_24dp, size, size);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


        bottomSheetSetup();
        locationCallbackSetup();

        presenter.takeView(this);
        presenter.onStart();


        return parent;
    }


    //region ANIMATIONS
    private void animateCrashDetectionLayout(boolean isVisible) {
        isCrashDetectionVisible = isVisible;
        if (isVisible) {
            tvCrashDetectionToggle.animate().alpha(1f).setDuration(200);
//            tvParkMyCar.animate().alpha(1f).setDuration(200);

            flHelpAndProgressbar.animate().alpha(0f).setDuration(200);
            tvPanicSelectCategoryLabel.animate().alpha(0f).setDuration(200);

        } else {
            tvCrashDetectionToggle.animate().alpha(0f).setDuration(200);
//            tvParkMyCar.animate().alpha(0f).setDuration(200);

            flHelpAndProgressbar.animate().alpha(1f).setDuration(200);
            tvPanicSelectCategoryLabel.animate().alpha(1f).setDuration(200);
        }
    }

    private void animateMyLocationButton(boolean isVisible) {
        if (fabMyLocation != null)
            fabMyLocation.animate().alpha(isVisible ? 1f : 0f).setDuration(200);

    }

    //endregion


    //region LOCATION SETUP
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        if (myLocation != null) {
            updateLocationOnMap(myLocation);
        }
//        if (userParkedCarLocation != null) {
//            displayUserParkedCarLocation(userParkedCarLocation);
//        }
        presenter.onMapReady();

        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                animateMyLocationButton(true);
            }
        });
        checkForLocationSettings();

//        map.setOnInfoWindowClickListener(marker -> {
//            if (marker.equals(userParkedCarMarker)) {
//                displayParkingDialog(marker.getPosition());
//            }
//        });
//        map.setOnMapClickListener(this::parkCarOnLocation);

        map.setOnMarkerClickListener(marker -> {
//            if (marker.equals(userParkedCarMarker)) {
//                displayParkingDialog(marker.getPosition());
//                return true;
//            }
            if (marker.equals(myLocationMarker)) {
                animateMyLocationButton(false);
                return true;
            }
            return false;
        });

    }


    private  LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    private void checkForLocationSettings() {

        //Create location request
        LocationRequest locationRequest = createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getContext());



        //check if user can execute stuff
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(runnable -> {
            //start updating location and displaying current location
            startLocationUpdates(locationRequest);
        });
        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CHECK_LOCATION_SETTINGS, null, 0, 0, 0, null);
//                    resolvable.startResolutionForResult(getActivity(), REQUEST_CHECK_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            } else {
                //TODO add location not available error
            }
        });



    }

//    private void parkCarOnLocation(LatLng latLng) {
//        if (userParkedCarMarker != null) {
//            dialogFactory.createCancelOkDialog(R.string.new_parked_car_location_dialog_title, R.string.delete_old_parked_car_dialog_message, (dialogInterface, i) -> {
//                presenter.parkMyCar(latLng);
//            }, (dialogInterface, i) -> dialogInterface.dismiss()).show();
//        } else {
//            presenter.parkMyCar(latLng);
//        }
//    }

    private void locationCallbackSetup() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = createLocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // do work here
                Location lastLocation = locationResult.getLastLocation();
                if (lastLocation != null)
                    updateLocationOnMap(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
            }
        };

    }

    @Override
    public void requestLocationPermission() {
        Context ctx = getContext();
        if (ctx == null)
            return;

        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (checkForPermissions(ctx, permissions)) {
            presenter.onLocationPermissionReady();
        } else {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onLocationPermissionGranted() {
        Context ctx = getContext();
        if (ctx == null)
            return;

        map.setMyLocationEnabled(false);
        displayLastKnownLocationOnMap();
    }


    //endregion


    private void bottomSheetSetup() {
        panicButtonSheetBehaviour = BottomSheetBehavior.from(panicBottomSheet);
        panicButtonSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (isCrashDetectionVisible && presenter.isExpanding(slideOffset)) {
                    animateCrashDetectionLayout(false);
                } else if (!isCrashDetectionVisible && !presenter.isExpanding(slideOffset))
                    animateCrashDetectionLayout(true);
            }
        });
        helpRequestBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetHelpRequestResponse);
        helpRequestBottomSheetBehaviour.setState(STATE_HIDDEN);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onStop();
        presenter.dropView();

        if (mFusedLocationProviderClient != null && locationCallback != null)
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @OnClick(R.id.fab_my_location)
    public void onMyLocationToggle() {
        displayLastKnownLocationOnMap();
    }

    @OnClick(R.id.imbHelpRequestBottomToggle)
    public void onHelpRequestBottomToggle() {
        int state = helpRequestBottomSheetBehaviour.getState();
        helpRequestBottomSheetBehaviour.setState(state == STATE_COLLAPSED ? STATE_EXPANDED : STATE_COLLAPSED);
    }


    @OnClick(R.id.fab)
    public void onFabClick() {
        if (panicButtonSheetBehaviour.getState() == STATE_EXPANDED) {
            panicButtonSheetBehaviour.setState(STATE_COLLAPSED);
        } else {
            panicButtonSheetBehaviour.setState(STATE_EXPANDED);
        }
    }

    @OnClick({R.id.tv_malfunction_category, R.id.tv_accident_category, R.id.tv_other_category})
    public void onNewHelpRequest(View v) {
        switch (v.getId()) {
            case R.id.tv_malfunction_category:
                createNewHelpRequest(HelpRequestCategory.Malfunction);
                break;
            case R.id.tv_accident_category:
                createNewHelpRequest(HelpRequestCategory.Accident);
                break;
            case R.id.tv_other_category:
                createNewHelpRequest(HelpRequestCategory.Other);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void createNewHelpRequest(HelpRequestCategory category) {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        presenter.onHelpRequestLocationNotAvailable(category);
                    } else {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        updateLocationOnMap(latLng);
                        presenter.makeRequest(latLng, category);
                    }
                });
    }


    @OnClick(R.id.tv_crash_detection_toggle)
    public void onCrashDetectionToggle() {
        Context context = getContext();
        if (context == null)
            return;
        Intent intent = new Intent(context, CrashDetectionService.class);
        if (servicesUtil.IsServiceRunning(context, CrashDetectionService.class)) {
            context.stopService(intent);
            EventBus.getDefault().post(CrashDetectionState.STOPPED);
        } else {
            ContextCompat.startForegroundService(context, intent);
            EventBus.getDefault().post(CrashDetectionState.RUNNING);
        }
    }

    @Override
    public void checkPhonePermissions() {
        Context context = getContext();
        if (context == null)
            return;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkForPermissions(context, permissions)) {
            presenter.makePhoneRequest();
        } else {
            requestPermissions(permissions, CALL_PHONE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void callPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void setMapStyleOverlay(int theme) {
        Context context = getContext();
        if (context == null)
            return;
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, theme));
        } catch (Exception e) {
            Timber.e("Error while applying google map style", e);
        }
    }


    @Override
    public void displayHelpRequestLocationNotAvailable(HelpRequestCategory category) {
        Snackbar make = Snackbar.make(parent, R.string.err_unable_to_get_location, Snackbar.LENGTH_INDEFINITE);
        make.setAction(R.string.retry, v -> {
            make.dismiss();
            this.createNewHelpRequest(category);
        });
        make.show();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId, int width, int height) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);

        vectorDrawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void displayNoHelpRequestInProgress() {
        helpRequestBottomSheetBehaviour.setBottomSheetCallback(null);
        helpRequestBottomSheetBehaviour.setState(STATE_HIDDEN);
        fab.setVisibility(View.VISIBLE);
        fabMyLocation.setVisibility(View.VISIBLE);
        tvHelpIncomingTime.setText(null);

    }

    @Override
    public void displayHelpRequestInProgress(HelpRequestResponseVM helpRequestResponseVM) {
        panicButtonSheetBehaviour.setState(STATE_HIDDEN);
        helpRequestBottomSheetBehaviour.setState(STATE_COLLAPSED);
        helpRequestBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case STATE_EXPANDED:
                        break;
                    case STATE_COLLAPSED:
                        break;
                    case STATE_HIDDEN:
                        helpRequestBottomSheetBehaviour.setState(STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                imbHelpRequestBottomToggle.setRotation(slideOffset * -180);
            }
        });
        fab.setVisibility(View.GONE);
        fabMyLocation.setVisibility(View.GONE);
        HelpRequestResponseController controller = new HelpRequestResponseController(this, getContext());
        rvHelpRequestResponseItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHelpRequestResponseItems.setController(controller);
        controller.setData(helpRequestResponseVM);
    }

    @Override
    public void displayMinutesUntilHelpArives(int minutes) {
        tvHelpIncomingTime.setText(minutes + "'");
    }

    @Override
    public void displayUserParkedCarLocation(LatLng latLng) {
//        if (userParkedCarMarker != null)
//            userParkedCarMarker.remove();
//
//        userParkedCarMarker = map.addMarker(new MarkerOptions().position(latLng).title("Lokacija vašeg vozila").icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_local_parking_black_24dp)));
//        userParkedCarMarker.showInfoWindow();
    }

    @Override
    public void displayUnableToSetParkedCarLocation(LatLng latLng) {
        Snackbar make = Snackbar.make(parent, R.string.unable_to_save_parking_location, Snackbar.LENGTH_LONG);
        make.setAction(R.string.retry, v -> presenter.parkMyCar(latLng));
    }

    @Override
    public void deleteParkedCarLocation() {
//        if (userParkedCarMarker != null) {
//            userParkedCarMarker.remove();
//            userParkedCarMarker = null;
//        }
    }

    @Override
    public boolean isServiceRunning() {
        return servicesUtil.IsServiceRunning(getContext(), CrashDetectionService.class);
    }

    @Override
    public void setCrashDetectionToggleOverlay(boolean isServiceRunning) {
        Context context = getContext();
        if (context == null)
            return;
        if (isServiceRunning) {
            tvCrashDetectionToggle.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_icons8_ekg_car_blue), null, null);
        } else
            tvCrashDetectionToggle.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_icons8_ekg_car_gray), null, null);

    }


    @Override
    public void displayError(int error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        pbBottomSheet.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

//    @OnClick(R.id.tv_park_my_car)
//    public void onParkMyCar() {
//
//        if (myLocationMarker == null)
//            return;
//        parkCarOnLocation(myLocationMarker.getPosition());
//    }


    private void displayParkingDialog(LatLng latlong) {
        dialogFactory.createCancelNeutralOkDialog(R.string.navigate_me,
                R.string.cancel,
                R.string.delete_parked_car_location,
                R.string.parked_car_dialog_title,
                R.string.parked_car_dialog_message,
                (view, i) -> {
                    navigateToLocation(latlong);

                },
                (view, i) -> view.dismiss(),
                (view, i) -> presenter.deleteParkedCarLocation()).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onLocationPermissionReady();
                }
                break;
            }
            case CALL_PHONE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.makePhoneRequest();
                }
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (myLocationMarker != null) {
            outState.putParcelable(KEY_PARCELABLE_MY_LOCATION_MARKER, myLocationMarker.getPosition());
        }
//        if (userParkedCarMarker != null) {
//            outState.putParcelable(KEY_PARCELABLE_USER_PARKED_LOCATION_MARKER, userParkedCarMarker.getPosition());
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CHECK_LOCATION_SETTINGS){
            if(resultCode==RESULT_OK){
                startLocationUpdates(locationRequest);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startLocationUpdates(LocationRequest locationRequest) {
        Task<Void> voidTask = mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);
        voidTask.addOnSuccessListener(runnable -> Timber.d("Location updates started successfully"));
        voidTask.addOnFailureListener(error -> Timber.e(error, "Location updates started successfully"));


        FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        displayLastKnownLocationOnMap();
    }

    @Override
    public void onCallSelected(String contactPhoneNumber) {
        Context context = getContext();
        if (context == null)
            return;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkForPermissions(context, permissions)) {
            this.callPhoneNumber(contactPhoneNumber);
        } else {
            requestPermissions(permissions, CALL_PHONE_HELP_REQUEST_BOTTOM_PERMISSION_REQUEST_CODE);
        }
    }


    //region location

    private void navigateToLocation(LatLng latlong) {
        try {
            Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latlong.latitude + "," + latlong.longitude));
            startActivity(navigation);
        } catch (Exception e) {
            Snackbar make = Snackbar.make(parent, R.string.unable_to_start_navigation, Snackbar.LENGTH_LONG);
            make.setAction(R.string.retry, v -> navigateToLocation(latlong));
            make.show();
        }
    }


    @Override
    public void displayLocationNotAvailable() {
        Snackbar make = Snackbar.make(parent, R.string.err_unable_to_get_location, Snackbar.LENGTH_INDEFINITE);
        make.setAction(R.string.retry, v -> {
            make.dismiss();
            this.displayLastKnownLocationOnMap();
        });
        make.show();
    }


    @SuppressLint("MissingPermission")
    private void displayLastKnownLocationOnMap() {
        if (mFusedLocationProviderClient == null)
            return;
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        presenter.unableToGetLocation();
                        return;
                    }
                    updateLocationOnMap(new LatLng(location.getLatitude(), location.getLongitude()));
                });
    }

    private void updateLocationOnMap(LatLng location) {
        if (location == null)
            return;
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 15);
        map.animateCamera(yourLocation);
        animateMyLocationButton(false);
        if (myLocationMarker != null)
            myLocationMarker.remove();

        myLocationMarker = map.addMarker(new MarkerOptions()
                .position(location)
                .icon(myLocationIcon)
                .draggable(false)
                .flat(true));
    }
    //endregion
}
