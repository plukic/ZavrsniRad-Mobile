package ba.ito.assistance.ui.services;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;


@EpoxyModelClass(layout = R.layout.list_item_services)
public abstract class ServicesViewModel extends EpoxyModelWithHolder<ServicesViewModel.Holder> {



    @EpoxyAttribute
    Drawable iconTop;
    @EpoxyAttribute
    Drawable iconRight;


    @EpoxyAttribute
    String title;


    @EpoxyAttribute(DoNotHash)
    View.OnClickListener clickListener;


    @Override
    public void unbind(ServicesViewModel.Holder holder) {
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        holder.itemView.setOnClickListener(clickListener);
        holder.tvServicesItem.setText(title);
        holder.tvServicesItem.setCompoundDrawablesWithIntrinsicBounds(null,iconTop,iconRight,null);
    }

    @Override
    protected ServicesViewModel.Holder createNewHolder() {
        return new ServicesViewModel.Holder();
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_services_item)
        TextView tvServicesItem;
    }

}
