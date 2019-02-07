package ba.ito.assistance.ui.password_reset;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;

public interface PasswordResetContract {
    interface  View extends BaseAsyncView<Presenter>{
        void togglePasswordAndToken(boolean isVisible);
        void displayPasswordResetError(String error);
        void displayEnterRequiredField();
        void navigateToHomeScreen();

        void displayCodeResendSuccessfully();

        void pleaseWaitUntilCodeArrives();
    }
    interface  Presenter extends BasePresenter<View> {
        boolean isValidUsername(String username);
        boolean isValidPassword(String password);
        boolean isValidToken(String token);
        void resendPasswordResetCode(String username);
        void resetPasswordButtonPressed(String username, String password, String token);

    }
}
