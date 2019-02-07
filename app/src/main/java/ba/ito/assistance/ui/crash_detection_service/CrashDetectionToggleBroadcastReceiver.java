package ba.ito.assistance.ui.crash_detection_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.EventLog;

import org.greenrobot.eventbus.EventBus;

import static ba.ito.assistance.ui.crash_detection_service.CrashDetectionState.RUNNING;
import static ba.ito.assistance.ui.crash_detection_service.CrashDetectionState.STOPPED;

public class CrashDetectionToggleBroadcastReceiver extends BroadcastReceiver {
    private static final String PARCELABLE_ENABLE_SERVICE = "PARCELABLE_ENABLE_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean enableService = intent.getBooleanExtra(PARCELABLE_ENABLE_SERVICE, false);
        Intent service = new Intent(context, CrashDetectionService.class);

        if (enableService) {
            EventBus.getDefault().post(RUNNING);
            ContextCompat.startForegroundService(context, service);
        } else {
            EventBus.getDefault().post(STOPPED);
            context.stopService(service);
        }
    }

    public static Intent getInstance(Context ctx, boolean enableService) {
        Intent intent = new Intent(ctx, CrashDetectionService.class);
        intent.putExtra(PARCELABLE_ENABLE_SERVICE, enableService);
        return intent;
    }
}
