package ba.ito.assistance.ui.login;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;

public interface LoginContract {

    interface  Presenter extends BasePresenter<View>
    {
        boolean validatePassword(String password);
        boolean validateUsername(String username);

        void loginUser(String username, String password);

        void loginUserWithoutFirebase(String username, String password);

        void loginUserWithFirebase(String username, String password, String updatedToken);
    }
    interface  View extends BaseAsyncView<View>{
        void displayLoginError(String error);
        void navigateToEnterProfileDataScreen();
        void navigateToHomeScreen();

        void displayCompleteFormWarning();

        void requestFirebaseToken(String username, String password);
    }
}
