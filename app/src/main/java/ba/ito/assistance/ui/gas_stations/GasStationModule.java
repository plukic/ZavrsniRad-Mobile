package ba.ito.assistance.ui.gas_stations;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.di.FragmentScoped;
import butterknife.BindDimen;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GasStationModule {


    @Binds
    @ActivityScoped
    abstract GasStationsContract.Presenter gasStationPresenter(GasStationPresenter presenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract  GasStationsFragment gasStationsFragment();


}
