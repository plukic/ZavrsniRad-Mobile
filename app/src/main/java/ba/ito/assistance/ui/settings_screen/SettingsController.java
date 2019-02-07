package ba.ito.assistance.ui.settings_screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.TypedEpoxyController;

import ba.ito.assistance.R;
import ba.ito.assistance.model.settings.SettingsVM;

public class SettingsController extends TypedEpoxyController<SettingsVM> {

    public interface ISettingsNavigation {
        void onLogout();

        void onUpdateSelected(SettingsFragment.SettingsUpdate type);

    }


    @AutoModel
    TwoRowsListItemModel_ username;

    @AutoModel
    TwoRowsListItemModel_ autoCrashDetect;

    @AutoModel
    TwoRowsListItemModel_ gps;

    @AutoModel
    TwoRowsListItemModel_ notifications;

    @AutoModel
    TwoRowsListItemModel_ nightMode;


    private Context context;
    private ISettingsNavigation callback;

    public SettingsController(Context context, ISettingsNavigation callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void buildModels(SettingsVM settingsVM) {
        Resources resources = context.getResources();

        String chage = resources.getString(R.string.label_change);
        setItem(resources, R.string.label_username, settingsVM.Username, username,(view)->{
            callback.onLogout();
        },resources.getString(R.string.label_logout));
        setItem(resources, R.string.label_auto_crash_detect, getIsEnabledString(resources, settingsVM.CrashDetectionEnabled), autoCrashDetect, (view) -> {
            callback.onUpdateSelected(SettingsFragment.SettingsUpdate.CRASH_DETECTION);
        },chage);
        setItem(resources, R.string.label_gps_permission, getIsEnabledString(resources, settingsVM.GpsEnabled), gps, (view) -> {
            callback.onUpdateSelected(SettingsFragment.SettingsUpdate.GPS);
        },chage);
        setItem(resources, R.string.label_notification, getIsEnabledString(resources, settingsVM.NotificationsEnabled), notifications, (view) -> {
            callback.onUpdateSelected(SettingsFragment.SettingsUpdate.NOTIFICATION);
        },chage);
        setItem(resources, R.string.label_night_mode, getIsEnabledString(resources, settingsVM.NightModeEnabled), nightMode, (view) -> {
            callback.onUpdateSelected(SettingsFragment.SettingsUpdate.NIGHT_MODE);
        },chage);


    }

    private String getLanguageString(Resources resources, String selectedLanguage) {
        return selectedLanguage == null ? resources.getString(R.string.option_not_selected) : selectedLanguage;
    }

    private String getIsEnabledString(Resources resources, boolean isEnabled) {
        return isEnabled ? resources.getString(R.string.option_on) : resources.getString(R.string.option_off);
    }

    private void setItem(Resources resources, int label, String content, TwoRowsListItemModel_ model) {
        setItem(resources, label, content, model, null,null);
    }

    private void setItem(Resources resources, int label, String content, TwoRowsListItemModel_ model, View.OnClickListener clickListener, String editText) {
        if (content == null)
            content = resources.getString(R.string.not_entered);

        model.title(resources.getString(label))
                .clickListener(clickListener)
                .content(content)
                .editText(editText)
                .addTo(this);
    }
}
