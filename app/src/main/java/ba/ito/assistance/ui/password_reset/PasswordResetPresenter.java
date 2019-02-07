package ba.ito.assistance.ui.password_reset;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyRegex;
import io.reactivex.disposables.Disposable;

public class PasswordResetPresenter implements PasswordResetContract.Presenter {
    private PasswordResetContract.View view;
    private IAccountRepo accountRepo;
    private boolean isInTokenReceivedState = false;
    private DateTime lastCodeResendTime;
    private ISchedulersProvider schedulersProvider;
    private Disposable requestPasswordResetTokenSubscribe;
    private BaseErrorFactory baseErrorFactory;
    private Disposable confirmPasswordResetCompletable;
    private MyRegex regex;

    @Inject
    public PasswordResetPresenter(IAccountRepo accountRepo, ISchedulersProvider schedulersProvider, BaseErrorFactory baseErrorFactory, MyRegex myRegex) {
        this.accountRepo = accountRepo;
        this.schedulersProvider = schedulersProvider;
        this.baseErrorFactory = baseErrorFactory;
        this.regex = myRegex;
    }

    @Override
    public boolean isValidUsername(String username) {
        return !username.isEmpty();
    }

    @Override
    public boolean isValidPassword(String password) {
        return regex.isValidPassword(password);
    }

    @Override
    public boolean isValidToken(String token) {
        return token.length() >= 4;
    }

    @Override
    public void resendPasswordResetCode(String username) {
        if (!isValidUsername(username)) {
            view.displayEnterRequiredField();
            return;
        }
        if (lastCodeResendTime != null && !lastCodeResendTime.isAfter(DateTime.now().plusMinutes(2))) {
            view.pleaseWaitUntilCodeArrives();
            return;
        }
        if (requestPasswordResetTokenSubscribe != null)
            requestPasswordResetTokenSubscribe.dispose();

        requestPasswordResetTokenSubscribe = accountRepo.requestPasswordResetToken(username)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .doOnComplete(()->view.displayLoading(false))
                .doOnError(throwable -> view.displayLoading(false))
                .subscribe(() -> {
                    lastCodeResendTime = new DateTime();
                    view.displayCodeResendSuccessfully();
                }, throwable -> {
                    view.displayPasswordResetError(baseErrorFactory.parseSingleError(throwable, "reset_password"));
                });
    }

    @Override
    public void resetPasswordButtonPressed(String username, String password, String token) {
        if (isInTokenReceivedState) {
            confirmPasswordReset(username, password, token);
        } else {
            sendPasswordResetToken(username);
        }
    }

    private void sendPasswordResetToken(String username) {
        if (!isValidUsername(username)) {
            view.displayEnterRequiredField();
            return;
        }

        requestPasswordResetTokenSubscribe = accountRepo.requestPasswordResetToken(username)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .doOnComplete(()->view.displayLoading(false))
                .doOnError(throwable -> view.displayLoading(false))
                .subscribe(() -> {
                    isInTokenReceivedState = true;
                    view.togglePasswordAndToken(true);
                }, throwable -> {
                    isInTokenReceivedState = false;
                    view.togglePasswordAndToken(false);
                    view.displayPasswordResetError(baseErrorFactory.parseSingleError(throwable, "reset_password"));
                });

    }

    private void confirmPasswordReset(String username, String password, String token) {

        if (!isValidUsername(username) || !isValidPassword(password) || !isValidToken(token)) {
            view.displayEnterRequiredField();
            return;
        }

        confirmPasswordResetCompletable = accountRepo.confirmPasswordReset(username, password, token)
                .subscribeOn(schedulersProvider.network())
                .andThen(accountRepo.loginUser(username, password))
                .flatMap(authenticationResponse -> accountRepo.loadUserProfileInfo())

                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .doOnComplete(()->view.displayLoading(false))
                .doOnError(throwable -> view.displayLoading(false))
                .subscribe(clientAccountInfo -> {
                    view.navigateToHomeScreen();
                }, throwable -> {
                    String error = baseErrorFactory.parseSingleError(throwable, "confirm_password_reset");
                    view.displayPasswordResetError(error);
                });
        ;

    }

    @Override
    public void takeView(PasswordResetContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        if (isInTokenReceivedState) {
            view.togglePasswordAndToken(true);
        } else {
            view.togglePasswordAndToken(false);
        }
    }

    @Override
    public void onStop() {
        if (confirmPasswordResetCompletable != null)
            confirmPasswordResetCompletable.dispose();
        if (requestPasswordResetTokenSubscribe != null)
            requestPasswordResetTokenSubscribe.dispose();
    }

}
