package ba.ito.assistance.ui.settings_screen;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_settings_header)
public abstract class SettingsListHeaderModel  extends EpoxyModelWithHolder<SettingsListHeaderModel.Holder> {

    @EpoxyAttribute
    String title;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.headerTitle.setText(title);
    }

    @Override
    protected SettingsListHeaderModel.Holder createNewHolder() {
        return new SettingsListHeaderModel.Holder();
    }

    static class Holder extends BaseEpoxyHolder{
        @BindView(R.id.tv_settings_header_title)
        TextView headerTitle;

    }

}
