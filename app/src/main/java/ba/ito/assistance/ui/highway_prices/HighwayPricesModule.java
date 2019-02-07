package ba.ito.assistance.ui.highway_prices;

import javax.inject.Named;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.ui.highways.HighwaysPresenter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HighwayPricesModule {

    @Provides
    @ActivityScoped
    static HighwayVM highway(HighwayPricesActivity activity){
        return activity.getHighway();
    }

    @Provides
    @ActivityScoped
    @Named("entrance")
    static HighwayTollboothVM entrance(HighwayPricesActivity activity){
        return activity.getEntrance();
    }

    @Provides
    @ActivityScoped
    @Named("exit")
    static HighwayTollboothVM exit(HighwayPricesActivity activity){
        return activity.getExit();
    }

    @Binds
    @ActivityScoped
    abstract  HighwayPricesContract.Presenter presenter(HighwayPricesPresenter presenter);

}
