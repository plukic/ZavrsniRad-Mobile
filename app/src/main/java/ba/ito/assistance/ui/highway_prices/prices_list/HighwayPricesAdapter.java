package ba.ito.assistance.ui.highway_prices.prices_list;

import android.content.res.Resources;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.Typed4EpoxyController;

import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.data.highway.VehiclesCategory;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.services.currency.ICurrencyService;


public class HighwayPricesAdapter extends Typed4EpoxyController<HighwayVM, List<HighwayRoutePriceVM>, HighwayTollboothVM, HighwayTollboothVM> {

    private ICurrencyService currencyService;
    private Resources res;


    @AutoModel
    HighwayPriceHeader_ highwayPriceHeader;
    @AutoModel
    HighwayPricesEmptyItem_ highwayEmptyState;

    public HighwayPricesAdapter(ICurrencyService currencyService, Resources res) {
        this.currencyService = currencyService;
        this.res = res;
    }

    @Override
    protected void buildModels(HighwayVM selectedHighway, List<HighwayRoutePriceVM> routes, HighwayTollboothVM entrance, HighwayTollboothVM exit) {


        highwayPriceHeader
                .highway(selectedHighway!=null?selectedHighway.Name:"")
                .route(entrance.Name + " - " + exit.Name)
                .addTo(this);
        if (routes == null || routes.isEmpty()) {
            highwayEmptyState
                    .addTo(this);
        } else {
            for (HighwayRoutePriceVM route : routes) {
                new HighwayPriceItem_()
                        .id(route.Id)
                        .price(currencyService.format(route.Currency, route.Price))
                        .vehiclesCategory(getVehiclesCatName(route.VehiclesCategory))
                        .addTo(this);
            }
        }
    }


    private String getVehiclesCatName(VehiclesCategory vehiclesCategory) {
        switch (vehiclesCategory) {
            case I:
                return res.getString(R.string.label_vehicles_category_I);
            case II:
                return res.getString(R.string.label_vehicles_category_II);
            case III:
                return res.getString(R.string.label_vehicles_category_III);
            case IV:
                return res.getString(R.string.label_vehicles_category_IV);
        }
        return null;
    }


}
