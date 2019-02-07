package ba.ito.assistance.ui.settings_screen;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.epoxy.EpoxyRecyclerView;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.model.settings.SettingsVM;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends DaggerFragment implements SettingsController.ISettingsNavigation, SettingsContract.View {

    public enum SettingsUpdate {
        CRASH_DETECTION, GPS, NIGHT_MODE, NOTIFICATION
    }
    @BindView(R.id.rv_settings)
    EpoxyRecyclerView rvSettings;
    Unbinder unbinder;

    @Inject
    DialogFactory dialogFactory;
    @Inject
    SettingsContract.Presenter presenter;
    @Inject
    ISchedulersProvider schedulersProvider;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private OnSettingsFragmentInteraction callback;
    private SettingsController controller;

    public SettingsFragment() {
    }

    @Override
    public void displayUserSettings(SettingsVM settingsVM) {
        controller.setData(settingsVM);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(rvSettings, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void OpenSettingsForGps() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public boolean IsGpsEnabled() {
        if (getContext() == null)
            return false;
        boolean coarseLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return coarseLocation && fineLocation;
    }


    @Override
    public void displaySettings(SettingsVM settingsVM) {
        controller.setData(settingsVM);
    }

    @Override
    public void changeTheme() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            activity.recreate();
    }

    public interface OnSettingsFragmentInteraction {
        void onLogoutAction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.title_settings);

        controller = new SettingsController(getContext(), this);
        rvSettings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSettings.setController(controller);

        presenter.takeView(this);

        presenter.onStart();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof OnSettingsFragmentInteraction))
            throw new InvalidParameterException("Host must implement" + OnSettingsFragmentInteraction.class.getSimpleName());

        callback = ((OnSettingsFragmentInteraction) context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        presenter.onStop();
        presenter.dropView();
    }

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onLogout() {
        dialogFactory.createCancelOkDialog(R.string.title_logout, R.string.title_logout_message, (dialogInterface, i) -> {
            callback.onLogoutAction();
            dialogInterface.dismiss();
        }, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }).show();
    }

    @Override
    public void onUpdateSelected(SettingsFragment.SettingsUpdate type) {
        presenter.updateSettings(type);
    }



}
