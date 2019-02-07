package ba.ito.assistance.ui.road_conditions;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.road_condition.RoadConditionType;
import ba.ito.assistance.model.road_condition.RoadConditionVM;

public interface RoadConditionContract {
    interface  Presenter extends BasePresenter<View>{
        void loadRoadConditionData();
        void refreshData();

        int getTabText(RoadConditionType roadConditionType);

        int getIcon(RoadConditionType roadConditionType);
    }
    interface  View extends BaseAsyncView<Presenter> {
        void displayNoData();
        void displayRoadConditions(List<RoadConditionVM> roadConditionVM);

        void displayFailedToRefreshData();
    }
}
