package ba.ito.assistance.ui.emergency_contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.airbnb.epoxy.EpoxyRecyclerView;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmergencyContactsActivity extends BaseDaggerAuthorizedActivity implements EmergencyContactsContract.View, EmergencyContactsController.IEmergencyContactCallback, EmergencyContactDialog.IEmergencyContactDialogCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_emergency_contacts)
    EpoxyRecyclerView rvEmergencyContacts;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab_add_new_contact)
    FloatingActionButton fabAddNewContact;
    @BindView(R.id.parent)
    CoordinatorLayout parent;


    EmergencyContactsController controller;

    @Inject
    EmergencyContactsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ButterKnife.bind(this);
        super.setupToolbar(toolbar, true, R.string.label_my_profile_emergency_contacts_informations);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());
        presenter.takeView(this);
        presenter.onStart();

        controller = new EmergencyContactsController(this, this);
        rvEmergencyContacts.setLayoutManager(new LinearLayoutManager(this));
        rvEmergencyContacts.setController(controller);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void displayNoEmergencyContacts() {
        Snackbar.make(parent, R.string.no_data, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayEmergencyContacts(List<EmergencyContactNumbers> numbersList) {
        controller.setData(numbersList);
    }

    public static Intent GetInstance(Context ctx) {
        return new Intent(ctx, EmergencyContactsActivity.class);
    }

    @OnClick(R.id.fab_add_new_contact)
    public void onNewContact() {
        EmergencyContactDialog.getInstance().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onEmergencyContactSelected(EmergencyContactNumbers emergencyContactNumbers) {
        EmergencyContactDialog.getInstance(emergencyContactNumbers).show(getSupportFragmentManager(), null);
    }

    @Override
    public void onEmergencyContact(EmergencyContactNumbers emergencyContactNumber, boolean isUpdateDialog) {
        presenter.onEmergencyContact(emergencyContactNumber,isUpdateDialog);
    }

    @Override
    public void onDelete(EmergencyContactNumbers emergencyContactNumber) {
        presenter.onDeleteContact(emergencyContactNumber);
    }
}
