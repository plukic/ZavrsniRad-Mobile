package ba.ito.assistance.ui.highway_select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HighwaySelectActivity extends BaseDaggerAuthorizedActivity implements HighwaySelectContract.View, HighwaySelectAdapter.HighwaySelectRouter {


    private static final String HIGHWAY_SELECT_TYPE = "HIGHWAY_SELECT_TYPE";
    private static final String SELECTED_HIGHWAY_PARCELABLE = "SELECTED_HIGHWAY_PARCELABLE";
    private static final String SELECTED_ENTRANCE_PARCELABLE = "SELECTED_ENTRANCE_PARCELABLE";
    private static final String SELECTED_EXIT_PARCELABLE = "SELECTED_ENTRANCE_PARCELABLE";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.no_data_parent)
    LinearLayout noDataParent;


    @Inject
    HighwaySelectContract.Presenter presenter;


    HighwaySelectAdapter adapter;
    @Inject
    DateAndTimeUtil dateAndTimeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_select);
        ButterKnife.bind(this);
        super.setupToolbar(toolbar, true, null);
        adapter = new HighwaySelectAdapter(this);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        presenter.takeView(this);
        presenter.onStart();
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData(true));
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

    //region CALLBACK
    public static HighwayVM getHighway(Intent data) {
        return data.getParcelableExtra(SELECTED_HIGHWAY_PARCELABLE);
    }

    public static HighwayTollboothVM getEntrance(Intent data) {
        return data.getParcelableExtra(SELECTED_ENTRANCE_PARCELABLE);

    }

    public static HighwayTollboothVM getExit(Intent data) {
        return data.getParcelableExtra(SELECTED_EXIT_PARCELABLE);
    }

    @Override
    public void onHighwaySelected(HighwayVM highwayVM) {
        Intent i = new Intent();
        i.putExtra(SELECTED_HIGHWAY_PARCELABLE, highwayVM);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onEntranceSelected(HighwayTollboothVM entrance) {
//        throw  new RuntimeException();
        Intent i = new Intent();
        i.putExtra(SELECTED_ENTRANCE_PARCELABLE, entrance);
        setResult(RESULT_OK, i);
        finish();

    }

    @Override
    public void onExitSelected(HighwayTollboothVM exit) {
        Intent i = new Intent();
        i.putExtra(SELECTED_EXIT_PARCELABLE, exit);
        setResult(RESULT_OK, i);
        finish();
    }


    public enum HighwaySelectType {
        HIGHWAY, ENTRANCE, EXIT
    }
    //endregion


    @Override
    public void displayHighways(List<HighwayVM> highwayVMS) {
        toggleEmptyDataLayout(true);
        adapter.updateHighways(highwayVMS);
    }

    @Override
    public void displayEntrances(List<HighwayTollboothVM> entrances) {
        toggleEmptyDataLayout(true);
        adapter.updateEntrances(entrances);
    }

    @Override
    public void displayExits(List<HighwayTollboothVM> exit) {
        toggleEmptyDataLayout(true);
        adapter.updateExits(exit);
    }

    @Override
    public void displayTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    private void toggleEmptyDataLayout(boolean hasData) {
        noDataParent.setVisibility(hasData ? View.GONE : View.VISIBLE);
        rvItems.setVisibility(hasData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayNoData() {
        toggleEmptyDataLayout(false);
    }

    @Override
    public void displayRefreshHighwaysFailed() {
        Snackbar.make(parent, R.string.msg_refresh_highways_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayRefreshExitsFailed() {
        Snackbar.make(parent, R.string.msg_refresh_exits_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayRefreshEntranceFailed() {
        Snackbar.make(parent, R.string.msg_refresh_entrance_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLastUpdateTime(DateTime dateTime) {

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            String subtitle = String.format(getString(R.string.updated_at), dateAndTimeUtil.FormatForLastUpdateSubtitle(dateTime));
            supportActionBar.setSubtitle(subtitle);
        }
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }


    public HighwaySelectType getSelectedType() {
        return (HighwaySelectType) getIntent().getSerializableExtra(HIGHWAY_SELECT_TYPE);
    }

    public HighwayVM getSelectedHighway() {
        return getIntent().getParcelableExtra(SELECTED_HIGHWAY_PARCELABLE);
    }

    public HighwayTollboothVM getSelectedEntrance() {
        return getIntent().getParcelableExtra(SELECTED_ENTRANCE_PARCELABLE);

    }

    public static Intent GetInstance(Context ctx, HighwaySelectType type, HighwayVM highwayVM, HighwayTollboothVM tollsboothVM) {
        Intent i = new Intent(ctx, HighwaySelectActivity.class);
        if (highwayVM != null)
            i.putExtra(SELECTED_HIGHWAY_PARCELABLE, highwayVM);
        if (tollsboothVM != null)
            i.putExtra(SELECTED_ENTRANCE_PARCELABLE, tollsboothVM);
        i.putExtra(HIGHWAY_SELECT_TYPE, type);

        return i;
    }

}
