package ba.ito.assistance.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerActivity;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.ui.password_reset.PasswordResetActivity;
import ba.ito.assistance.ui.register.RegisterActivity;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseDaggerActivity implements LoginContract.View {


    private static final String EXTRA_USER_LOGGED_OUT_MESSAGE = "EXTRA_USER_LOGGED_OUT_MESSAGE ";
    @Inject
    LoginContract.Presenter presenter;
    @Inject
    DialogFactory dialogFactory;
    @Inject
    ISchedulersProvider schedulersProvider;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_password_reset)
    Button btnPasswordReset;
    @BindView(R.id.parent)
    CoordinatorLayout parent;

    private ProgressDialog loginPleaseWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_USER_LOGGED_OUT_MESSAGE)) {
            Snackbar.make(parent, getIntent().getStringExtra(EXTRA_USER_LOGGED_OUT_MESSAGE), Snackbar.LENGTH_LONG).show();
        }
        presenter.takeView(this);
        presenter.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }


    @OnClick(R.id.btn_register
    )
    public void onRegister(){
        startActivity(RegisterActivity.GetInstance(this));
    }
    @OnEditorAction(R.id.ed_password)
    boolean onEdPasswordEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            presenter.loginUser(edUsername.getText().toString(), edPassword.getText().toString());
            hideKeyboard();
        }
        return false;
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClick() {
        presenter.loginUser(edUsername.getText().toString(), edPassword.getText().toString());
        hideKeyboard();
    }

    @OnClick(R.id.btn_password_reset)
    public void onLabelPasswordReset() {
        startActivity(PasswordResetActivity.getInstance(this));
    }


    @Override
    public void displayLoginError(String error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToEnterProfileDataScreen() {
        Snackbar.make(parent, "Poruka o profile data screen", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayCompleteFormWarning() {
        Snackbar.make(parent, R.string.error_please_fill_in_form, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void requestFirebaseToken(String username, String password) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this, instanceIdResult -> {
            String updatedToken = instanceIdResult.getToken();
            presenter.loginUserWithFirebase(username,password,updatedToken);

        }).addOnFailureListener(runnable -> {
            presenter.loginUserWithoutFirebase(username,password);
        });
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        if (isLoading) {
            loginPleaseWaitDialog = dialogFactory.createProgressDialog(R.string.please_wait);
            loginPleaseWaitDialog.show();
        } else if (loginPleaseWaitDialog != null) {
            loginPleaseWaitDialog.dismiss();
        }
    }

    public static Intent getInstanceWithMessage(Context context, String message) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_USER_LOGGED_OUT_MESSAGE, message);

        return intent;
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
