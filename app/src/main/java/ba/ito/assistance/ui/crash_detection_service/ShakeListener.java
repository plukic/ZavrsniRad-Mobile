package ba.ito.assistance.ui.crash_detection_service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.content.ContextCompat;

import ba.ito.assistance.services.crash_detection.ICrashDetectionLogicService;
import ba.ito.assistance.util.ServicesUtil;
import timber.log.Timber;

public class ShakeListener implements SensorEventListener {

    private final Context context;
    private ServicesUtil servicesUtil;
    private ICrashDetectionLogicService crashDetectionLogicService;
    public ShakeListener(Context ctx, ServicesUtil servicesUtil, ICrashDetectionLogicService crashDetectionLogicService) {
        this.context = ctx;
        this.servicesUtil = servicesUtil;
        this.crashDetectionLogicService = crashDetectionLogicService;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!servicesUtil.IsServiceRunning(context,CrashDetectedService.class) && crashDetectionLogicService.HasAccidentOccurred(event)){
            startCrashDetectedService();
        }
    }

    private void startCrashDetectedService() {
        Intent intent = new Intent(context,CrashDetectedService.class);
        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
