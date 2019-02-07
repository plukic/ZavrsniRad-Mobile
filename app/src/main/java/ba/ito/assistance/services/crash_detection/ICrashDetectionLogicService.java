package ba.ito.assistance.services.crash_detection;

import android.hardware.SensorEvent;

import com.google.android.gms.location.DetectedActivity;

public interface ICrashDetectionLogicService {
    boolean IsInVehicle(DetectedActivity detectedActivity);
    boolean HasStoppedDriving(DetectedActivity detectedActivity);

    boolean HasAccidentOccurred(SensorEvent event);
}
