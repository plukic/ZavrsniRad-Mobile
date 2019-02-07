package ba.ito.assistance.services.firebase;


import android.app.Notification;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.data.help_request.IHelpRequestRepo;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.ui.notifications.NotificationManager;
import ba.ito.assistance.util.ISchedulersProvider;
import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    IHelpRequestRepo helpRequestRepo;
    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    IAccountRepo accountRepo;

    @Inject
    NotificationManager notificationManager;
    private Disposable disposable;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            if (!HomeActivity.IsInForeground && accountRepo.areNotificationsEnabled()) {
                String helpRequestState = remoteMessage.getData().get("HelpRequestState");
                Notification helpRequestResponseNotification = notificationManager.createHelpRequestResponseNotification(this,helpRequestState);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NotificationManager.NOTIFICATION_HELP_REQUEST_RESPONSE_ID, helpRequestResponseNotification);
            }
            EventBus.getDefault().post(remoteMessage);
            Timber.d("Message data payload: " + remoteMessage.getData());
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Timber.d("New token generated " + s);
        disposable =
                accountRepo.GetDeviceUniqueId()
                .flatMapCompletable(uniqueId-> helpRequestRepo.registerUserFirebaseToken(s,uniqueId))
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(() -> Timber.d("Firebase token updated successfully " + s), Timber::e);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
