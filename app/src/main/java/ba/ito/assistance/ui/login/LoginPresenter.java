package ba.ito.assistance.ui.login;

import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.data.help_request.IHelpRequestRepo;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private Disposable subscribe;
    private ISchedulersProvider schedulersProvider;
    private IAccountRepo accountRepo;
    private IHelpRequestRepo helpRequestRepo;
    private BaseErrorFactory baseErrorFactory;
    @Inject
    public LoginPresenter(ISchedulersProvider schedulersProvider, IAccountRepo accountRepo, IHelpRequestRepo helpRequestRepo, BaseErrorFactory baseErrorFactory) {
        this.schedulersProvider = schedulersProvider;
        this.accountRepo = accountRepo;
        this.helpRequestRepo = helpRequestRepo;
        this.baseErrorFactory = baseErrorFactory;
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() >= 6;
    }

    @Override
    public boolean validateUsername(String username) {
        return username.length() >= 3;
    }

    @Override
    public void loginUser(String username, String password) {
        if (!validatePassword(password) || !validateUsername(username)) {
            view.displayCompleteFormWarning();
            return;
        }

        view.requestFirebaseToken(username,password);

    }

    @Override
    public void loginUserWithoutFirebase(String username, String password) {
        subscribe = accountRepo.
                loginUser(username, password)
                .subscribeOn(schedulersProvider.network())
                .flatMap(authenticationResponse -> accountRepo.loadUserProfileInfo())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe((clientsDetailsModel)-> {
                    view.displayLoading(false);
                    view.navigateToHomeScreen();
                }, throwable -> {
                    view.displayLoading(false);
                    String error = baseErrorFactory.parseSingleError(throwable,"login_error");
                    view.displayLoginError(error);
                });
    }

    @Override
    public void loginUserWithFirebase(String username, String password, String updatedToken) {
        subscribe = accountRepo.
                loginUser(username, password)
                .subscribeOn(schedulersProvider.network())
                .flatMap(authenticationResponse -> accountRepo.loadUserProfileInfo())
                .flatMap(clientsDetailsModel -> accountRepo.GetDeviceUniqueId())
                .flatMapCompletable(deviceId-> helpRequestRepo.registerUserFirebaseToken(updatedToken, deviceId))
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(()-> {
                    view.displayLoading(false);
                    view.navigateToHomeScreen();
                }, throwable -> {
                    view.displayLoading(false);
                    String error = baseErrorFactory.parseSingleError(throwable,"login_error");
                    view.displayLoginError(error);
                });
    }


    @Override
    public void takeView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (subscribe != null)
            subscribe.dispose();
    }
}
