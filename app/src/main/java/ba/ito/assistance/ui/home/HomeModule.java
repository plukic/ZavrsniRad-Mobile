package ba.ito.assistance.ui.home;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.di.FragmentScoped;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.ui.main_map.MainMapContract;
import ba.ito.assistance.ui.main_map.MainMapFragment;
import ba.ito.assistance.ui.main_map.MainMapPresenter;
import ba.ito.assistance.ui.weather_and_speed.WeatherAndSpeedContract;
import ba.ito.assistance.ui.weather_and_speed.WeatherAndSpeedFragment;
import ba.ito.assistance.ui.my_profile.MyProfileFragment;
import ba.ito.assistance.ui.settings_screen.SettingsContract;
import ba.ito.assistance.ui.settings_screen.SettingsFragment;
import ba.ito.assistance.ui.settings_screen.SettingsPresenter;
import ba.ito.assistance.ui.weather_and_speed.WeatherAndSpeedPresenter;
import ba.ito.assistance.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(HomeActivity homeActivity, IConfigurationService configurationService){
        return new DialogFactory(homeActivity, configurationService);
    }

    @Binds
    @ActivityScoped
    abstract MainMapContract.Presenter mainMapPresenter(MainMapPresenter mainMapPresenter);

    @Binds
    @ActivityScoped
    abstract WeatherAndSpeedContract.Presenter weatherAndSpeedContractPresenter(WeatherAndSpeedPresenter weatherAndSpeedPresenter);


    @Binds
    @ActivityScoped
    abstract SettingsContract.Presenter settingsPresenter(SettingsPresenter settingsPresenter);


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyProfileFragment myProfileFragment();
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainMapFragment mapFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract WeatherAndSpeedFragment weatherAndSpeedFragment();

}
