package ba.ito.assistance.ui.gas_stations;

import android.content.res.Resources;

import com.airbnb.epoxy.TypedEpoxyController;
import com.bumptech.glide.RequestManager;

import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import ba.ito.assistance.model.gas_stations.GasStationFuel;
import ba.ito.assistance.services.currency.ICurrencyService;
import ba.ito.assistance.ui.common.ListItemDivider_;
import ba.ito.assistance.ui.common.ListItemTextAccentAction_;
import ba.ito.assistance.ui.gas_stations.vm.GasCompanyFuelPriceListItem_;
import ba.ito.assistance.ui.gas_stations.vm.GasCompanyHeaderListItem_;
import ba.ito.assistance.util.DateAndTimeUtil;

public class GasCompanyPricesController extends TypedEpoxyController<List<GasCompanyFuelPrices>> {

    public interface IGasCompanyPricesNavigation{
        void OnGasStationSelected(GasCompanyFuelPrices listItemVM);
    }
    private RequestManager requestManager;
    private ICurrencyService currencyService;
    private DateAndTimeUtil dateAndTimeUtil;
    private Resources resources;
    private IGasCompanyPricesNavigation callback;
    public GasCompanyPricesController(RequestManager requestManager, ICurrencyService currencyService, DateAndTimeUtil dateAndTimeUtil, Resources resources, IGasCompanyPricesNavigation callback) {
        this.requestManager = requestManager;
        this.currencyService = currencyService;
        this.dateAndTimeUtil = dateAndTimeUtil;
        this.resources = resources;
        this.callback = callback;
    }

    @Override
    protected void buildModels(List<GasCompanyFuelPrices> data) {
        if (data.isEmpty()) {
            //TODO Add empty state
            return;
        }

        for (GasCompanyFuelPrices item : data) {
            new GasCompanyHeaderListItem_()
                    .id(item.GasCompanyId)
                    .gasStationUrl(item.CompanyLogoUrl)
                    .gasCompanyDescription(item.GasCompanyDescription)
                    .requestManager(requestManager)
                    .addTo(this);


            List<GasStationFuel> fuel = item.Fuel;
            new ListItemDivider_()
                    .id("company_header_divider_" + item.GasCompanyId)
                    .addTo(this);
            for (GasStationFuel gasStationFuel : fuel) {
                new GasCompanyFuelPriceListItem_()
                        .id(gasStationFuel.Id)
                        .fuelName(gasStationFuel.FuelName)
                        .price(currencyService.format(gasStationFuel.Currency, gasStationFuel.Price))
                        .lastUpdateTime(String.format(resources.getString(R.string.fuel_price_updated_at), dateAndTimeUtil.FormatForGasCompanyUpdate(gasStationFuel.UpdateAt)))
                        .addTo(this);
                new ListItemDivider_()
                        .id("list_item_divider" + gasStationFuel.Id)
                        .addTo(this);
            }

            new ListItemTextAccentAction_()
                    .id("gas_company_see_more" + item.GasCompanyId)
                    .callback((view) -> {
                    callback.OnGasStationSelected(item);
                    })
                    .addTo(this);
        }
    }
}
