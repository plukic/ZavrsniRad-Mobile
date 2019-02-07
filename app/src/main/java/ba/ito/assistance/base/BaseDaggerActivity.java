package ba.ito.assistance.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

import ba.ito.assistance.AppExceptionHandler;
import ba.ito.assistance.BuildConfig;
import ba.ito.assistance.services.configuration.IConfigurationService;
import dagger.android.support.DaggerAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseDaggerActivity extends DaggerAppCompatActivity {

    @Inject
    IConfigurationService configurationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentTheme = configurationService.getTheme();
        setTheme(currentTheme);
//        if (!BuildConfig.DEBUG) {
            Thread.UncaughtExceptionHandler fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(fabricExceptionHandler, this));
//        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void setupToolbar(Toolbar toolbar, boolean displayBackNavigation, int title) {
        if (toolbar == null)
            return;

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && displayBackNavigation) {
            supportActionBar.setTitle(title);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setupToolbar(Toolbar toolbar, boolean displayBackNavigation, String title) {
        if (toolbar == null)
            return;

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && displayBackNavigation) {
            supportActionBar.setTitle(title);
//            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void hideKeyboard() {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
            if (imm != null)
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

//    @Override
//    public void uncaughtException(Thread thread, Throwable throwable) {
//        Intent intent = SplashActivity.getInstance(this, throwable);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        System.exit(0);
//    }
}
