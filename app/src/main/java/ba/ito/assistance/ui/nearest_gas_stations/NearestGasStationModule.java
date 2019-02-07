package ba.ito.assistance.ui.nearest_gas_stations;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.util.DialogFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module
public abstract class NearestGasStationModule {


    @Provides
    @ActivityScoped
    static RequestManager glideRequestManager(NearestGasStationsActivity activity){
        return Glide.with(activity);
    }
    @Binds
    @ActivityScoped
    abstract NearestGasStationsContract.Presenter gasStationPresenter(NearestGasStationsPresenter presenter);

    @Provides
    @ActivityScoped
    static DialogFactory dialogFactory(NearestGasStationsActivity activity, IConfigurationService configurationService){
        return new DialogFactory(activity, configurationService);
    }
}
