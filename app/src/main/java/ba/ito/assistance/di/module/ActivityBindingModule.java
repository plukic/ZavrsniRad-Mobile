package ba.ito.assistance.di.module;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.ui.cameras.CamerasActivity;
import ba.ito.assistance.ui.cameras.CamerasModule;
import ba.ito.assistance.ui.emergency_contacts.EmergencyContactsActivity;
import ba.ito.assistance.ui.emergency_contacts.EmergencyContractModule;
import ba.ito.assistance.ui.gas_stations.GasStationActivity;
import ba.ito.assistance.ui.gas_stations.GasStationModule;
import ba.ito.assistance.ui.highway_prices.HighwayPricesActivity;
import ba.ito.assistance.ui.highway_prices.HighwayPricesModule;
import ba.ito.assistance.ui.highway_select.HighwaySelectActivity;
import ba.ito.assistance.ui.highway_select.HighwaySelectModule;
import ba.ito.assistance.ui.highways.HighwaysActivity;
import ba.ito.assistance.ui.highways.HighwaysModule;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.ui.home.HomeModule;
import ba.ito.assistance.ui.login.LoginActivity;
import ba.ito.assistance.ui.login.LoginModule;
import ba.ito.assistance.ui.nearest_gas_stations.NearestGasStationModule;
import ba.ito.assistance.ui.nearest_gas_stations.NearestGasStationsActivity;
import ba.ito.assistance.ui.password_reset.PasswordResetActivity;
import ba.ito.assistance.ui.password_reset.PasswordResetModule;
import ba.ito.assistance.ui.register.RegisterActivity;
import ba.ito.assistance.ui.register.RegisterModule;
import ba.ito.assistance.ui.road_conditions.RoadConditionsActivity;
import ba.ito.assistance.ui.road_conditions.RoadConditionsModule;
import ba.ito.assistance.ui.splash_screen.SplashActivity;
import ba.ito.assistance.ui.user_information.UserInformationActivity;
import ba.ito.assistance.ui.user_information.UserInformationModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract SplashActivity splashScreenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity home();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PasswordResetModule.class)
    abstract PasswordResetActivity passwordResetActivity();

//    @ActivityScoped
//    @ContributesAndroidInjector(modules = EditProfileModule.class)
//    abstract EditProfileInfoActivity editProfileInfoActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RoadConditionsModule.class)
    abstract RoadConditionsActivity roadConditionDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = NearestGasStationModule.class)
    abstract NearestGasStationsActivity nearestGasStationsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = GasStationModule.class)
    abstract GasStationActivity gasStationActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = HighwaysModule.class)
    abstract HighwaysActivity highwaysActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = HighwaySelectModule.class)
    abstract HighwaySelectActivity highwaySelectActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = HighwayPricesModule.class)
    abstract HighwayPricesActivity highwayPricesActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = CamerasModule.class)
    abstract CamerasActivity camerasActivity();



    @ActivityScoped
    @ContributesAndroidInjector(modules = UserInformationModule.class)
    abstract UserInformationActivity userInformationActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EmergencyContractModule.class)
    abstract EmergencyContactsActivity emergencyContactsActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = RegisterModule.class)
    abstract RegisterActivity registerActivity();


}
