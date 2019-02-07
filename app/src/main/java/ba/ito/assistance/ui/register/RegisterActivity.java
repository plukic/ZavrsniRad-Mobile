package ba.ito.assistance.ui.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerActivity;
import ba.ito.assistance.ui.home.HomeActivity;
import ba.ito.assistance.util.DialogFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterActivity extends BaseDaggerActivity implements RegisterContract.View {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    RegisterContract.Presenter presenter;

    @Inject
    DialogFactory dialogFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_first_name)
    EditText edFirstName;
    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;
    @BindView(R.id.ed_last_name)
    EditText edLastName;
    @BindView(R.id.til_last_name)
    TextInputLayout tilLastName;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.ed_phone_number)
    EditText edPhoneNumber;
    @BindView(R.id.til_phone_number)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter.takeView(this);
        presenter.onStart();
        setupValidation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();

    }

    private void setupValidation() {
        Resources r = getResources();
        compositeDisposable.add(RxTextView
                .textChanges(edFirstName)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidFirstName(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_firstname_invalid))
                .subscribe(error -> tilFirstName.setError(error)));

        compositeDisposable.add(RxTextView
                .textChanges(edLastName)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidLastName(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_lastname_invalid))
                .subscribe(error -> tilLastName.setError(error)));

        compositeDisposable.add(RxTextView
                .textChanges(edUsername)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidUsername(charSequence.toString()))
                .map(isValid -> isValid ? "": r.getString(R.string.error_field_username_invalid))
                .subscribe(error -> tilUsername.setError(error)));

        compositeDisposable.add(RxTextView
                .textChanges(edPassword)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidPassword(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_password_invalid))
                .subscribe(error -> tilPassword.setError(error)));


        compositeDisposable.add(RxTextView
                .textChanges(edPhoneNumber)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidPhoneNumber(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_phone_invalid))
                .subscribe(error -> tilPhoneNumber.setError(error)));


        compositeDisposable.add(RxTextView
                .textChanges(edEmail)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidEmailAddress(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_email_invalid))
                .subscribe(error -> tilEmail.setError(error)));
    }

    @OnClick(R.id.fab_register)
    public void onRegister() {
        String firstName = edFirstName.getText().toString();
        String lastName = edLastName.getText().toString();
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();
        String phoneNumber = edPhoneNumber.getText().toString();
        String email = edEmail.getText().toString();
        presenter.registerUser(firstName, lastName, email, username, password, phoneNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent GetInstance(Context ctx) {
        return new Intent(ctx, RegisterActivity.class);
    }

    @Override
    public void displayFillFormsError() {
        Snackbar.make(parent, R.string.error_please_fill_in_form, Snackbar.LENGTH_LONG).show();;
    }

    @Override
    public void displayError(String error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();;
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        if (progressDialog != null)
            progressDialog.dismiss();
        if (isLoading) {
            progressDialog = dialogFactory.createProgressDialog(R.string.please_wait);
            progressDialog.show();
        }
    }
}
