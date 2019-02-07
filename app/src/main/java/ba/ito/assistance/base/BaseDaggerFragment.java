package ba.ito.assistance.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import dagger.android.support.DaggerFragment;

public abstract class BaseDaggerFragment  extends DaggerFragment{
    protected boolean checkForPermissions(@NonNull Context ctx, String[] permissions) {
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(ctx, p) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    protected void setupToolbar(Toolbar toolbar, boolean displayBackNavigation, String title) {
        if (toolbar == null)
            return;
        BaseDaggerActivity activity = ((BaseDaggerActivity) getActivity());
        if(activity==null)
            return;

        activity.setSupportActionBar(toolbar);
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null && displayBackNavigation) {
            supportActionBar.setTitle(title);
//            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
