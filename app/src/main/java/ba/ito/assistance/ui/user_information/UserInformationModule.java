package ba.ito.assistance.ui.user_information;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.di.FragmentScoped;
import ba.ito.assistance.ui.common.ProfileUpdateDialog;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class UserInformationModule {

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract ProfileUpdateDialog example1FragmentInjector();


    @ActivityScoped
    @Binds
    abstract IUserInformationContract.Presenter providesPresenter(UserInformationPresenter presenter);
}
