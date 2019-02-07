package ba.ito.assistance.services.crash_detection;

import android.hardware.SensorEvent;
import android.provider.Settings;

import com.google.android.gms.location.DetectedActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class CrashDetectionLogicService implements ICrashDetectionLogicService {
    private static final int FORCE_THRESHOLD = 15000;
    private static final int TIME_THRESHOLD = 75;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 150;
    private static final int SHAKE_COUNT = 1;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;
    private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
    private long mLastTime;


    @Inject
    public CrashDetectionLogicService() {
    }

    @Override
    public boolean IsInVehicle(DetectedActivity detectedActivity) {
        if (detectedActivity.getType() != DetectedActivity.IN_VEHICLE)
            return false;
        return detectedActivity.getConfidence() > 75;
    }

    @Override
    public boolean HasStoppedDriving(DetectedActivity detectedActivity) {
        if (detectedActivity.getType() == DetectedActivity.IN_VEHICLE || detectedActivity.getType() == DetectedActivity.STILL)
            return false;
        return detectedActivity.getConfidence() > 75;
    }

    @Override
    public boolean HasAccidentOccurred(SensorEvent event) {
        long now = System.currentTimeMillis();
        if ((now - mLastForce) > SHAKE_TIMEOUT) {
            mShakeCount = 0;
        }
        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;
            Timber.d("Speed(" + speed + ")");
            if (speed > FORCE_THRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    Timber.d("Speed(" + speed + ") > FORCE_THRESHOLD(" + FORCE_THRESHOLD + ")");
                    Timber.d("***********************ACCIDENT DETECTED****************************");
                    return true;
                }
                mLastForce = now;
            }
            mLastTime = now;
            mLastX = event.values[0];
            mLastY = event.values[1];
            mLastZ = event.values[2];
        }
        return false;
    }
}
