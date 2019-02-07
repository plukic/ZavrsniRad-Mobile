package ba.ito.assistance.ui.gas_stations.vm;

import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_gas_company_fuel_price)
public abstract class GasCompanyFuelPriceListItem extends EpoxyModelWithHolder<GasCompanyFuelPriceListItem.Holder> {


    @EpoxyAttribute
    String fuelName;
    @EpoxyAttribute
    String lastUpdateTime;
    @EpoxyAttribute
    String price;

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.tvFuelName.setText(fuelName);
        holder.tvLastUpdateTime.setText(lastUpdateTime);
        holder.tvPrice.setText(price);
    }


    public class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_fuel_name)
        TextView tvFuelName;
        @BindView(R.id.tv_last_update_time)
        TextView tvLastUpdateTime;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.guideline)
        Guideline guideline;


    }
}
