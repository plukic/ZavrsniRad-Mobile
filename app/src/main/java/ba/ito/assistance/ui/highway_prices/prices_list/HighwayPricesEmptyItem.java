package ba.ito.assistance.ui.highway_prices.prices_list;


import android.support.annotation.NonNull;

import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;

@EpoxyModelClass(layout = R.layout.list_item_highway_price_empty)
public abstract class HighwayPricesEmptyItem extends EpoxyModelWithHolder<HighwayPricesEmptyItem.Holder> {


    @Override
    public void bind(@NonNull Holder holder) {
    }

    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }


    static class Holder extends BaseEpoxyHolder {
    }
}
