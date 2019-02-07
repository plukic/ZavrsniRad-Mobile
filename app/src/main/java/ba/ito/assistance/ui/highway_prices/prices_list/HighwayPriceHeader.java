package ba.ito.assistance.ui.highway_prices.prices_list;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_highway_price_header)
public abstract class HighwayPriceHeader extends EpoxyModelWithHolder<HighwayPriceHeader.Holder> {

    @EpoxyAttribute
    String highway;

    @EpoxyAttribute
    String route;


    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvHighway.setText(highway);
        holder.tvRoutes.setText(route);
    }

    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }


    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_route)
        TextView tvRoutes;
        @BindView(R.id.tv_highway)
        TextView tvHighway;
    }
}
