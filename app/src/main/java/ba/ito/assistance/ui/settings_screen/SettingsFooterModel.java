package ba.ito.assistance.ui.settings_screen;


import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_settings_footer)
public abstract class SettingsFooterModel  extends EpoxyModelWithHolder<SettingsFooterModel.Holder> {

    @EpoxyAttribute
    String description;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvDescription.setText(description);
    }

    @Override
    protected SettingsFooterModel.Holder createNewHolder() {
        return new SettingsFooterModel.Holder();
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_description)
        TextView tvDescription;
    }

}
