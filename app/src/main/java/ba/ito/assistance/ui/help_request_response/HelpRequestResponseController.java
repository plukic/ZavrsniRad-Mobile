package ba.ito.assistance.ui.help_request_response;

import android.content.Context;
import android.content.res.Resources;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.TypedEpoxyController;

import ba.ito.assistance.R;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;

public class HelpRequestResponseController extends TypedEpoxyController<HelpRequestResponseVM> {
    public interface IHelpRequestInteraction{
        void onCallSelected(String contactPhoneNumber);
    }
    @AutoModel
    HelpRequestResponseListItem_ contactPhone;
    @AutoModel
    HelpRequestResponseListItem_ helpRequestState;
    @AutoModel
    HelpRequestResponseListItem_ shortInstructions;

    private IHelpRequestInteraction callback;
    private Context ctx;

    public HelpRequestResponseController(IHelpRequestInteraction callback, Context ctx) {
        this.callback = callback;
        this.ctx = ctx;
    }

    @Override
    protected void buildModels(HelpRequestResponseVM data) {
        Resources res = ctx.getResources();

        contactPhone.text(data.ContactPhoneNumber)
                .title(res.getString(R.string.label_title_free_contact_phone))
                .drawableRight(res.getDrawable(R.drawable.ic_phone))
                .clickListener((view)->{
                    callback.onCallSelected(data.ContactPhoneNumber);
                })

                .addTo(this);
        helpRequestState
                .text(data.HelpRequestState)
                .title(res.getString(R.string.label_title_help_request_state))
                .addTo(this);
        shortInstructions
                .text(data.ShortInstructions)
                .title(res.getString(R.string.label_title_short_instructions))
                .addTo(this);
    }
}
