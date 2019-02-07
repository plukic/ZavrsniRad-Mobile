package ba.ito.assistance.ui.emergency_contacts;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.airbnb.epoxy.TypedEpoxyController;

import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.ui.settings_screen.TwoRowsListItemModel_;

public class EmergencyContactsController extends TypedEpoxyController<List<EmergencyContactNumbers>> {

    public interface IEmergencyContactCallback{
        void onEmergencyContactSelected(EmergencyContactNumbers emergencyContactNumbers);
    }
    private Context context;
    private IEmergencyContactCallback callback;
    public EmergencyContactsController(Context context, IEmergencyContactCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void buildModels(List<EmergencyContactNumbers> clientsDetailsModel) {
        Resources resources = context.getResources();
        String changeLabel = resources.getString(R.string.label_change);
        for (EmergencyContactNumbers item : clientsDetailsModel) {
            new TwoRowsListItemModel_()
                    .id(item.Id)
                    .title(item.ContactFullName)
                    .content(item.PhoneNumber)
                    .clickListener(view -> {
                        callback.onEmergencyContactSelected(item);
                    })
                    .editText(changeLabel)
                    .addTo(this);
        }
    }


    private void setItem(Resources resources, int label, String content, TwoRowsListItemModel_ model) {
        setItem(resources, label, content, model, null, null);
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
