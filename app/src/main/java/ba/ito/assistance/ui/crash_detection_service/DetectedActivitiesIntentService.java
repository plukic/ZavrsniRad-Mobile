package ba.ito.assistance.ui.crash_detection_service;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.AssistanceApp;
import ba.ito.assistance.R;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.services.crash_detection.ICrashDetectionLogicService;
import ba.ito.assistance.ui.notifications.NotificationManager;
import ba.ito.assistance.util.ServicesUtil;
import dagger.android.DaggerIntentService;
import timber.log.Timber;

public class DetectedActivitiesIntentService extends DaggerIntentService {


    @Inject
    IAccountRepo accountRepo;
    @Inject
    ServicesUtil servicesUtil;
    @Inject
    ICrashDetectionLogicService crashDetectionService;
    @Inject
    NotificationManager notificationManager;
    @Inject
    EventBus eventBus;

    public DetectedActivitiesIntentService() {
        super("DetectedActivitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isCrashDetectionEnabled = accountRepo.IsAutomaticCrashDetectionServiceEnabled();
        if (!isCrashDetectionEnabled){
            Timber.d("AutomaticCrashDetection is disabled");
            return;
        }

        if (!ActivityRecognitionResult.hasResult(intent)) {
            Timber.d("ActivityRecognitionResult has no result");
            return;
        }
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        if (result == null) {
            Timber.d("ActivityRecognitionResult is null");
            return;
        }

        DetectedActivity mostProbableActivity = result.getMostProbableActivity();
        if (mostProbableActivity == null) {
            return;
        }
        Timber.d("Most probable activity is " + getTypeName(mostProbableActivity.getType()) + " with confidence " + mostProbableActivity.getConfidence());
        List<DetectedActivity> probableActivities = result.getProbableActivities();
        for (DetectedActivity probableActivity : probableActivities) {
            Timber.d("Detected activity is " + getTypeName(probableActivity.getType()) + " with confidence " + probableActivity.getConfidence());
        }


//        boolean areNotificationsEnabled = accountRepo.areNotificationsEnabled();
        boolean myServiceRunning = servicesUtil.IsServiceRunning(this, CrashDetectionService.class);

        if (crashDetectionService.IsInVehicle(mostProbableActivity) && !myServiceRunning) {
            Timber.d("User is in vehicle and service is not running");
            Timber.d("*******Starting foreground service.*******");
            Intent service = CrashDetectionService.getInstance(this);
            ContextCompat.startForegroundService(this, service);
            eventBus.post(CrashDetectionState.RUNNING);
        }
//        else if (crashDetectionService.HasStoppedDriving(mostProbableActivity) && myServiceRunning) {
//            Timber.d("User is walking and notifications are enabled");
//            if (areNotificationsEnabled) {
//                Timber.d("Notifications are enabled.");
//                createTrackingCompletedNotification(this);
//            }
//            Intent service = CrashDetectionService.getInstance(this);
//            Timber.d("*******Stopping foreground service.*******");
//            stopService(service);
//            eventBus.post(CrashDetectionState.STOPPED);
//        }
    }


    private String getTypeName(int activityType) {
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.UNKNOWN:
                return "UNKNOWN";
            case DetectedActivity.TILTING:
                return "TILTING";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.RUNNING:
                return "RUNNING";
        }
        return "UNKNOWN TRANSITION TYPE";
    }


    private void createTrackingCompletedNotification(Context ctx) {
        Notification crashDetectionFinishedNotification = notificationManager.createCrashDetectionFinishedNotification(ctx);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(NotificationManager.NOTIFICATION_CRASH_DETECTION_TRACKING_COMPLETED_ID, crashDetectionFinishedNotification);
    }


}
