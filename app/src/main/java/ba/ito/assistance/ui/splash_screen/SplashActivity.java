package ba.ito.assistance.ui.splash_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import ba.ito.assistance.AppExceptionHandler;
import ba.ito.assistance.base.BaseDaggerActivity;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.ui.login.LoginActivity;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseDaggerActivity {

    private static final String THROWABLE_EXTRA = "THROWABLE_EXTRA";
    @Inject
    IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;


    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(THROWABLE_EXTRA)) {
            Throwable throwable = (Throwable) getIntent().getSerializableExtra(THROWABLE_EXTRA);
            Crashlytics.logException(throwable);
        }
        subscribe = accountRepo.isUserLogged()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(isLogged -> {
                    if (isLogged)
                        goToHomeScreen();
                    else {
                        goToLoginScreen();
                    }
                }, throwable -> {
                    goToLoginScreen();
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null)
            subscribe.dispose();
    }

    private void goToHomeScreen() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    private void goToLoginScreen() {
        startActivity(LoginActivity.getInstance(SplashActivity.this));
        finish();

    }

    public static Intent getInstance(Context context, Throwable throwable) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(THROWABLE_EXTRA, throwable);
        return intent;
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, SplashActivity.class);
    }
}
