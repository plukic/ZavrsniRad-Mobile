package ba.ito.assistance.ui.password_reset;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.di.ActivityScoped;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyRegex;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PasswordResetModule {

    @ActivityScoped
    @Provides
    static DialogFactory providesDialogFactory(PasswordResetActivity passwordResetActivity, IConfigurationService configurationService){
        return new DialogFactory(passwordResetActivity, configurationService);
    }

    @Provides
    @ActivityScoped
    static PasswordResetContract.Presenter presenter(PasswordResetActivity passwordResetActivity, IAccountRepo accountRepo, ISchedulersProvider schedulersProvider, BaseErrorFactory baseErrorFactory, MyRegex myRegex){
        Object object= passwordResetActivity.getLastCustomNonConfigurationInstance();
        if(object instanceof  PasswordResetPresenter)
            return ((PasswordResetPresenter) object);

        return new PasswordResetPresenter(accountRepo,schedulersProvider,baseErrorFactory, myRegex);
    }


}
