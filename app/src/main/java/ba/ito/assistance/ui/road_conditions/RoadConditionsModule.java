package ba.ito.assistance.ui.road_conditions;

import ba.ito.assistance.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class RoadConditionsModule {

    @Binds
    @ActivityScoped
    abstract RoadConditionContract.Presenter presenter(RoadConditionsPresenter passwordResetActivity);
}
