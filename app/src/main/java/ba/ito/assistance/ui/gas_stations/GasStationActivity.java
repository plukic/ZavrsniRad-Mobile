package ba.ito.assistance.ui.gas_stations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class GasStationActivity extends BaseDaggerAuthorizedActivity implements GasStationsContract.View {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private GasStationPageAdapter pagerAdapter;
    private Task<Location> locationTask;


    @Inject
    GasStationsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_gas_stations);

        presenter.takeView(this);
        presenter.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static Intent getInstance(Context context) {
        return new Intent(context, GasStationActivity.class);
    }

    @Override
    public boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;

    }

    @SuppressLint("MissingPermission")
    @Override
    public void requestLatestLocation() {
        locationTask = LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null)
                        presenter.onLatestLocationNotAvailable();
                    else {
                        presenter.onLatestLocation(location.getLatitude(), location.getLongitude());
                    }
                });

    }

    @Override
    public void requestLocationPremissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED)
                    presenter.onLocationPermissionGranted();
                else if (grantResults.length > 0)
                    Snackbar.make(container, R.string.err_location_permission_needed, Snackbar.LENGTH_LONG).show();
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void displayLatestLocationNotAvailable() {
        Snackbar.make(mainContent, R.string.err_unable_to_get_location, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayNoCompanyFuelInformations() {
        //TODO add empty screen
        Snackbar.make(mainContent, R.string.no_data, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayCompanyFuelInformations(Map<FuelTypeEnum, List<GasCompanyFuelPrices>> fuelTypeEnumListMap) {
        Set<FuelTypeEnum> fuelTypeEnums = fuelTypeEnumListMap.keySet();

        tabs.removeAllTabs();
        pagerAdapter = new GasStationPageAdapter(getSupportFragmentManager());

        for (FuelTypeEnum item : fuelTypeEnums) {

            tabs.addTab(tabs.newTab().setText(getTabName(item)));
            pagerAdapter.addItem(GasStationsFragment.newInstance((ArrayList<GasCompanyFuelPrices>) fuelTypeEnumListMap.get(item)));
        }

        container.setAdapter(pagerAdapter);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));

    }

    private int getTabName(FuelTypeEnum item) {
        switch (item) {
            case Petrol:
                return R.string.tab_fuel_petrol;
            case Diesel:
                return R.string.tab_fuel_diesel;
            case LPG:
                return R.string.tab_fuel_lpg;
            case Other:
                return R.string.tab_fuel_other;
        }
        throw new InvalidParameterException();
    }

    @Override
    public void displayUnexpectedError() {

    }

    @Override
    public void displayLoading(boolean isLoading) {
        if(isLoading){
            container.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
        }else{
            container.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
        }
    }


    public class GasStationPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentItems;

        public GasStationPageAdapter(FragmentManager fm) {
            super(fm);
            fragmentItems = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragmentItems.get(position);
        }

        private void addItem(Fragment fragment) {
            fragmentItems.add(fragment);
        }

        @Override
        public int getCount() {
            return fragmentItems.size();
        }
    }


}
