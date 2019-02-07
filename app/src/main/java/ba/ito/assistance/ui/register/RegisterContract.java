package ba.ito.assistance.ui.register;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BasePendingResult;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;

public interface RegisterContract {

    interface  Presenter extends BasePresenter<View> {

        boolean isValidFirstName(String value);
        boolean isValidLastName(String value);
        boolean isValidEmailAddress(String value);
        boolean isValidUsername(String value);
        boolean isValidPassword(String value);
        boolean isValidPhoneNumber(String value);

        void registerUser(String firstName, String lastName, String email, String username, String password, String phoneNumber);
    }
    interface View extends BaseAsyncView<Presenter> {

        void displayFillFormsError();

        void displayError(String error);

        void navigateToMainScreen();
    }

}
