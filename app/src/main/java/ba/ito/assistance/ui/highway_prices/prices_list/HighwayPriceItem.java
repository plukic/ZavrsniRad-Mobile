package ba.ito.assistance.ui.highway_prices.prices_list;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_highway_price)
public abstract class HighwayPriceItem extends EpoxyModelWithHolder<HighwayPriceItem.Holder> {

    @EpoxyAttribute
    String vehiclesCategory;

    @EpoxyAttribute
    String price;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvVehicleCategory.setText(vehiclesCategory);
        holder.tvPrice.setText(price);
    }

    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }


    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_vehicle_category)
        TextView tvVehicleCategory;
        @BindView(R.id.tv_price)
        TextView tvPrice;

    }
}
