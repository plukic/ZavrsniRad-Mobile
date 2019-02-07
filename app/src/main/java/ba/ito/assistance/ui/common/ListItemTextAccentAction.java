package ba.ito.assistance.ui.common;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_text_accent_action)
public abstract class ListItemTextAccentAction extends EpoxyModelWithHolder<ListItemTextAccentAction.Holder> {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    View.OnClickListener callback;


    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.tvSeeMore.setOnClickListener(callback);
    }

    @Override
    public void unbind(@NonNull Holder holder) {
        super.unbind(holder);
        holder.tvSeeMore.setOnClickListener(null);
    }

    public class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_see_more)
        TextView tvSeeMore;

    }
}
