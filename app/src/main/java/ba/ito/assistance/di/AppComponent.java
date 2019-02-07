package ba.ito.assistance.di;

import android.app.Application;

import javax.inject.Singleton;

import ba.ito.assistance.AssistanceApp;
import ba.ito.assistance.di.module.ActivityBindingModule;
import ba.ito.assistance.di.module.ApplicationModule;
import ba.ito.assistance.di.module.ServicesBindingModule;
import ba.ito.assistance.di.module.DataModule;
import ba.ito.assistance.di.module.MappingModule;
import ba.ito.assistance.di.module.RoomModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        ServicesBindingModule.class,
        MappingModule.class,
        RoomModule.class,
        DataModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(AssistanceApp application);

    @Override
    void inject(DaggerApplication instance);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
