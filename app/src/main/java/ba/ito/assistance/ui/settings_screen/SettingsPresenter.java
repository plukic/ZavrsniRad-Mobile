package ba.ito.assistance.ui.settings_screen;


import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.settings.SettingsVM;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.ui.settings_screen.SettingsContract.Presenter;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class SettingsPresenter implements Presenter {
    private SettingsContract.View view;
    private IAccountRepo accountRepo;
    private ISchedulersProvider schedulersProvider;
    private Disposable subscribe;
    private Disposable initialAccountLoad;

    @Inject
    public SettingsPresenter(IAccountRepo accountRepo, ISchedulersProvider schedulersProvider) {
        this.accountRepo = accountRepo;
        this.schedulersProvider = schedulersProvider;
    }



    @Override
    public void onDataUpdated(ClientsDetailsModel data) {
        loadUserSettings();
    }


    @Override
    public void updateSettings(SettingsFragment.SettingsUpdate type) {
        if (subscribe != null)
            subscribe.dispose();
        if (type == SettingsFragment.SettingsUpdate.GPS) {
            view.OpenSettingsForGps();
            return;
        }
        subscribe = accountRepo.ToggleSettings(type)
                .andThen(accountRepo.getUserSettings())
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .map(settingsVM -> {
                    settingsVM.GpsEnabled = view.IsGpsEnabled();
                    return settingsVM;
                })
                .subscribe((settingsVM) -> {
                    if (type == SettingsFragment.SettingsUpdate.NIGHT_MODE)
                        view.changeTheme();
                    else
                        view.displaySettings(settingsVM);
                }, throwable -> {
                    view.displayUnexpectedError();
                });
    }


    @Override
    public void takeView(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        view.displaySettings(new SettingsVM());
        loadUserSettings();
    }

    private void loadUserSettings() {
        initialAccountLoad = accountRepo
                .getUserSettings()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .map(settingsVM -> {
                    settingsVM.GpsEnabled = view.IsGpsEnabled();
                    return settingsVM;
                })
                .subscribe(settingsVM -> {
                    view.displaySettings(settingsVM);
                }, throwable -> view.displayUnexpectedError());
    }

    @Override
    public void onStop() {
        if (initialAccountLoad != null)
            initialAccountLoad.dispose();
        if (subscribe != null)
            subscribe.dispose();
    }
}
