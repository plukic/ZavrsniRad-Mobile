package ba.ito.assistance.ui.settings_screen;


import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;
import ba.ito.assistance.model.settings.SettingsVM;
import ba.ito.assistance.model.user.BloodTypes;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.model.user.Genders;

public interface  SettingsContract  {

    interface  View extends BaseView<Presenter>{
        void displayUserSettings(SettingsVM settingsVM);
        void displayUnexpectedError();

        void OpenSettingsForGps();

        boolean IsGpsEnabled();



        void displaySettings(SettingsVM settingsVM);

        void changeTheme();
    }
    interface Presenter extends BasePresenter<View>{
        void onDataUpdated(ClientsDetailsModel data);


        void updateSettings(SettingsFragment.SettingsUpdate type);

    }
}
