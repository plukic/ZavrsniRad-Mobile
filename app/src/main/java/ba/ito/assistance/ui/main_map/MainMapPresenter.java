package ba.ito.assistance.ui.main_map;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.data.help_request.IHelpRequestRepo;
import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.ui.crash_detection_service.CrashDetectionState;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainMapPresenter implements MainMapContract.Presenter {

    private MainMapContract.View view;
    private IHelpRequestRepo helpRequestRepo;
    private ISchedulersProvider schedulersProvider;
    private IAccountRepo accountRepo;
    private BaseErrorFactory baseErrorFactory;
    private IConfigurationService configurationService;

    private Disposable userHelpRequestResponseSubscribe;
    private Disposable countdownSubscribe;
    private Disposable subscribe;

    private EventBus eventBus;
    private Disposable lastUserParkedLocation;
    private Disposable subscribeParkMyCar;
    private Disposable deleteParkedCarLocationSubscribe;
    private double lastDraggingState;


    @Inject
    public MainMapPresenter(IHelpRequestRepo helpRequestRepo, ISchedulersProvider schedulersProvider, IAccountRepo accountRepo, BaseErrorFactory baseErrorFactory,
                            IConfigurationService configurationService, EventBus eventBus) {
        this.helpRequestRepo = helpRequestRepo;
        this.schedulersProvider = schedulersProvider;
        this.accountRepo = accountRepo;
        this.baseErrorFactory = baseErrorFactory;
        this.configurationService = configurationService;
        this.eventBus = eventBus;
    }

    @Override
    public void onMapReady() {
        if (view == null)
            return;
        int theme = configurationService.getMapTheme();
        view.setMapStyleOverlay(theme);
        view.requestLocationPermission();

        checkForUserParking();

    }



    @Override
    public void makeRequest(LatLng location, HelpRequestCategory helpRequestCategory) {
        if (subscribe != null)
            subscribe.dispose();
        subscribe = helpRequestRepo.makeNewRequest(location, helpRequestCategory)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .doOnComplete(() -> view.displayLoading(false))
                .doOnError(throwable -> view.displayLoading(false))
                .subscribe(() -> {
                    view.checkPhonePermissions();
                }, throwable -> {
                    String t = baseErrorFactory.parseSingleError(throwable, "");
                    view.displayError(R.string.msg_unexpected_error);
                });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationPermissionReady() {
        view.onLocationPermissionGranted();

    }

    private void checkForUserHelpRequestResponses() {
        userHelpRequestResponseSubscribe =
                helpRequestRepo
                        .GetHelpRequestResponseForClient()
                        .subscribeOn(schedulersProvider.network())
                        .observeOn(schedulersProvider.main())
                        .subscribe(helpRequestResponseVM ->
                        {
                            if (shouldStartRequestCountdow(helpRequestResponseVM)) {
                                view.displayHelpRequestInProgress(helpRequestResponseVM);
                                startHelpRequestCountdown(helpRequestResponseVM);
                            } else {
                                view.displayNoHelpRequestInProgress();
                                stopHelpRequestCountdown();
                            }
                        }, throwable -> {
                            view.displayNoHelpRequestInProgress();
                        });
    }

    private void stopHelpRequestCountdown() {
        if (countdownSubscribe != null)
            countdownSubscribe.dispose();
    }

    private boolean shouldStartRequestCountdow(HelpRequestResponseVM helpRequestResponseVM) {
        int minutes = Minutes.minutesBetween(DateTime.now(), helpRequestResponseVM.HelpIncomingDateTime).getMinutes();
        return minutes >= 1;
    }

    private void startHelpRequestCountdown(HelpRequestResponseVM helpRequestResponseVM) {
        stopHelpRequestCountdown();

        int seconds = Seconds.secondsBetween(DateTime.now(), helpRequestResponseVM.HelpIncomingDateTime).getSeconds();
        view.displayMinutesUntilHelpArives(seconds / 60);

        countdownSubscribe = io.reactivex.Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(schedulersProvider.network())
                .take(seconds)
                .map(aLong -> (int) (seconds - aLong) / 60)
                .observeOn(schedulersProvider.main())
                .subscribe(min -> {
                    if (min <= 1)
                        view.displayNoHelpRequestInProgress();
                    else
                        view.displayMinutesUntilHelpArives(min);
                });
    }

    @Override
    public void makePhoneRequest() {
        subscribe = accountRepo.loadUserProfileInfo()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .doFinally(() -> view.displayLoading(false))
                .doOnError(throwable -> view.displayLoading(false))
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe((clientAccountInfo) -> {
                    view.callPhoneNumber(clientAccountInfo.SupportNumber);
                }, throwable -> {
                    view.displayError(R.string.msg_unexpected_error);
                });
    }

    @Override
    public void unableToGetLocation() {
        view.displayLocationNotAvailable();
    }

    @Override
    public void onHelpRequestLocationNotAvailable(HelpRequestCategory category) {
        view.displayHelpRequestLocationNotAvailable(category);
    }



    @Override
    public void parkMyCar(LatLng latLng) {
        subscribeParkMyCar = accountRepo.setUserLastParkedLocation(latLng)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(() -> {
                    view.displayUserParkedCarLocation(latLng);
                }, throwable -> {
                    view.displayUnableToSetParkedCarLocation(latLng);
                });
    }

    @Override
    public void deleteParkedCarLocation() {
        deleteParkedCarLocationSubscribe = accountRepo.setUserLastParkedLocation(null)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(() -> {
                    view.deleteParkedCarLocation();
                }, Timber::e);
    }

    @Override
    public boolean isExpanding(float slideOffset) {
        if (slideOffset < lastDraggingState) {
            lastDraggingState = slideOffset;
            return false;
        } else {
            lastDraggingState = slideOffset;
            return true;
        }
    }


    @Override
    public void takeView(MainMapContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        eventBus.register(this);
        checkForUserHelpRequestResponses();
        boolean isServiceRunning =  view.isServiceRunning();
        view.setCrashDetectionToggleOverlay(isServiceRunning);
    }

    private void checkForUserParking() {
        lastUserParkedLocation = accountRepo.getUserLastParkedLocation()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(latLng -> view.displayUserParkedCarLocation(latLng), Timber::e);
    }


    @Override
    public void onStop() {
        eventBus.unregister(this);
        if (subscribe != null)
            subscribe.dispose();
        if (userHelpRequestResponseSubscribe != null)
            userHelpRequestResponseSubscribe.dispose();
        if (countdownSubscribe != null)
            countdownSubscribe.dispose();

        if (lastUserParkedLocation != null)
            lastUserParkedLocation.dispose();
        if (subscribeParkMyCar != null)
            subscribeParkMyCar.dispose();
        if (deleteParkedCarLocationSubscribe != null)
            deleteParkedCarLocationSubscribe.dispose();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RemoteMessage remoteMessage) {
        checkForUserHelpRequestResponses();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCrashDetectionToggle(CrashDetectionState crashDetectionState) {
        view.setCrashDetectionToggleOverlay(crashDetectionState==CrashDetectionState.RUNNING);
    }


}
