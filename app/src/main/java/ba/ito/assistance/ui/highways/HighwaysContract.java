package ba.ito.assistance.ui.highways;

import java.util.List;

import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.base.BaseView;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;

public interface HighwaysContract {

    interface  View extends BaseView<Presenter>{
        void  displaySelectedHighway(String selectedHighway);
        void  displaySelectedEntrance(String selectedEntrance);
        void  displaySelectedExit(String selectedExit);

        void displaySelectHighwayError();

        void displaySelectExitError();

        void displaySelectEntranceError();


        void goToPrices(HighwayVM highway, HighwayTollboothVM entrance, HighwayTollboothVM exit);
    }

    interface  Presenter extends BasePresenter<View>{
        void onClearHighway();
        void onClearEntrance();
        void onClearExit();

        void onSubmit();

        HighwayTollboothVM getSelectedEntrance();
        HighwayVM getSelectedHighway();
        HighwayTollboothVM getSelectedExit();

        void setSelectedHighway(HighwayVM selectedHighway);
        void setSelectedEntrance(HighwayTollboothVM entrance);
        void setSelectedExit(HighwayTollboothVM exit);

        void onStart(HighwayVM selectedHighway, HighwayTollboothVM selectedEntrace, HighwayTollboothVM selectedExit);
    }
}
