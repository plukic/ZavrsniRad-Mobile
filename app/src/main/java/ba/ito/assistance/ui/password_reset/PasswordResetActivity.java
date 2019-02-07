package ba.ito.assistance.ui.password_reset;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerActivity;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class PasswordResetActivity extends BaseDaggerActivity implements PasswordResetContract.View {


    @BindView(R.id.ed_username)
    TextInputEditText edUsername;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.ed_password)
    TextInputEditText edPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.ed_password_reset_code)
    TextInputEditText edPasswordResetCode;
    @BindView(R.id.til_password_reset_code)
    TextInputLayout tilPasswordResetCode;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;

    @Inject
    PasswordResetContract.Presenter presenter;
    @Inject
    DialogFactory dialogFactory;
    @Inject
    ISchedulersProvider schedulersProvider;

    @BindView(R.id.parent)
    ScrollView parent;
    private ProgressDialog pleaseWaitDialog;

    private Disposable usernameValidationDisposable;
    private Disposable passwordValidationDisposable;
    private Disposable tokenValidationDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        ButterKnife.bind(this);

        presenter.takeView(this);
        presenter.onStart();
        setupValidationRules();

    }

    private void setupValidationRules() {
        Observable<Boolean> usernameObservable = RxTextView.textChanges(edUsername)
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(username -> presenter.isValidUsername(username.toString()));


        Observable<Boolean> passwordObservable = RxTextView.textChanges(edPassword)
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(password -> presenter.isValidPassword(password.toString()));

        Observable<Boolean> tokenObservable = RxTextView.textChanges(edPasswordResetCode)
                .skipInitialValue()
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(token -> presenter.isValidToken(token.toString()));


        usernameValidationDisposable = usernameObservable
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        tilUsername.setError(null);
                    else
                        tilUsername.setError(getResources().getString(R.string.error_field_username_invalid));
                });


        passwordValidationDisposable = passwordObservable
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        tilPassword.setError(null);
                    else
                        tilPassword.setError(getResources().getString(R.string.error_field_password_invalid));
                });


        tokenValidationDisposable = tokenObservable
                .subscribeOn(schedulersProvider.computation())
                .observeOn(schedulersProvider.main())
                .subscribe(aBoolean -> {
                    if (aBoolean)
                        tilPasswordResetCode.setError(null);
                    else
                        tilPasswordResetCode.setError(getResources().getString(R.string.error_field_token_invalid));
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
        if (usernameValidationDisposable != null)
            usernameValidationDisposable.dispose();
        if (passwordValidationDisposable != null)
            passwordValidationDisposable.dispose();
        if (tokenValidationDisposable != null)
            tokenValidationDisposable.dispose();
    }

    @OnClick(R.id.btn_send_code)
    public void onSendCode() {
        presenter.resetPasswordButtonPressed(edUsername.getText().toString(), edPassword.getText().toString(), edPasswordResetCode.getText().toString());
    }

    @OnClick(R.id.label_resend_code)
    public void onResendCode() {
        presenter.resendPasswordResetCode(edUsername.getText().toString());
    }

    @Override
    public void togglePasswordAndToken(boolean isVisible) {
        if (isVisible) {
            tilPassword.setVisibility(View.VISIBLE);
            tilPasswordResetCode.setVisibility(View.VISIBLE);
            btnSendCode.setText(R.string.label_submit);
        } else {
            tilPassword.setVisibility(View.GONE);
            tilPasswordResetCode.setVisibility(View.GONE);
            btnSendCode.setText(R.string.label_new_password);
        }
    }

    @Override
    public void displayPasswordResetError(String error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayEnterRequiredField() {
        Snackbar.make(parent, R.string.error_please_fill_in_form, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    @Override
    public void displayCodeResendSuccessfully() {
        Snackbar.make(parent, R.string.msg_code_resend_successfull, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void pleaseWaitUntilCodeArrives() {
        Snackbar.make(parent, R.string.msg_please_wait_for_code_resend, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        if (isLoading) {
            pleaseWaitDialog = dialogFactory.createProgressDialog(R.string.please_wait);
            pleaseWaitDialog.show();
        } else if (pleaseWaitDialog != null) {
            pleaseWaitDialog.dismiss();
        }
    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, PasswordResetActivity.class);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }
}
