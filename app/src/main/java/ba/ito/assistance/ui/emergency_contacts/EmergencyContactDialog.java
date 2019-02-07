package ba.ito.assistance.ui.emergency_contacts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyRegex;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatDialogFragment;
import io.reactivex.disposables.Disposable;

public class EmergencyContactDialog extends DaggerAppCompatDialogFragment {


    private Disposable contactFullNameSubscribe;
    private Button buttonPositive;
    private Disposable phoneNumberValidationSubscribe;
    private boolean isUpdateDialog;

    public interface IEmergencyContactDialogCallback {
        void onEmergencyContact(EmergencyContactNumbers emergencyContactNumber, boolean isUpdateDialog);

        void onDelete(EmergencyContactNumbers emergencyContactNumber);
    }

    public static EmergencyContactDialog getInstance() {
        return new EmergencyContactDialog();
    }

    public static EmergencyContactDialog getInstance(EmergencyContactNumbers emergencyContactNumbers) {
        EmergencyContactDialog instance = getInstance();
        Bundle b = new Bundle();
        b.putParcelable(KEY_EMERGENCY_CONTACT_NUMBER, emergencyContactNumbers);

        instance.setArguments(b);
        return instance;
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_full_name)
    TextInputEditText etFullName;
    @BindView(R.id.til_input_full_name)
    TextInputLayout tilInputFullName;
    @BindView(R.id.et_phone_number)
    TextInputEditText etPhoneNumber;
    @BindView(R.id.till_phone_number)
    TextInputLayout tillPhoneNumber;
    Unbinder unbinder;


    private IEmergencyContactDialogCallback callback;
    private static final String KEY_EMERGENCY_CONTACT_NUMBER = "KEY_EMERGENCY_CONTACT_NUMBER";


    private EmergencyContactNumbers emergencyContactNumber;

    @Inject
    IConfigurationService configurationService;


    @Inject
    ISchedulersProvider schedulersProvider;

    @Inject
    MyRegex myRegex;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof IEmergencyContactDialogCallback)) {
            throw new RuntimeException("Parent must implement " + IEmergencyContactDialogCallback.class.getSimpleName());
        }
        callback = (IEmergencyContactDialogCallback) context;
    }


    private boolean IsObjectValid() {
        String fullName = etFullName.getEditableText().toString();
        String phoneNumber = etPhoneNumber.getEditableText().toString();

        if (!myRegex.isValidEmergencyContactNumber(phoneNumber, emergencyContactNumber.PhoneNumber))
            return false;

        if (!myRegex.isValidFullName(fullName, emergencyContactNumber.ContactFullName))
            return false;

        EmergencyContactNumbers number = new EmergencyContactNumbers();
        number.PhoneNumber = phoneNumber;
        number.ContactFullName = fullName;

        return !number.equals(emergencyContactNumber);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dialogTheme = configurationService.getDialogTheme();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), dialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_emergency_contact_popup, null);
        unbinder = ButterKnife.bind(this, view);


        isUpdateDialog = false;
        Bundle arguments = getArguments();
        if (arguments != null)
            emergencyContactNumber = arguments.getParcelable(KEY_EMERGENCY_CONTACT_NUMBER);

        isUpdateDialog = emergencyContactNumber != null;

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_EMERGENCY_CONTACT_NUMBER)) {
            emergencyContactNumber = savedInstanceState.getParcelable(KEY_EMERGENCY_CONTACT_NUMBER);
        }

        if (emergencyContactNumber == null) {
            emergencyContactNumber = new EmergencyContactNumbers();
        }


        etFullName.setText(emergencyContactNumber.ContactFullName);
        etPhoneNumber.setText(emergencyContactNumber.PhoneNumber);

        setupValidation();


        builder.setView(view)
                .setPositiveButton(isUpdateDialog? R.string.label_update:R.string.label_save, (dialog, id) -> {
                    if (IsObjectValid()) {
                        EmergencyContactNumbers model = new EmergencyContactNumbers();
                        model.PhoneNumber = etPhoneNumber.getEditableText().toString();
                        model.ContactFullName = etFullName.getEditableText().toString();
                        model.Id=emergencyContactNumber.Id;
                        callback.onEmergencyContact(model, isUpdateDialog);
                    }
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> EmergencyContactDialog.this.getDialog().cancel());
        if (isUpdateDialog)
            builder.setNegativeButton(R.string.label_delete, (dialogInterface, i) -> {
                callback.onDelete(emergencyContactNumber);
            });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            buttonPositive.setEnabled(IsObjectValid());
        });
        return alertDialog;


    }

    private void setupValidation() {

        contactFullNameSubscribe = RxTextView.textChanges(etFullName)
                .observeOn(schedulersProvider.main())
                .map(inputText -> IsObjectValid())
                .subscribe(isValid -> {
                    if (buttonPositive != null) {
                        buttonPositive.setEnabled(isValid);
                    }
                });


        phoneNumberValidationSubscribe = RxTextView.textChanges(etPhoneNumber)
                .observeOn(schedulersProvider.main())
                .map(inputText -> IsObjectValid())
                .subscribe(isValid -> {
                    if (buttonPositive != null) {
                        buttonPositive.setEnabled(isValid);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        if (contactFullNameSubscribe != null) {
            contactFullNameSubscribe.dispose();
        }
        if (phoneNumberValidationSubscribe != null) {
            phoneNumberValidationSubscribe.dispose();
        }
    }


}
