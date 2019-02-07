package ba.ito.assistance.ui.highways;

import javax.inject.Inject;

import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;

public class HighwaysPresenter implements HighwaysContract.Presenter {
    private HighwaysContract.View view;

    private HighwayVM highway;
    private HighwayTollboothVM entrance;
    private HighwayTollboothVM exit;

    @Inject
    public HighwaysPresenter() {
    }

    @Override
    public void onClearHighway() {
        entrance = null;
        exit = null;
        highway = null;
        bindData();
    }

    private void bindData() {
        if (highway == null)
            view.displaySelectedHighway(null);
        else
            view.displaySelectedHighway(highway.Name);

        if (entrance == null)
            view.displaySelectedEntrance(null);
        else
            view.displaySelectedEntrance(entrance.Name);

        if (exit == null)
            view.displaySelectedExit(null);
        else
            view.displaySelectedExit(exit.Name);
    }

    @Override
    public void onClearEntrance() {
        entrance = null;
        exit = null;
        view.displaySelectedEntrance(null);
        view.displaySelectedExit(null);
    }

    @Override
    public void onClearExit() {
        exit = null;
        view.displaySelectedExit(null);
    }

    @Override
    public void onSubmit() {
        if (highway == null) {
            view.displaySelectHighwayError();
            return;
        }
        if (entrance == null) {
            view.displaySelectEntranceError();
            return;
        }
        if (exit == null) {
            view.displaySelectExitError();
            return;
        }

        view.goToPrices(highway, entrance, exit);
    }

    @Override
    public HighwayTollboothVM getSelectedEntrance() {
        return entrance;
    }

    @Override
    public HighwayVM getSelectedHighway() {
        return highway;
    }

    @Override
    public HighwayTollboothVM getSelectedExit() {
        return exit;
    }

    @Override
    public void setSelectedHighway(HighwayVM selectedHighway) {
        this.highway = selectedHighway;
        bindData();
    }

    @Override
    public void setSelectedExit(HighwayTollboothVM exit) {
        this.exit = exit;
        bindData();
    }

    @Override
    public void onStart(HighwayVM selectedHighway, HighwayTollboothVM selectedEntrace, HighwayTollboothVM selectedExit) {
        this.highway = selectedHighway;
        this.entrance = selectedEntrace;
        this.exit = selectedExit;
        bindData();
    }

    @Override
    public void setSelectedEntrance(HighwayTollboothVM entrance) {
        this.entrance = entrance;
        bindData();
    }

    @Override
    public void takeView(HighwaysContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        bindData();
    }

    @Override
    public void onStop() {

    }
}
