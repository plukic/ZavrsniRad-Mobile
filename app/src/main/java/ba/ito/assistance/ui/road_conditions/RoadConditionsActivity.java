package ba.ito.assistance.ui.road_conditions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.ui.road_conditions.details.RoadConditionsDetailsFragment;
import ba.ito.assistance.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RoadConditionsActivity extends BaseDaggerAuthorizedActivity implements RoadConditionContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.tabs)
    TabLayout tabs;


    @Inject
    DateAndTimeUtil dateAndTimeUtil;
    @BindView(R.id.no_data_parent)
    LinearLayout noDataParent;

    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Inject
    RoadConditionContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_conditions);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_road_conditions);

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
        } else if (item.getItemId() == R.id.action_settings) {
            presenter.refreshData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, RoadConditionsActivity.class);
    }

    @Override
    public void displayNoData() {
        noDataParent.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_road_conditions, menu);
        return true;
    }


    @Override
    public void displayRoadConditions(List<RoadConditionVM> roadConditionVM) {
        noDataParent.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabs.removeAllTabs();
        int title;
        for (RoadConditionVM item : roadConditionVM) {
            title = presenter.getTabText(item.RoadConditionType);
            mSectionsPagerAdapter.addItem(RoadConditionsDetailsFragment.newInstance(item));
            tabs.addTab(tabs.newTab().setText(title));
        }

        container.setAdapter(mSectionsPagerAdapter);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setSubtitle(String.format(getString(R.string.updated_at), dateAndTimeUtil.FormatForLastUpdateSubtitle(roadConditionVM.get(0).UpdatedAt)));
        }

    }

    @Override
    public void displayFailedToRefreshData() {
        Snackbar.make(mainContent, R.string.unable_to_refresh_feed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(mainContent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<RoadConditionsDetailsFragment> fragmentItems;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentItems = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragmentItems.get(position);
        }

        private void addItem(RoadConditionsDetailsFragment roadConditionsDetailsFragment) {
            fragmentItems.add(roadConditionsDetailsFragment);
        }

        @Override
        public int getCount() {
            return fragmentItems.size();
        }
    }
}
