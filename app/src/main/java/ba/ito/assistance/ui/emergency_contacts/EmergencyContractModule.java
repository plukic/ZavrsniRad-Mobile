package ba.ito.assistance.ui.emergency_contacts;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EmergencyContractModule {

    @ActivityScoped
    @Binds
   abstract EmergencyContactsContract.Presenter providesPresenter(EmergencyContactPresenter presenter);

    @FragmentScoped
    @ContributesAndroidInjector
   abstract EmergencyContactDialog emergencyContactDialog();

}
