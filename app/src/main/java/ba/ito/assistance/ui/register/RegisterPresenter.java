package ba.ito.assistance.ui.register;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.user.ConfigurationGroupEnum;
import ba.ito.assistance.model.user.MobileClientCreateModel;
import ba.ito.assistance.ui.register.RegisterContract.Presenter;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyRegex;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterPresenter implements Presenter {
    @Inject

    MyRegex myRegex;
    @Inject
    IAccountRepo accountRepo;

    @Inject
    BaseErrorFactory baseErrorFactory;
    @Inject
    ISchedulersProvider schedulersProvider;


    private RegisterContract.View view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public RegisterPresenter(MyRegex myRegex) {
        this.myRegex = myRegex;
    }

    @Override
    public boolean isValidFirstName(String value) {
        return myRegex.isValidFirstName(value);
    }

    @Override
    public boolean isValidLastName(String value) {
        return myRegex.isValidLastName(value);
    }

    @Override
    public boolean isValidEmailAddress(String value) {
        return myRegex.isValidEmail(value);
    }

    @Override
    public boolean isValidUsername(String value) {
        return myRegex.isValidUsername(value);
    }

    @Override
    public boolean isValidPassword(String value) {
        return myRegex.isValidPassword(value);
    }

    @Override
    public boolean isValidPhoneNumber(String value) {
        return myRegex.isValidPhoneNumber(value);
    }

    private boolean areFieldsValid(String firstName, String lastName, String email, String username, String password, String phoneNumber) {
        return isValidFirstName(firstName) && isValidLastName(lastName) && isValidEmailAddress(email) && isValidUsername(username) && isValidPassword(password) && isValidPhoneNumber(phoneNumber);
    }

    @Override
    public void registerUser(String firstName, String lastName, String email, String username, String password, String phoneNumber) {
        boolean formStatus = areFieldsValid(firstName, lastName, email, username, password, phoneNumber);
        if (!formStatus) {
            view.displayFillFormsError();
            return;
        }
        MobileClientCreateModel clientCreateModel = new MobileClientCreateModel();
        clientCreateModel.FirstName = firstName;
        clientCreateModel.LastName = lastName;
        clientCreateModel.Email = email;
        clientCreateModel.CardNumber = username;
        clientCreateModel.Password = password;
        clientCreateModel.PhoneNumber = phoneNumber;
        clientCreateModel.ConfigurationGroup = ConfigurationGroupEnum.CO;

        compositeDisposable.add(
                accountRepo.registerUser(clientCreateModel)
                        .andThen(accountRepo.loginUser(clientCreateModel.CardNumber, clientCreateModel.Password))
                        .subscribeOn(schedulersProvider.network())
                        .observeOn(schedulersProvider.main())
                        .doOnSubscribe(disposable -> view.displayLoading(true))
                        .subscribe(authenticationResponse -> {
                            view.displayLoading(false);
                            view.navigateToMainScreen();
                        }, throwable -> {
                            view.displayLoading(false);
                            String error = baseErrorFactory.parseSingleError(throwable, "register_error");
                            view.displayError(error);
                        }));

    }


    @Override
    public void takeView(RegisterContract.View view) {

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
        compositeDisposable.dispose();
    }
}
