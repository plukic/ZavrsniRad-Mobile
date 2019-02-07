package ba.ito.assistance.ui.highway_select;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public abstract class HighwaySelectModule {





    @ActivityScoped
    @Provides
    static HighwaySelectActivity.HighwaySelectType providesType(HighwaySelectActivity activity){
        return activity.getSelectedType();
    }


    @ActivityScoped
    @Provides
    @Nullable
    static HighwayVM providesSelectedhighway(HighwaySelectActivity activity){
        return activity.getSelectedHighway();
    }

    @ActivityScoped
    @Provides
    @Nullable
    static HighwayTollboothVM providesSelectedEntrance(HighwaySelectActivity activity){
        return activity.getSelectedEntrance();
    }


    @ActivityScoped
    @Binds
    abstract HighwaySelectContract.Presenter providesPresenter(HighwaySelectPresenter presenter);

}

