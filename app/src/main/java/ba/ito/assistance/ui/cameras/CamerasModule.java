package ba.ito.assistance.ui.cameras;

import ba.ito.assistance.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class CamerasModule {

    @Binds
    @ActivityScoped
    abstract  CamerasContract.Presenter presenter(CamerasPresenter presenter);
}
