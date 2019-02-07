package ba.ito.assistance.ui.crash_detection_service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.ito.assistance.services.crash_detection.ICrashDetectionLogicService;
import ba.ito.assistance.ui.notifications.NotificationManager;
import ba.ito.assistance.util.ServicesUtil;
import dagger.android.DaggerService;

import static ba.ito.assistance.ui.crash_detection_service.CrashDetectionState.STOPPED;

public class CrashDetectionService extends DaggerService {

    @Inject
    ba.ito.assistance.ui.notifications.NotificationManager notificationManager;

    private SensorManager sensorManager;
    private Sensor sensor;
    private ShakeListener shakeListener;

    @Inject
    ServicesUtil servicesUtil;
    @Inject
    ICrashDetectionLogicService crashDetectionLogicService;
    @Inject
    EventBus eventBus;
    public CrashDetectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null && sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        }
        shakeListener = new ShakeListener(this, servicesUtil, crashDetectionLogicService);
        if (sensorManager != null && sensor != null) {
            sensorManager.registerListener(shakeListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensor != null && shakeListener != null && sensorManager != null) {
            sensorManager.unregisterListener(shakeListener, sensor);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (sensorManager == null || sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
            displaySensorUnavailableNotification();
            eventBus.post(STOPPED);
            stopSelf();
            return START_NOT_STICKY;
        }
        Notification notification = notificationManager.createCrashDetectionForegroundServiceNotification(this);
        startForeground(NotificationManager.NOTIFICATION_CRASH_DETECTION_FOREGROUND_SERVICE_ID, notification);
        return START_NOT_STICKY;

    }

    private void displaySensorUnavailableNotification() {
        Notification sensorUnavailableNotification = notificationManager.createSensorUnavailableNotification(this);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationManager.NOTIFICATION_CRASH_DETECTION_SENSOR_UNAVAILABLE_ID, sensorUnavailableNotification);
    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, CrashDetectionService.class);
    }
}
