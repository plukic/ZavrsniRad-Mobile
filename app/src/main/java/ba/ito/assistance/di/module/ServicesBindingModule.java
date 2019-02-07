package ba.ito.assistance.di.module;

import ba.ito.assistance.services.firebase.MyFirebaseMessagingService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectedService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectionService;
import ba.ito.assistance.ui.crash_detection_service.DetectedActivitiesIntentService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServicesBindingModule {
    @ContributesAndroidInjector
    abstract DetectedActivitiesIntentService DetectedActivitiesIntentService();
    @ContributesAndroidInjector
    abstract CrashDetectionService crashDetectionService();
    @ContributesAndroidInjector
    abstract CrashDetectedService crashDetectedService();

    @ContributesAndroidInjector
    abstract MyFirebaseMessagingService myFirebaseMessagingService();

}
