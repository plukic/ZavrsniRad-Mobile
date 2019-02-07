package ba.ito.assistance.ui.nearest_gas_stations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.services.currency.ICurrencyService;
import ba.ito.assistance.util.DialogFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NearestGasStationsActivity extends BaseDaggerAuthorizedActivity implements NearestGasStationsContract.View, NearestGasStationAdapter.INearestGasStations {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Inject
    NearestGasStationsContract.Presenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_gas_stations)
    RecyclerView rvGasStations;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data_parent)
    LinearLayout noDataParent;
    private NearestGasStationAdapter adapter;

    @Inject
    DialogFactory dialogFactory;

    @Inject
    ICurrencyService currencyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_gas_stations);
        ButterKnife.bind(this);
        super.setupToolbar(toolbar, true, R.string.title_activity_nearest_gas_stations);
        adapter = new NearestGasStationAdapter(new ArrayList<>(), Glide.with(this), currencyService, this);
        rvGasStations.setAdapter(adapter);
        rvGasStations.setLayoutManager(new LinearLayoutManager(this));

        presenter.takeView(this);
        presenter.onStart();
        presenter.loadGasStations();

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshGasStations());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nearest_gas_stations, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_nearest_gas_stations_settings) {
            displayGasStationsSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayGasStationsSettings() {
        Resources r = getResources();
        dialogFactory
                .createSimpleListDialog(
                        Arrays.asList(r.getString(R.string.fuel_diesel_subtitle),
                                r.getString(R.string.fuel_petrol_subtitle),
                                r.getString(R.string.fuel_lpg_subtitle),
                                r.getString(R.string.fuel_other_subtitle)), position -> {
                            switch (position) {
                                case 0:
                                    presenter.changeFuelType(FuelTypeEnum.Diesel);
                                    break;
                                case 1:
                                    presenter.changeFuelType(FuelTypeEnum.Petrol);
                                    break;
                                case 2:
                                    presenter.changeFuelType(FuelTypeEnum.LPG);
                                    break;
                                case 3:
                                    presenter.changeFuelType(FuelTypeEnum.Other);
                                    break;
                            }
                        }
                ).show();
    }

    @Override
    public void displayNoGasStations() {
        rvGasStations.setVisibility(View.GONE);
        noDataParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayGasStations(List<GasStationListItemVM> gasStations) {
        rvGasStations.setVisibility(View.VISIBLE);
        noDataParent.setVisibility(View.GONE);
        adapter.updateData(gasStations);
    }

    @Override
    public double getDistanceInKilometers(double lat, double longitude) {
        return 0;
    }

    @Override
    public void requestMyLocation() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (checkForPermissions(this, permissions)) {
            getLocationAndCalculateDistances();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationAndCalculateDistances() {
        LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this))
                .getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                displayErrorWhileLoadingDistance();
                return;
            }
            presenter.calculateGasStationLocations(location.getLatitude(), location.getLongitude());
        });
    }

    private boolean checkForPermissions(Context ctx, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationAndCalculateDistances();
                }
                break;
            }
        }
    }

    @Override
    public void displayErrorWhileLoadingDistance() {
        Snackbar make = Snackbar.make(mainContent, R.string.err_calculating_distances, Snackbar.LENGTH_LONG);
        make.setAction(R.string.retry, v -> requestMyLocation());
        make.show();
    }

    @Override
    public void displayDistancesCalculated(List<GasStationListItemVM> items) {

        adapter.updateData(items);
    }

    @Override
    public void displaySubtitle(int subtitle) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setSubtitle(subtitle);
    }

    @Override
    public void displayErrorWhileRefreshingData() {
        Snackbar.make(mainContent, R.string.unable_to_refresh_feed, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(mainContent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, NearestGasStationsActivity.class);
    }

    @Override
    public void onGasStationSelected(GasStationListItemVM gasStationVM) {
        String label = gasStationVM.GasCompanyName + " " + gasStationVM.GasCompanyCity + " " + gasStationVM.GasCompanyAddress;

        String uriBegin = "geo:" + gasStationVM.Lat + "," + gasStationVM.Long;
        String query = gasStationVM.Lat + "," + gasStationVM.Long + "(" + label + ")";

        Uri uri = Uri.parse(uriBegin + "?q=" + Uri.encode(query) + "&z=16");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
