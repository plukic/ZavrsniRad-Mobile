package ba.ito.assistance.ui.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ba.ito.assistance.AssistanceApp;
import ba.ito.assistance.R;
import ba.ito.assistance.services.firebase.MyFirebaseMessagingService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectedService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectionToggleBroadcastReceiver;
import ba.ito.assistance.ui.home.HomeActivity;

public class NotificationManager {
    public static final int NOTIFICATION_CRASH_DETECTION_FOREGROUND_SERVICE_ID = 1;
    public static final int NOTIFICATION_CRASH_DETECTION_SENSOR_UNAVAILABLE_ID = 2;
    public static final int NOTIFICATION_CRASH_DETECTION_TRACKING_COMPLETED_ID = 2;
    public static final int NOTIFICATION_CRASH_DETECTED_COUNTDOWN_ID = 2;
    public static final int NOTIFICATION_CRASH_DETECTED_MAKING_HELP_REQUEST_ID = 2;
    public static final int NOTIFICATION_CRASH_FINISHED_MAKING_HELP_REQUEST_ID = 3;
    public static final int NOTIFICATION_HELP_REQUEST_RESPONSE_ID = 4;

    @Inject
    public NotificationManager() {
    }

    public Notification createSensorUnavailableNotification(Context ctx) {

        Resources resources = ctx.getResources();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setContentTitle(resources.getString(R.string.notification_sensor_error))
                .setContentText(ctx.getString(R.string.notification_sensor_error_content))
                .setWhen(DateTime.now().getMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        return mBuilder.build();
    }

    public Notification createCrashDetectionForegroundServiceNotification(Context ctx) {
        //region notification click
        Intent notificationIntent = HomeActivity.getInstance(ctx);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //endregion

        //region StopServiceIntent
        Intent intentHide = new Intent(ctx, CrashDetectionToggleBroadcastReceiver.class);
        PendingIntent hide = PendingIntent.getBroadcast(ctx, (int) System.currentTimeMillis(), intentHide, 0);
        //endregion

        Resources resources = ctx.getResources();
        return new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.crash_detection_foreground_notification_content_title))
                .setContentText(resources.getString(R.string.crash_detection_foreground_notification_content_text))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText(resources.getString(R.string.notification_summary_have_a_nice_trip))
                        .bigText(resources.getString(R.string.notification_crash_detection_big_text)))
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.ic_crashed_car, resources.getString(R.string.notifications_turn_off), hide)
                .setContentIntent(pendingIntent)
                .build();
    }

    public Notification createCrashDetectionFinishedNotification(Context ctx) {
        Resources resources = ctx.getResources();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setContentTitle(resources.getString(R.string.driving_finished_notification_content_title) )
                .setContentText(resources.getString(R.string.driving_finished_notification_content_content_text))
                .setWhen(DateTime.now().getMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return mBuilder.build();
    }

    public Notification createCrashDetectionDetectedNotification(Context ctx, long currentSecond) {

        Intent stopSelf = CrashDetectedService.getInstance(ctx,true);
        PendingIntent pStopSelf = PendingIntent.getService(ctx, 0, stopSelf,PendingIntent.FLAG_CANCEL_CURRENT);
        Resources resources = ctx.getResources();
        return new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.crash_detection_foreground_notification_content_title))
                .setContentText(String.format(resources.getString(R.string.crash_detection_will_activate_in), currentSecond))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText(ctx.getString(R.string.accident_detected))
                        .bigText(String.format(resources.getString(R.string.crash_detection_will_activate_in_to_disable_press_button_turn_off), currentSecond)))
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setVibrate(new long[]{200,500,200})
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(R.drawable.ic_crashed_car, resources.getString(R.string.notifications_turn_off), pStopSelf)
                .build();
    }

    public Notification createMakingHelpRequestNotification(Context ctx) {
        Resources resources = ctx.getResources();
        return new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setProgress(0,100,true)
                .setContentTitle(resources.getString(R.string.crash_detection_foreground_notification_content_title))
                .setContentText(resources.getString(R.string.sending_help_request_in_progress))
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
    }

    public Notification createFinishedMakingHelpRequestNotification(Context ctx, boolean isSuccess) {
        Resources resources = ctx.getResources();
        return new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.crash_detection_foreground_notification_content_title))
                .setContentText(isSuccess?resources.getString(R.string.notification_help_request_sent_successfully):resources.getString(R.string.notification_error_while_sending_help_request))
                .setWhen(DateTime.now().getMillis())
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
    }

    public Notification createHelpRequestResponseNotification(Context ctx, String helpRequestState) {
        //region notification click
        Intent notificationIntent = HomeActivity.getInstance(ctx);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //endregion

        Resources resources = ctx.getResources();
        return new NotificationCompat.Builder(ctx, AssistanceApp.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.help_request_response_notification_title))
                .setContentText(helpRequestState)
                .setContentIntent(pendingIntent)
                .setWhen(DateTime.now().getMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{200,500,200})
                .setSmallIcon(R.drawable.ic_crashed_car)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();
    }
}
