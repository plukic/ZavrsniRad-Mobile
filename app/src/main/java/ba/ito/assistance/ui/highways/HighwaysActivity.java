package ba.ito.assistance.ui.highways;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.ui.highway_prices.HighwayPricesActivity;
import ba.ito.assistance.ui.highway_select.HighwaySelectActivity;
import ba.ito.assistance.util.DialogFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ba.ito.assistance.ui.highway_select.HighwaySelectActivity.HighwaySelectType.ENTRANCE;
import static ba.ito.assistance.ui.highway_select.HighwaySelectActivity.HighwaySelectType.EXIT;
import static ba.ito.assistance.ui.highway_select.HighwaySelectActivity.HighwaySelectType.HIGHWAY;

public class HighwaysActivity extends BaseDaggerAuthorizedActivity implements HighwaysContract.View {

    private static final int REQUEST_CODE_HIGHWAY_SELECT = 100;
    private static final int REQUEST_CODE_ENTRANCE_SELECT = 101;
    private static final int REQUEST_CODE_EXIT_SELECT = 102;


    private static final String PARCELABLE_EXTRA_HIGHWAY = "PARCELABLE_EXTRA_HIGHWAY";
    private static final String PARCELABLE_EXTRA_ENTRANCE = "PARCELABLE_EXTRA_ENTRANCE";
    private static final String PARCELABLE_EXTRA_EXIT = "PARCELABLE_EXTRA_EXIT";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.lbl_select_highway)
    TextView lblSelectHighway;
    @BindView(R.id.tv_highway)
    TextView tvHighway;
    @BindView(R.id.btn_clear_highway)
    ImageButton btnClearHighway;
    @BindView(R.id.lbl_tollboth_enter)
    TextView lblTollbothEnter;
    @BindView(R.id.tv_tollbooth_enter_select)
    TextView tvTollboothEnterSelect;
    @BindView(R.id.fl_select_enter_tollboth)
    FrameLayout flSelectEnterTollboth;
    @BindView(R.id.btn_clear_tollboth_enter)
    ImageButton btnClearTollbothEnter;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.lbl_tollboth_exit)
    TextView lblTollbothExit;
    @BindView(R.id.tv_tollbooth_exit_select)
    TextView tvTollboothExitSelect;
    @BindView(R.id.btn_clear_tollboth_exit)
    ImageButton btnClearTollbothExit;

    @Inject
    HighwaysContract.Presenter presenter;

    private HighwayVM selectedHighway;
    private HighwayTollboothVM selectedEntrace;
    private HighwayTollboothVM selectedExit;


    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highways);
        ButterKnife.bind(this);
        setupToolbar(toolbar, true, null);

        if(savedInstanceState!=null){
            selectedHighway=savedInstanceState.getParcelable(PARCELABLE_EXTRA_HIGHWAY);
            selectedEntrace=savedInstanceState.getParcelable(PARCELABLE_EXTRA_ENTRANCE);
            selectedExit=savedInstanceState.getParcelable(PARCELABLE_EXTRA_EXIT);
        }
        presenter.takeView(this);
        presenter.onStart(selectedHighway,selectedEntrace,selectedExit);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARCELABLE_EXTRA_HIGHWAY,presenter.getSelectedHighway());
        outState.putParcelable(PARCELABLE_EXTRA_ENTRANCE,presenter.getSelectedEntrance());
        outState.putParcelable(PARCELABLE_EXTRA_EXIT,presenter.getSelectedExit());
        super.onSaveInstanceState(outState);

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

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, HighwaysActivity.class);
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        presenter.onSubmit();
    }

    @OnClick(R.id.fl_select_enter_tollboth)
    public void onEnterTollbothSelect() {
        if (presenter.getSelectedHighway() == null) {
            Snackbar.make(parent, R.string.please_select_highway, Snackbar.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(HighwaySelectActivity.GetInstance(this, ENTRANCE, presenter.getSelectedHighway(), presenter.getSelectedEntrance()), REQUEST_CODE_ENTRANCE_SELECT);
    }

    @OnClick(R.id.fl_select_exit_tollboth)
    public void onExitTollbothSelect() {
        if (presenter.getSelectedHighway() == null) {
            displaySelectHighwayError();
            return;
        }
        if (presenter.getSelectedEntrance() == null) {
            displaySelectEntranceError();
            return;
        }
        startActivityForResult(HighwaySelectActivity.GetInstance(this, EXIT, presenter.getSelectedHighway(), presenter.getSelectedEntrance()), REQUEST_CODE_EXIT_SELECT);
    }

    @OnClick(R.id.fl_select_highway)
    public void onHighwaySelect() {
        startActivityForResult(HighwaySelectActivity.GetInstance(this, HIGHWAY, presenter.getSelectedHighway(), presenter.getSelectedEntrance()), REQUEST_CODE_HIGHWAY_SELECT);
    }

    @OnClick({R.id.btn_clear_highway, R.id.btn_clear_tollboth_enter, R.id.btn_clear_tollboth_exit})
    public void onClearSelected(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_highway:
                presenter.onClearHighway();
                break;
            case R.id.btn_clear_tollboth_enter:
                presenter.onClearEntrance();
            case R.id.btn_clear_tollboth_exit:
                presenter.onClearExit();
                break;
        }
    }

    @Override
    public void displaySelectedHighway(String selectedHighway) {
        if (selectedHighway != null)
            tvHighway.setText(selectedHighway);
        else {
            tvHighway.setText(getResources().getString(R.string.empty_select));
        }
    }

    @Override
    public void displaySelectedEntrance(String selectedEntrance) {
        if (selectedEntrance != null)
            tvTollboothEnterSelect.setText(selectedEntrance);
        else {
            tvTollboothEnterSelect.setText(getResources().getString(R.string.empty_select));
        }
    }

    @Override
    public void displaySelectedExit(String selectedExit) {
        if (selectedExit != null)
            tvTollboothExitSelect.setText(selectedExit);
        else {
            tvTollboothExitSelect.setText(getResources().getString(R.string.empty_select));
        }
    }

    @Override
    public void displaySelectHighwayError() {
        Snackbar.make(parent, R.string.please_select_highway, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displaySelectExitError() {
        Snackbar.make(parent, R.string.please_select_exit, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displaySelectEntranceError() {
        Snackbar.make(parent, R.string.please_select_tollbooth_entrance, Snackbar.LENGTH_SHORT).show();
    }





    @Override
    public void goToPrices(HighwayVM highway, HighwayTollboothVM entrance, HighwayTollboothVM exit) {
        startActivity(HighwayPricesActivity.GetInstance(this, highway, entrance, exit));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_HIGHWAY_SELECT:
                presenter.onClearHighway();
                presenter.setSelectedHighway(HighwaySelectActivity.getHighway(data));
                break;
            case REQUEST_CODE_ENTRANCE_SELECT:
                presenter.onClearEntrance();
                presenter.setSelectedEntrance(HighwaySelectActivity.getEntrance(data));
                break;
            case REQUEST_CODE_EXIT_SELECT:
                presenter.onClearExit();
                presenter.setSelectedExit(HighwaySelectActivity.getExit(data));
                break;
        }

    }
}
