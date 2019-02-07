package ba.ito.assistance.ui.highways;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HighwaysModule {

    @ActivityScoped
    @Binds
    abstract HighwaysContract.Presenter providesPresenter(HighwaysPresenter presenter);

    @ActivityScoped
    @Provides
    static DialogFactory dialogFactory(HighwaysActivity activity, IConfigurationService configurationService){
        return new DialogFactory(activity, configurationService);
    }

}
