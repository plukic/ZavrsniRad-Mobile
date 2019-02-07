package ba.ito.assistance.ui.home;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.ui.crash_detection_service.DetectedActivitiesIntentService;
import ba.ito.assistance.ui.main_map.MainMapFragment;
import ba.ito.assistance.ui.my_profile.MyProfileFragment;
import ba.ito.assistance.ui.services.ServicesFragment;
import ba.ito.assistance.ui.settings_screen.SettingsFragment;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.NoSwipePager;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends BaseDaggerAuthorizedActivity implements SettingsFragment.OnSettingsFragmentInteraction {
    public static boolean IsInForeground=false;
    @Inject
    IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;


    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;


    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.parent)
    FrameLayout parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupViewPager();
        botomNavigationSetup();

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (!wasSelected) {
                viewPager.setCurrentItem(position);
            }
            return true;
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task task = activityRecognitionClient.requestActivityUpdates(10*1000, pendingIntent);
        task.addOnSuccessListener(o -> Timber.e("Connection success"));
        task.addOnFailureListener(Timber::e);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        viewPager = (NoSwipePager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(MainMapFragment.newInstance(), R.string.nav_label_home_screen);
//        pagerAdapter.addFragment(ServicesFragment.newInstance(), R.string.nav_label_services);
        pagerAdapter.addFragment(MyProfileFragment.newInstance(), R.string.nav_label_profile_screen);
        pagerAdapter.addFragment(SettingsFragment.newInstance(), R.string.nav_label_settings);
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IsInForeground=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        IsInForeground=false;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            bottomNavigation.setCurrentItem(0);
            viewPager.setCurrentItem(0);
            return;
        }
        super.onBackPressed();
    }

    private void botomNavigationSetup() {
        Resources resources = getResources();

        List<AHBottomNavigationItem> listItemnavigations = Arrays.asList(
                new AHBottomNavigationItem(resources.getString(R.string.nav_label_home_screen), R.drawable.ic_home_icon_64),
//                new AHBottomNavigationItem(resources.getString(R.string.nav_label_services), R.drawable.ic_info_squared),
                new AHBottomNavigationItem(resources.getString(R.string.nav_label_profile_screen), R.drawable.ic_icons8_contacts),
                new AHBottomNavigationItem(resources.getString(R.string.nav_label_settings), R.drawable.ic_icons8_adjust_64)
        );

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int colorPrimary = typedValue.data;

        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.addItems(listItemnavigations);
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setColored(false);
        bottomNavigation.setAccentColor(color);
        bottomNavigation.setBackgroundColor(colorPrimary);
        bottomNavigation.setDefaultBackgroundColor(colorPrimary);
    }


    @Override
    public void onLogoutAction() {
        super.logoutUser(null);
    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, HomeActivity.class);
    }
}
