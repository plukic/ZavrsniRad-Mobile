package ba.ito.assistance.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageRequest;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.ui.user_information.UserInformationsController;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyRegex;
import dagger.android.support.DaggerAppCompatDialogFragment;
import io.reactivex.disposables.Disposable;

public class ProfileUpdateDialog extends DaggerAppCompatDialogFragment {
    private static String BUNDLE_KEY_PREVIOUS_VALUE = "BUNDLE_KEY_PREVIOUS_VALUE";
    private static String BUNDLE_KEY_UPDATE_TYPE = "BUNDLE_KEY_UPDATE_TYPE";
    private static String BUNDLE_KEY_TITLE = "BUNDLE_KEY_TITLE";
    private UserInformationsController.UserInformationUpdateType updateType;
    private Disposable subscribe;
    private Button buttonPositive;
    private String oldValue;
    private String title;


    public interface IProfileUpdateCallback {
        void onPositiveButton(String newValue, UserInformationsController.UserInformationUpdateType updateType);
    }

    public static ProfileUpdateDialog getInstance(String value, UserInformationsController.UserInformationUpdateType type, String title) {
        ProfileUpdateDialog profileUpdateDialog = new ProfileUpdateDialog();

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PREVIOUS_VALUE, value);
        bundle.putString(BUNDLE_KEY_TITLE, title);
        bundle.putSerializable(BUNDLE_KEY_UPDATE_TYPE, type);

        profileUpdateDialog.setArguments(bundle);
        return profileUpdateDialog;
    }


    private IProfileUpdateCallback callback;

    @Inject
    MyRegex myRegex;
    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    IConfigurationService configurationService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof IProfileUpdateCallback)) {
            throw new RuntimeException("Parent must implement " + IProfileUpdateCallback.class.getSimpleName());
        }
        callback = (IProfileUpdateCallback) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dialogTheme = configurationService.getDialogTheme();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), dialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle arguments = getArguments();

        oldValue = arguments.getString(BUNDLE_KEY_PREVIOUS_VALUE);
        title = arguments.getString(BUNDLE_KEY_TITLE);
        updateType = (UserInformationsController.UserInformationUpdateType) arguments.getSerializable(BUNDLE_KEY_UPDATE_TYPE);


        View view = inflater.inflate(R.layout.dialog_user_profile_update_popup, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextInputLayout textInputLayout = view.findViewById(R.id.et_input_layout);
        TextInputEditText editText = view.findViewById(R.id.et_input);


        editText.setText(oldValue);
        tvTitle.setText(title);


        subscribe = RxTextView.textChanges(editText)
                .observeOn(schedulersProvider.main())
                .map(inputText -> myRegex.isValidProfileInput(inputText.toString(), updateType, oldValue))
                .subscribe(isValid -> {
                    if (buttonPositive != null) {
                        buttonPositive.setEnabled(isValid);
                    }
                    if (isValid) {
                        textInputLayout.setError(null);
                    } else {
                        textInputLayout.setError(getErrorText(updateType, oldValue, editText.getEditableText().toString()));
                    }
                });

        builder.setView(view)
                .setPositiveButton(R.string.label_update, (dialog, id) -> {
                    String newValue = editText.getEditableText().toString();
                    if (myRegex.isValidProfileInput(newValue, updateType, oldValue)) {
                        callback.onPositiveButton(newValue, updateType);
                    } else {
                        textInputLayout.setError(getErrorText(updateType, oldValue, newValue));
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> ProfileUpdateDialog.this.getDialog().cancel());


        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            buttonPositive.setEnabled(myRegex.isValidProfileInput(editText.getEditableText().toString(), updateType, oldValue));
        });
        return alertDialog;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null)
            subscribe.dispose();
    }

    private String getErrorText(UserInformationsController.UserInformationUpdateType updateType, String oldValue, String newValue) {
        Resources resources = getResources();

        if (oldValue != null && oldValue.equals(newValue)) {
            return null;
        }
        switch (updateType) {
            case FIRST_NAME:
                return resources.getString(R.string.description_update_enter_firstname);
            case LAST_NAME:
                return resources.getString(R.string.description_update_enter_lastname);
            case EMAIL:
                return resources.getString(R.string.error_field_email_invalid);
            case PHONE_NUMBER:
                return resources.getString(R.string.error_field_phone_invalid);
            case ADDRESS:
                return resources.getString(R.string.error_field_address_invalid);
            case CITY:
                return resources.getString(R.string.error_field_city_invalid);
        }
        return null;
    }


}
