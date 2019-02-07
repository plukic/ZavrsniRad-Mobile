package ba.ito.assistance.ui.weather_and_speed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.Objects;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


public class WeatherAndSpeedFragment extends BaseDaggerFragment implements WeatherAndSpeedContract.View {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.iv_clouds)
    ImageView ivClouds;
    @BindView(R.id.tv_current_speed)
    TextView tvCurrentSpeed;
    Unbinder unbinder;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.pb_loading_weather)
    ProgressBar pbLoadingWeather;



    @Inject
    WeatherAndSpeedContract.Presenter presenter;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;

    @Inject
    public WeatherAndSpeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_weather_and_speed, container, false);
        unbinder = ButterKnife.bind(this, inflate);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationCallbackSetup();
        presenter.takeView(this);
        presenter.onStart();

        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(mFusedLocationClient!=null && locationCallback !=null)
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        presenter.onStop();
        presenter.dropView();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void requestWeatherLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> presenter.onWeatherLocation(location));
    }

    private void locationCallbackSetup() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                    presenter.onLocationUpdate(locationResult.getLastLocation());
            }
        };

    }

    @Override
    public void displayNoWeatherInformation() {
        tvCity.setText(R.string.unknown_weather_city);
        tvTemperature.setText(R.string.unknown_weather_temperature);
    }

    @Override
    public void displayWeatherInformations(String city, double temperatureInCelsius) {
        tvCity.setText(city);
        DecimalFormat df = new DecimalFormat("#.00");

        String weatherString = getResources().getString(R.string.weather_celsius);
        String format = df.format(temperatureInCelsius);
        tvTemperature.setText(String.format("%s %s", format, weatherString));
    }

    @Override
    public void displayLoadingWeather(boolean isLoading) {
        pbLoadingWeather.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        ivClouds.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void requestLocationPermission() {
        Context ctx = getContext();
        if (ctx == null)
            return;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (checkForPermissions(ctx, permissions)) {
            presenter.onLocationPermissionGranted();
        } else {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void displayCurrentSpeedNotAvailable() {
        tvCurrentSpeed.setText(R.string.not_available);
    }

    @Override
    public void displayCurrentSpeed(float currentSpeedInKmH) {
        tvCurrentSpeed.setText(String.format(getString(R.string.speed_km_h), currentSpeedInKmH));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void requestSpeedUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void displayErrorWhileRefreshingWeather() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onLocationPermissionGranted();
                }else{
                    presenter.onLocationPermissionDeclined();
                }
                break;
            }
        }
    }
}
