package ba.ito.assistance.ui.settings_screen;
import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import android.view.View;
import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_settings_remove_account)
public abstract class SettingsRemoveAccountModel  extends EpoxyModelWithHolder<SettingsRemoveAccountModel.Holder> {

    @EpoxyAttribute(DoNotHash)
    View.OnClickListener clickListener;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvLogout.setOnClickListener(clickListener);
    }

    @Override
    public void unbind(@NonNull Holder holder) {
        holder.tvLogout.setOnClickListener(null);
    }


    @Override
    protected SettingsRemoveAccountModel.Holder createNewHolder() {
        return new SettingsRemoveAccountModel.Holder();
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_logout)
        TextView tvLogout;
    }

}