package ba.ito.assistance.ui.gas_stations;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import ba.ito.assistance.services.currency.ICurrencyService;
import ba.ito.assistance.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public class GasStationsFragment extends DaggerFragment implements GasCompanyPricesController.IGasCompanyPricesNavigation {


    private static final String KEY_GAS_COMPANY_FUEL_PRICES = "KEY_GAS_COMPANY_FUEL_PRICES";
    @BindView(R.id.rv_gas_stations)
    EpoxyRecyclerView rvGasStations;
    Unbinder unbinder;
    private ArrayList<GasCompanyFuelPrices> gasComapanyFuelPrices;
    private View parent;

    @Inject
    ICurrencyService currencyService;
    @Inject
    DateAndTimeUtil dateAndTimeUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gasComapanyFuelPrices = getArguments().getParcelableArrayList(KEY_GAS_COMPANY_FUEL_PRICES);
        parent = inflater.inflate(R.layout.fragment_gas_stations_prices, container, false);
        unbinder = ButterKnife.bind(this, parent);

        GasCompanyPricesController pricesController = new GasCompanyPricesController(Glide.with(this), currencyService, dateAndTimeUtil, getResources(), this);
        rvGasStations.setController(pricesController);
        pricesController.setData(gasComapanyFuelPrices);
        return parent;
    }

    public static Fragment newInstance(ArrayList<GasCompanyFuelPrices> gasCompanyFuelPrices) {
        Fragment f = new GasStationsFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(KEY_GAS_COMPANY_FUEL_PRICES, gasCompanyFuelPrices);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void OnGasStationSelected(GasCompanyFuelPrices vm) {
        String label = vm.GasCompanyName + " " + vm.GasCompanyName + " " + vm.GasStationLocation;

        String uriBegin = "geo:" + vm.Lat + "," + vm.Long;
        String query = vm.Lat + "," + vm.Long + "(" + label + ")";

        Uri uri = Uri.parse(uriBegin + "?q=" + Uri.encode(query) + "&z=16");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
