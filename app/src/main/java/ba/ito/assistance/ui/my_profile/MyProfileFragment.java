package ba.ito.assistance.ui.my_profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerFragment;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.ui.emergency_contacts.EmergencyContactsActivity;
import ba.ito.assistance.ui.user_information.UserInformationActivity;
import ba.ito.assistance.util.DateAndTimeUtil;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;


public class MyProfileFragment extends BaseDaggerFragment implements IMyProfileItemCallback {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tv_full_name)
    TextView tvFullName;
//    @BindView(R.id.tv_insurance_policy)
//    TextView tvInsurancePolicy;
    @BindView(R.id.rv_user_profile_items)
    RecyclerView rvUserProfileItems;
    Unbinder unbinder;
//    @BindView(R.id.tv_account_active_to)
//    TextView tvAccountActiveTo;
    private View parent;


    @Inject
    IAccountRepo accountRepo;

    @Inject
    DateAndTimeUtil dateAndTimeUtil;
    @Inject
    ISchedulersProvider schedulersProvider;
    private Disposable subscribe;
    private int REQUEST_CODE_USER_INFORMATION_ACTIVITY = 100;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this, parent);


        MyProfileItemsController controller = new MyProfileItemsController(getContext(), this);
        rvUserProfileItems.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvUserProfileItems.setAdapter(controller.getAdapter());
        controller.requestModelBuild();


        subscribe = accountRepo.loadUserProfileInfo()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(this::updateClientData, throwable -> {
                    tvFullName.setText("N/A");
//                    tvInsurancePolicy.setText("N/A");
//                    tvAccountActiveTo.setText("N/A");
                });

        return parent;
    }

    private void updateClientData(ClientsDetailsModel clientsDetailsModel) {
        tvFullName.setText(String.format("%s %s", clientsDetailsModel.FirstName, clientsDetailsModel.LastName));
//        tvInsurancePolicy.setText(clientsDetailsModel.InsurancePolicyName);
//        tvAccountActiveTo.setText(dateAndTimeUtil.FormatAccountActiveTo(clientsDetailsModel.AccountActiveTo));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (subscribe != null)
            subscribe.dispose();
    }


    @Override
    public void onMyProfileItemSelected(MyProfileItemsController.MyProfileItemType type) {
        if (type == MyProfileItemsController.MyProfileItemType.EMERGENCY_NUMBERS)
            startActivity(EmergencyContactsActivity.GetInstance(getContext()));
        else
            startActivityForResult(UserInformationActivity.getInstance(getContext(), type), REQUEST_CODE_USER_INFORMATION_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_USER_INFORMATION_ACTIVITY) {
            ClientsDetailsModel clientsDetailsModel = UserInformationActivity.GetIntentResult(data);
            updateClientData(clientsDetailsModel);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
