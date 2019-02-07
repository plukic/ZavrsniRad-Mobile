package ba.ito.assistance.ui.highway_prices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.airbnb.epoxy.EpoxyRecyclerView;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.services.currency.ICurrencyService;
import ba.ito.assistance.ui.highway_prices.prices_list.HighwayPricesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HighwayPricesActivity extends BaseDaggerAuthorizedActivity implements HighwayPricesContract.View {

    private static final String PARCELABLE_EXTRA_PRICES = "PARCELABLE_EXTRA_PRICES";
    private static final String PARCELABLE_EXTRA_ENTRANCE = "PARCELABLE_EXTRA_ENTRANCE";
    private static final String PARCELABLE_EXTRA_EXIT = "PARCELABLE_EXTRA_EXIT";
    private static final String PARCELABLE_EXTRA_HIGHWAY = "PARCELABLE_EXTRA_HIGHWAY";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_items)
    EpoxyRecyclerView rvItems;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    ICurrencyService currencyService;

    @Inject
    HighwayPricesContract.Presenter presenter;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private HighwayPricesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_prices);
        ButterKnife.bind(this);
        super.setupToolbar(toolbar, true, R.string.title_activity_highway_prices);


        adapter = new HighwayPricesAdapter(currencyService, this.getResources());
        rvItems.setController(adapter);

        HighwayVM highway = getHighway();
        HighwayTollboothVM entrance = getEntrance();
        HighwayTollboothVM exit = getExit();

        adapter.setData(highway, null, entrance, exit);

        presenter.takeView(this);
        presenter.onStart();
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshPrices(true));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }

    public HighwayVM getHighway() {
        return getIntent().getParcelableExtra(PARCELABLE_EXTRA_HIGHWAY);
    }


    public HighwayTollboothVM getEntrance() {
        return getIntent().getParcelableExtra(PARCELABLE_EXTRA_ENTRANCE);
    }


    public HighwayTollboothVM getExit() {
        return getIntent().getParcelableExtra(PARCELABLE_EXTRA_EXIT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent GetInstance(Context context, HighwayVM highwayVM, HighwayTollboothVM entrance, HighwayTollboothVM exit) {
        Intent i = new Intent(context, HighwayPricesActivity.class);
        i.putExtra(PARCELABLE_EXTRA_ENTRANCE, entrance);
        i.putExtra(PARCELABLE_EXTRA_EXIT, exit);
        i.putExtra(PARCELABLE_EXTRA_HIGHWAY, highwayVM);
        return i;
    }

    @Override
    public void displayPrices(HighwayVM highwayVM, HighwayTollboothVM entrance, HighwayTollboothVM exit, List<HighwayRoutePriceVM> prices) {
        adapter.setData(highwayVM, prices, entrance, exit);
    }

    @Override
    public void displayRefreshingPricesFailed() {
        Snackbar make = Snackbar.make(parent, R.string.msg_refresh_highway_prices_failed, Snackbar.LENGTH_LONG);
        make.setAction(R.string.retry, (view) -> {
            presenter.refreshPrices(true);
        });
        make.show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }
}
