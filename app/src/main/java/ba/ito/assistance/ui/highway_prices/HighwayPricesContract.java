package ba.ito.assistance.ui.highway_prices;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;

public interface HighwayPricesContract {
    interface  View extends BaseAsyncView<Presenter>{
        void displayPrices(HighwayVM highwayVM, HighwayTollboothVM entrance, HighwayTollboothVM exit, List<HighwayRoutePriceVM> prices);

        void displayRefreshingPricesFailed();
    }
    interface  Presenter extends BasePresenter<View>
    {
        void refreshPrices(boolean forceRefresh);
    }
}
