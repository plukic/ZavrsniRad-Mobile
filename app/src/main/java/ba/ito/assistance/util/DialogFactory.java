package ba.ito.assistance.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.ContextThemeWrapper;

import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.services.configuration.IConfigurationService;


public class DialogFactory {


    public interface IDialogSelectCallback {
        void onClick(int position);
    }

    private Context context;
    private IConfigurationService configurationService;
    public DialogFactory(Context context, IConfigurationService configurationService) {
        this.context = context;
        this.configurationService = configurationService;
    }


    public AlertDialog createCancelOkDialog(@StringRes int title, @StringRes int message, DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel) {
        int dialogTheme = configurationService.getDialogTheme();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context,dialogTheme))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK,onOk)
                .setNegativeButton(R.string.Cancel, onCancel);
        return alertDialog.create();
    }

    public AlertDialog createCancelNeutralOkDialog(int positive, int neutral, int negative, int title, int message, DialogInterface.OnClickListener onOk
            , DialogInterface.OnClickListener onNeutral
            , DialogInterface.OnClickListener onCancel) {
        int dialogTheme = configurationService.getDialogTheme();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context,dialogTheme))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive,onOk)
                .setNeutralButton( neutral,onNeutral)
                .setNegativeButton(negative, onCancel);
        return alertDialog.create();
    }


    public Dialog createSimpleListDialog(List<String> options, IDialogSelectCallback callback){
        int dialogTheme = configurationService.getDialogTheme();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,dialogTheme));
        CharSequence charOptions[]= options.toArray(new CharSequence[options.size()]);
        builder.setItems(charOptions, (dialog, which) -> callback.onClick(which));

        return builder.create();
    }





    public ProgressDialog createProgressDialog(String message) {
        int dialogTheme = configurationService.getDialogTheme();
        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(context,dialogTheme));
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public ProgressDialog createProgressDialog(
            @StringRes int messageResource) {
        return createProgressDialog(context.getString(messageResource));
    }
}