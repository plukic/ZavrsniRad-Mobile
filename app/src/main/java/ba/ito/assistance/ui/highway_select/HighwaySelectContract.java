package ba.ito.assistance.ui.highway_select;

import android.support.annotation.StringRes;

import org.joda.time.DateTime;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;

public interface HighwaySelectContract  {

    interface View extends BaseAsyncView<Presenter>{
        void displayHighways(List<HighwayVM> highwayVMS);
        void displayEntrances(List<HighwayTollboothVM> entrances);
        void displayExits(List<HighwayTollboothVM> exit);
        void displayTitle(@StringRes int title);

        void displayNoData();

        void displayRefreshHighwaysFailed();

        void displayRefreshExitsFailed();

        void displayRefreshEntranceFailed();

        void displayLastUpdateTime(DateTime dateTime);
    }
    interface  Presenter extends BasePresenter<View>{
        void refreshData(boolean forceRefresh);
        void loadData();
    }

}
