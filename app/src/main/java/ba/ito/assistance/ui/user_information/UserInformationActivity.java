package ba.ito.assistance.ui.user_information;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.airbnb.epoxy.EpoxyRecyclerView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.ui.common.ProfileUpdateDialog;
import ba.ito.assistance.ui.my_profile.MyProfileItemsController;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInformationActivity extends BaseDaggerAuthorizedActivity implements UserInformationsController.IUserInformationCallback, ProfileUpdateDialog.IProfileUpdateCallback, IUserInformationContract.View {


    private static String INTENT_EXTRA_PROFILE_ITEM_TYPE ="INTENT_EXTRA_PROFILE_ITEM_TYPE";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_user_informations)
    EpoxyRecyclerView rvUserInformations;
    @BindView(R.id.parent)
    CoordinatorLayout parent;


    @Inject
    IAccountRepo accountRepo;


    @Inject
    ISchedulersProvider schedulersProvider;

    @Inject
    IUserInformationContract.Presenter presenter;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserInformationsController controller;
    private String title;
    private static String INTENT_RESULT_EXTRA_DATA="INTENT_RESULT_EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        ButterKnife.bind(this);

        MyProfileItemsController.MyProfileItemType selectedItemType= (MyProfileItemsController.MyProfileItemType) getIntent().getSerializableExtra(INTENT_EXTRA_PROFILE_ITEM_TYPE);
        title = getTitle(selectedItemType);

        super.setupToolbar(toolbar, true, title);


        controller = new UserInformationsController(this, this,selectedItemType);
        rvUserInformations.setLayoutManager(new LinearLayoutManager(this));
        rvUserInformations.setController(controller);


        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshUserInformationData());

        presenter.takeView(this);
        presenter.onStart();


    }

    private String getTitle(MyProfileItemsController.MyProfileItemType selectedItemType) {
        Resources resources=getResources();
        switch (selectedItemType) {
            case USER_INFORMATION:
                return resources.getString(R.string.label_my_profile_user_informations);
            case PERSONAL_INFORMATION:
                return resources.getString(R.string.label_my_profile_personal_informations);
            case HEALTH_INFORMATION:
                return resources.getString(R.string.label_my_profile_health_informations);
            case EMERGENCY_NUMBERS:
                return resources.getString(R.string.hint_enter_emergency_phone_number);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onStop();
        presenter.dropView();
    }

    public static Intent getInstance(Context context,MyProfileItemsController.MyProfileItemType type) {
        Intent intent = new Intent(context, UserInformationActivity.class);
        intent.putExtra(INTENT_EXTRA_PROFILE_ITEM_TYPE,type);
        return intent;
    }


    @Override
    public void onUserInformationUpdate(UserInformationsController.UserInformationUpdateType userInformationUpdateType, String value) {
        ProfileUpdateDialog.getInstance(value, userInformationUpdateType,title).show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPositiveButton(String newValue, UserInformationsController.UserInformationUpdateType updateType) {
        presenter.onUpdateValue(newValue, updateType);
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
    public void displayUserData(ClientsDetailsModel clientsDetailsModel) {
        controller.setData(clientsDetailsModel);
    }

    @Override
    public void displayUserDataUpdated(ClientsDetailsModel clientsDetailsModel) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_RESULT_EXTRA_DATA,clientsDetailsModel);
        setResult(RESULT_OK, intent);
        controller.setData(clientsDetailsModel);
    }

    public static ClientsDetailsModel GetIntentResult(Intent intent){
        return intent.getParcelableExtra(INTENT_RESULT_EXTRA_DATA);
    }
}
