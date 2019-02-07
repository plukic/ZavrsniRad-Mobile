package ba.ito.assistance.ui.crash_detection_service;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.data.help_request.IHelpRequestRepo;
import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.ui.notifications.NotificationManager;
import ba.ito.assistance.util.ISchedulersProvider;
import dagger.android.DaggerService;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CrashDetectedService extends DaggerService {
    private static final String BOOLEAN_EXTRA_STOP_SERVICE = "BOOLEAN_EXTRA_STOP_SERVICE";
//    private static final int CRASH_DETECTED_FINISHED_NOTIFICATION_ID = 102;
//    private int CRASH_DETECTED_NOTIFICATION_ID = 101;

//    MediaPlayer player;

    @Inject
    ba.ito.assistance.ui.notifications.NotificationManager notificationManager;
    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    IHelpRequestRepo helpRequestRepo;
    @Inject
    IAccountRepo accountRepo;
    private Disposable subscribe;
    private Disposable helpRequestSubscribe;
    private static final int CRASH_DETECTION_TIME_IN_SECONDS = 15;


    //region LIFECYCLE
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        player = MediaPlayer.create(this, uri);
//        player.setLooping(true);
//        player.setVolume(100, 100);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (containsStopService(intent)) {
            stopSelf();
            return START_STICKY;
        }
//        player.start();

        if (subscribe != null)
            subscribe.dispose();

        subscribe = io.reactivex.Observable.interval(1, TimeUnit.SECONDS, schedulersProvider.computation())
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .take(CRASH_DETECTION_TIME_IN_SECONDS)
                .map(aLong -> CRASH_DETECTION_TIME_IN_SECONDS - aLong)
                .subscribe(onCrashDetectionTimerTick());

        Notification crashDetectionDetectedNotification = notificationManager.createCrashDetectionDetectedNotification(this, CRASH_DETECTION_TIME_IN_SECONDS);
        startForeground(NotificationManager.NOTIFICATION_CRASH_DETECTED_COUNTDOWN_ID, crashDetectionDetectedNotification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        player.stop();
//        player.release();
        if (subscribe != null)
            subscribe.dispose();
        if (helpRequestSubscribe != null)
            helpRequestSubscribe.dispose();
    }
    //endregion


    @NonNull
    private Consumer<Long> onCrashDetectionTimerTick() {
        return second -> {
            displayCountdownNotification(second);
            if (second == 1) {
//                player.stop();
                displayMakingHelpRequestNotification();
                makeEmergencyCall();
            }
        };
    }


    //region Notifications
    private void displayCountdownNotification(Long second) {
        Notification crashDetectionDetectedNotification = notificationManager.createCrashDetectionDetectedNotification(this, second);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationManager.NOTIFICATION_CRASH_DETECTED_COUNTDOWN_ID, crashDetectionDetectedNotification);
    }

    private void displayMakingHelpRequestNotification() {
        Notification crashDetectionDetectedNotification = notificationManager.createMakingHelpRequestNotification(this);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationManager.NOTIFICATION_CRASH_DETECTED_MAKING_HELP_REQUEST_ID, crashDetectionDetectedNotification);
    }

    private void displayFinishedMakingHelpRequestNotification(boolean isSuccess) {
        Notification crashDetectionDetectedNotification = notificationManager.createFinishedMakingHelpRequestNotification(this, isSuccess);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationManager.NOTIFICATION_CRASH_FINISHED_MAKING_HELP_REQUEST_ID, crashDetectionDetectedNotification);
    }

    //endregion


    private void makeEmergencyCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            makeHelpRequest(null);
            return;
        }
        Task<Location> lastLocation = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        lastLocation.addOnSuccessListener(this::makeHelpRequest);
        lastLocation.addOnFailureListener(e -> makeHelpRequest(null));

    }

    private void makeHelpRequest(Location location) {
        LatLng latLng = getLatLng(location);

        helpRequestSubscribe = helpRequestRepo.makeNewRequest(latLng, HelpRequestCategory.Accident)
                .subscribeOn(schedulersProvider.network())
                .onErrorComplete()
                .andThen(accountRepo.loadUserProfileInfo())
                .observeOn(schedulersProvider.main())
                .subscribe((clientAccountInfo) -> {
                    displayFinishedMakingHelpRequestNotification(true);
                    if (clientAccountInfo.SupportNumber != null && !clientAccountInfo.SupportNumber.isEmpty()) {
                        makePhoneCall(clientAccountInfo.SupportNumber);
                    }
                    stopSelf();
                }, throwable -> {
                    displayFinishedMakingHelpRequestNotification(false);
                    stopSelf();
                });
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.startActivity(intent);
    }

    @NonNull
    private LatLng getLatLng(Location location) {
        LatLng latLng;
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            latLng = new LatLng(0, 0);
        }
        return latLng;
    }


    public static Intent getInstance(Context ctx, boolean stopService) {
        Intent i = new Intent(ctx, CrashDetectedService.class);
        i.putExtra(BOOLEAN_EXTRA_STOP_SERVICE, stopService);
        return i;
    }
    private boolean containsStopService(Intent intent) {
        return intent != null && intent.getBooleanExtra(BOOLEAN_EXTRA_STOP_SERVICE, false);
    }

}
