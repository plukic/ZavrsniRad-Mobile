package ba.ito.assistance.ui.login;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {

    @Binds
    @ActivityScoped
    abstract LoginContract.Presenter providesPresenter(LoginPresenter loginPresenter);
    @ActivityScoped
    @Provides
    static DialogFactory providesDialogFactory(LoginActivity context, IConfigurationService configurationService){
        return new DialogFactory(context, configurationService);
    }
}
